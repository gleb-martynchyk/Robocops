package org.jazzteam.martynchyk.service.implementation;

import org.jazzteam.martynchyk.tasks.BaseTask;
import org.jazzteam.martynchyk.tasks.TaskPriority;
import org.jazzteam.martynchyk.tasks.TaskStatus;
import org.testng.annotations.DataProvider;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

public class TaskServiceDataSource {
    @DataProvider(name = "TasksAndHasNextResult")
    public Object[] getTasksAndResult() {
        return new Object[][]{
                {
                        Collections.emptyList(),
                        false
                },
                {
                        Collections.singletonList(
                                new BaseTask("", TaskPriority.HIGH, TaskStatus.CREATED, new Date(0))
                        ),
                        true
                },
                {
                        Collections.singletonList(
                                new BaseTask("", TaskPriority.HIGH, TaskStatus.ASSIGNED, new Date(0))
                        ),
                        false
                },
                {
                        Arrays.asList(
                                new BaseTask("", TaskPriority.HIGH, TaskStatus.ASSIGNED, new Date(0)),
                                new BaseTask("", TaskPriority.HIGH, TaskStatus.DONE, new Date(0))
                        ),
                        false
                },
                {
                        Arrays.asList(
                                new BaseTask("", TaskPriority.HIGH, TaskStatus.ASSIGNED, new Date(0)),
                                new BaseTask("", TaskPriority.HIGH, TaskStatus.DONE, new Date(0)),
                                new BaseTask("", TaskPriority.HIGH, TaskStatus.DONE, new Date(0)),
                                new BaseTask("", TaskPriority.HIGH, TaskStatus.ASSIGNED, new Date(0))
                        ),
                        false
                },
                {
                        Arrays.asList(
                                new BaseTask("", TaskPriority.HIGH, TaskStatus.ASSIGNED, new Date(0)),
                                new BaseTask("", TaskPriority.HIGH, TaskStatus.DONE, new Date(0)),
                                new BaseTask("", TaskPriority.HIGH, TaskStatus.DONE, new Date(0)),
                                new BaseTask("", TaskPriority.HIGH, TaskStatus.CREATED, new Date(0))
                        ),
                        true
                },
        };
    }
}