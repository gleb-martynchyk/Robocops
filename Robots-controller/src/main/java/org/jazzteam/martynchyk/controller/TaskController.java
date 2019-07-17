package org.jazzteam.martynchyk.controller;

import org.jazzteam.martynchyk.service.implementation.TaskService;
import org.jazzteam.martynchyk.tasks.BaseTask;
import org.jazzteam.martynchyk.tasks.implementation.GuardTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@CrossOrigin(maxAge = 3600)
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

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public BaseTask updateTask(@RequestBody BaseTask task) {
        taskService.update(task);
        return taskService.findById(task.getId());
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTask(@PathVariable("id") Long id) {
        taskService.deleteById(id);
    }


    @GetMapping(path = "json")
    public BaseTask getJson() {
        return new BaseTask();
    }

    @GetMapping(path = "jsons")
    public List<BaseTask> getJsons() {
        return Arrays.asList(new BaseTask(), new GuardTask());
    }
}
