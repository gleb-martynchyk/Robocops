package org.jazzteam.martynchyk.robots;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jazzteam.martynchyk.IdGenerator;
import org.jazzteam.martynchyk.tasks.Task;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Getter
@Setter
public class BaseRobot implements Robot {

    private long id;
    private String name;
    private Set<Class> allowedTasks;
    private Queue<Task> taskQueue;
    private static Logger log = LogManager.getLogger(BaseRobot.class);

    public BaseRobot() {
        this.id = IdGenerator.generateUniqueId();
        this.taskQueue = new LinkedList<>();
        this.allowedTasks = new HashSet<>();
    }

    @Override
    public boolean addTask(Task task) {
        if (allowedTasks.contains(task.getClass()))
            return taskQueue.offer(task);
        else return false;
    }

    public Report executeTask(Task task) {
        Report report = task.execute();
        log.info(Thread.currentThread().getName() + " - task complete");
        report.getExecutors().add(this);
        return report;
    }

    @Override
    public Report executeTaskFromQueue() {
        if (!taskQueue.isEmpty()) {
            return executeTask(taskQueue.poll());
        } else return null;
    }

    public Future<Report> executeNextTask() {
        log.info(Thread.currentThread().getName() + " - task started");
        Callable<Report> task = this::executeTaskFromQueue;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        return executor.submit(task);
    }
}
