package de.mscho.toftws.calendar.util;

import de.mscho.toftws.entity.calendar.event.*;
import de.mscho.toftws.entity.calendar.recurrence.Recurrence;
import de.mscho.toftws.entity.calendar.util.CalendarEventBuilder;
import de.mscho.toftws.util.DateTimeProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
public class CalendarEventBuilderTest {

    TimepointEvent timepoint = new TimepointEvent();

    TimespanEvent timespan = new TimespanEvent();

    DayEvent day = new DayEvent();

    DayspanEvent dayspan = new DayspanEvent();

    OffsetDateTime dateTime;

    OffsetDateTime until;

    LocalDate date = LocalDate.of(2002, 7, 12);

    @BeforeEach
    public void beforeEach() {
        date = LocalDate.of(2002, 7, 12);
        dateTime = OffsetDateTime.of(date, LocalTime.of(12, 45, 32), ZoneOffset.UTC);
        until = OffsetDateTime.of(date.plusDays(5), LocalTime.of(22, 45, 29), ZoneOffset.UTC);
    }

    @Test
    public void Duration_of_Timepoint_is_Zero() {

        CalendarEventBuilder builder = new CalendarEventBuilder(dateTime);

        CalendarEvent event = builder.build(timepoint, null, null);

        assertEquals(0, event.getCalendarPosition().getDuration());
    }

    @Test
    public void UntilDate_of_Timepoint_is_StartDate_for_Recurrence_Never() {

        CalendarEventBuilder builder = new CalendarEventBuilder(dateTime);

        CalendarEvent event = builder.build(timepoint, null, Recurrence.NEVER);

        assertEquals(dateTime, event.getCalendarPosition().getStartDate());
    }

    @Test
    public void UntilDate_of_Timespan_is_StartDate_plus_Duration_for_Recurrence_Never() {

        long duration = 60 * 3;
        OffsetDateTime start = dateTime.plusSeconds(duration);
        CalendarEventBuilder builder = new CalendarEventBuilder(dateTime, duration);

        CalendarEvent event = builder.build(timespan, null, Recurrence.NEVER);

        assertEquals(start, event.getCalendarPosition().getUntilDate());
    }

    @Test
    public void Duration_of_Day_Event_is_24_Hours() {

        long duration = 24 * 60 * 60;
        CalendarEventBuilder builder = new CalendarEventBuilder(date);

        CalendarEvent event = builder.build(day, null, null);

        assertEquals(duration, event.getCalendarPosition().getDuration());
    }

    @Test
    public void UntilDate_of_Day_is_StartDate_plus_24_Hours_for_Recurrence_Never() {

        long duration = 24 * 60 * 60;
        OffsetDateTime start = OffsetDateTime.of(date.atStartOfDay(), ZoneOffset.UTC);
        CalendarEventBuilder builder = new CalendarEventBuilder(date);

        CalendarEvent event = builder.build(dayspan, null, Recurrence.NEVER);

        assertEquals(start.plusSeconds(duration), event.getCalendarPosition().getUntilDate());
    }

    @Test
    public void UntilDate_of_Event_is_Max_for_Recurrence_not_Never_and_UntilDate_Null() {

        CalendarEventBuilder builder = new CalendarEventBuilder(dateTime);

        CalendarEvent event = builder.build(timepoint, null, null);

        assertEquals(DateTimeProperties.MAX_OFFSET_DATETIME, event.getCalendarPosition().getUntilDate());
    }

    @Test
    public void UntilDate_of_Event_is_Same_for_Recurrence_not_Never() {

        CalendarEventBuilder builder = new CalendarEventBuilder(dateTime);

        CalendarEvent event = builder.build(timepoint, until, null);

        assertEquals(until, event.getCalendarPosition().getUntilDate());
    }

    @Test
    public void If_Input_is_DateTime_AllDay_is_False() {

        CalendarEventBuilder builder = new CalendarEventBuilder(dateTime);

        CalendarEvent event = builder.build(timepoint, null, null);

        assertFalse(event.getCalendarPosition().isAllDay());
    }

    @Test
    public void If_Input_is_Date_AllDay_is_True() {

        CalendarEventBuilder builder = new CalendarEventBuilder(date);

        CalendarEvent event = builder.build(day, null, null);

        assertTrue(event.getCalendarPosition().isAllDay());
    }
}
