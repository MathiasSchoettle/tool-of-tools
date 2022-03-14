package de.mscho.toftws.calendar.service;

import de.mscho.toftws.calendar.entity.event.*;
import de.mscho.toftws.calendar.entity.recurrence.Recurrence;

import java.time.OffsetDateTime;
import java.util.List;

public interface CalendarEventService {

    CalendarEvent addTimepointEvent(TimepointEvent timepointEvent, Recurrence recurrence, OffsetDateTime potentialUntilDate);

    CalendarEvent addTimespanEvent(TimespanEvent timespanEvent, Recurrence recurrence, OffsetDateTime potentialUntilDate);

    CalendarEvent addDayEvent(DayEvent dayEvent, Recurrence recurrence, OffsetDateTime potentialUntilDate);

    CalendarEvent addDayspanEvent(DayspanEvent dayspanevent, Recurrence recurrence, OffsetDateTime potentialUntilDate);

    List<AbstractCalendarEvent> getEventsOfTimespan(OffsetDateTime fromDate, OffsetDateTime toDate);
}
