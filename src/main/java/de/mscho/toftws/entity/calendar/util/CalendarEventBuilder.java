package de.mscho.toftws.entity.calendar.util;

import de.mscho.toftws.entity.calendar.event.AbstractCalendarEvent;
import de.mscho.toftws.entity.calendar.event.CalendarEvent;
import de.mscho.toftws.entity.calendar.event.CalendarPosition;
import de.mscho.toftws.entity.calendar.recurrence.Recurrence;
import de.mscho.toftws.util.DateTimeProperties;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

public class CalendarEventBuilder {

    private final OffsetDateTime startDateTime;

    private final long duration;

    private final boolean allDay;

    public CalendarEventBuilder(OffsetDateTime startDateTime) {
        this(startDateTime, 0);
    }

    public CalendarEventBuilder(OffsetDateTime startDateTime, long duration) {
        this.startDateTime = startDateTime;
        this.duration = duration;
        this.allDay = false;
    }

    public CalendarEventBuilder(LocalDate startDate) {
        this(startDate, 1);
    }

    public CalendarEventBuilder(LocalDate startDate, long amountOfDays) {
        this.startDateTime = OffsetDateTime.of(startDate.atStartOfDay(), ZoneOffset.UTC);
        this.duration = TimeUnit.DAYS.toSeconds(amountOfDays);
        this.allDay = true;
    }

    public CalendarEvent build(AbstractCalendarEvent event, OffsetDateTime potentialUntilDate, Recurrence recurrence) {

        CalendarPosition position = buildCalendarPosition(potentialUntilDate, recurrence);
        return buildCalendarEvent(event, position);
    }

    private CalendarPosition buildCalendarPosition(OffsetDateTime potentialUntilDate, Recurrence recurrence) {

        CalendarPosition position = new CalendarPosition();
        position.setStartDate(startDateTime);
        position.setDuration(duration);
        position.setAllDay(allDay);

        OffsetDateTime untilDate = buildUntilDate(potentialUntilDate, recurrence);
        position.setUntilDate(untilDate);
        position.setRecurrence(recurrence);

        return position;
    }

    private OffsetDateTime buildUntilDate(OffsetDateTime potentialUntilDate, Recurrence recurrence) {

        if(Recurrence.NEVER.equals(recurrence)) {
            return this.startDateTime.plusSeconds(duration);
        }

        return potentialUntilDate == null ? DateTimeProperties.MAX_OFFSET_DATETIME : potentialUntilDate;
    }

    private CalendarEvent buildCalendarEvent(AbstractCalendarEvent event, CalendarPosition position) {

        CalendarEvent calendarEvent = prefillCalendarEvent(event);
        calendarEvent.setCalendarPosition(position);

        return calendarEvent;
    }

    private CalendarEvent prefillCalendarEvent(AbstractCalendarEvent event) {

        CalendarEvent calendarEvent = new CalendarEvent();
        calendarEvent.setTitle(event.getTitle());
        calendarEvent.setDescription(event.getDescription());
        calendarEvent.setHexColor(event.getHexColor());
        calendarEvent.setLocation(event.getLocation());
        calendarEvent.setMeetingLink(event.getMeetingLink());
        return calendarEvent;
    }
}
