package org.jazzteam.martynchyk.tasks;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.jazzteam.martynchyk.IdGenerator;
import org.jazzteam.martynchyk.robots.Report;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@BsonDiscriminator
public class BaseTask implements Task {
    private long id;
    private String description;
    private TaskPriority taskPriority;
    private TaskStatus status;
    private Date createDate;
    private int difficultyMilliseconds;

    public BaseTask() {
        id = IdGenerator.generateUniqueId();
        taskPriority = TaskPriority.LOW;
        status = TaskStatus.CREATED;
        createDate = new Date();
        difficultyMilliseconds = 5000;
    }

    public BaseTask(String description, TaskPriority taskPriority, TaskStatus status, Date createDate) {
        this.id = IdGenerator.generateUniqueId();
        this.description = description;
        this.taskPriority = taskPriority;
        this.status = status;
        this.createDate = createDate;
    }

    @Override
    public Report execute() {
        Report report = new Report();
        report.setStartDate(new Date());
        report.setTask(this);
        this.setStatus(TaskStatus.INPROCESS);
        try {
            TimeUnit.MILLISECONDS.sleep(difficultyMilliseconds);
        } catch (InterruptedException e) {
            System.out.println("Was interrupted");
        }
        this.setStatus(TaskStatus.DONE);
        report.setEndDate(new Date());
        return report;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseTask task = (BaseTask) o;
        return id == task.id &&
                difficultyMilliseconds == task.difficultyMilliseconds &&
                taskPriority == task.taskPriority &&
                status == task.status &&
                createDate.equals(task.createDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, taskPriority, status, createDate, difficultyMilliseconds);
    }
}
