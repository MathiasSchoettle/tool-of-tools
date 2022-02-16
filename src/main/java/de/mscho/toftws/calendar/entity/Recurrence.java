package de.mscho.toftws.calendar.entity;

import lombok.AllArgsConstructor;
import org.springframework.format.datetime.standard.DateTimeContext;


import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public enum Recurrence {

    WEEKLY(TimeUnit.DAYS.toSeconds(7)),
    BI_WEEKLY(TimeUnit.DAYS.toSeconds(7 * 2)),
    MONTHLY(TimeUnit.DAYS.toSeconds(7 * 4)),
    YEARLY(TimeUnit.DAYS.toSeconds(365)),
    NEVER(0);

    public final long seconds;
}
