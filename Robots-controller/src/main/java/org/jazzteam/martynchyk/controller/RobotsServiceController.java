package org.jazzteam.martynchyk.controller;

import org.jazzteam.martynchyk.service.RobotsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping(value = "api/v1/robots-service")
public class RobotsServiceController {
    private final RobotsService robotsService;

    @Autowired
    public RobotsServiceController(RobotsService robotsService) {
        this.robotsService = robotsService;
    }

    @GetMapping(path = "/robots")
    @ResponseStatus(HttpStatus.OK)
    public void startOrStopAllRobots(@RequestParam(value = "command") String command) {
        switch (command) {
            case "start":
                robotsService.startAllRobots();
                break;
            case "stop":
                robotsService.stopAllRobots();
                break;
        }
    }

    @GetMapping(path = "/{command}")
    @ResponseStatus(HttpStatus.OK)
    public void startExecution(@PathVariable("command") String command) {
        switch (command) {
            case "start":
                robotsService.startExecutionInThread();
                break;
            case "stop":
                robotsService.stopExecution();
                break;
        }
    }
}
