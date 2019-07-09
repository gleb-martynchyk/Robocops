package org.jazzteam.martynchyk.controller;

import org.jazzteam.martynchyk.service.implementation.TaskService;
import org.jazzteam.martynchyk.tasks.BaseTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(value = "api/v1/tasks")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BaseTask get(@PathVariable("id") Long id) {
        return taskService.findById(id);
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BaseTask> getAll() {
        return StreamSupport.stream(taskService.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BaseTask createTask(@RequestBody BaseTask task) {
        return taskService.create(task);
    }


    @GetMapping(path = "json")
    public BaseTask get() {
        return new BaseTask();
    }
}
