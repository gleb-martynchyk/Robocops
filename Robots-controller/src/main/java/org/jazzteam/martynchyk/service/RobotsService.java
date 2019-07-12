package org.jazzteam.martynchyk.service;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jazzteam.martynchyk.robots.BaseRobot;
import org.jazzteam.martynchyk.robots.Report;
import org.jazzteam.martynchyk.robots.Robot;
import org.jazzteam.martynchyk.service.implementation.TaskService;
import org.jazzteam.martynchyk.tasks.BaseTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Getter
@Setter
@Service
public class RobotsService {
    @Autowired
    private TaskService taskService;
    private Set<Robot> robots;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private Map<Robot, Set<Report>> robotsReports;
    private static Logger log = LogManager.getLogger(RobotsService.class);

    public RobotsService() {
        this.robots = new HashSet<>();
        this.robotsReports = new ConcurrentHashMap<>();
    }

    public boolean addRobot(Robot robot) {
        return robots.add(robot);
    }

    public boolean removeRobot(Robot robot) {
        return robots.remove(robot);
    }

    public BaseRobot findRobotById(Long id) {
        return (BaseRobot) getRobots().stream()
                .filter(robot -> ((BaseRobot) robot).getId() == id)
                .findFirst()
                .orElse(null);
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

    private boolean robotsDone() {
        return robots.size() == robots.stream()
                .filter(robot -> ((BaseRobot) robot).getTaskQueue().isEmpty())
                .count();
    }

    public void collectReports() {
        for (Robot robotToCast : robots) {
            BaseRobot robot = (BaseRobot) robotToCast;
            Set<Report> reports = null;
            synchronized (robot.getReports()) {
                reports = new HashSet<>(robot.getReports());
            }
            if (reports.isEmpty()) {
                continue;
            }
            if (robotsReports.containsKey(robot)) {
                robotsReports.get(robot).addAll(reports);
            } else {
                robotsReports.put(robot, reports);
            }
        }
    }

    public void updateTasksInDatabase() {
        Iterator<Map.Entry<Robot, Set<Report>>> mapIterator = robotsReports.entrySet().iterator();
        while (mapIterator.hasNext()) {
            Iterator<Report> setIterator = mapIterator.next().getValue().iterator();
            while (setIterator.hasNext()) {
                Report report = setIterator.next();
                if (report != null) {
                    taskService.update((BaseTask) report.getTask());
                    addReport(report);
                }
            }
        }
    }

    public void startExecution() {
        running.set(true);
        while (running.get() && !Thread.currentThread().isInterrupted()) {
            if (taskService.hasNextToExecute()) {
                if (sendTask() == null) {
                    log.warn("Робот не нашелся");
                }
            }
            collectReports();
            updateTasksInDatabase();
            try {
                //TODO Тесты валятся с малым временем 30мс
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void sendAllTasks() {
        running.set(true);
        while (running.get() && !Thread.currentThread().isInterrupted()) {
            if (taskService.hasNextToExecute()) {
                if (sendTask() == null) {
                    log.warn("Робот не нашелся");
                }
            }
            collectReports();
            updateTasksInDatabase();
        }
    }

    public Thread startExecutionInThread() {
        Thread thread = new Thread(this::startExecution);
        thread.start();
        return thread;
    }

    public void stopExecution() {
        running.set(false);
    }

    public void startAllRobots() {
        robots.forEach(Robot::startExecution);
    }

    public void stopAllRobots() {
        robots.forEach(Robot::stopExecution);
    }

    public synchronized Robot sendTask() {
        BaseTask nextTask = taskService.findNext();
        if (nextTask == null) {
            return null;
        }
        synchronized (nextTask) {
            //TODO тут бывает таск не обновляется в бд, поэтому происходит больше выполнений чем тасков
            AtomicReference<Robot> robotToExecuteTask = new AtomicReference<>(null);
            robots.stream()
                    .filter(robot -> robot.canExecute(nextTask))
                    .min(Comparator.comparingInt(o -> ((BaseRobot) o).getTaskQueue().size()))
                    .ifPresent(robot -> {
                                if (robot.addTask(nextTask)) {
                                    taskService.update(nextTask);
                                    robotToExecuteTask.set(robot);
                                } else {
                                    log.warn("задача не отправлена");
                                }
                            }
                    );
            return robotToExecuteTask.get();
        }
    }

    public Robot sendTask(Robot robot) {
        BaseTask nextTask = taskService.findNext();
        if (nextTask == null) {
            return null;
        }
        robot.addTask(nextTask);
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
        taskService.update(nextTask);
        return robotsThatGetTask;
    }
}
