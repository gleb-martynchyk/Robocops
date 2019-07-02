package org.jazzteam.martynchyk.logger;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class LoggerEntry {
    private Level level;
    private String message;
    private String threadName;
    private Date date;

    public LoggerEntry(Level level, String message, String threadName) {
        this.level = level;
        this.message = message;
        this.threadName = threadName;
        this.date = new Date();
    }
}
