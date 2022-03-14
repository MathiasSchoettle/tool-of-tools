package de.mscho.toftws.calendar.entity.recurrence;

import de.mscho.toftws.calendar.entity.recurrence.generator.MonthlyDateGenerator;
import de.mscho.toftws.calendar.entity.recurrence.generator.NeverDateGenerator;
import de.mscho.toftws.calendar.entity.recurrence.generator.RecurrenceDateGenerator;
import de.mscho.toftws.calendar.entity.recurrence.generator.WeeklyDateGenerator;
import lombok.AllArgsConstructor;


import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public enum Recurrence {

    WEEKLY(TimeUnit.DAYS.toSeconds(7), new WeeklyDateGenerator(1)),
    BI_WEEKLY(TimeUnit.DAYS.toSeconds(7 * 2), new WeeklyDateGenerator(2)),
    MONTHLY(TimeUnit.DAYS.toSeconds(7 * 4), new MonthlyDateGenerator(1)),
    YEARLY(TimeUnit.DAYS.toSeconds(365) , new MonthlyDateGenerator(12)),
    NEVER(0, new NeverDateGenerator());

    public final long seconds;

    public final RecurrenceDateGenerator dateGenerator;
}
