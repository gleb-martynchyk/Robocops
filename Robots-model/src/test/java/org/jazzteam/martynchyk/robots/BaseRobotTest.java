package org.jazzteam.martynchyk.robots;

import org.jazzteam.martynchyk.robots.implementation.OfficerRobot;
import org.jazzteam.martynchyk.tasks.BaseTask;
import org.jazzteam.martynchyk.tasks.Task;
import org.jazzteam.martynchyk.tasks.TaskPriority;
import org.jazzteam.martynchyk.tasks.TaskStatus;
import org.jazzteam.martynchyk.tasks.implementation.GuardTask;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.*;
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
        assertEquals(3, robot.getTaskQueue().size());
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
        assertEquals(report.getExecutor(), robot);
    }

    @Test(timeOut = 1500)
    public void testStartExecution_ParallelExecuteTasks() {
        BaseRobot robot1 = new BaseRobot();
        BaseTask task1 = new GuardTask();

        robot.getAllowedTasks().add(task.getClass());
        robot.addTask(task);

        robot1.getAllowedTasks().add(task1.getClass());
        robot1.addTask(task1);

        task.setDifficultyMilliseconds(1000);
        task1.setDifficultyMilliseconds(1000);

        robot.startExecution();
        robot1.startExecution();

        try {
            TimeUnit.MILLISECONDS.sleep(1050);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        robot.stopExecution();
        robot1.stopExecution();

        Report report = robot.getReports().iterator().next();
        Report report1 = robot1.getReports().iterator().next();

        assertEquals((double) report.getStartDate().getTime(), (double) report1.getStartDate().getTime(), 10);
    }

    @Test
    public void testStartExecution_CantExecuteTwoTasksAtOneMoment() {
        BaseTask task1 = new GuardTask();
        robot.getAllowedTasks().add(task.getClass());
        robot.addTask(task);
        robot.addTask(task1);

        task.setDifficultyMilliseconds(200);
        task1.setDifficultyMilliseconds(300);

        robot.startExecution();

        try {
            TimeUnit.MILLISECONDS.sleep(600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        robot.stopExecution();
        Iterator reports = robot.getReports().iterator();
        Report report = (Report) reports.next();
        Report report1 = (Report) reports.next();

        assertNotEquals((double) report.getStartDate().getTime(), (double) report1.getStartDate().getTime(), 10);
    }

    @Test
    public void testStartExecution_OneTaskInTwoRobots() {
        BaseRobot robot1 = new BaseRobot();
        task.setDifficultyMilliseconds(1000);

        robot.getAllowedTasks().add(task.getClass());
        robot.addTask(task);
        robot1.getAllowedTasks().add(task.getClass());
        robot1.addTask(task);

        robot.startExecution();
        robot1.startExecution();

        try {
            TimeUnit.MILLISECONDS.sleep(1050);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        robot.stopExecution();
        robot1.stopExecution();

        boolean report = robot.getReports().iterator().hasNext();
        boolean report1 = robot1.getReports().iterator().hasNext();

        boolean result = report && !report1 || !report && report1;
        assertTrue(result);
    }

    @Test(dataProviderClass = BaseRobotDataSource.class, dataProvider = "TasksToExecute")
    public void testExecuteAllFromQueue_OneExecuteAllTasks(List<BaseTask> baseTasks) {
        for (BaseTask task : baseTasks) {
            task.setDifficultyMilliseconds(1);
            robot.getAllowedTasks().add(task.getClass());
            robot.addTask(task);
        }

        robot.executeAllFromQueue();

        int doneTaskAmount = (int) baseTasks.stream()
                .filter(baseTask -> !baseTask.getStatus().equals(TaskStatus.CREATED))
                .count();

        assertEquals(doneTaskAmount, baseTasks.size());
    }

    @Test(dataProviderClass = BaseRobotDataSource.class, dataProvider = "TasksToExecute")
    public void testExecuteAllFromQueue_CollectReports(List<BaseTask> baseTasks) {
        int tasksToExecute = (int) baseTasks.stream()
                .filter(baseTask -> baseTask.getStatus().equals(TaskStatus.CREATED))
                .count();

        for (BaseTask task : baseTasks) {
            task.setDifficultyMilliseconds(1);
            robot.getAllowedTasks().add(task.getClass());
            robot.addTask(task);
        }

        robot.executeAllFromQueue();

        int futuresAmount = robot.getReports().size();
        assertEquals(futuresAmount, tasksToExecute);
    }

    @Test
    public void testExecuteAllFromQueue_CollectAndCreateReports() {
        BaseTask task1 = new GuardTask();
        robot.getAllowedTasks().add(task.getClass());
        robot.addTask(task);
        robot.addTask(task1);

        task.setDifficultyMilliseconds(1);
        task1.setDifficultyMilliseconds(1);

        robot.executeAllFromQueue();

        assertEquals(robot.getReports().size(), 2);
    }

    //TODO слишком долго выполняется
    @Test(dataProviderClass = BaseRobotDataSource.class, dataProvider = "TasksToExecute")
    public void testStartExecution_CollectReports(List<BaseTask> baseTasks) {
        int tasksToExecute = (int) baseTasks.stream()
                .filter(baseTask -> baseTask.getStatus().equals(TaskStatus.CREATED))
                .count();

        for (BaseTask task : baseTasks) {
            task.setDifficultyMilliseconds(1);
            robot.getAllowedTasks().add(task.getClass());
            robot.addTask(task);
        }

        robot.startExecution();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        robot.stopExecution();

        int futuresAmount = robot.getReports().size();

        assertEquals(futuresAmount, tasksToExecute);
    }

}