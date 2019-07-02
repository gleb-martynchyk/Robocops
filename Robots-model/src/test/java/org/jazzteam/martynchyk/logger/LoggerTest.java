package org.jazzteam.martynchyk.logger;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class LoggerTest {

    private Logger log;
    private String message = "this is info message";

    @Test
    public void testGetLogger() {
        assertNotNull(Logger.getLogger());
    }

    @Test
    public void testGetLogger_ReturnPreviousObject() {
        Logger log = Logger.getLogger();
        assertEquals(Logger.getLogger(), log);
    }

    @Test
    public void testInfo_SaveEntry() {
        log = Logger.getLogger();
        log.info(message);
        assertFalse(log.getEntries().isEmpty());
    }

    @Test
    public void testInfo_EntryEquals() {
        LoggerEntry entryExpected = new LoggerEntry(Level.INFO, message, Thread.currentThread().getName());
        log = Logger.getLogger();
        log.info(message);
        assertNotEquals(log.getEntries().get(0), entryExpected);
    }

    @Test
    public void testWarn_SaveEntry() {
        log = Logger.getLogger();
        log.warn(message);
        assertFalse(log.getEntries().isEmpty());
    }

    @Test
    public void testWarn_EntryEquals() {
        LoggerEntry entryExpected = new LoggerEntry(Level.WARN, message, Thread.currentThread().getName());
        log = Logger.getLogger();
        log.warn(message);
        assertNotEquals(log.getEntries().get(0), entryExpected);
    }

    @Test
    public void testError_SaveEntry() {
        log = Logger.getLogger();
        log.error(message);
        assertFalse(log.getEntries().isEmpty());
    }

    @Test
    public void testError_EntryEquals() {
        LoggerEntry entryExpected = new LoggerEntry(Level.ERROR, message, Thread.currentThread().getName());
        log = Logger.getLogger();
        log.error(message);
        assertNotEquals(log.getEntries().get(0), entryExpected);
    }
}