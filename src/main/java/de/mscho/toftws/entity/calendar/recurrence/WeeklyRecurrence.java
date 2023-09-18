package de.mscho.toftws.entity.calendar.recurrence;

import de.mscho.toftws.entity.calendar.util.DaySetConverters;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.time.temporal.TemporalAdjusters.previousOrSame;
import static java.time.temporal.TemporalAdjusters.nextOrSame;

@NoArgsConstructor
@Entity
public class WeeklyRecurrence extends Recurrence {
    @Positive
    @NotNull
    public long offset;
    @Convert(converter = DaySetConverters.DaySetAttributeConverter.class)
    @NotEmpty
    public Set<DayOfWeek> weekDays;

    public WeeklyRecurrence(ZonedDateTime start, ZonedDateTime end, long offset, Set<DayOfWeek> weekDays) {
        super(start, end);
        this.offset = offset;
        this.weekDays = weekDays;
    }

    @Override
    public List<ZonedDateTime> generateOccurrences(ZonedDateTime from, ZonedDateTime to) {
        var occurrences = new ArrayList<ZonedDateTime>();

        var earliest = start.isBefore(from) ? from.with(start.toLocalTime()) : start;
        var latest = to.isBefore(end) ? to : end;
        var current = firstOccurrenceWeek(earliest);

        while (current.isBefore(to) && current.isBefore(latest)) {
            var ofWeek = getOccurrencesOfWeek(current, earliest, latest);
            occurrences.addAll(ofWeek);
            current = current.plusWeeks(offset);
        }

        return occurrences;
    }

    private ZonedDateTime firstOccurrenceWeek(ZonedDateTime earliest) {
        var first = earliest.with(previousOrSame(DayOfWeek.MONDAY));
        long weeksBetween = start.with(previousOrSame(DayOfWeek.MONDAY)).minusNanos(1).until(first, ChronoUnit.WEEKS);
        return first.plusWeeks(weeksBetween % offset);
    }

    private List<ZonedDateTime> getOccurrencesOfWeek(ZonedDateTime current, ZonedDateTime earliest, ZonedDateTime latest) {
        return weekDays.stream().map(d -> current.with(nextOrSame(d)))
                .filter(d -> !d.isBefore(earliest) && d.isBefore(latest))
                .sorted().toList();
    }
}
