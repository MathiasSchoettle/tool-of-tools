package unit.calendar;

import de.mscho.toftws.entity.calendar.recurrence.WeeklyRecurrence;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("unit")
public class WeeklyRecurrenceTest {
    private static final ZoneId CET = ZoneId.of("CET");

    @Test
    void testStartEndInTimeframe() {
        var from = dayMonthHour(1, 1, 0);
        var to = dayMonthHour(4, 1, 0);

        var start = dayMonthHour(2, 1, 9);
        var end = dayMonthHour(3, 1, 0);

        var rec = recurrence(1, start, end, "2,4,5");
        var list = rec.generateOccurrences(from, to);

        assertEquals(DayOfWeek.THURSDAY, list.get(0).getDayOfWeek());
        assertEquals(DayOfWeek.TUESDAY, list.get(11).getDayOfWeek());
        assertEquals(12, list.size());
    }

    @Test
    void testStartOutsideOfTimeframe() {
        var from = dayMonthHour(3, 12, 0);
        var to = dayMonthHour(10, 3, 0);

        var start = dayMonthHour(2, 4, 9);
        var end = dayMonthHour(4, 2, 12);

        var rec = recurrence(1, start, end, "6,7");
        var list = rec.generateOccurrences(from, to);

        assertEquals(DayOfWeek.SUNDAY, list.get(0).getDayOfWeek());
        assertEquals(DayOfWeek.SUNDAY, list.get(6).getDayOfWeek());
        assertEquals(7, list.size());
    }

    @Test
    void testStartOutsideOfTimeframeDifferentFromTimezone() {
        var from = dayMonthHour(3, 12, 0).withZoneSameLocal(ZoneId.of("GMT"));
        var to = dayMonthHour(10, 3, 0);

        var start = dayMonthHour(2, 4, 9);
        var end = dayMonthHour(4, 2, 12);

        var rec = recurrence(1, start, end, "6,7");
        var list = rec.generateOccurrences(from, to);

        assertEquals(DayOfWeek.SUNDAY, list.get(0).getDayOfWeek());
        assertEquals(DayOfWeek.SUNDAY, list.get(6).getDayOfWeek());
        assertEquals(7, list.size());
    }

    @Test
    void testEndOutsideOfTimeframe() {
        var from = dayMonthHour(2, 1, 0);
        var to = dayMonthHour(2, 28, 23);

        var start = dayMonthHour(2, 4, 9);
        var end = dayMonthHour(4, 2, 12);

        var rec = recurrence(1, start, end, "3,4,5");
        var list = rec.generateOccurrences(from, to);

        assertEquals(DayOfWeek.WEDNESDAY, list.get(0).getDayOfWeek());
        assertEquals(DayOfWeek.FRIDAY, list.get(8).getDayOfWeek());
        assertEquals(9, list.size());
    }

    @Test
    void testWithOffset() {
        var from = dayMonthHour(1, 1, 0);
        var to = dayMonthHour(5, 1, 23);

        var start = dayMonthHour(3, 2, 9);
        var end = dayMonthHour(3, 30, 12);

        var rec = recurrence(2, start, end, "1,4");
        var list = rec.generateOccurrences(from, to);

        assertEquals(DayOfWeek.THURSDAY, list.get(0).getDayOfWeek());
        assertEquals(DayOfWeek.THURSDAY, list.get(4).getDayOfWeek());
        assertEquals(5, list.size());
    }

    @Test
    void testWithOffsetFromNotSameWeekAsStart() {
        var from = dayMonthHour(2, 8, 0);
        var to = dayMonthHour(5, 1, 23);

        var start = dayMonthHour(2, 3, 9);
        var end = dayMonthHour(2, 28, 12);

        var rec = recurrence(2, start, end, "6,7");
        var list = rec.generateOccurrences(from, to);

        assertEquals(2, list.size());
    }

    @Test
    void testNoOccurrences() {
        var from = dayMonthHour(2, 3, 0);
        var to = dayMonthHour(2, 13, 23);

        var start = dayMonthHour(2, 2, 9);
        var end = dayMonthHour(3, 30, 12);

        var rec = recurrence(2, start, end, "4");
        var list = rec.generateOccurrences(from, to);

        assertEquals(0, list.size());
    }

    private ZonedDateTime dayMonthHour(int month, int day, int hour) {
        return ZonedDateTime.of(LocalDateTime.of(2023, month, day, hour, 0), CET);
    }

    private WeeklyRecurrence recurrence(int offset, ZonedDateTime start, ZonedDateTime end, String days) {
        var recurrence = new WeeklyRecurrence();
        recurrence.offset = offset;
        recurrence.weekDays = Arrays.stream(days.split(","))
                .map(Integer::parseInt)
                .map(DayOfWeek::of)
                .collect(Collectors.toSet());
        recurrence.start = start;
        recurrence.end = end;
        return recurrence;
    }
}
