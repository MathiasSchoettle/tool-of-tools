package de.mscho.toftws.service;

import de.mscho.toftws.entity.calendar.Event;
import de.mscho.toftws.entity.calendar.payload.EventDto;
import de.mscho.toftws.entity.calendar.payload.EventRequest;
import de.mscho.toftws.entity.calendar.payload.OffsetEventRequest;
import de.mscho.toftws.entity.calendar.payload.WeeklyEventRequest;
import de.mscho.toftws.entity.calendar.recurrence.*;
import de.mscho.toftws.repository.calendar.EventContentRepo;
import de.mscho.toftws.repository.calendar.EventRepo;
import de.mscho.toftws.repository.calendar.RecurrenceRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CalendarService {
    private final Logger logger;
    private final CategoryService categoryService;
    private final EventRepo eventRepo;
    private final EventContentRepo contentRepo;
    private final RecurrenceRepo recurrenceRepo;

    public List<EventDto> getEvents(ZonedDateTime from, ZonedDateTime to) {
        var events = eventRepo.getEventsByRecurrenceStartBeforeAndRecurrenceEndAfter(to, from);
        return events.stream().map(e -> e.getEvents(from, to)).flatMap(Collection::stream).toList();
    }

    public void deleteEvent(long id) {
        eventRepo.deleteById(id);
        logger.info("Deleted Event. id:{}", id);
    }

    public void createDailyEvent(OffsetEventRequest request) {
        var recurrence = new DailyRecurrence(request.start, request.end, request.offset);
        recurrenceRepo.save(recurrence);
        var event = createEvent(request, recurrence);
        logger.info("Created daily calendar event. id:{}", event.id);
    }

    public void createWeeklyEvent(WeeklyEventRequest request) {
        var recurrence = new WeeklyRecurrence(request.start, request.end, request.offset, request.weekDays);
        recurrenceRepo.save(recurrence);
        var event = createEvent(request, recurrence);
        logger.info("Created weekly calendar event. id:{}", event.id);
    }

    public void createMonthlyEvent(OffsetEventRequest request) {
        var recurrence = new MonthlyRecurrence(request.start, request.end, request.offset);
        recurrenceRepo.save(recurrence);
        var event = createEvent(request, recurrence);
        logger.info("Created monthly calendar event. id:{}", event.id);
    }

    public void createYearlyEvent(EventRequest request) {
        var recurrence = new YearlyRecurrence(request.start, request.end);
        recurrenceRepo.save(recurrence);
        var event = createEvent(request, recurrence);
        logger.info("Created yearly calendar event. id:{}", event.id);
    }

    private Event createEvent(EventRequest request, Recurrence recurrence) {
        var event = new Event();
        var content = request.content.toEntity();
        content = contentRepo.save(content);

        event.duration = request.duration;
        event.content = content;
        event.recurrence = recurrence;
        event.creator = null;
        event.category = categoryService.getCategory(request.categoryId);

        return eventRepo.save(event);
    }
}
