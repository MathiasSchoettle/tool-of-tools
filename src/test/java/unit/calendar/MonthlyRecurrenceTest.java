package unit.calendar;

import de.mscho.toftws.entity.calendar.recurrence.MonthlyRecurrence;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("unit")
public class MonthlyRecurrenceTest {
    private static final ZoneId CET = ZoneId.of("CET");

    @Test
    void testInTimeFrame() {
        var from = dayMonthHour(1, 1, 0);
        var to = dayMonthHour(5, 1, 23);

        var start = dayMonthHour(1, 14, 9);
        var end = dayMonthHour(3, 30, 12);

        var rec = recurrence(1, start, end);
        var list = rec.generateOccurrences(from, to);

        assertEquals(DayOfWeek.SATURDAY, list.get(0).getDayOfWeek());
        assertEquals(DayOfWeek.TUESDAY, list.get(2).getDayOfWeek());
        assertEquals(3, list.size());
    }

    @Test
    void testStartOutsideOfTimeframe() {
        var from = dayMonthHour(10, 2, 0).withYear(2022);
        var to = dayMonthHour(11, 30, 23).withYear(2022);

        var start = dayMonthHour(9, 9, 9).withYear(2022);
        var end = dayMonthHour(11, 30, 12).withYear(2022);

        var rec = recurrence(1, start, end);
        var list = rec.generateOccurrences(from, to);

        assertEquals(DayOfWeek.SUNDAY, list.get(0).getDayOfWeek());
        assertEquals(DayOfWeek.WEDNESDAY, list.get(1).getDayOfWeek());
        assertEquals(2, list.size());
    }

    @Test
    void testEndOutsideOfTimeFrame() {
        var from = dayMonthHour(4, 3, 0);
        var to = dayMonthHour(7, 30, 23);

        var start = dayMonthHour(4, 25, 9);
        var end = dayMonthHour(9, 30, 12);

        var rec = recurrence(1, start, end);
        var list = rec.generateOccurrences(from, to);

        assertEquals(DayOfWeek.TUESDAY, list.get(0).getDayOfWeek());
        assertEquals(DayOfWeek.TUESDAY, list.get(3).getDayOfWeek());
        assertEquals(4, list.size());
    }

    @Test
    void testOutsideOfTimeFrame() {
        var from = dayMonthHour(2, 2, 0);
        var to = dayMonthHour(4, 3, 23);

        var start = dayMonthHour(1, 1, 9);
        var end = dayMonthHour(7, 30, 12);

        var rec = recurrence(1, start, end);
        var list = rec.generateOccurrences(from, to);

        assertEquals(DayOfWeek.WEDNESDAY, list.get(0).getDayOfWeek());
        assertEquals(DayOfWeek.SATURDAY, list.get(1).getDayOfWeek());
        assertEquals(2, list.size());
    }

    @Test
    void testNoOccurrences() {
        var from = dayMonthHour(2, 2, 0);
        var to = dayMonthHour(2, 27, 23);

        var start = dayMonthHour(1, 1, 9);
        var end = dayMonthHour(7, 30, 12);

        var rec = recurrence(1, start, end);
        var list = rec.generateOccurrences(from, to);

        assertEquals(0, list.size());
    }

    @Test
    void testWithOffset() {
        var from = dayMonthHour(3, 1, 0);
        var to = dayMonthHour(9, 1, 23);

        var start = dayMonthHour(2, 1, 9);
        var end = dayMonthHour(9, 30, 12);

        var rec = recurrence(3, start, end);
        var list = rec.generateOccurrences(from, to);

        assertEquals(DayOfWeek.MONDAY, list.get(0).getDayOfWeek());
        assertEquals(DayOfWeek.TUESDAY, list.get(1).getDayOfWeek());
        assertEquals(2, list.size());
    }

    @Test
    void testFromIsValidOccurrence() {
        var from = dayMonthHour(2, 1, 0);
        var to = dayMonthHour(4, 3, 23);

        var start = dayMonthHour(1, 1, 9);
        var end = dayMonthHour(7, 30, 12);

        var rec = recurrence(1, start, end);
        var list = rec.generateOccurrences(from, to);

        assertEquals(DayOfWeek.WEDNESDAY, list.get(0).getDayOfWeek());
        assertEquals(DayOfWeek.SATURDAY, list.get(2).getDayOfWeek());
        assertEquals(3, list.size());
    }

    @Test
    void testWithLastDayOfMonth() {
        var from = dayMonthHour(2, 1, 0);
        var to = dayMonthHour(10, 30, 23);

        var start = dayMonthHour(1, 31, 9);
        var end = dayMonthHour(5, 1, 12);

        var rec = recurrence(1, start, end);
        var list = rec.generateOccurrences(from, to);

        assertEquals(DayOfWeek.TUESDAY, list.get(0).getDayOfWeek());
        assertEquals(28, list.get(0).getDayOfMonth());
        assertEquals(DayOfWeek.SUNDAY, list.get(2).getDayOfWeek());
        assertEquals(30, list.get(2).getDayOfMonth());
        assertEquals(3, list.size());
    }

    private ZonedDateTime dayMonthHour(int month, int day, int hour) {
        return ZonedDateTime.of(LocalDateTime.of(2023, month, day, hour, 0), CET);
    }

    private MonthlyRecurrence recurrence(int offset, ZonedDateTime start, ZonedDateTime end) {
        var recurrence = new MonthlyRecurrence();
        recurrence.start = start;
        recurrence.offset = offset;
        recurrence.end =  end;
        return recurrence;
    }
}
