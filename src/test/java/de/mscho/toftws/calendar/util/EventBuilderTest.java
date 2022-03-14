package de.mscho.toftws.calendar.util;

import de.mscho.toftws.calendar.entity.event.CalendarEvent;
import de.mscho.toftws.calendar.entity.event.CalendarPosition;
import de.mscho.toftws.calendar.entity.recurrence.Recurrence;
import de.mscho.toftws.calendar.entity.util.EventBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
public class EventBuilderTest {

    private static final String message = "Expected amount of events is not actual amount of events";

    OffsetDateTime start = OffsetDateTime.of(2021, 2, 1, 12, 0, 0, 0, ZoneOffset.UTC);

    OffsetDateTime end = OffsetDateTime.of(2021, 7, 12, 18, 30, 0, 0, ZoneOffset.UTC);

    CalendarEvent event;

    @BeforeEach
    public void beforeEach() {
        CalendarPosition position = new CalendarPosition();
        position.setDuration(0);
        position.setAllDay(false);
        position.setRecurrence(Recurrence.NEVER);

        event = new CalendarEvent();
        event.setCalendarPosition(position);
    }

    @Test
    public void Amount_correct_for_Once_Event_in_Timeframe() {

        OffsetDateTime eventStart = OffsetDateTime.of(2021, 3, 22, 10, 32, 16, 0, ZoneOffset.UTC);
        event.getCalendarPosition().setStartDate(eventStart);
        event.getCalendarPosition().setUntilDate(eventStart);

        var events = new EventBuilder(event, start, end).build();
        assertEquals(1, events.size(), message);
    }

    @Test
    public void Amount_correct_for_Once_Event_outside_Timeframe() {

        OffsetDateTime eventStart = OffsetDateTime.of(2021, 1, 31, 10, 32, 16, 0, ZoneOffset.UTC);
        event.getCalendarPosition().setStartDate(eventStart);
        event.getCalendarPosition().setUntilDate(eventStart);

        var events = new EventBuilder(event, start, end).build();
        assertEquals(0, events.size(), message);
    }

    @Test
    public void Amount_correct_Weekly_Event_Start_and_End_in_Timeframe() {

        OffsetDateTime eventStart = OffsetDateTime.of(2021, 3, 22, 10, 32, 16, 0, ZoneOffset.UTC);
        OffsetDateTime eventUntil = OffsetDateTime.of(2021, 6, 12, 4, 13, 2, 0, ZoneOffset.UTC);

        event.getCalendarPosition().setStartDate(eventStart);
        event.getCalendarPosition().setUntilDate(eventUntil);
        event.getCalendarPosition().setRecurrence(Recurrence.WEEKLY);

        var events = new EventBuilder(event, start, end).build();

        assertEquals(12, events.size(), message);
    }

    @Test
    public void Amount_correct_Weekly_Event_End_in_Timeframe() {

        OffsetDateTime eventStart = OffsetDateTime.of(2020, 12, 4, 10, 32, 16, 0, ZoneOffset.UTC);
        OffsetDateTime eventUntil = OffsetDateTime.of(2021, 4, 6, 4, 13, 2, 0, ZoneOffset.UTC);

        event.getCalendarPosition().setStartDate(eventStart);
        event.getCalendarPosition().setUntilDate(eventUntil);
        event.getCalendarPosition().setRecurrence(Recurrence.WEEKLY);

        var events = new EventBuilder(event, start, end).build();

        assertEquals(9, events.size(), message);
    }

    @Test
    public void Amount_correct_Weekly_Event_Start_in_Timeframe() {

        OffsetDateTime eventStart = OffsetDateTime.of(2021, 6, 21, 10, 32, 16, 0, ZoneOffset.UTC);
        OffsetDateTime eventUntil = OffsetDateTime.of(2021, 8, 3, 4, 13, 2, 0, ZoneOffset.UTC);

        event.getCalendarPosition().setStartDate(eventStart);
        event.getCalendarPosition().setUntilDate(eventUntil);
        event.getCalendarPosition().setRecurrence(Recurrence.WEEKLY);

        var events = new EventBuilder(event, start, end).build();

        assertEquals(4, events.size(), message);
    }

    @Test
    public void Amount_correct_Weekly_Event_Start_and_End_outside_Timeframe() {

        OffsetDateTime eventStart = OffsetDateTime.of(2021, 1, 24, 10, 32, 16, 0, ZoneOffset.UTC);
        OffsetDateTime eventUntil = OffsetDateTime.of(2021, 9, 12, 4, 13, 2, 0, ZoneOffset.UTC);

        event.getCalendarPosition().setStartDate(eventStart);
        event.getCalendarPosition().setUntilDate(eventUntil);
        event.getCalendarPosition().setRecurrence(Recurrence.WEEKLY);

        var events = new EventBuilder(event, start, end).build();

        assertEquals(23, events.size(), message);
    }

    @Test
    public void Event_With_Start_on_Start_of_Timeframe_is_Included() {

        event.getCalendarPosition().setStartDate(start);
        event.getCalendarPosition().setUntilDate(start);

        var events = new EventBuilder(event, start, end).build();

        assertEquals(1, events.size(), message);
    }

    @Test
    public void Event_With_Start_on_End_of_Timeframe_is_Included() {

        event.getCalendarPosition().setStartDate(end);
        event.getCalendarPosition().setUntilDate(end);

        var events = new EventBuilder(event, start, end).build();

        assertEquals(1, events.size(), message);
    }
}
