package org.jazzteam.martynchyk.robots;

import org.jazzteam.martynchyk.tasks.BaseTask;

public interface Robot {
    void startExecution();

    void stopExecution();

    boolean addTask(BaseTask task);

    boolean canExecute(BaseTask task);

    Report executeTaskFromQueue();
}
