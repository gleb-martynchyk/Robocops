package org.jazzteam.martynchyk.config;

import org.jazzteam.martynchyk.service.RobotsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@ComponentScan(basePackages = "org.jazzteam.martynchyk.config")
public class RobotServiceConfig {
    @Bean
    @Scope("singleton")
    public RobotsService robotsService() {
        return new RobotsService();
    }
}
