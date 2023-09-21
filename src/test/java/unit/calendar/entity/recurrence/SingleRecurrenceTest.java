package unit.calendar.entity.recurrence;

import de.mscho.toftws.calendar.entity.recurrence.SingleRecurrence;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("unit")
public class SingleRecurrenceTest {
    @Test
    void testInTimeFrame() {
        var start = instant(1, 17, 5);
        var from = instant(1, 6, 0);
        var to = instant(2, 22, 0);

        var recurrence = recurrence(start);
        var occurrences = recurrence.generateOccurrences(from, to);

        assertEquals(DayOfWeek.TUESDAY, occurrences.get(0).getDayOfWeek());
        assertEquals(1, occurrences.size());
    }

    @Test
    void testOutsideOfTimeFrame() {
        var start = instant(3, 17, 5);
        var from = instant(1, 6, 0);
        var to = instant(2, 22, 0);

        var recurrence = recurrence(start);
        var occurrences = recurrence.generateOccurrences(from, to);

        assertEquals(0, occurrences.size());
    }

    private Instant instant(int month, int day, int hour) {
        return LocalDateTime.of(2023, month, day, hour, 0).toInstant(ZoneOffset.UTC);
    }

    private SingleRecurrence recurrence(Instant start) {
        return new SingleRecurrence(start, start, ZoneId.of("UTC"));
    }
}
