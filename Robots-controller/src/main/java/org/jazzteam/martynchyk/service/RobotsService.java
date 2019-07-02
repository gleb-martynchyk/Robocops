package org.jazzteam.martynchyk.service;

import lombok.Getter;
import lombok.Setter;
import org.jazzteam.martynchyk.robots.BaseRobot;
import org.jazzteam.martynchyk.robots.Report;
import org.jazzteam.martynchyk.robots.Robot;
import org.jazzteam.martynchyk.service.implementation.TaskService;
import org.jazzteam.martynchyk.tasks.BaseTask;
import org.jazzteam.martynchyk.tasks.Task;
import org.jazzteam.martynchyk.tasks.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
    private Map<Robot, Report> robotsReports;

    public RobotsService() {
        this.robots = new HashSet<>();
    }

    public boolean addRobot(Robot robot) {
        return robots.add(robot);
    }

    public boolean removeRobot(Robot robot) {
        return robots.remove(robot);
    }

    public void startExecution() {
        running = true;
        while (running) {
            if (taskService.hasNextToExecute()) {
                sendTask();
            }
            try {
                TimeUnit.MILLISECONDS.sleep(30);
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

    // добавить ко всем в очередь, выполнит первый кто приступит
    //TODO написать тесты, когда один таск в очереди у нескольких роботов, и только один его обработает
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
