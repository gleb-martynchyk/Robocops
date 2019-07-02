package org.jazzteam.martynchyk.config;

import org.jazzteam.martynchyk.dao.implementation.TaskDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "org.jazzteam.martynchyk.config")
public class TaskDaoConfig  {
    @Bean
    public TaskDao cityDao() {
        return new TaskDao();
    }
}
