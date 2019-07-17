package org.jazzteam.martynchyk.dao;

import org.jazzteam.martynchyk.config.TaskDaoConfig;
import org.jazzteam.martynchyk.dao.implementation.TaskDao;
import org.jazzteam.martynchyk.tasks.BaseTask;
import org.jazzteam.martynchyk.tasks.TaskPriority;
import org.jazzteam.martynchyk.tasks.TaskStatus;
import org.jazzteam.martynchyk.tasks.implementation.GuardTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.testng.Assert.assertNull;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;

@WebAppConfiguration
@ContextConfiguration(classes = {TaskDaoConfig.class})
public class TaskDaoTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private TaskDao taskDao;
    private BaseTask expectedTask;

    @Test(dataProviderClass = TaskDaoDataSource.class, dataProvider = "TasksAndPriorityTask")
    public void testFindNextToAssign(List<BaseTask> tasks) {
        for (BaseTask task : tasks) {
            taskDao.create(task);
        }
        expectedTask = tasks.get(0);

        assertEquals(expectedTask, taskDao.findNext());

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
    public void testUpdate_UpdateStatus() {
        expectedTask = new BaseTask("1", TaskPriority.HIGH, TaskStatus.CREATED, new Date(0));
        taskDao.create(expectedTask);

        BaseTask task = taskDao.findById(expectedTask.getId());
        task.setStatus(TaskStatus.ASSIGNED);
        taskDao.update(task);
        BaseTask actualTask = taskDao.findById(expectedTask.getId());
        assertEquals(actualTask.getStatus(), TaskStatus.ASSIGNED);
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