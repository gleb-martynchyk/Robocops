package org.jazzteam.martynchyk.tasks;

import org.jazzteam.martynchyk.exception.ExecutionDoneTask;
import org.jazzteam.martynchyk.robots.Report;

public interface Task {
    Report execute() throws ExecutionDoneTask;
}
