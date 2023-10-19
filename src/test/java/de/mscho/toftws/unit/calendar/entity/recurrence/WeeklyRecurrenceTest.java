package de.mscho.toftws.unit.calendar.entity.recurrence;

import de.mscho.toftws.calendar.entity.recurrence.WeeklyRecurrence;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WeeklyRecurrenceTest {
    @Test
    void testStartEndInTimeframe() {
        var from = instant(1, 1, 0);
        var to = instant(4, 1, 0);

        var start = instant(2, 1, 9);
        var end = instant(3, 1, 0);

        var rec = recurrence(1, start, end, "2,4,5");
        var list = rec.generateOccurrences(from, to);

        assertEquals(DayOfWeek.THURSDAY, list.get(0).getDayOfWeek());
        assertEquals(DayOfWeek.TUESDAY, list.get(11).getDayOfWeek());
        assertEquals(12, list.size());
    }

    @Test
    void testStartOutsideOfTimeframe() {
        var from = instant(3, 12, 0);
        var to = instant(10, 3, 0);

        var start = instant(2, 4, 9);
        var end = instant(4, 2, 12);

        var rec = recurrence(1, start, end, "6,7");
        var list = rec.generateOccurrences(from, to);

        assertEquals(DayOfWeek.SUNDAY, list.get(0).getDayOfWeek());
        assertEquals(DayOfWeek.SUNDAY, list.get(6).getDayOfWeek());
        assertEquals(7, list.size());
    }

    @Test
    void testEndOutsideOfTimeframe() {
        var from = instant(2, 1, 0);
        var to = instant(2, 28, 23);

        var start = instant(2, 4, 9);
        var end = instant(4, 2, 12);

        var rec = recurrence(1, start, end, "3,4,5");
        var list = rec.generateOccurrences(from, to);

        assertEquals(DayOfWeek.WEDNESDAY, list.get(0).getDayOfWeek());
        assertEquals(DayOfWeek.FRIDAY, list.get(8).getDayOfWeek());
        assertEquals(9, list.size());
    }

    @Test
    void testWithOffset() {
        var from = instant(1, 1, 0);
        var to = instant(5, 1, 23);

        var start = instant(3, 2, 9);
        var end = instant(3, 30, 12);

        var rec = recurrence(2, start, end, "1,4");
        var list = rec.generateOccurrences(from, to);

        assertEquals(DayOfWeek.THURSDAY, list.get(0).getDayOfWeek());
        assertEquals(DayOfWeek.THURSDAY, list.get(4).getDayOfWeek());
        assertEquals(5, list.size());
    }

    @Test
    void testWithOffsetFromNotSameWeekAsStart() {
        var from = instant(2, 8, 0);
        var to = instant(5, 1, 23);

        var start = instant(2, 3, 9);
        var end = instant(2, 28, 12);

        var rec = recurrence(2, start, end, "6,7");
        var list = rec.generateOccurrences(from, to);

        assertEquals(2, list.size());
    }

    @Test
    void testNoOccurrences() {
        var from = instant(2, 3, 0);
        var to = instant(2, 13, 23);

        var start = instant(2, 2, 9);
        var end = instant(3, 30, 12);

        var rec = recurrence(2, start, end, "4");
        var list = rec.generateOccurrences(from, to);

        assertEquals(0, list.size());
    }

    private Instant instant(int month, int day, int hour) {
        return LocalDateTime.of(2023, month, day, hour, 0).toInstant(ZoneOffset.UTC);
    }

    private WeeklyRecurrence recurrence(int offset, Instant start, Instant end, String days) {
        var weekDays = Arrays.stream(days.split(","))
                .map(Integer::parseInt)
                .map(DayOfWeek::of)
                .collect(Collectors.toCollection(TreeSet::new));
        return new WeeklyRecurrence(start, end, ZoneId.of("UTC"), offset, weekDays);
    }
}
