package org.jazzteam.martynchyk.robots;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jazzteam.martynchyk.IdGenerator;
import org.jazzteam.martynchyk.tasks.BaseTask;
import org.jazzteam.martynchyk.tasks.Task;
import org.jazzteam.martynchyk.tasks.TaskStatus;

import java.util.*;
import java.util.concurrent.*;

@Getter
@Setter
public class BaseRobot implements Robot {

    private long id;
    private String name;
    private Set<Class> allowedTasks;
    private Queue<Task> taskQueue;
    private Set<Report> reports;
    private boolean running;
    private static Logger log = LogManager.getLogger(BaseRobot.class);

    public BaseRobot() {
        id = IdGenerator.generateUniqueId();
        taskQueue = new LinkedList<>();
        allowedTasks = new HashSet<>();
        reports = new HashSet<>();
    }


    @Override
    public void startExecution() {
        running = true;
        while (!taskQueue.isEmpty() && running) {
            try {
                //TODO в дальнейшем добавить заполнение reports множества во все методы типа execute
                Future<Report> report = executeTaskFromQueueMultiThread();
                reports.add(report.get());
                TimeUnit.MILLISECONDS.sleep(300);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void stopExecution() {
        running = false;
    }

    @Override
    public boolean canExecute(Task task) {
        return allowedTasks.contains(task.getClass());
    }

    @Override
    public boolean addTask(Task task) {
        if (canExecute(task) && !((BaseTask) task).getStatus().equals(TaskStatus.DONE)) {
            return taskQueue.offer(task);
        } else return false;
    }

    public Report executeTask(Task task) {
        Report report = task.execute();
        log.info(Thread.currentThread().getName() + " - task complete");
        report.getExecutors().add(this);
        return report;
    }

    @Override
    public synchronized Report executeTaskFromQueue() {
        log.info(Thread.currentThread().getName() + " - task started");
        if (taskQueue.isEmpty()) {
            return null;
        }
        BaseTask nextTask = (BaseTask) taskQueue.poll();
        //TODO вызывает interruptedException
        synchronized (nextTask) {
            while (!nextTask.getStatus().equals(TaskStatus.CREATED)) {
                nextTask = (BaseTask) taskQueue.poll();
            }
            return executeTask(nextTask);
        }
    }

    public Future<Report> executeTaskFromQueueMultiThread() {
        Callable<Report> task = this::executeTaskFromQueue;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        return executor.submit(task);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseRobot robot = (BaseRobot) o;
        return id == robot.id &&
                running == robot.running &&
                allowedTasks.equals(robot.allowedTasks) &&
                taskQueue.equals(robot.taskQueue) &&
                reports.equals(robot.reports);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, allowedTasks, taskQueue, reports, running);
    }
}
