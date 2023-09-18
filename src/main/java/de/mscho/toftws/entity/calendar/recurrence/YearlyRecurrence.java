package de.mscho.toftws.entity.calendar.recurrence;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
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

    public YearlyRecurrence(ZonedDateTime start, ZonedDateTime end, long offset) {
        super(start, end);
        this.offset = offset;
    }

    @Override
    public List<ZonedDateTime> generateOccurrences(ZonedDateTime from, ZonedDateTime to) {
        var occurrences = new ArrayList<ZonedDateTime>();
        var addedYears = yearsToAdd(from);
        var current = start.plusYears(addedYears);

        while (current.isBefore(to) && current.isBefore(end)) {
            occurrences.add(current);
            current = start.plusYears(++addedYears);
        }

        return occurrences;
    }

    private long yearsToAdd(ZonedDateTime from) {
        if (start.isAfter(from)) return 0;
        return start.until(from.minusNanos(1), ChronoUnit.YEARS) + 1;
    }
}
