package de.mscho.toftws.calendar.entity.recurrence;

import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Positive;
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

    public MonthlyRecurrence(ZonedDateTime start, ZonedDateTime end, long offset) {
        super(start, end);
        this.offset = offset;
    }

    @Override
    public List<ZonedDateTime> generateOccurrences(ZonedDateTime from, ZonedDateTime to) {
        var occurrences = new ArrayList<ZonedDateTime>();
        long addedMonths = monthsToAdd(from);
        var current = start.plusMonths(addedMonths);

        while (current.isBefore(to) && current.isBefore(end)) {
            occurrences.add(current);
            addedMonths += offset;
            current = start.plusMonths(addedMonths);
        }

        return occurrences;
    }

    private long monthsToAdd(ZonedDateTime from) {
        if (from.isBefore(start)) return 0;
        // the amount of months we need to add to land on a date after from
        long monthsToFirst = start.until(from.minusNanos(1), ChronoUnit.MONTHS) + 1;
        // if we have an offset we need our months to add to be a multiple of it
        return ((monthsToFirst + offset - 1) / offset) * offset;
    }
}
