package org.jazzteam.martynchyk;

import org.jazzteam.martynchyk.tasks.Task;

import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

public class TaskQueue {
    private Queue<Task> taskQueue;

    public TaskQueue() {
        this.taskQueue = new PriorityBlockingQueue<>();
    }

    public Task poll() {
        return taskQueue.poll();
    }

    public Task peek() {
        return taskQueue.peek();
    }

    public boolean offer(Task task) {
        return taskQueue.offer(task);
    }
}
