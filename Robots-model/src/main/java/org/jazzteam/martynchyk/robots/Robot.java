package org.jazzteam.martynchyk.robots;

import org.jazzteam.martynchyk.tasks.Task;

import java.util.concurrent.Future;

public interface Robot {
    void startExecution();
    void stopExecution();
    boolean addTask(Task task);
    boolean canExecute(Task task);
    Report executeTaskFromQueue();
    Future<Report> executeTaskFromQueueMultiThread();
}
