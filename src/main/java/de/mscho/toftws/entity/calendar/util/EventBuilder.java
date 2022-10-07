package de.mscho.toftws.entity.calendar.util;

import de.mscho.toftws.entity.calendar.event.AbstractCalendarEvent;
import de.mscho.toftws.entity.calendar.event.AbstractEvent;
import de.mscho.toftws.entity.calendar.event.CalendarEvent;
import de.mscho.toftws.entity.calendar.event.CalendarPosition;
import de.mscho.toftws.entity.calendar.recurrence.generator.RecurrenceDateGenerator;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EventBuilder {

    private final CalendarPosition position;

    private final RecurrenceDateGenerator dateGenerator;

    private final OffsetDateTime from;

    private final OffsetDateTime to;

    private final AbstractEvent event;

    public EventBuilder(CalendarEvent calendarEvent, OffsetDateTime from, OffsetDateTime to) {
        this.position = calendarEvent.getCalendarPosition();
        this.dateGenerator = this.position.getRecurrence().dateGenerator;
        this.from = from;
        this.to = to;
        this.event = AbstractEvent.getEventFromCalendarEvent(calendarEvent);
    }

    public List<AbstractCalendarEvent> build() {

        List<OffsetDateTime> eventDateTimes = getOccurrencesForCalendarEvent();

        return eventDateTimes.stream()
                .map(event::ofNewStart)
                .collect(Collectors.toList());
    }

    private List<OffsetDateTime> getOccurrencesForCalendarEvent() {

        OffsetDateTime currentOccurrence = dateGenerator.generateFirstOccurrence(from, to, position.getStartDate());

        List<OffsetDateTime> occurrences = new ArrayList<>();

        while(!currentOccurrence.isAfter(to) && !currentOccurrence.isAfter(position.getUntilDate())) {
            occurrences.add(currentOccurrence);
            currentOccurrence = dateGenerator.getNextOccurrence(currentOccurrence);
        }

        return occurrences;
    }
}
