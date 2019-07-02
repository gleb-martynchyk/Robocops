package org.jazzteam.martynchyk.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("tasks/")
public class TaskController {
    @GetMapping
    public String get() {
        return "hello controller get";
    }
}
