package de.mscho.toftws.calendar.service;

import de.mscho.toftws.configuration.security.AuthenticationProvider;
import de.mscho.toftws.calendar.entity.Event;
import de.mscho.toftws.calendar.entity.payload.CalendarEventRequest;
import de.mscho.toftws.calendar.entity.payload.EventDto;
import de.mscho.toftws.calendar.entity.recurrence.*;
import de.mscho.toftws.calendar.repository.DeviationRepo;
import de.mscho.toftws.calendar.repository.EventContentRepo;
import de.mscho.toftws.calendar.repository.EventRepo;
import de.mscho.toftws.calendar.repository.RecurrenceRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
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
    private final DeviationRepo deviationRepo;
    private final AuthenticationProvider authenticationProvider;

    public List<EventDto> getEvents(ZonedDateTime from, ZonedDateTime to) {
        var user = authenticationProvider.getAuthenticatedUser();
        var events = eventRepo.findEventsByRecurrenceStartBeforeAndRecurrenceEndAfterAndUser(to, from, user);
        return events.stream().map(e -> e.getEvents(from, to)).flatMap(Collection::stream).toList();
    }

    public void createEvent(CalendarEventRequest request) {
        var event = getEvent(request);
        event = eventRepo.save(event);
        logger.info("created event(id:{})", event.id);
    }

    public void deleteEvent(long eventId) {
        var user = authenticationProvider.getAuthenticatedUser();
        long deleted = eventRepo.deleteByIdAndUser(eventId, user);

        if (deleted > 0) logger.info("Deleted Event. id:{}", eventId);
    }

    public void editEvent(long eventId, CalendarEventRequest request) {
        var user = authenticationProvider.getAuthenticatedUser();
        var eventOptional = eventRepo.findByIdAndUser(eventId, user);

        if (eventOptional.isEmpty()) {
            logger.info("No event(id: {}) found for update", eventId);
            return;
        }

        var existingEvent = eventOptional.get();
        clearEvent(existingEvent);

        var otherEvent = getEvent(request);
        existingEvent.fill(otherEvent);

        existingEvent = eventRepo.save(existingEvent);
        logger.info("edited event(id:{})", existingEvent.id);
    }

    private void clearEvent(Event event) {
        deviationRepo.deleteAll(event.deviations);
        contentRepo.delete(event.content);
        recurrenceRepo.delete(event.recurrence);
    }

    private Event getEvent(CalendarEventRequest request) {
        return switch (request.type) {
            case SINGLE -> getSingleEvent(request);
            case DAILY -> getDailyEvent(request);
            case WEEKLY -> getWeeklyEvent(request);
            case MONTHLY -> getMonthlyEvent(request);
            case YEARLY -> getYearlyEvent(request);
        };
    }

    private Event getSingleEvent(CalendarEventRequest request) {
        var end = request.start.plusSeconds(request.duration);
        var recurrence = new SingleRecurrence(request.start, end);
        return buildEvent(request, recurrence);
    }

    private Event getDailyEvent(CalendarEventRequest request) {
        var end = request.end;

        if (request.occurrences != null) {
            end = request.start.plusDays((long) request.offset * request.occurrences - 1).plusSeconds(request.duration);
        }

        var recurrence = new DailyRecurrence(request.start, end, request.offset);
        return buildEvent(request, recurrence);
    }

    private Event getWeeklyEvent(CalendarEventRequest request) {
        var end = request.end;

        if (request.occurrences != null) {
            end = request.start.plusWeeks((long) request.offset * request.occurrences - 1);
            end = end.with(TemporalAdjusters.nextOrSame(request.weekDays.last())).plusSeconds(request.duration);
        }

        var recurrence = new WeeklyRecurrence(request.start, end, request.offset, request.weekDays);
        return buildEvent(request, recurrence);
    }

    private Event getMonthlyEvent(CalendarEventRequest request) {
        var end = request.end;

        if (request.occurrences != null) {
            end = request.start.plusMonths((long) request.offset * request.occurrences - 1);
        }

        var recurrence = new MonthlyRecurrence(request.start, end, request.offset);
        return buildEvent(request, recurrence);
    }

    private Event getYearlyEvent(CalendarEventRequest request) {
        var end = request.end;

        if (request.occurrences != null) {
            end = request.start.plusYears((long) request.offset * request.occurrences - 1).plusSeconds(request.duration);
        }

        var recurrence = new YearlyRecurrence(request.start, end, request.offset);
        return buildEvent(request, recurrence);
    }

    private Event buildEvent(CalendarEventRequest request, Recurrence recurrence) {
        var event = new Event();
        event.content = request.content.toEntity();
        event.duration = request.duration;
        event.recurrence = recurrence;
        event.user = authenticationProvider.getAuthenticatedUser();

        var categoryOptional = categoryService.getCategory(request.categoryId);
        categoryOptional.ifPresent(category -> event.category = category);

        return event;
    }
}
