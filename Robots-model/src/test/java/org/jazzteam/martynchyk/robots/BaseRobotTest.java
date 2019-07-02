package org.jazzteam.martynchyk.robots;

import org.jazzteam.martynchyk.robots.implementation.OfficerRobot;
import org.jazzteam.martynchyk.tasks.BaseTask;
import org.jazzteam.martynchyk.tasks.Task;
import org.jazzteam.martynchyk.tasks.TaskPriority;
import org.jazzteam.martynchyk.tasks.TaskStatus;
import org.jazzteam.martynchyk.tasks.implementation.GuardTask;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
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
    public void testAddTask_Many() {
        robot.getAllowedTasks().add(GuardTask.class);
        robot.addTask(new GuardTask());
        robot.addTask(new GuardTask());
        robot.addTask(new GuardTask());
        assertEquals( 3,robot.getTaskQueue().size());
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

    @Test(timeOut = 1500)
    public void testExecuteTaskInMultiThread() {
        BaseRobot robot1 = new BaseRobot();
        BaseTask task1 = new GuardTask();

        robot.getAllowedTasks().add(task.getClass());
        robot.addTask(task);

        robot1.getAllowedTasks().add(task1.getClass());
        robot1.addTask(task1);

        task.setDifficultyMilliseconds(1000);
        task1.setDifficultyMilliseconds(1000);

        Future<Report> future1 = robot.executeTaskFromQueueMultiThread();
        Future<Report> future2 = robot1.executeTaskFromQueueMultiThread();
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

        assertEquals(report.getStartDate().getTime(), report1.getStartDate().getTime(), 50);
    }

    @Test
    public void testExecuteTaskCantExecuteTwoTasksAtOneMoment() {
        BaseTask task1 = new GuardTask();
        robot.getAllowedTasks().add(task.getClass());
        robot.addTask(task);
        robot.addTask(task1);

        task.setDifficultyMilliseconds(2000);
        task1.setDifficultyMilliseconds(3000);

        Future<Report> future1 = robot.executeTaskFromQueueMultiThread();
        Future<Report> future2 = robot.executeTaskFromQueueMultiThread();
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
        assertEquals(Math.abs(report.getEndDate().getTime() - report1.getEndDate().getTime()),
                task1.getDifficultyMilliseconds(), 50);
    }

    @Test
    public void testExecuteTaskFromQueue_OneTaskInTwoRobots() {
        BaseRobot robot1 = new BaseRobot();
        task.setDifficultyMilliseconds(1000);

        robot.getAllowedTasks().add(task.getClass());
        robot.addTask(task);
        robot1.getAllowedTasks().add(task.getClass());
        robot1.addTask(task);

        Future<Report> future1 = robot.executeTaskFromQueueMultiThread();
        Future<Report> future2 = robot1.executeTaskFromQueueMultiThread();
        Report report1 = null;
        Report report2 = null;

        try {
            report1 = future1.get();
            report2 = future2.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        boolean oneOfReportNotNull = report1 != null && report2 == null || report1 == null && report2 != null;
        assertTrue(oneOfReportNotNull);
    }

    @Test(dataProviderClass = BaseRobotDataSource.class, dataProvider = "TasksToExecute")
    public void testStartExecution_OneExecuteAllTasks(List<BaseTask> baseTasks) {

        for (BaseTask task : baseTasks) {
            task.setDifficultyMilliseconds(1);
            robot.getAllowedTasks().add(task.getClass());
            robot.addTask(task);
        }

        robot.startExecution();

        int doneTaskAmount = (int) baseTasks.stream()
                .filter(baseTask -> !baseTask.getStatus().equals(TaskStatus.CREATED))
                .count();

        assertEquals(doneTaskAmount, baseTasks.size());
    }
}