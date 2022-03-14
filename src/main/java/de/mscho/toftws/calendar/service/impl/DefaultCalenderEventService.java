package de.mscho.toftws.calendar.service.impl;

import de.mscho.toftws.calendar.entity.event.*;
import de.mscho.toftws.calendar.entity.recurrence.Recurrence;
import de.mscho.toftws.calendar.repository.CalendarEventRepository;
import de.mscho.toftws.calendar.service.CalendarEventService;
import de.mscho.toftws.calendar.entity.util.CalendarEventBuilder;
import de.mscho.toftws.calendar.entity.util.EventBuilder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor

@Service
public class DefaultCalenderEventService implements CalendarEventService {

    private final CalendarEventRepository calendarEventRepository;

    private final Logger logger;

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public CalendarEvent addTimepointEvent(TimepointEvent timepointEvent, Recurrence recurrence, OffsetDateTime potentialUntilDate) {

        OffsetDateTime startDateTime = timepointEvent.getDateTime();

        CalendarEventBuilder calendarEventBuilder = new CalendarEventBuilder(startDateTime);
        CalendarEvent calendarEvent = calendarEventBuilder.build(timepointEvent, potentialUntilDate, recurrence);

        return calendarEventRepository.save(calendarEvent);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public CalendarEvent addTimespanEvent(TimespanEvent timespanEvent, Recurrence recurrence, OffsetDateTime potentialUntilDate) {

        OffsetDateTime startDateTime = timespanEvent.getStartDateTime();
        long duration = timespanEvent.getDuration();

        CalendarEventBuilder calendarEventBuilder = new CalendarEventBuilder(startDateTime, duration);
        CalendarEvent calendarEvent = calendarEventBuilder.build(timespanEvent, potentialUntilDate, recurrence);

        return calendarEventRepository.save(calendarEvent);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public CalendarEvent addDayEvent(DayEvent dayEvent, Recurrence recurrence, OffsetDateTime potentialUntilDate) {

        LocalDate startDate = dayEvent.getDate();

        CalendarEventBuilder calendarEventBuilder = new CalendarEventBuilder(startDate);
        CalendarEvent calendarEvent = calendarEventBuilder.build(dayEvent, potentialUntilDate, recurrence);

        return calendarEventRepository.save(calendarEvent);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public CalendarEvent addDayspanEvent(DayspanEvent dayspanEvent, Recurrence recurrence, OffsetDateTime potentialUntilDate) {

        LocalDate startDate = dayspanEvent.getStartDate();
        long amountOfDays = dayspanEvent.getDays();

        CalendarEventBuilder calendarEventBuilder = new CalendarEventBuilder(startDate, amountOfDays);
        CalendarEvent calendarEvent = calendarEventBuilder.build(dayspanEvent, potentialUntilDate, recurrence);

        return calendarEventRepository.save(calendarEvent);
    }

    @Override
    public List<AbstractCalendarEvent> getEventsOfTimespan(OffsetDateTime fromDate, OffsetDateTime toDate) {

        List<AbstractCalendarEvent> returnEvents = new ArrayList<>();
        List<CalendarEvent> calendarEvents = calendarEventRepository.findAllByCalendarPosition_UntilDateGreaterThanEqualAndCalendarPosition_StartDateLessThan(fromDate, toDate);

        for( CalendarEvent calendarEvent : calendarEvents ) {
            List<AbstractCalendarEvent> events = new EventBuilder(calendarEvent, fromDate, toDate).build();
            returnEvents.addAll(events);
        }

        return returnEvents;
    }
}
