package de.mscho.toftws.calendar.entity.recurrence;

import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Positive;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
public class MonthlyRecurrence extends Recurrence {
    @Positive
    @NotNull
    public long offset;

    public MonthlyRecurrence(Instant start, Instant end, ZoneId zoneId, long offset) {
        super(start, end, zoneId);
        this.offset = offset;
    }

    @Override
    public List<ZonedDateTime> generateOccurrences(Instant from, Instant to) {
        var occurrences = new ArrayList<ZonedDateTime>();
        var zonedStart = start.atZone(zoneId);

        long addedMonths = monthsToAdd(from.atZone(zoneId));

        var current = zonedStart.plusMonths(addedMonths);

        while (current.toInstant().isBefore(to) && current.isBefore(end.atZone(zoneId))) {
            occurrences.add(current);
            addedMonths += offset;
            current = zonedStart.plusMonths(addedMonths);
        }

        return occurrences;
    }

    private long monthsToAdd(ZonedDateTime from) {
        var zonedStart = start.atZone(zoneId);
        if (from.isBefore(zonedStart)) return 0;
        // the amount of months we need to add to land on a date after from
        long monthsToFirst = zonedStart.until(from.minusNanos(1), ChronoUnit.MONTHS) + 1;
        // if we have an offset we need our months to add to be a multiple of it
        return ((monthsToFirst + offset - 1) / offset) * offset;
    }
}
