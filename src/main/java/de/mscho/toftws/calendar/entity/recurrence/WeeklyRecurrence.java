package de.mscho.toftws.calendar.entity.recurrence;

import de.mscho.toftws.calendar.entity.util.DaySetConverters;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import static java.time.temporal.TemporalAdjusters.*;

@NoArgsConstructor
@Entity
public class WeeklyRecurrence extends Recurrence {
    @Positive
    @NotNull
    public long offset;
    @Convert(converter = DaySetConverters.DaySetAttributeConverter.class)
    @NotEmpty
    public SortedSet<DayOfWeek> weekDays;

    public WeeklyRecurrence(Instant start, Instant end, ZoneId zoneId, long offset, SortedSet<DayOfWeek> weekDays) {
        super(start, end, zoneId);
        this.offset = offset;
        this.weekDays = weekDays;
    }

    @Override
    public List<ZonedDateTime> generateOccurrences(Instant from, Instant to) {
        var occurrences = new ArrayList<ZonedDateTime>();

        ZonedDateTime earliest = earliest(start, from);
        ZonedDateTime latest = to.isBefore(end) ? to.atZone(zoneId) : end.atZone(zoneId);

        ZonedDateTime currentWeek = firstOccurrenceWeek(earliest);

        while (currentWeek.toInstant().isBefore(to) && currentWeek.isBefore(latest)) {
            var ofWeek = getOccurrencesOfWeek(currentWeek, earliest, latest);
            occurrences.addAll(ofWeek);
            currentWeek = currentWeek.plusWeeks(offset);
        }

        return occurrences;
    }

    private ZonedDateTime earliest(Instant start, Instant from) {
        var zonedStart = start.atZone(zoneId);
        long daysBetween = zonedStart.until(from.atZone(zoneId).minusNanos(1), ChronoUnit.DAYS) + 1;
        return start.isAfter(from) ? zonedStart : zonedStart.plusDays(daysBetween);
    }

    private ZonedDateTime firstOccurrenceWeek(ZonedDateTime earliest) {
        var first = earliest.with(previousOrSame(DayOfWeek.MONDAY));
        long weeksBetween = start.atZone(zoneId).with(previousOrSame(DayOfWeek.MONDAY)).minusNanos(1).until(first, ChronoUnit.WEEKS);
        return first.plusWeeks(weeksBetween % offset);
    }

    private List<ZonedDateTime> getOccurrencesOfWeek(ZonedDateTime current, ZonedDateTime earliest, ZonedDateTime latest) {
        return weekDays.stream().map(d -> current.with(nextOrSame(d)))
                .filter(d -> !d.isBefore(earliest) && d.isBefore(latest))
                .sorted().toList();
    }
}
