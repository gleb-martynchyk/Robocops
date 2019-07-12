package org.jazzteam.martynchyk.robots;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jazzteam.martynchyk.IdGenerator;
import org.jazzteam.martynchyk.tasks.BaseTask;
import org.jazzteam.martynchyk.tasks.Task;
import org.jazzteam.martynchyk.tasks.TaskStatus;

import java.util.HashSet;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
@Setter
public class BaseRobot implements Robot {

    private long id;
    private String name;
    private Set<Class> allowedTasks;
    private volatile Queue<Task> taskQueue;
    private Set<Report> reports;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private static Logger log = LogManager.getLogger(BaseRobot.class);
    private int i = 0;

    public synchronized Set<Report> getReports() {
        return reports;
    }

    public synchronized void setReports(Set<Report> reports) {
        this.reports = reports;
    }

    public BaseRobot() {
        id = IdGenerator.generateUniqueId();
        //TODO можно будет убрать
        taskQueue = new ConcurrentLinkedQueue<>();
        allowedTasks = new HashSet<>();
        reports = new HashSet<>();
    }

    @Override
    public boolean canExecute(BaseTask task) {
        return allowedTasks.contains(task.getClass());
    }

    @Override
    public boolean addTask(BaseTask task) {
        if (canExecute(task) && task.getStatus().equals(TaskStatus.CREATED) && !taskQueue.contains(task)) {
            task.setStatus(TaskStatus.ASSIGNED);
            return taskQueue.offer(task);
        } else return false;
    }

    @Override
    public void startExecution() {
        running.set(true);
        new Thread(() -> {
            while (running.get()) {
                executeAllFromQueue();
                try {
                    TimeUnit.MILLISECONDS.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void stopExecution() {
        running.set(false);
    }

    public void executeAllFromQueue() {
        while (!taskQueue.isEmpty()) {
            Report report = executeTaskFromQueue();
            if (report != null) {
                synchronized (reports) {
                    reports.add(report);
                }
            }
            try {
                TimeUnit.MILLISECONDS.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public synchronized Report executeTaskFromQueue() {
        if (taskQueue.isEmpty()) {
            return null;
        }
        synchronized (taskQueue) {
            BaseTask nextTask = (BaseTask) taskQueue.poll();
            while (nextTask.getStatus().equals(TaskStatus.DONE) || nextTask.getStatus().equals(TaskStatus.INPROCESS)) {
                if (!taskQueue.isEmpty()) {
                    nextTask = (BaseTask) taskQueue.poll();
                } else return null;
            }
            log.info(this.getId()%100+" - task started: "+ nextTask );
            return executeTask(nextTask);
        }
    }

    public Report executeTask(Task task) {
        Report report = task.execute();
        log.info(this.getId()%100+" - task complete: "+ ((BaseTask)task));
        report.setExecutor(this);
        return report;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseRobot robot = (BaseRobot) o;
        return id == robot.id &&
                running == robot.running &&
                allowedTasks.equals(robot.allowedTasks) &&
                taskQueue.equals(robot.taskQueue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, allowedTasks);
    }

    @Override
    public String toString() {
        return "BaseRobot{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", allowedTasks=" + allowedTasks +
                ", reports=" + reports +
                ", running=" + running +
                ", i=" + i +
                '}';
    }
}
