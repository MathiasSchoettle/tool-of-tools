package de.mscho.toftws.calendar.entity.calendar.validate;

import de.mscho.toftws.entity.calendar.event.CalendarPosition;
import de.mscho.toftws.entity.calendar.recurrence.Recurrence;
import de.mscho.toftws.entity.calendar.validate.CalendarPositionValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
public class CalendarPositionValidatorTest {

    CalendarPositionValidator validator;

    OffsetDateTime startDateTime;

    @BeforeEach
    public void beforeEach() {
        validator = new CalendarPositionValidator();
        startDateTime = OffsetDateTime.of(LocalDateTime.of(2020, 3, 4, 2, 4, 52), ZoneOffset.UTC);
    }

    @Test
    public void Valid_CalendarPosition_Weekly_is_Validated() {

        var untilDateTime = OffsetDateTime.of(LocalDateTime.of(2020, 7, 3, 4, 32, 9), ZoneOffset.UTC);

        CalendarPosition position = new CalendarPosition();
        position.setDuration(3800);
        position.setAllDay(false);
        position.setRecurrence(Recurrence.WEEKLY);
        position.setStartDate(startDateTime);
        position.setUntilDate(untilDateTime);

        boolean isValid = validator.isValid(position, null);
        assertTrue(isValid);
    }

    @Test
    public void Valid_CalendarPosition_Never_is_Validated() {

        var untilDateTime = OffsetDateTime.of(LocalDateTime.of(2020, 7, 3, 4, 32, 9), ZoneOffset.UTC);

        CalendarPosition position = new CalendarPosition();
        position.setDuration(0);
        position.setAllDay(false);
        position.setRecurrence(Recurrence.NEVER);
        position.setStartDate(startDateTime);
        position.setUntilDate(untilDateTime);

        boolean isValid = validator.isValid(position, null);
        assertTrue(isValid);
    }

    @Test
    public void Invalid_CalendarPosition_Never_is_Validated() {

        var untilDateTime = OffsetDateTime.of(LocalDateTime.of(2020, 7, 3, 4, 32, 9), ZoneOffset.UTC);

        CalendarPosition position = new CalendarPosition();
        position.setDuration(2304);
        position.setAllDay(false);
        position.setRecurrence(Recurrence.NEVER);
        position.setStartDate(startDateTime);
        position.setUntilDate(untilDateTime);

        boolean isValid = validator.isValid(position, null);
        assertFalse(isValid);
    }

    @Test
    public void Invalid_CalendarPosition_Weekly_is_Validated() {

        var untilDateTime = OffsetDateTime.of(LocalDateTime.of(2020, 7, 3, 4, 32, 9), ZoneOffset.UTC);

        CalendarPosition position = new CalendarPosition();
        position.setDuration(60 * 60 * 24 * 8);
        position.setAllDay(false);
        position.setRecurrence(Recurrence.WEEKLY);
        position.setStartDate(startDateTime);
        position.setUntilDate(untilDateTime);

        boolean isValid = validator.isValid(position, null);
        assertFalse(isValid);
    }

    @Test
    public void Calendar_Position_With_UntilDate_Before_StartDate_is_Invalid() {

        var untilDateTime = OffsetDateTime.of(LocalDateTime.of(2019, 7, 3, 4, 32, 9), ZoneOffset.UTC);

        CalendarPosition position = new CalendarPosition();
        position.setDuration(1800);
        position.setAllDay(false);
        position.setRecurrence(Recurrence.WEEKLY);
        position.setStartDate(startDateTime);
        position.setUntilDate(untilDateTime);

        boolean isValid = validator.isValid(position, null);
        assertFalse(isValid);
    }
}
