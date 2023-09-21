package unit.calendar.entity.recurrence;


import de.mscho.toftws.calendar.entity.recurrence.YearlyRecurrence;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("unit")
public class YearlyRecurrenceTest {
    @Test
    void testInTimeframe() {
        var from = instant(2020, 1, 1);
        var to = instant(2025, 1, 1);

        var start = instant(2022, 1, 9);
        var end = instant(2024, 2, 1);

        var rec = recurrence(start, end);
        var list = rec.generateOccurrences(from, to);

        assertYearMonthDay(list.get(0), 2022, 1, 9);
        assertYearMonthDay(list.get(1), 2023, 1, 9);
        assertYearMonthDay(list.get(2), 2024, 1, 9);
        assertEquals(3, list.size());
    }

    @Test
    void testStartOutsideOfTimeframe() {
        var from = instant(2022, 1, 1);
        var to = instant(2025, 1, 1);

        var start = instant(2020, 1, 9);
        var end = instant(2024, 2, 1);

        var rec = recurrence(start, end);
        var list = rec.generateOccurrences(from, to);

        assertYearMonthDay(list.get(0), 2022, 1, 9);
        assertYearMonthDay(list.get(1), 2023, 1, 9);
        assertYearMonthDay(list.get(2), 2024, 1, 9);
        assertEquals(3, list.size());
    }

    @Test
    void testEndOutsideOfTimeframe() {
        var from = instant(2020, 1, 1);
        var to = instant(2025, 4, 1);

        var start = instant(2022, 1, 9);
        var end = instant(2026, 2, 1);

        var rec = recurrence(start, end);
        var list = rec.generateOccurrences(from, to);

        assertYearMonthDay(list.get(0), 2022, 1, 9);
        assertYearMonthDay(list.get(1), 2023, 1, 9);
        assertYearMonthDay(list.get(2), 2024, 1, 9);
        assertYearMonthDay(list.get(3), 2025, 1, 9);
        assertEquals(4, list.size());
    }

    @Test
    void testOutsideOfTimeframe() {
        var from = instant(2022, 1, 1);
        var to = instant(2025, 4, 1);

        var start = instant(2020, 1, 9);
        var end = instant(2026, 2, 1);

        var rec = recurrence(start, end);
        var list = rec.generateOccurrences(from, to);

        assertYearMonthDay(list.get(0), 2022, 1, 9);
        assertYearMonthDay(list.get(1), 2023, 1, 9);
        assertYearMonthDay(list.get(2), 2024, 1, 9);
        assertYearMonthDay(list.get(3), 2025, 1, 9);
        assertEquals(4, list.size());
    }

    @Test
    void testNoOccurrences() {
        var from = instant(2020, 4, 1);
        var to = instant(2021, 1, 1);

        var start = instant(2020, 2, 9);
        var end = instant(2020, 3, 1);

        var rec = recurrence(start, end);
        var list = rec.generateOccurrences(from, to);

        assertEquals(0, list.size());
    }

    @Test
    void testFebruary29() {
        var from = instant(2020, 1, 1);
        var to = instant(2030, 4, 1);

        var start = instant(2024, 2, 29);
        var end = instant(2028, 12, 1);

        var rec = recurrence(start, end);
        var list = rec.generateOccurrences(from, to);

        assertYearMonthDay(list.get(0), 2024, 2, 29);
        assertYearMonthDay(list.get(1), 2025, 2, 28);
        assertYearMonthDay(list.get(2), 2026, 2, 28);
        assertYearMonthDay(list.get(3), 2027, 2, 28);
        assertYearMonthDay(list.get(4), 2028, 2, 29);
        assertEquals(5, list.size());
    }

    @Test
    void testWithOffset() {
        var from = instant(2020, 1, 1);
        var to = instant(2030, 1, 1);

        var start = instant(2022, 1, 9);
        var end = instant(2029, 2, 1);

        var rec = recurrence(start, end);
        rec.offset = 3;
        var list = rec.generateOccurrences(from, to);

        assertYearMonthDay(list.get(0), 2022, 1, 9);
        assertYearMonthDay(list.get(1), 2025, 1, 9);
        assertYearMonthDay(list.get(2), 2028, 1, 9);
        assertEquals(3, list.size());
    }

    @Test
    void testWithOffsetAndFromIsAfterStart() {
        var from = instant(2025, 1, 1);
        var to = instant(2030, 1, 1);

        var start = instant(2022, 1, 9);
        var end = instant(2029, 2, 1);

        var rec = recurrence(start, end);
        rec.offset = 2;
        var list = rec.generateOccurrences(from, to);

        assertYearMonthDay(list.get(0), 2026, 1, 9);
        assertYearMonthDay(list.get(1), 2028, 1, 9);
        assertEquals(2, list.size());
    }

    private Instant instant(int year, int month, int day) {
        return LocalDateTime.of(year, month, day, 0, 0).toInstant(ZoneOffset.UTC);
    }

    private void assertYearMonthDay(ZonedDateTime date, int year, int month, int day) {
        assertEquals(year, date.getYear());
        assertEquals(month, date.getMonthValue());
        assertEquals(day, date.getDayOfMonth());
    }

    private YearlyRecurrence recurrence(Instant start, Instant end) {
        return new YearlyRecurrence(start, end, ZoneId.of("UTC"), 1);
    }
}
