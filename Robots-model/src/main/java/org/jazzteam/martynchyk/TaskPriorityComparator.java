package org.jazzteam.martynchyk;

import org.jazzteam.martynchyk.tasks.BaseTask;
import org.jazzteam.martynchyk.tasks.Task;

import java.util.Comparator;

public class TaskPriorityComparator implements Comparator<Task> {

    public int compare(Task o1, Task o2) {
        return ((BaseTask) o1).getTaskPriority().compareTo(((BaseTask) o2).getTaskPriority());
    }
}
