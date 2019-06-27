package org.jazzteam.martynchyk.dao;

import org.jazzteam.martynchyk.dao.implementation.TaskDao;
import org.jazzteam.martynchyk.tasks.BaseTask;
import org.jazzteam.martynchyk.tasks.TaskPriority;
import org.jazzteam.martynchyk.tasks.TaskStatus;
import org.jazzteam.martynchyk.tasks.implementation.GuardTask;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertNull;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;

public class TaskDaoTest {

    private TaskDao taskDao;
    private BaseTask expectedTask;

    @BeforeMethod
    public void setUp() {
        taskDao = new TaskDao();
    }


    @Test(dataProviderClass = TaskDaoDataSource.class, dataProvider = "TasksAndPriorityTask")
    public void testFindParametrized(List<BaseTask> tasks) {
        for (BaseTask task : tasks) {
            taskDao.create(task);
        }
        expectedTask = tasks.get(0);

        assertEquals(expectedTask, taskDao.findPriority());

        for (BaseTask task : tasks) {
            taskDao.deleteById(task.getId());
        }
    }

    @Test
    public void testDeleteById() {
        expectedTask = new BaseTask();
        expectedTask.setDescription("Arrest Johnny");
        expectedTask.setTaskPriority(TaskPriority.LOW);
        expectedTask.setStatus(TaskStatus.CREATED);
        taskDao.create(expectedTask);
        taskDao.deleteById(expectedTask.getId());
        assertFalse(taskDao.findAll().iterator().hasNext());
    }

    @Test
    public void testFindAllWhenDBEmpty() {
        assertFalse(taskDao.findAll().iterator().hasNext());
    }

    @Test
    public void testFindNotExistingTask() {
        expectedTask = new BaseTask();
        assertNull(taskDao.findById(expectedTask.getId()));
    }

    @Test
    public void testCreateAndFind() {
        expectedTask = new BaseTask();
        expectedTask.setDescription("Arrest Johnny");
        expectedTask.setTaskPriority(TaskPriority.LOW);
        expectedTask.setStatus(TaskStatus.CREATED);

        taskDao.create(expectedTask);
        BaseTask actualTask = taskDao.findById(expectedTask.getId());
        assertEquals(actualTask, expectedTask);
        taskDao.deleteById(expectedTask.getId());
    }

    //TODO исправить, чтобы наследники так же могли возращаться с бд
    @Test
    public void testCreateAndFindGuardTask() {
        expectedTask = new GuardTask();
        expectedTask.setDescription("Arrest Johnny");
        expectedTask.setTaskPriority(TaskPriority.LOW);
        expectedTask.setStatus(TaskStatus.CREATED);

        taskDao.create(expectedTask);
        BaseTask actualTask = taskDao.findById(expectedTask.getId());
        assertEquals(actualTask, expectedTask);
        taskDao.deleteById(expectedTask.getId());
    }

    @Test
    public void testUpdateWhenNothingChanges() {
        expectedTask = new BaseTask();
        expectedTask.setDescription("Arrest Johnny");
        expectedTask.setTaskPriority(TaskPriority.LOW);
        expectedTask.setStatus(TaskStatus.CREATED);
        taskDao.create(expectedTask);

        taskDao.update(expectedTask);
        BaseTask actualTask = taskDao.findById(expectedTask.getId());
        assertEquals(actualTask, expectedTask);
        taskDao.deleteById(expectedTask.getId());
    }

    @Test
    public void testUpdate() {
        expectedTask = new BaseTask();
        expectedTask.setDescription("Arrest Johnny");
        expectedTask.setTaskPriority(TaskPriority.LOW);
        expectedTask.setStatus(TaskStatus.CREATED);
        taskDao.create(expectedTask);

        expectedTask.setDescription("Updated description");
        expectedTask.setTaskPriority(TaskPriority.HIGH);
        expectedTask.setStatus(TaskStatus.DONE);
        taskDao.update(expectedTask);
        BaseTask actualTask = taskDao.findById(expectedTask.getId());
        assertEquals(actualTask, expectedTask);
        taskDao.deleteById(expectedTask.getId());
    }

    @Test
    public void testFindAllAndDelete() {
        BaseTask taskA = new BaseTask();
        BaseTask taskB = new BaseTask();
        BaseTask taskC = new BaseTask();

        taskDao.create(taskA);
        taskDao.create(taskB);
        taskDao.create(taskC);

        List<BaseTask> tasksExpected = new ArrayList<>();
        tasksExpected.add(taskA);
        tasksExpected.add(taskB);
        tasksExpected.add(taskC);

        Assert.assertEquals(taskDao.findAll(), tasksExpected);

        List<Long> ids = new ArrayList<>();
        for (BaseTask task : tasksExpected) {
            ids.add(task.getId());
        }
        taskDao.deleteMany(ids);
    }
}