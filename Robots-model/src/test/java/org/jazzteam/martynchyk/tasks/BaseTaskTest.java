package org.jazzteam.martynchyk.tasks;

import org.jazzteam.martynchyk.robots.Report;
import org.jazzteam.martynchyk.tasks.implementation.PatrolCityTask;
import org.jazzteam.martynchyk.tasks.implementation.PatrolRoadsAndHighwaysTask;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class BaseTaskTest {

    private BaseTask task;


    @Test
    public void testExecuteTaskReturnReport() {
        task = new PatrolRoadsAndHighwaysTask();
        task.setTaskPriority(TaskPriority.LOW);
        task.setDescription("Patrol the city");
        assertNotNull(task.execute());
    }

    @Test
    public void testExecuteTaskChangeStatus() {
        task = new PatrolCityTask();
        task.setTaskPriority(TaskPriority.LOW);
        task.setDescription("Patrol the city");
        task.execute();
        assertEquals(task.getStatus(), TaskStatus.DONE);
    }

    @Test
    public void testExecuteTaskReportFieldsNotNull() {
        task = new PatrolCityTask();
        task.setTaskPriority(TaskPriority.LOW);
        task.setDescription("Patrol the city");
        Report report = task.execute();
        assertNotNull(report.getTask());
        assertNotNull(report.getEndDate());
        assertNotNull(report.getStartDate());
    }
}