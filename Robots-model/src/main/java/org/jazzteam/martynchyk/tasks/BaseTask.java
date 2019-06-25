package org.jazzteam.martynchyk.tasks;

import lombok.Data;
import org.jazzteam.martynchyk.IdGenerator;
import org.jazzteam.martynchyk.robots.Report;

@Data
public class BaseTask implements Task {
    private long id;
    private String description;
    private TaskPriority taskPriority;
    private TaskStatus taskStatus;

    public BaseTask() {
        this.id = IdGenerator.generateUniqueId();
        this.taskPriority = TaskPriority.LOW;
        this.taskStatus = TaskStatus.CREATED;
    }

    @Override
    public Report execute() {
        return null;
    }
}
