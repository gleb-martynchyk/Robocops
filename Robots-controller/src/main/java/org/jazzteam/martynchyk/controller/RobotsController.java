package org.jazzteam.martynchyk.controller;

import org.jazzteam.martynchyk.robots.BaseRobot;
import org.jazzteam.martynchyk.robots.Robot;
import org.jazzteam.martynchyk.service.RobotsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping(value = "api/v1/robots")
public class RobotsController {
    private final RobotsService robotsService;

    @Autowired
    public RobotsController(RobotsService robotsService) {
        this.robotsService = robotsService;
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Robot get(@PathVariable("id") Long id) {
        return robotsService.findRobotById(id);
    }

    @GetMapping(path = "start-robots")
    @ResponseStatus(HttpStatus.OK)
    public void startAllRobots() {
        robotsService.startAllRobots();
    }

    @GetMapping(path = "start-execution")
    @ResponseStatus(HttpStatus.OK)
    public void startExecution() {
        robotsService.startExecution();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Set<Robot> getAll() {
        return robotsService.getRobots();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BaseRobot createRobot(@RequestBody BaseRobot robot) {
        robotsService.addRobot(robot);
        return robotsService.findRobotById(robot.getId());
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public BaseRobot updateRobot(@RequestBody BaseRobot robotNew) {
        BaseRobot robotOld = robotsService.findRobotById(robotNew.getId());
        robotOld.setAllowedTasks(robotNew.getAllowedTasks());
        robotOld.setName(robotOld.getName());
        return robotOld;
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteRobot(@PathVariable("id") Long id) {
        robotsService.removeRobot(robotsService.findRobotById(id));
    }

    @GetMapping(path = "json")
    public BaseRobot get() {
        return new BaseRobot();
    }
}
