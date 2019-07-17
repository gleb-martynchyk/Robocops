package org.jazzteam.martynchyk.robots;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.jazzteam.martynchyk.tasks.Task;

import java.util.Date;

@Getter
@Setter
public class Report {
    private Task task;
    private Date startDate;
    private Date endDate;
    @JsonIgnore
    private Robot executor;

    public Report() {
        startDate = new Date();
    }
}
