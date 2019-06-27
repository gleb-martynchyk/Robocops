package org.jazzteam.martynchyk.tasks;

import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.jazzteam.martynchyk.IdGenerator;
import org.jazzteam.martynchyk.robots.Report;

import java.util.Date;
import java.util.Random;
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
        this.id = IdGenerator.generateUniqueId();
        this.taskPriority = TaskPriority.LOW;
        this.status = TaskStatus.CREATED;
        this.createDate = new Date();
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
        this.setStatus(TaskStatus.INPROCESS);
        try {
            Random random = new Random();
//            TimeUnit.SECONDS.sleep(5 + random.nextInt(5));
            TimeUnit.SECONDS.sleep(difficulty);
        } catch (InterruptedException e) {
            System.out.println("Was interrupted");
        }
        this.setStatus(TaskStatus.DONE);
        Report report = new Report();
        report.setEndDate(new Date());
        report.setStartDate(this.createDate);
        return report;
    }
}
