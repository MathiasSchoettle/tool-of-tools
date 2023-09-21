package de.mscho.toftws.calendar.entity.recurrence;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
public class YearlyRecurrence extends Recurrence {
    @Positive
    @NotNull
    public long offset;

    public YearlyRecurrence(Instant start, Instant end, ZoneId zoneId, long offset) {
        super(start, end, zoneId);
        this.offset = offset;
    }

    @Override
    public List<ZonedDateTime> generateOccurrences(Instant from, Instant to) {
        var occurrences = new ArrayList<ZonedDateTime>();
        var zonedStart = start.atZone(zoneId);

        var addedYears = yearsToAdd(from.atZone(zoneId));
        var current = zonedStart.plusYears(addedYears);

        while (current.toInstant().isBefore(to) && current.isBefore(end.atZone(zoneId))) {
            occurrences.add(current);
            addedYears += offset;
            current = zonedStart.plusYears(addedYears);
        }

        return occurrences;
    }

    private long yearsToAdd(ZonedDateTime from) {
        var zonedStart = start.atZone(zoneId);
        if (zonedStart.isAfter(from)) return 0;

        long yearsBetween = zonedStart.until(from.minusNanos(1), ChronoUnit.YEARS);
        return (yearsBetween / offset + 1) * offset;
    }
}
