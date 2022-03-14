package de.mscho.toftws.util.convert;

import de.mscho.toftws.calendar.entity.recurrence.Recurrence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
public class RecurrenceConverterTest {

    RecurrenceConverter converter;

    @BeforeEach
    public void beforeEach() {
        converter = new RecurrenceConverter();
    }

    @Test
    public void Source_Is_converted_To_Recurrence() {

        String source = "never";
        Recurrence recurrence = converter.convert(source);

        assertEquals(Recurrence.NEVER, recurrence);
    }

    @Test
    public void Source_With_Different_Cases_Is_converted_To_Recurrence() {

        String source = "wEEkLy";
        Recurrence recurrence = converter.convert(source);

        assertEquals(Recurrence.WEEKLY, recurrence);
    }

    @Test
    public void Invalid_Source_Throws_IllegalArgumentException() {

        String source = "invalid_string";
        assertThrows(IllegalArgumentException.class, () -> converter.convert(source));
    }
}
