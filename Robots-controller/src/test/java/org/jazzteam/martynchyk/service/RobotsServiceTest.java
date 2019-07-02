package org.jazzteam.martynchyk.service;

import org.jazzteam.martynchyk.config.RobotServiceConfig;
import org.jazzteam.martynchyk.config.TaskServiceConfig;
import org.jazzteam.martynchyk.robots.BaseRobot;
import org.jazzteam.martynchyk.robots.Robot;
import org.jazzteam.martynchyk.service.implementation.TaskService;
import org.jazzteam.martynchyk.tasks.BaseTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

import static org.testng.Assert.*;


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

    //TODO потом дописать
//    @Test
//    public void testStartExecution() {
//    }
//
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

        Set<Robot> actualSet = new HashSet<>();
        actualSet.add(robot1);

        assertEquals(robotsService.sendTask(robotSet), actualSet);
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