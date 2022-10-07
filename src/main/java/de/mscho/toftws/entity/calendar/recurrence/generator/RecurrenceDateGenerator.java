package de.mscho.toftws.entity.calendar.recurrence.generator;

import de.mscho.toftws.util.DateTimeProperties;

import java.time.OffsetDateTime;

public abstract class RecurrenceDateGenerator {

    public OffsetDateTime generateFirstOccurrence(OffsetDateTime from, OffsetDateTime to, OffsetDateTime start) {

        if (start.isBefore(from)) {
            return getFirstOccurrenceWithStartBeforeFrom(from, to, start);
        }

        if (start.isAfter(to)) {
            return DateTimeProperties.MAX_OFFSET_DATETIME;
        }

        return start;
    }

    protected abstract OffsetDateTime getFirstOccurrenceWithStartBeforeFrom(OffsetDateTime from, OffsetDateTime to, OffsetDateTime start);

    public abstract OffsetDateTime getNextOccurrence(OffsetDateTime dateTime);

    public abstract OffsetDateTime getLastOccurrence(OffsetDateTime start, OffsetDateTime end);
}
