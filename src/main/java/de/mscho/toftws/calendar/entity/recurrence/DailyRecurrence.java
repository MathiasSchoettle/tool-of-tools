package de.mscho.toftws.calendar.entity.recurrence;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Positive;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import java.util.List;

@NoArgsConstructor
@Entity
public class DailyRecurrence extends Recurrence {
    @Positive
    @NotNull
    public long offset;

    public DailyRecurrence(Instant start, Instant end, ZoneId zoneId, long offset) {
        super(start, end, zoneId);
        this.offset = offset;
    }

    @Override
    public List<ZonedDateTime> generateOccurrences(Instant from, Instant to) {
        var occurrences = new ArrayList<ZonedDateTime>();
        var current = firstOccurrence(from);
        var zonedEnd = end.atZone(zoneId);

        while (current.toInstant().isBefore(to) && current.isBefore(zonedEnd)) {
            occurrences.add(current);
            current = current.plusDays(offset);
        }

        return occurrences;
    }

    private ZonedDateTime firstOccurrence(Instant from) {
        var zonedStart = start.atZone(zoneId);

        if (!from.isAfter(start)) return zonedStart;

        // minus one nano to catch cases where from date is a valid occurrence
        long daysBetween = zonedStart.until(from.atZone(zoneId).minusNanos(1), ChronoUnit.DAYS);
        // compensate by adding one more offset
        long daysToAdd = (daysBetween / offset + 1) * offset;

        return zonedStart.plusDays(daysToAdd);
    }
}
