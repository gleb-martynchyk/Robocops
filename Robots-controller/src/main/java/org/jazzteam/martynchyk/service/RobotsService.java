package org.jazzteam.martynchyk.service;

import lombok.Getter;
import lombok.Setter;
import org.jazzteam.martynchyk.robots.BaseRobot;
import org.jazzteam.martynchyk.robots.Report;
import org.jazzteam.martynchyk.robots.Robot;
import org.jazzteam.martynchyk.service.implementation.TaskService;
import org.jazzteam.martynchyk.tasks.BaseTask;
import org.jazzteam.martynchyk.tasks.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Getter
@Setter
@Service
public class RobotsService {
    @Autowired
    private TaskService taskService;
    private Set<Robot> robots;
    private boolean running;
    private Map<Robot, Set<Future<Report>>> robotsFutures;
    private Map<Robot, Set<Report>> robotsReports;

    public RobotsService() {
        this.robots = new HashSet<>();
        this.robotsFutures = new HashMap<>();
        this.robotsReports = new HashMap<>();
    }

    public boolean addRobot(Robot robot) {
        return robots.add(robot);
    }

    public boolean removeRobot(Robot robot) {
        return robots.remove(robot);
    }

    private void addReport(Report report) {
        Robot robot = report.getExecutor();
        if (robotsReports.containsKey(robot)) {
            robotsReports.get(robot).add(report);
        } else {
            Set<Report> set = new HashSet<>();
            set.add(report);
            robotsReports.put(robot, set);
        }
    }

    public synchronized void collectReports() {
        synchronized (robots) {
            //TODO почему создается несколько однаковых ключей?
            for (Robot robotToCast : robots) {
                BaseRobot robot=(BaseRobot) robotToCast;
                Set<Future<Report>> reports = ((BaseRobot) robot).getFutureReports();
                if(reports.isEmpty()){
                    continue;
                }
                if (robotsFutures.containsKey(robot)) {
                    robotsFutures.get(robot).addAll(reports);
                } else {
                    robotsFutures.put(robot, reports);
                }
            }
        }
    }

    public void updateTasksInDatabase() {
        for (Map.Entry<Robot, Set<Future<Report>>> robotReports : robotsFutures.entrySet()) {
            Iterator<Future<Report>> iterator = robotReports.getValue().iterator();
            while (iterator.hasNext()) {
                Future<Report> future = iterator.next();
                if (future.isDone()) {
                    try {
                        Report report = future.get();
                        taskService.update((BaseTask) report.getTask());
                        //TODO тут валилась concurrentException
                        iterator.remove();
                        addReport(report);
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void startExecution() {
        running = true;
        while (running) {
            if (taskService.hasNextToExecute()) {
                sendTask();
            }
            collectReports();
            updateTasksInDatabase();
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopExecution() {
        running = false;
    }

    public void startAllRobots() {
        robots.forEach(Robot::startExecution);
    }

    public Robot sendTask() {
        BaseTask nextTask = taskService.findNext();
        if (nextTask == null) {
            return null;
        }
        AtomicReference<Robot> robotToExecuteTask = new AtomicReference<>();
        robots.stream()
                .filter(robot -> robot.canExecute(nextTask))
                .min(Comparator.comparingInt(o -> ((BaseRobot) o).getTaskQueue().size()))
                .ifPresent(robot -> {
                            robot.addTask(nextTask);
                            robotToExecuteTask.set(robot);
                            nextTask.setStatus(TaskStatus.ASSIGNED);
                            taskService.update(nextTask);
                        }
                );
        //TODO должны ли роботы автоматически создаваться, если не хватает?
        return robotToExecuteTask.get();
    }

    public Robot sendTask(Robot robot) {
        BaseTask nextTask = taskService.findNext();
        if (nextTask == null) {
            return null;
        }
        robot.addTask(nextTask);
        nextTask.setStatus(TaskStatus.ASSIGNED);
        taskService.update(nextTask);
        return robot;
    }

    public Set<Robot> sendTask(Set<Robot> robots) {
        BaseTask nextTask = taskService.findNext();
        if (nextTask == null) {
            return null;
        }
        Set<Robot> robotsThatGetTask = new HashSet<>();
        for (Robot robot : robots) {
            if (robot.addTask(nextTask)) {
                robotsThatGetTask.add(robot);
            }
        }
        nextTask.setStatus(TaskStatus.ASSIGNED);
        taskService.update(nextTask);
        return robotsThatGetTask;
    }
}
