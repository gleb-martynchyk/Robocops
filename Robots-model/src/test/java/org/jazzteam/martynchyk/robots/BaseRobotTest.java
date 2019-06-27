package org.jazzteam.martynchyk.robots;

import org.jazzteam.martynchyk.robots.implementation.OfficerRobot;
import org.jazzteam.martynchyk.tasks.BaseTask;
import org.jazzteam.martynchyk.tasks.Task;
import org.jazzteam.martynchyk.tasks.TaskPriority;
import org.jazzteam.martynchyk.tasks.implementation.GuardTask;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;

public class BaseRobotTest {

    private BaseTask task;
    private BaseRobot robot;

    @BeforeMethod
    public void setUp() {
        robot = new BaseRobot();
        task = new GuardTask();
        task.setTaskPriority(TaskPriority.LOW);
    }

    @Test
    public void testInheritorConstructorInitializeFields() {
        BaseRobot officer = new OfficerRobot();
        assertNotNull(officer.getAllowedTasks());
        assertNotNull(officer.getTaskQueue());
    }

    @Test
    public void testAddNotAllowedTask() {
        assertFalse(robot.addTask(task));
    }

    @Test
    public void testAddAllowedTask() {
        robot.getAllowedTasks().add(task.getClass());
        assertTrue(robot.addTask(task));
    }

    @Test
    public void testAddTaskWhenParentAllowed() {
        robot.getAllowedTasks().add(Task.class);
        assertFalse(robot.addTask(task));
    }


    @Test
    public void testExecuteTaskFromQueue() {
        robot.getAllowedTasks().add(task.getClass());
        robot.addTask(task);
        Report report = robot.executeTaskFromQueue();
        assertNotNull(report);
        assertTrue(report.getExecutors().contains(robot));
    }

    @Test(timeOut = 2500)
    public void testExecuteTaskInMultiThread() {
        robot.getAllowedTasks().add(task.getClass());
        robot.addTask(task);

        BaseRobot robot1 = new BaseRobot();
        BaseTask task1 = new GuardTask();
        robot1.getAllowedTasks().add(task1.getClass());
        robot1.addTask(task1);

        task.setDifficulty(2);
        task1.setDifficulty(2);

        Future<Report> future1 = robot.executeNextTask();
        Future<Report> future2 = robot1.executeNextTask();
        Report report = null;
        Report report1 = null;

        try {
            report = future1.get();
            report1 = future2.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        assertNotNull(report);
        assertNotNull(report1);
        assertEquals(report.getEndDate().getTime(), report1.getEndDate().getTime(),50);
    }
}