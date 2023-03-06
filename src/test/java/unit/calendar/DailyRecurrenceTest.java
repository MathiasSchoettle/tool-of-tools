package unit.calendar;

import de.mscho.toftws.entity.calendar.recurrence.DailyRecurrence;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("unit")
public class DailyRecurrenceTest {

    private static final ZoneId CET = ZoneId.of("CET");

    @Test
    void testDailyRecurrence() {
        var start = dayMonthHour(1, 1, 12);
        var from = dayMonthHour(1, 10, 12);
        var to = dayMonthHour(1, 20, 12);
        var recurrence = recurrence(2, start, dayMonthHour(1, 30, 12));
        var occurrences = recurrence.generateOccurrences(from, to);
        assertEquals(5, occurrences.size());
    }

    @Test
    void testFromIsValidOccurrence() {
        var start = dayMonthHour(1, 1, 12);
        var from = dayMonthHour(1, 10, 12);
        var to = dayMonthHour(1, 20, 12);
        var recurrence = recurrence(1, start, dayMonthHour(1, 30, 12));
        var occurrences = recurrence.generateOccurrences(from, to);
        assertEquals(10, occurrences.size());
    }

    @Test
    void testNoOccurrenceInTimeframe() {
        var start = dayMonthHour(1, 1, 12);
        var from = dayMonthHour(1, 6, 12);
        var to = dayMonthHour(1, 8, 12);
        var recurrence = recurrence(4, start, dayMonthHour(1, 30, 12));
        var occurrences = recurrence.generateOccurrences(from, to);
        assertEquals(0, occurrences.size());
    }

    @Test
    void testDifferingStartTimes() {
        var start = dayMonthHour(1, 1, 5);
        var from = dayMonthHour(1, 6, 0);
        var to = dayMonthHour(2, 13, 0);
        var recurrence = recurrence(3, start, dayMonthHour(2, 28, 0));
        var occurrences = recurrence.generateOccurrences(from, to);
        assertEquals(13, occurrences.size());
    }

    @Test
    void testEndIsBeforeTo() {
        var start = dayMonthHour(1, 1, 5);
        var from = dayMonthHour(1, 6, 0);
        var to = dayMonthHour(1, 22, 0);
        var recurrence = recurrence(5, start, dayMonthHour(1, 12, 0));
        var occurrences = recurrence.generateOccurrences(from, to);
        assertEquals(2, occurrences.size());
    }

    private ZonedDateTime dayMonthHour(int month, int day, int hour) {
        return ZonedDateTime.of(LocalDateTime.of(2022, month, day, hour, 0), CET);
    }

    private DailyRecurrence recurrence(int offset, ZonedDateTime start, ZonedDateTime end) {
        return new DailyRecurrence(start, end, offset);
    }
}
