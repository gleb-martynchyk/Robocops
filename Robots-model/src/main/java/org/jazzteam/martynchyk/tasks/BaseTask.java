package org.jazzteam.martynchyk.tasks;

import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.jazzteam.martynchyk.IdGenerator;
import org.jazzteam.martynchyk.robots.Report;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Data
@BsonDiscriminator
public class BaseTask implements Task {
    private long id;
    private String description;
    private TaskPriority taskPriority;
    private TaskStatus status;
    private Date createDate;
    private int difficulty;

    public BaseTask() {
        id = IdGenerator.generateUniqueId();
        taskPriority = TaskPriority.LOW;
        status = TaskStatus.CREATED;
        createDate = new Date();
        difficulty = 5;
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
            TimeUnit.SECONDS.sleep(difficulty);
        } catch (InterruptedException e) {
            System.out.println("Was interrupted");
        }
        this.setStatus(TaskStatus.DONE);
        report.setEndDate(new Date());
        return report;
    }
}
