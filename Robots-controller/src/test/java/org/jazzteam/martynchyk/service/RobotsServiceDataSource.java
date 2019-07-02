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
                //first element need to be expected
//                Collections.singletonList(
//                        new BaseTask("", TaskPriority.HIGH, TaskStatus.CREATED, new Date(0))
//                ),
                Arrays.asList(
                        new BaseTask("", TaskPriority.HIGH, TaskStatus.CREATED, new Date(0)),
//                        new BaseTask("", TaskPriority.HIGH, TaskStatus.CREATED, new Date(1)),
//                        new BaseTask("", TaskPriority.HIGH, TaskStatus.CREATED, new Date(2)),
//                        new BaseTask("", TaskPriority.MIDDLE, TaskStatus.DONE, new Date(3)),
                        new BaseTask("", TaskPriority.MIDDLE, TaskStatus.CREATED, new Date(4))
                ),
//                Arrays.asList(
//                        new BaseTask("", TaskPriority.HIGH, TaskStatus.CREATED, new Date(5)),
//                        new BaseTask("", TaskPriority.MIDDLE, TaskStatus.CREATED, new Date(1)),
//                        new BaseTask("", TaskPriority.HIGH, TaskStatus.ASSIGNED, new Date(0))
//                ),
//                Arrays.asList(
//                        new BaseTask("", TaskPriority.MIDDLE, TaskStatus.CREATED, new Date(1)),
//                        new BaseTask("", TaskPriority.LOW, TaskStatus.ASSIGNED, new Date(0))
//                ),
//                Arrays.asList(
//                        new BaseTask("", TaskPriority.LOW, TaskStatus.CREATED, new Date(0)),
//                        new BaseTask("", TaskPriority.LOW, TaskStatus.ASSIGNED, new Date(1))
//                ),
//                Arrays.asList(
//                        new BaseTask("", TaskPriority.LOW, TaskStatus.CREATED, new Date(0)),
//                        new BaseTask("", TaskPriority.LOW, TaskStatus.CREATED, new Date(1)),
//                        new BaseTask("", TaskPriority.LOW, TaskStatus.CREATED, new Date(2)),                        new BaseTask("", TaskPriority.HIGH, TaskStatus.CREATED, new Date(0)),
//                        new BaseTask("", TaskPriority.HIGH, TaskStatus.CREATED, new Date(1)),
//                        new BaseTask("", TaskPriority.HIGH, TaskStatus.CREATED, new Date(2)),
//                        new BaseTask("", TaskPriority.MIDDLE, TaskStatus.CREATED, new Date(3)),
//                        new BaseTask("", TaskPriority.MIDDLE, TaskStatus.CREATED, new Date(4))
//                ),
        };
    }
}