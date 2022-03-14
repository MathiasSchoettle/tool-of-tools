package de.mscho.toftws.calendar.entity.event;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.OffsetDateTime;
import java.util.concurrent.TimeUnit;

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractEvent extends AbstractCalendarEvent {

    abstract AbstractEvent copy();

    abstract void setNewStart(OffsetDateTime start);

    public AbstractEvent ofNewStart(OffsetDateTime start) {
        AbstractEvent event = getEvent();
        event.setNewStart(start);
        return event;
    }

    private AbstractEvent getEvent() {
        AbstractEvent event = copy();
        event.setEventContent(this);
        return event;
    }

    private void setEventContent(AbstractCalendarEvent event) {
        this.title = event.title;
        this.description = event.description;
        this.hexColor = event.hexColor;
        this.location = event.location;
        this.meetingLink = event.meetingLink;
    }

    public static AbstractEvent getEventFromCalendarEvent(CalendarEvent calendarEvent) {

        CalendarPosition position = calendarEvent.getCalendarPosition();
        AbstractEvent event;

        if (position.getDuration() == 0) {
            event = new TimepointEvent(position.getStartDate());
        } else if (!position.isAllDay()) {
            event = new TimespanEvent(position.getStartDate(), position.getDuration());
        } else if (position.getDuration() == TimeUnit.DAYS.toSeconds(1)) {
            event = new DayEvent(position.getStartDate().toLocalDate());
        } else {
            event = new DayspanEvent(position.getStartDate().toLocalDate(), position.getDuration() / TimeUnit.DAYS.toSeconds(1));
        }

        event.setEventContent(calendarEvent);
        return event;
    }
}