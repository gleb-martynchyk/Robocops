package org.jazzteam.martynchyk;

import org.jazzteam.martynchyk.tasks.TaskPriority;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.testng.AssertJUnit.assertEquals;

public class TaskPriorityComparatorTest {

    private List<TaskPriority> taskPriorityList;
    private List<TaskPriority> expectedPriorityList;

    @Test
    public void testCompare() {
        taskPriorityList = Arrays.asList(TaskPriority.LOW, TaskPriority.MIDDLE, TaskPriority.HIGH);
        expectedPriorityList = Arrays.asList(TaskPriority.HIGH, TaskPriority.MIDDLE, TaskPriority.LOW);
        Collections.sort(taskPriorityList);
        assertEquals(expectedPriorityList, taskPriorityList);
    }

    @Test
    public void testCompareWithDuplicates() {
        taskPriorityList = Arrays.asList(TaskPriority.HIGH, TaskPriority.LOW, TaskPriority.MIDDLE,
                TaskPriority.LOW, TaskPriority.HIGH, TaskPriority.MIDDLE);
        expectedPriorityList = Arrays.asList(TaskPriority.HIGH, TaskPriority.HIGH, TaskPriority.MIDDLE,
                TaskPriority.MIDDLE, TaskPriority.LOW, TaskPriority.LOW);
        Collections.sort(taskPriorityList);
        assertEquals(expectedPriorityList, taskPriorityList);
    }


}