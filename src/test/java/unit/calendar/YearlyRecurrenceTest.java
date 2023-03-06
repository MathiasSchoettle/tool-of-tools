package unit.calendar;


import de.mscho.toftws.entity.calendar.recurrence.YearlyRecurrence;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("unit")
public class YearlyRecurrenceTest {
    private static final ZoneId CET = ZoneId.of("CET");

    @Test
    void testInTimeframe() {
        var from = dayMonthHour(2020, 1, 1);
        var to = dayMonthHour(2025, 1, 1);

        var start = dayMonthHour(2022, 1, 9);
        var end = dayMonthHour(2024, 2, 1);

        var rec = recurrence(start, end);
        var list = rec.generateOccurrences(from, to);

        assertYearMonthDay(list.get(0), 2022, 1, 9);
        assertYearMonthDay(list.get(1), 2023, 1, 9);
        assertYearMonthDay(list.get(2), 2024, 1, 9);
        assertEquals(3, list.size());
    }

    @Test
    void testStartOutsideOfTimeframe() {
        var from = dayMonthHour(2022, 1, 1);
        var to = dayMonthHour(2025, 1, 1);

        var start = dayMonthHour(2020, 1, 9);
        var end = dayMonthHour(2024, 2, 1);

        var rec = recurrence(start, end);
        var list = rec.generateOccurrences(from, to);

        assertYearMonthDay(list.get(0), 2022, 1, 9);
        assertYearMonthDay(list.get(1), 2023, 1, 9);
        assertYearMonthDay(list.get(2), 2024, 1, 9);
        assertEquals(3, list.size());
    }

    @Test
    void testEndOutsideOfTimeframe() {
        var from = dayMonthHour(2020, 1, 1);
        var to = dayMonthHour(2025, 4, 1);

        var start = dayMonthHour(2022, 1, 9);
        var end = dayMonthHour(2026, 2, 1);

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
        var from = dayMonthHour(2022, 1, 1);
        var to = dayMonthHour(2025, 4, 1);

        var start = dayMonthHour(2020, 1, 9);
        var end = dayMonthHour(2026, 2, 1);

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
        var from = dayMonthHour(2020, 4, 1);
        var to = dayMonthHour(2021, 1, 1);

        var start = dayMonthHour(2020, 2, 9);
        var end = dayMonthHour(2020, 3, 1);

        var rec = recurrence(start, end);
        var list = rec.generateOccurrences(from, to);

        assertEquals(0, list.size());
    }

    @Test
    void testFebruary29() {
        var from = dayMonthHour(2020, 1, 1);
        var to = dayMonthHour(2030, 4, 1);

        var start = dayMonthHour(2024, 2, 29);
        var end = dayMonthHour(2028, 12, 1);

        var rec = recurrence(start, end);
        var list = rec.generateOccurrences(from, to);

        assertYearMonthDay(list.get(0), 2024, 2, 29);
        assertYearMonthDay(list.get(1), 2025, 2, 28);
        assertYearMonthDay(list.get(2), 2026, 2, 28);
        assertYearMonthDay(list.get(3), 2027, 2, 28);
        assertYearMonthDay(list.get(4), 2028, 2, 29);
        assertEquals(5, list.size());
    }

    private ZonedDateTime dayMonthHour(int year, int month, int day) {
        return ZonedDateTime.of(LocalDateTime.of(year, month, day, 0, 0), CET);
    }

    private void assertYearMonthDay(ZonedDateTime date, int year, int month, int day) {
        assertEquals(year, date.getYear());
        assertEquals(month, date.getMonthValue());
        assertEquals(day, date.getDayOfMonth());
    }

    private YearlyRecurrence recurrence(ZonedDateTime start, ZonedDateTime end) {
        var recurrence = new YearlyRecurrence();
        recurrence.start = start;
        recurrence.end = end;
        return recurrence;
    }
}
