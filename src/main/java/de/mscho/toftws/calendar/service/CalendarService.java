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

import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;

import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.previousOrSame;

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
        var event = getEventFromRequest(request);
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

        var otherEvent = getEventFromRequest(request);
        existingEvent.fill(otherEvent);

        existingEvent = eventRepo.save(existingEvent);
        logger.info("edited event(id:{})", existingEvent.id);
    }

    private void clearEvent(Event event) {
        deviationRepo.deleteAll(event.deviations);
        contentRepo.delete(event.content);
        recurrenceRepo.delete(event.recurrence);
    }

    public Event getEventFromRequest(CalendarEventRequest request) {
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
            end = request.start.plusDays(request.offset * (request.occurrences - 1)).plusSeconds(request.duration);
        }

        var recurrence = new DailyRecurrence(request.start, end, request.offset);
        return buildEvent(request, recurrence);
    }

    private Event getWeeklyEvent(CalendarEventRequest request) {
        var end = request.end;

        // weekly recurrences are tricky when it comes to the occurrence count
        // it depends on which day of the week we start the recurrence and how many occurrences are requested
        // this makes the calculation of the end date a lot more complicated than other recurrence types
        if (request.occurrences != null) {
            // the event start moved to the monday of the week
            var weekStart = request.start.with(previousOrSame(DayOfWeek.MONDAY));

            // the amount of event occurrences we "skipped" e.g. if we have occurrences on Mon, Tue and Fri
            // and our start date is a Fri, we skipped 2 days (Mon, Tue)
            int skipped = (int) request.weekDays.stream().filter(weekDay -> weekDay.compareTo(request.start.getDayOfWeek()) < 0).count();

            // added to the weekStart date we get the monday in the week the end date will be in
            // if we have an offset we need to add additional "empty" weeks in between the weeks which contain the events
            int weeksToAdd = (int) Math.ceil((float) (request.occurrences + skipped) / (float) request.weekDays.size());
            weeksToAdd = (int) (request.offset * (weeksToAdd - 1));

            // the weekDay the last occurrence is in
            // putting together the weekDate and the weekDay gives us the last occurrence
            int lastIndex = (request.occurrences + skipped - 1) % request.weekDays.size();
            DayOfWeek lastDay = (DayOfWeek) request.weekDays.toArray()[lastIndex];
            end = weekStart.plusWeeks(weeksToAdd).with(nextOrSame(lastDay)).plusSeconds(request.duration);
        }

        var recurrence = new WeeklyRecurrence(request.start, end, request.offset, request.weekDays);
        return buildEvent(request, recurrence);
    }

    private Event getMonthlyEvent(CalendarEventRequest request) {
        var end = request.end;

        if (request.occurrences != null) {
            end = request.start.plusMonths(request.offset * (request.occurrences - 1)).plusSeconds(request.duration);
        }

        var recurrence = new MonthlyRecurrence(request.start, end, request.offset);
        return buildEvent(request, recurrence);
    }

    private Event getYearlyEvent(CalendarEventRequest request) {
        var end = request.end;

        if (request.occurrences != null) {
            end = request.start.plusYears(request.offset * (request.occurrences - 1)).plusSeconds(request.duration);
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
