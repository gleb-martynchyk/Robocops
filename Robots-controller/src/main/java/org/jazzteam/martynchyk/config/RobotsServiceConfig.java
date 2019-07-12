package org.jazzteam.martynchyk.config;

import org.jazzteam.martynchyk.service.RobotsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "org.jazzteam.martynchyk.config")
public class RobotsServiceConfig {
    @Bean
//    @Scope("prototype")
    public RobotsService robotsService() {
        return new RobotsService();
    }
}
