package org.jazzteam.martynchyk;

import org.jazzteam.martynchyk.logger.Logger;
import org.jazzteam.martynchyk.logger.LoggerEntry;
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
@RequestMapping(value = "api/v1/logs")
public class LoggerController {
    private Logger log = Logger.getLogger();

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<LoggerEntry> getAll() {
        return log.getEntries();
    }
}
