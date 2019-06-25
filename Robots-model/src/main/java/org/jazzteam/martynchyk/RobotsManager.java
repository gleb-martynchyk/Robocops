package org.jazzteam.martynchyk;

import org.jazzteam.martynchyk.robots.Robot;

import java.util.HashSet;
import java.util.Set;

public class RobotsManager {
    private Set<Robot> robots;

    public RobotsManager() {
        this.robots = new HashSet<>();
    }

    public boolean SendTask() {
        return false;
    }

    public boolean SendTaskById() {
        return false;
    }

    public boolean SendTaskToAll() {
        return false;
    }
}
