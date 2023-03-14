package de.mscho.toftws.entity.calendar.recurrence;

import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Positive;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import java.util.List;

@NoArgsConstructor
@Entity
public class DailyRecurrence extends Recurrence {
    @Positive
    public int offset;

    public DailyRecurrence(ZonedDateTime start, ZonedDateTime end, int offset) {
        super(start, end);
        this.offset = offset;
    }

    @Override
    public List<ZonedDateTime> generateOccurrences(ZonedDateTime from, ZonedDateTime to) {
        var occurrences = new ArrayList<ZonedDateTime>();
        var current = firstOccurrence(from);

        while (current.isBefore(to) && current.isBefore(end)) {
            occurrences.add(current);
            current = current.plusDays(offset);
        }

        return occurrences;
    }

    private ZonedDateTime firstOccurrence(ZonedDateTime from) {
        if (!from.isAfter(start)) return start;

        // minus one nano to catch cases where from date is a valid occurrence
        long daysBetween = start.until(from.minusNanos(1), ChronoUnit.DAYS);
        // compensate by adding one more offset
        long daysToAdd = (daysBetween / offset + 1) * offset;

        return start.plusDays(daysToAdd);
    }
}