package de.mscho.toftws.unit.calendar.entity.recurrence;

import de.mscho.toftws.calendar.entity.recurrence.MonthlyRecurrence;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

// TODO too many assertions in single tests?
public class MonthlyRecurrenceTest {
    @Test
    void testInTimeFrame() {
        var from = instant(1, 1, 0);
        var to = instant(5, 1, 23);

        var start = instant(1, 14, 9);
        var end = instant(3, 30, 12);

        var rec = recurrence(1, start, end);
        var list = rec.generateOccurrences(from, to);

        assertEquals(DayOfWeek.SATURDAY, list.get(0).getDayOfWeek());
        assertEquals(DayOfWeek.TUESDAY, list.get(2).getDayOfWeek());
        assertEquals(3, list.size());
    }

    @Test
    void testStartOutsideOfTimeframe() {
        var from = instant(10, 2, 0);
        var to = instant(11, 30, 23);

        var start = instant(9, 9, 9);
        var end = instant(11, 30, 12);

        var rec = recurrence(1, start, end);
        var list = rec.generateOccurrences(from, to);

        assertEquals(DayOfWeek.MONDAY, list.get(0).getDayOfWeek());
        assertEquals(DayOfWeek.THURSDAY, list.get(1).getDayOfWeek());
        assertEquals(2, list.size());
    }

    @Test
    void testEndOutsideOfTimeFrame() {
        var from = instant(4, 3, 0);
        var to = instant(7, 30, 23);

        var start = instant(4, 25, 9);
        var end = instant(9, 30, 12);

        var rec = recurrence(1, start, end);
        var list = rec.generateOccurrences(from, to);

        assertEquals(DayOfWeek.TUESDAY, list.get(0).getDayOfWeek());
        assertEquals(DayOfWeek.TUESDAY, list.get(3).getDayOfWeek());
        assertEquals(4, list.size());
    }

    @Test
    void testOutsideOfTimeFrame() {
        var from = instant(2, 2, 0);
        var to = instant(4, 3, 23);

        var start = instant(1, 1, 9);
        var end = instant(7, 30, 12);

        var rec = recurrence(1, start, end);
        var list = rec.generateOccurrences(from, to);

        assertEquals(DayOfWeek.WEDNESDAY, list.get(0).getDayOfWeek());
        assertEquals(DayOfWeek.SATURDAY, list.get(1).getDayOfWeek());
        assertEquals(2, list.size());
    }

    @Test
    void testNoOccurrences() {
        var from = instant(2, 2, 0);
        var to = instant(2, 27, 23);

        var start = instant(1, 1, 9);
        var end = instant(7, 30, 12);

        var rec = recurrence(1, start, end);
        var list = rec.generateOccurrences(from, to);

        assertEquals(0, list.size());
    }

    @Test
    void testWithOffset() {
        var from = instant(3, 1, 0);
        var to = instant(9, 1, 23);

        var start = instant(2, 1, 9);
        var end = instant(9, 30, 12);

        var rec = recurrence(3, start, end);
        var list = rec.generateOccurrences(from, to);

        assertEquals(DayOfWeek.MONDAY, list.get(0).getDayOfWeek());
        assertEquals(DayOfWeek.TUESDAY, list.get(1).getDayOfWeek());
        assertEquals(2, list.size());
    }

    @Test
    void testFromIsValidOccurrence() {
        var from = instant(2, 1, 0);
        var to = instant(4, 3, 23);

        var start = instant(1, 1, 9);
        var end = instant(7, 30, 12);

        var rec = recurrence(1, start, end);
        var list = rec.generateOccurrences(from, to);

        assertEquals(DayOfWeek.WEDNESDAY, list.get(0).getDayOfWeek());
        assertEquals(DayOfWeek.SATURDAY, list.get(2).getDayOfWeek());
        assertEquals(3, list.size());
    }

    @Test
    void testWithLastDayOfMonth() {
        var from = instant(2, 1, 0);
        var to = instant(10, 30, 23);

        var start = instant(1, 31, 9);
        var end = instant(5, 1, 12);

        var rec = recurrence(1, start, end);
        var list = rec.generateOccurrences(from, to);

        assertEquals(DayOfWeek.TUESDAY, list.get(0).getDayOfWeek());
        assertEquals(28, list.get(0).getDayOfMonth());
        assertEquals(DayOfWeek.SUNDAY, list.get(2).getDayOfWeek());
        assertEquals(30, list.get(2).getDayOfMonth());
        assertEquals(3, list.size());
    }

    private Instant instant(int month, int day, int hour) {
        return LocalDateTime.of(2023, month, day, hour, 0).toInstant(ZoneOffset.UTC);
    }

    private MonthlyRecurrence recurrence(int offset, Instant start, Instant end) {
        return new MonthlyRecurrence(start, end, ZoneId.of("UTC"), offset);
    }
}
