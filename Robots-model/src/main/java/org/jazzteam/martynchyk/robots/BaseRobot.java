package org.jazzteam.martynchyk.robots;

import org.jazzteam.martynchyk.IdGenerator;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class BaseRobot implements Robot {
    private long id;
    private String name;
    private Set<Class> allowedTasks;
    private Queue<Class> taskQueue;

    public BaseRobot() {
        this.id = IdGenerator.generateUniqueId();
        this.allowedTasks = new HashSet<>();
        this.taskQueue = new PriorityQueue<>();
    }

    @Override
    public boolean addTask() {
        return false;
    }
}
