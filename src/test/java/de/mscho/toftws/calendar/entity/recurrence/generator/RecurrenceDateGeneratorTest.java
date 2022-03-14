package de.mscho.toftws.calendar.entity.recurrence.generator;

import de.mscho.toftws.util.DateTimeProperties;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.*;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RecurrenceDateGeneratorTest {

    @Mock
    RecurrenceDateGenerator generator;

    @BeforeEach
    public void beforeAll() {
        when(generator.generateFirstOccurrence(any(OffsetDateTime.class), any(OffsetDateTime.class), any(OffsetDateTime.class))).thenCallRealMethod();
    }

    @Test
    public void Start_After_To_Returns_Max_Date() {

        OffsetDateTime from = OffsetDateTime.of(2022, 3, 26, 1, 42, 0, 0, ZoneOffset.UTC);
        OffsetDateTime to = OffsetDateTime.of(2022, 7, 1, 12, 1, 0, 0, ZoneOffset.UTC);
        OffsetDateTime start = OffsetDateTime.of(2022, 8, 2, 9, 48, 0, 0, ZoneOffset.UTC);
        OffsetDateTime expected = DateTimeProperties.MAX_OFFSET_DATETIME;

        OffsetDateTime dateTime = generator.generateFirstOccurrence(from, to, start);

        assertEquals(expected, dateTime);
    }

    @Test
    public void Start_Equal_to_From_Returns_Start() {

        OffsetDateTime fromAndStart = OffsetDateTime.of(2022, 3, 26, 1, 42, 0, 0, ZoneOffset.UTC);
        OffsetDateTime to = OffsetDateTime.of(2022, 7, 1, 12, 1, 0, 0, ZoneOffset.UTC);
        OffsetDateTime expected = OffsetDateTime.of(2022, 3, 26, 1, 42, 0, 0, ZoneOffset.UTC);

        OffsetDateTime dateTime = generator.generateFirstOccurrence(fromAndStart, to, fromAndStart);

        assertEquals(expected, dateTime);
    }

    @Test
    public void Start_Equal_to_To_Returns_Start() {

        OffsetDateTime from = OffsetDateTime.of(2022, 3, 26, 1, 42, 0, 0, ZoneOffset.UTC);
        OffsetDateTime toAndStart = OffsetDateTime.of(2022, 7, 1, 12, 1, 0, 0, ZoneOffset.UTC);
        OffsetDateTime expected = OffsetDateTime.of(2022, 7, 1, 12, 1, 0, 0, ZoneOffset.UTC);

        OffsetDateTime dateTime = generator.generateFirstOccurrence(from, toAndStart, toAndStart);

        assertEquals(expected, dateTime);
    }

    @Test
    public void Start_in_Timeframe_Returns_Start() {

        OffsetDateTime from = OffsetDateTime.of(2022, 3, 26, 1, 42, 0, 0, ZoneOffset.UTC);
        OffsetDateTime to = OffsetDateTime.of(2022, 7, 1, 12, 1, 0, 0, ZoneOffset.UTC);
        OffsetDateTime start = OffsetDateTime.of(2022, 5, 24, 5, 29, 0, 0, ZoneOffset.UTC);
        OffsetDateTime expected = OffsetDateTime.of(2022, 5, 24, 5, 29, 0, 0, ZoneOffset.UTC);

        OffsetDateTime dateTime = generator.generateFirstOccurrence(from, to, start);

        assertEquals(expected, dateTime);
    }
}
