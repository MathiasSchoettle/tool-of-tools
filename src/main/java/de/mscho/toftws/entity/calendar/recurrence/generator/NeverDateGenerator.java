package de.mscho.toftws.entity.calendar.recurrence.generator;

import de.mscho.toftws.util.DateTimeProperties;

import java.time.OffsetDateTime;

public class NeverDateGenerator extends RecurrenceDateGenerator {

    @Override
    protected OffsetDateTime getFirstOccurrenceWithStartBeforeFrom(OffsetDateTime from, OffsetDateTime to, OffsetDateTime start) {
        return DateTimeProperties.MAX_OFFSET_DATETIME;
    }

    @Override
    public OffsetDateTime getNextOccurrence(OffsetDateTime dateTime) {
        return DateTimeProperties.MAX_OFFSET_DATETIME;
    }

    @Override
    public OffsetDateTime getLastOccurrence(OffsetDateTime start, OffsetDateTime end) {
        return start;
    }
}
