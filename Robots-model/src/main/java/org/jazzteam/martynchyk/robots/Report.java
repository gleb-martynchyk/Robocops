package org.jazzteam.martynchyk.robots;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class Report {
    private Date startDate;
    private Date endDate;
    private Set<Robot> executors;

    public Report() {
        executors = new HashSet<>();
    }
}
