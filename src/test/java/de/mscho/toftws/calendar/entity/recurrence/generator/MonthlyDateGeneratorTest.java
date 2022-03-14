package de.mscho.toftws.calendar.entity.recurrence.generator;

import de.mscho.toftws.util.DateTimeProperties;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("unit")
public class MonthlyDateGeneratorTest {

    RecurrenceDateGenerator generator = new MonthlyDateGenerator(1);

    @Test
    public void Negative_MonthCount_Throws_IllegalArgumentException() {

        assertThrows(IllegalArgumentException.class, () -> new MonthlyDateGenerator(-1));
    }

    @Test
    public void Start_Before_From_Returns_CorrectDate() {

        OffsetDateTime from = OffsetDateTime.of(2022, 3, 26, 1, 42, 0, 0, ZoneOffset.UTC);
        OffsetDateTime to = OffsetDateTime.of(2022, 7, 1, 12, 1, 0, 0, ZoneOffset.UTC);
        OffsetDateTime start = OffsetDateTime.of(2022, 2, 9, 2, 15, 0, 0, ZoneOffset.UTC);
        OffsetDateTime expected = OffsetDateTime.of(2022, 4, 9, 2, 15, 0, 0, ZoneOffset.UTC);

        OffsetDateTime dateTime = generator.generateFirstOccurrence(from, to, start);

        assertEquals(expected, dateTime);
    }

    @Test
    public void Start_Before_From_Skips_Month_if_Month_has_to_Few_Days() {

        OffsetDateTime from = OffsetDateTime.of(2022, 2, 1, 7, 42, 0, 0, ZoneOffset.UTC);
        OffsetDateTime to = OffsetDateTime.of(2022, 7, 1, 12, 1, 0, 0, ZoneOffset.UTC);
        OffsetDateTime start = OffsetDateTime.of(2022, 1, 31, 6, 42, 0, 0, ZoneOffset.UTC);
        OffsetDateTime expected = OffsetDateTime.of(2022, 3, 31, 6, 42, 0, 0, ZoneOffset.UTC);

        OffsetDateTime dateTime = generator.generateFirstOccurrence(from, to, start);

        assertEquals(expected, dateTime);
    }

    @Test
    public void Start_Before_From_Returns_CorrectDate_in_New_Year() {

        OffsetDateTime from = OffsetDateTime.of(2021, 12, 12, 7, 42, 0, 0, ZoneOffset.UTC);
        OffsetDateTime to = OffsetDateTime.of(2022, 7, 1, 12, 1, 0, 0, ZoneOffset.UTC);
        OffsetDateTime start = OffsetDateTime.of(2021, 11, 11, 12, 15, 0, 0, ZoneOffset.UTC);
        OffsetDateTime expected = OffsetDateTime.of(2022, 1, 11, 12, 15, 0, 0, ZoneOffset.UTC);

        OffsetDateTime dateTime = generator.generateFirstOccurrence(from, to, start);

        assertEquals(expected, dateTime);
    }

    @Test
    public void Start_Before_From_Returns_Max_Date_for_First_Date_After_To() {

        OffsetDateTime from = OffsetDateTime.of(2022, 3, 26, 1, 42, 0, 0, ZoneOffset.UTC);
        OffsetDateTime to = OffsetDateTime.of(2022, 4, 1, 12, 1, 0, 0, ZoneOffset.UTC);
        OffsetDateTime start = OffsetDateTime.of(2022, 2, 9, 2, 15, 0, 0, ZoneOffset.UTC);
        OffsetDateTime expected = DateTimeProperties.MAX_OFFSET_DATETIME;

        OffsetDateTime dateTime = generator.generateFirstOccurrence(from, to, start);

        assertEquals(expected, dateTime);
    }

    @Test
    public void Get_Next_Occurrence_Returns_Correct_Date() {

        OffsetDateTime start = OffsetDateTime.of(2022, 3, 29, 18, 32, 0, 0, ZoneOffset.UTC);
        OffsetDateTime expected = OffsetDateTime.of(2022, 4, 29, 18, 32, 0, 0, ZoneOffset.UTC);

        OffsetDateTime dateTime = generator.getNextOccurrence(start);

        assertEquals(expected, dateTime);
    }

    @Test
    public void Get_Next_Occurrence_Skips_Month_if_Month_has_to_Few_Days() {

        OffsetDateTime start = OffsetDateTime.of(2022, 8, 31, 1, 55, 0, 0, ZoneOffset.UTC);
        OffsetDateTime expected = OffsetDateTime.of(2022, 10, 31, 1, 55, 0, 0, ZoneOffset.UTC);

        OffsetDateTime dateTime = generator.getNextOccurrence(start);

        assertEquals(expected, dateTime);
    }
}
