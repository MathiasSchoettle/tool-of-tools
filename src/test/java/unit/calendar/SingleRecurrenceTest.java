package unit.calendar;

import de.mscho.toftws.entity.calendar.recurrence.SingleRecurrence;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("unit")
public class SingleRecurrenceTest {
    private static final ZoneId CET = ZoneId.of("CET");

    @Test
    void testInTimeFrame() {
        var start = dayMonthHour(1, 17, 5);
        var from = dayMonthHour(1, 6, 0);
        var to = dayMonthHour(2, 22, 0);

        var recurrence = recurrence(start);
        var occurrences = recurrence.generateOccurrences(from, to);

        assertEquals(DayOfWeek.TUESDAY, occurrences.get(0).getDayOfWeek());
        assertEquals(1, occurrences.size());
    }

    @Test
    void testOutsideOfTimeFrame() {
        var start = dayMonthHour(3, 17, 5);
        var from = dayMonthHour(1, 6, 0);
        var to = dayMonthHour(2, 22, 0);

        var recurrence = recurrence(start);
        var occurrences = recurrence.generateOccurrences(from, to);

        assertEquals(0, occurrences.size());
    }

    private ZonedDateTime dayMonthHour(int month, int day, int hour) {
        return ZonedDateTime.of(LocalDateTime.of(2023, month, day, hour, 0), CET);
    }

    private SingleRecurrence recurrence(ZonedDateTime start) {
        var recurrence = new SingleRecurrence();
        recurrence.start = start;
        return recurrence;
    }
}
