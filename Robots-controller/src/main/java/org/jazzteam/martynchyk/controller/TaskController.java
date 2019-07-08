package org.jazzteam.martynchyk.controller;

import org.jazzteam.martynchyk.service.implementation.TaskService;
import org.jazzteam.martynchyk.tasks.BaseTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/tasks")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Iterable<BaseTask> getAll() {
        return taskService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BaseTask createTask(@RequestBody BaseTask task) {
        return taskService.create(task);
    }


    @GetMapping
    public String get() {
        return "hello controller get";
    }
}
