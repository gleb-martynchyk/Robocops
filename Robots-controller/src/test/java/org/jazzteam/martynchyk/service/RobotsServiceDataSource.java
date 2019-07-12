package org.jazzteam.martynchyk.service;

import org.jazzteam.martynchyk.tasks.BaseTask;
import org.jazzteam.martynchyk.tasks.TaskPriority;
import org.jazzteam.martynchyk.tasks.TaskStatus;
import org.testng.annotations.DataProvider;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

public class RobotsServiceDataSource {
    @DataProvider(name = "TasksToExecute")
    public Object[] getTasksToExecute() {
        return new Object[]{
//                first element need to be expected

                Collections.singletonList(
                        new BaseTask("01", TaskPriority.HIGH, TaskStatus.CREATED, new Date(0))
                ),
                Arrays.asList(
                        new BaseTask("11", TaskPriority.HIGH, TaskStatus.CREATED, new Date(0)),
                        new BaseTask("12", TaskPriority.HIGH, TaskStatus.CREATED, new Date(1)),
                        new BaseTask("13", TaskPriority.HIGH, TaskStatus.CREATED, new Date(2)),
                        new BaseTask("14", TaskPriority.MIDDLE, TaskStatus.DONE, new Date(3)),
                        new BaseTask("15", TaskPriority.MIDDLE, TaskStatus.CREATED, new Date(4))
                ),
                Arrays.asList(
                        new BaseTask("21", TaskPriority.HIGH, TaskStatus.CREATED, new Date(5)),
                        new BaseTask("22", TaskPriority.MIDDLE, TaskStatus.CREATED, new Date(1)),
                        new BaseTask("23", TaskPriority.HIGH, TaskStatus.ASSIGNED, new Date(0))
                ),
                Arrays.asList(
                        new BaseTask("31", TaskPriority.MIDDLE, TaskStatus.CREATED, new Date(1)),
                        new BaseTask("32", TaskPriority.LOW, TaskStatus.ASSIGNED, new Date(0))
                ),
                Arrays.asList(
                        new BaseTask("41", TaskPriority.LOW, TaskStatus.CREATED, new Date(0)),
                        new BaseTask("42", TaskPriority.LOW, TaskStatus.ASSIGNED, new Date(1))
                ),
                Arrays.asList(
                        new BaseTask("51", TaskPriority.LOW, TaskStatus.CREATED, new Date(0)),
                        new BaseTask("52", TaskPriority.LOW, TaskStatus.CREATED, new Date(1)),
                        new BaseTask("53", TaskPriority.LOW, TaskStatus.CREATED, new Date(2)),
                        new BaseTask("54", TaskPriority.HIGH, TaskStatus.ASSIGNED, new Date(0)),
                        new BaseTask("55", TaskPriority.HIGH, TaskStatus.CREATED, new Date(1)),
                        new BaseTask("56", TaskPriority.HIGH, TaskStatus.CREATED, new Date(2)),
                        new BaseTask("57", TaskPriority.MIDDLE, TaskStatus.CREATED, new Date(3)),
                        new BaseTask("58", TaskPriority.MIDDLE, TaskStatus.CREATED, new Date(4))
                ),
        };
    }
}