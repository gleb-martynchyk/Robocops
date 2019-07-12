package org.jazzteam.martynchyk.tasks;

public enum TaskPriority {
    HIGH(2),
    MIDDLE(1),
    LOW(0);

    public int priority;

    TaskPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return "TaskPriority{" +
                "priority=" + priority +
                "} " + super.toString();
    }
}
