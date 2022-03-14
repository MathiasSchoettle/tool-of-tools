package de.mscho.toftws.calendar.entity.recurrence.generator;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.concurrent.TimeUnit;

public class WeeklyDateGenerator extends RecurrenceDateGenerator {

    private final long secondsOfWeeks;

    private static final int MAX_WEEK_COUNT = 12;

    public WeeklyDateGenerator(int weekCount) {

        if (weekCount < 1 || weekCount > MAX_WEEK_COUNT)
            throw new IllegalArgumentException("Week count must be larger than 0 and lesser or equal than " + MAX_WEEK_COUNT);

        this.secondsOfWeeks = TimeUnit.DAYS.toSeconds(7L * weekCount);
    }

    @Override
    public OffsetDateTime getFirstOccurrenceWithStartBeforeFrom(OffsetDateTime from, OffsetDateTime to, OffsetDateTime start) {

        long secondsBetween = Duration.between(start, from).toSeconds();
        long secondsTo = secondsOfWeeks - secondsBetween % secondsOfWeeks;

        return from.plusSeconds(secondsTo);
    }

    @Override
    public OffsetDateTime getNextOccurrence(OffsetDateTime dateTime) {
        return dateTime.plusSeconds(secondsOfWeeks);
    }
}
