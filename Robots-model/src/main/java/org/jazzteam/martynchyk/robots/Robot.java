package org.jazzteam.martynchyk.robots;

import org.jazzteam.martynchyk.tasks.Task;

public interface Robot {
    boolean addTask(Task task);
    Report executeTaskFromQueue();
}
