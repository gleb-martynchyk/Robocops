package org.jazzteam.martynchyk.service;

import org.jazzteam.martynchyk.config.RobotServiceConfig;
import org.jazzteam.martynchyk.config.TaskServiceConfig;
import org.jazzteam.martynchyk.robots.BaseRobot;
import org.jazzteam.martynchyk.robots.Report;
import org.jazzteam.martynchyk.robots.Robot;
import org.jazzteam.martynchyk.service.implementation.TaskService;
import org.jazzteam.martynchyk.tasks.BaseTask;
import org.jazzteam.martynchyk.tasks.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
        robotsService.getRobotsReports().clear();
        robotsService.getRobotsFutures().clear();
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
    //TODO чтобы задачи распределялись равномерно
    @Test(dataProviderClass = RobotsServiceDataSource.class, dataProvider = "TasksToExecute")
    public void testStartExecution_TwoRobotsExecuteAllTasksFromsRobotsService(List<BaseTask> baseTasks) {
        int tasksToExecute = (int) baseTasks.stream()
                .filter(baseTask -> baseTask.getStatus().equals(TaskStatus.CREATED))
                .count();

        BaseRobot robot1 = new BaseRobot();
        BaseRobot robot2 = new BaseRobot();
        robotsService.addRobot(robot1);
        robotsService.addRobot(robot2);
        robotsService.startAllRobots();

        for (BaseTask task : baseTasks) {
            task.setDifficultyMilliseconds(1);
            robot1.getAllowedTasks().add(task.getClass());
            robot2.getAllowedTasks().add(task.getClass());
            taskService.create(task);
        }

        //start method startExecution in other thread
        Runnable task = robotsService::startExecution;
        new Thread(task).start();
        try {
            TimeUnit.SECONDS.sleep(8);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //TODO нужно поставить таймер с циклом, чтобы ожидать меньше
//        try {
//            TimeUnit.MILLISECONDS.sleep(1000);
//            while (robotsService.numberOfRunningTasks() != 0) {
//                TimeUnit.MILLISECONDS.sleep(100);
//            }
//            robotsService.stopExecution();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        robotsService.startExecution();

        int doneTaskAmount = 0;
        for (Map.Entry<Robot, Set<Report>> robotReports : robotsService.getRobotsReports().entrySet()) {
            doneTaskAmount += robotReports.getValue().size();
        }

        taskService.deleteMany(baseTasks.stream()
                .map(BaseTask::getId)
                .collect(Collectors.toCollection(ArrayList::new)));

        assertEquals(tasksToExecute, doneTaskAmount);
    }


    @Test(dataProviderClass = RobotsServiceDataSource.class, dataProvider = "TasksToExecute")
    public void testCollectReports(List<BaseTask> baseTasks) {
        BaseRobot robot1 = new BaseRobot();
        BaseRobot robot2 = new BaseRobot();
        robotsService.addRobot(robot1);
        robotsService.addRobot(robot2);

        Set<BaseTask> robot1Queue = new HashSet<>();
        Set<BaseTask> robot2Queue = new HashSet<>();

        for (int i = 0; i < baseTasks.size(); i++) {
            BaseTask task = baseTasks.get(i);
            task.setDifficultyMilliseconds(1);
            robot1.getAllowedTasks().add(task.getClass());
            robot2.getAllowedTasks().add(task.getClass());
            if (i % 2 == 0) {
                if (robot1.addTask(task))
                    robot1Queue.add(task);
            } else {
                if (robot2.addTask(task))
                    robot2Queue.add(task);
            }
        }

        robot1.executeAllFromQueue();
        robot2.executeAllFromQueue();

        robotsService.collectReports();
        robotsService.collectReports();

        if (robot1Queue.isEmpty()) {
            assertFalse(robotsService.getRobotsFutures().containsKey(robot1));
        } else {
            assertEquals(robotsService.getRobotsFutures().get(robot1), robot1.getFutureReports());
        }
        if (robot2Queue.isEmpty()) {
            assertFalse(robotsService.getRobotsFutures().containsKey(robot2));
        } else {
            assertEquals(robotsService.getRobotsFutures().get(robot2), robot2.getFutureReports());
        }
    }

    @Test(dataProviderClass = RobotsServiceDataSource.class, dataProvider = "TasksToExecute")
    public void testUpdateTasksInDatabase(List<BaseTask> baseTasks) {
        BaseRobot robot1 = new BaseRobot();
        BaseRobot robot2 = new BaseRobot();
        robotsService.addRobot(robot1);
        robotsService.addRobot(robot2);

        Set<BaseTask> robot1Queue = new HashSet<>();
        Set<BaseTask> robot2Queue = new HashSet<>();
        Map<BaseTask, Boolean> taskResults = new HashMap<>();
        for (int i = 0; i < baseTasks.size(); i++) {
            BaseTask task = baseTasks.get(i);
            task.setDifficultyMilliseconds(1);
            robot1.getAllowedTasks().add(task.getClass());
            robot2.getAllowedTasks().add(task.getClass());
            taskService.create(task);
            if (i % 2 == 0) {
                if (robot1.addTask(task))
                    robot1Queue.add(task);
            } else {
                if (robot2.addTask(task))
                    robot2Queue.add(task);
            }
        }
        robot1.executeAllFromQueue();
        robot2.executeAllFromQueue();

        robotsService.collectReports();
        robotsService.updateTasksInDatabase();

        for (BaseTask task : robot1Queue) {
            taskResults.put(task, taskService.findById(task.getId()).getStatus() == TaskStatus.DONE);
        }
        for (BaseTask task : robot2Queue) {
            taskResults.put(task, taskService.findById(task.getId()).getStatus() == TaskStatus.DONE);
        }

        taskService.deleteMany(baseTasks.stream().map(BaseTask::getId)
                .collect(Collectors.toCollection(ArrayList::new)));

        assertFalse(taskResults.containsValue(false));
    }

// TODO потом дописать
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
        taskService.deleteById(task.getId());
        assertEquals(robotActual, expectedRobot);
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
        taskService.deleteById(task.getId());
        assertNull(robotsService.sendTask());
    }

    @Test
    public void testSendTaskToRobot() {
        BaseTask task = new BaseTask();
        taskService.create(task);
        BaseRobot robotExpected = new BaseRobot();
        robotExpected.getAllowedTasks().add(task.getClass());
        robotsService.addRobot(robotExpected);
        BaseRobot robotActual = (BaseRobot) robotsService.sendTask(robotExpected);
        taskService.deleteById(task.getId());
        assertEquals(robotActual, robotExpected);
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
        boolean isEmptyRobotsAssignedSet = robotsService.sendTask(robotSet).isEmpty();
        taskService.deleteById(task.getId());
        assertTrue(isEmptyRobotsAssignedSet);
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
        taskService.deleteById(task.getId());

        assertEquals(expectedSet, actualSet);
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