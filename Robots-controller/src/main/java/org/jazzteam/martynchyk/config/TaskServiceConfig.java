package org.jazzteam.martynchyk.config;

import org.jazzteam.martynchyk.service.implementation.TaskService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "org.jazzteam.martynchyk.config")
public class TaskServiceConfig {
    @Bean
    public TaskService taskService() {
        return new TaskService();
    }
}
