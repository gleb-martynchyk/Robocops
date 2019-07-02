package org.jazzteam.martynchyk.logger;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Logger {
    private static volatile Logger instance;
    private List<LoggerEntry> entries;

    private Logger() {
        this.entries = new ArrayList<>();
    }

    public static Logger getLogger() {
        Logger localInstance = instance;
        if (localInstance == null) {
            synchronized (Logger.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new Logger();
                }
            }
        }
        return localInstance;
    }

    public void info(String message) {
        entries.add(new LoggerEntry(Level.INFO, message, Thread.currentThread().getName()));
    }

    public void warn(String message) {
        entries.add(new LoggerEntry(Level.WARN, message, Thread.currentThread().getName()));
    }

    public void error(String message) {
        entries.add(new LoggerEntry(Level.ERROR, message, Thread.currentThread().getName()));
    }
}
