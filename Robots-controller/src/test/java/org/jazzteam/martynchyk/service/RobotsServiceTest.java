package org.jazzteam.martynchyk.service;

import org.jazzteam.martynchyk.config.RobotServiceConfig;
import org.jazzteam.martynchyk.config.TaskServiceConfig;
import org.jazzteam.martynchyk.robots.BaseRobot;
import org.jazzteam.martynchyk.robots.Robot;
import org.jazzteam.martynchyk.service.implementation.TaskService;
import org.jazzteam.martynchyk.tasks.BaseTask;
import org.jazzteam.martynchyk.tasks.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.*;
import static org.testng.AssertJUnit.assertEquals;


@WebAppConfiguration
@ContextConfiguration(classes = {TaskServiceConfig.class, RobotServiceConfig.class})
public class RobotsServiceTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private TaskService taskService;
    @Autowired
    private RobotsService robotsService;

    @BeforeMethod
    public void setUp() {
        robotsService.getRobots().clear();
    }

    @Test
    public void testAddRobot() {
        Robot robot = new BaseRobot();
        robotsService.addRobot(robot);
        assertTrue(robotsService.getRobots().contains(robot));
    }

    @Test
    public void testRemoveRobot() {
        Robot robot = new BaseRobot();
        robotsService.addRobot(robot);
        robotsService.removeRobot(robot);
        assertFalse(robotsService.getRobots().contains(robot));
    }


    //TODO доделать чтобы изменения сохранялись в бд
    @Ignore
    @Test(dataProviderClass = RobotsServiceDataSource.class, dataProvider = "TasksToExecute")
    public void testStartExecution_TwoRobotsExecuteAllTasks(List<BaseTask> baseTasks) {
        BaseRobot robot1 = new BaseRobot();
        BaseRobot robot2 = new BaseRobot();
        robotsService.addRobot(robot1);
        robotsService.addRobot(robot2);

        for (BaseTask task : baseTasks) {
            task.setDifficultyMilliseconds(1);
            robot1.getAllowedTasks().add(task.getClass());
            robot2.getAllowedTasks().add(task.getClass());
            taskService.create(task);
        }

        //start method startExecution in other thread
        Runnable task = robotsService::startExecution;
        Thread thread = new Thread(task);
        thread.start();
        try {
            TimeUnit.SECONDS.sleep(1);
            robotsService.stopExecution();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        robotsService.startExecution();

        int doneTaskAmount = (int) baseTasks.stream()
                .filter(baseTask -> baseTask.getStatus().equals(TaskStatus.DONE))
                .count();

        assertEquals(doneTaskAmount, baseTasks.size());
    }

    //TODO потом дописать
//    @Test
//    public void testStopExecution() {
//    }

    @Test
    public void testSendTask() {
        BaseTask task = new BaseTask();
        taskService.create(task);
        BaseRobot expectedRobot = new BaseRobot();
        expectedRobot.getAllowedTasks().add(task.getClass());
        robotsService.addRobot(expectedRobot);
        BaseRobot robotActual = (BaseRobot) robotsService.sendTask();
        assertEquals(robotActual, expectedRobot);
        taskService.deleteById(task.getId());
    }

    @Test
    public void testSendTask_ReturnNullWhenNoTasksToExecute() {
        robotsService.addRobot(new BaseRobot());
        assertNull(robotsService.sendTask());
    }

    @Test
    public void testSendTask_ReturnNullWhenNoRobotsToExecuteTask() {
        BaseTask task = new BaseTask();
        taskService.create(task);
        assertNull(robotsService.sendTask());
        taskService.deleteById(task.getId());
    }

    @Test
    public void testSendTaskToRobot() {
        BaseTask task = new BaseTask();
        taskService.create(task);
        BaseRobot robotExpected = new BaseRobot();
        robotExpected.getAllowedTasks().add(task.getClass());
        robotsService.addRobot(robotExpected);
        BaseRobot robotActual = (BaseRobot) robotsService.sendTask(robotExpected);
        assertEquals(robotActual, robotExpected);
        taskService.deleteById(task.getId());
    }

    @Test
    public void testSendTaskToRobot_ReturnNull() {
        BaseRobot robotExpected = new BaseRobot();
        robotsService.addRobot(robotExpected);
        assertNull(robotsService.sendTask(robotExpected));
    }

    @Test
    public void testSendTaskToRobots_ReturnEmptySetWhenNoRobotsToExecute() {
        BaseTask task = new BaseTask();
        taskService.create(task);
        Robot robot1 = new BaseRobot();
        Robot robot2 = new BaseRobot();
        Robot robot3 = new BaseRobot();
        Set<Robot> robotSet = new HashSet<>();
        robotSet.add(robot1);
        robotSet.add(robot2);
        robotSet.add(robot3);

        assertTrue(robotsService.sendTask(robotSet).isEmpty());
        taskService.deleteById(task.getId());
    }

    @Test
    public void testSendTaskToRobots() {
        BaseTask task = new BaseTask();
        taskService.create(task);
        BaseRobot robot1 = new BaseRobot();
        robot1.getAllowedTasks().add(task.getClass());
        BaseRobot robot2 = new BaseRobot();
        BaseRobot robot3 = new BaseRobot();

        Set<Robot> robotSet = new HashSet<>();
        robotSet.add(robot1);
        robotSet.add(robot2);
        robotSet.add(robot3);

        Set<Robot> actualSet = robotsService.sendTask(robotSet);

        Set<Robot> expectedSet = new HashSet<>();
        expectedSet.add(robot1);

        assertEquals(expectedSet, actualSet);
        taskService.deleteById(task.getId());
    }

    @Test
    public void testSendTaskToRobots_ReturnNullWhenNoTasksToExecute() {
        Set<Robot> robotSet = new HashSet<>();
        robotSet.add(new BaseRobot());
        robotSet.add(new BaseRobot());
        robotSet.add(new BaseRobot());

        assertNull(robotsService.sendTask(robotSet));
    }
}