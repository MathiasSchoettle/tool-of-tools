package unit.calendar.entity.recurrence;

import de.mscho.toftws.calendar.entity.recurrence.DailyRecurrence;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("unit")
public class DailyRecurrenceTest {
    @Test
    void testDailyRecurrence() {
        var start = instant(1, 1, 12);
        var from = instant(1, 10, 12);
        var to = instant(1, 20, 12);
        var recurrence = recurrence(2, start, instant(1, 30, 12));
        var occurrences = recurrence.generateOccurrences(from, to);
        assertEquals(5, occurrences.size());
    }

    @Test
    void testFromIsValidOccurrence() {
        var start = instant(1, 1, 12);
        var from = instant(1, 10, 12);
        var to = instant(1, 20, 12);
        var recurrence = recurrence(1, start, instant(1, 30, 12));
        var occurrences = recurrence.generateOccurrences(from, to);
        assertEquals(10, occurrences.size());
    }

    @Test
    void testNoOccurrenceInTimeframe() {
        var start = instant(1, 1, 12);
        var from = instant(1, 6, 12);
        var to = instant(1, 8, 12);
        var recurrence = recurrence(4, start, instant(1, 30, 12));
        var occurrences = recurrence.generateOccurrences(from, to);
        assertEquals(0, occurrences.size());
    }

    @Test
    void testDifferingStartTimes() {
        var start = instant(1, 1, 5);
        var from = instant(1, 6, 0);
        var to = instant(2, 13, 0);
        var recurrence = recurrence(3, start, instant(2, 28, 0));
        var occurrences = recurrence.generateOccurrences(from, to);
        assertEquals(13, occurrences.size());
    }

    @Test
    void testEndIsBeforeTo() {
        var start = instant(1, 1, 5);
        var from = instant(1, 6, 0);
        var to = instant(1, 22, 0);
        var recurrence = recurrence(5, start, instant(1, 12, 0));
        var occurrences = recurrence.generateOccurrences(from, to);
        assertEquals(2, occurrences.size());
    }

    private Instant instant(int month, int day, int hour) {
        return LocalDateTime.of(2022, month, day, hour, 0).toInstant(ZoneOffset.UTC);
    }

    private DailyRecurrence recurrence(int offset, Instant start, Instant end) {
        return new DailyRecurrence(start, end, ZoneId.of("UTC"), offset);
    }
}
