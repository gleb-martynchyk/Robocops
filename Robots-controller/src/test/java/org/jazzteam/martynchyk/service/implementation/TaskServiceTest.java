package org.jazzteam.martynchyk.service.implementation;

import org.jazzteam.martynchyk.config.TaskServiceConfig;
import org.jazzteam.martynchyk.tasks.BaseTask;
import org.jazzteam.martynchyk.tasks.implementation.GuardTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

@WebAppConfiguration
@ContextConfiguration(classes = {TaskServiceConfig.class})
public class TaskServiceTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private TaskService taskService;

    @Test
    public void testHasNextToExecute() {
        BaseTask task = new GuardTask();
        taskService.create(task);
        assertTrue(taskService.hasNextToExecute());
        taskService.deleteById(task.getId());
    }

    @Test(dataProviderClass = TaskServiceDataSource.class, dataProvider = "TasksAndHasNextResult")
    public void testHasNextToExecuteParametrized(List<BaseTask> tasks, boolean result) {
        for (BaseTask task : tasks) {
            taskService.create(task);
        }

        assertEquals(taskService.hasNextToExecute(), result);

        for (BaseTask task : tasks) {
            taskService.deleteById(task.getId());
        }
    }
}