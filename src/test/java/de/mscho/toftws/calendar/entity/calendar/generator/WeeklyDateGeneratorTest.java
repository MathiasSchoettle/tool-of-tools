package de.mscho.toftws.calendar.entity.calendar.generator;

import de.mscho.toftws.entity.calendar.recurrence.generator.RecurrenceDateGenerator;
import de.mscho.toftws.entity.calendar.recurrence.generator.WeeklyDateGenerator;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("unit")
public class WeeklyDateGeneratorTest {

    RecurrenceDateGenerator generator = new WeeklyDateGenerator(1);

    @Test
    public void Negative_WeekCount_Throws_IllegalArgumentException() {

        assertThrows(IllegalArgumentException.class, () -> new WeeklyDateGenerator(-1));
    }

    @Test
    public void Start_Before_From_Returns_CorrectDate() {

        OffsetDateTime from = OffsetDateTime.of(2022, 3, 26, 1, 42, 0, 0, ZoneOffset.UTC);
        OffsetDateTime to = OffsetDateTime.of(2022, 7, 1, 12, 1, 0, 0, ZoneOffset.UTC);
        OffsetDateTime start = OffsetDateTime.of(2022, 2, 12, 18, 32, 0, 0, ZoneOffset.UTC);
        OffsetDateTime expected = OffsetDateTime.of(2022, 3, 26, 18, 32, 0, 0, ZoneOffset.UTC);

        OffsetDateTime dateTime = generator.generateFirstOccurrence(from, to, start);

        assertEquals(expected, dateTime);
    }

    @Test
    public void Get_Next_Occurrence_Returns_Correct_Date() {

        OffsetDateTime start = OffsetDateTime.of(2022, 3, 29, 18, 32, 0, 0, ZoneOffset.UTC);
        OffsetDateTime expected = OffsetDateTime.of(2022, 4, 5, 18, 32, 0, 0, ZoneOffset.UTC);

        OffsetDateTime dateTime = generator.getNextOccurrence(start);

        assertEquals(expected, dateTime);
    }
}
