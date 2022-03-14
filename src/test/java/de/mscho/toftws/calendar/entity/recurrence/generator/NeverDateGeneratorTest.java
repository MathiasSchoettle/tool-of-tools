package de.mscho.toftws.calendar.entity.recurrence.generator;

import de.mscho.toftws.util.DateTimeProperties;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("unit")
public class NeverDateGeneratorTest {

    RecurrenceDateGenerator generator = new NeverDateGenerator();

    @Test
    public void Start_Before_From_Returns_Max_Date() {

        OffsetDateTime from = OffsetDateTime.of(2022, 3, 26, 1, 42, 0, 0, ZoneOffset.UTC);
        OffsetDateTime to = OffsetDateTime.of(2022, 7, 1, 12, 1, 0, 0, ZoneOffset.UTC);
        OffsetDateTime start = OffsetDateTime.of(2022, 1, 12, 18, 32, 0, 0, ZoneOffset.UTC);
        OffsetDateTime expected = DateTimeProperties.MAX_OFFSET_DATETIME;

        OffsetDateTime dateTime = generator.generateFirstOccurrence(from, to, start);

        assertEquals(expected, dateTime);
    }

    @Test
    public void Get_Next_Occurrence_Returns_Max_Date() {

        OffsetDateTime expected = DateTimeProperties.MAX_OFFSET_DATETIME;

        OffsetDateTime dateTime = generator.getNextOccurrence(null);

        assertEquals(expected, dateTime);
    }
}
