package de.mscho.toftws.calendar.service;

import de.mscho.toftws.calendar.entity.Event;
import de.mscho.toftws.calendar.entity.payload.EventRequest;
import de.mscho.toftws.calendar.entity.recurrence.*;
import de.mscho.toftws.calendar.repository.DeviationRepo;
import de.mscho.toftws.calendar.repository.EventContentRepo;
import de.mscho.toftws.calendar.repository.RecurrenceRepo;
import de.mscho.toftws.configuration.security.AuthenticatedUserProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;

import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.previousOrSame;

@Service
@RequiredArgsConstructor
@Transactional
public class EventService {
    private final CategoryService categoryService;
    private final EventContentRepo contentRepo;
    private final RecurrenceRepo recurrenceRepo;
    private final DeviationRepo deviationRepo;
    private final AuthenticatedUserProvider authenticationProvider;

    public Event getEventFromRequest(EventRequest request) {
        return switch (request.type) {
            case SINGLE -> getSingleEvent(request);
            case DAILY -> getDailyEvent(request);
            case WEEKLY -> getWeeklyEvent(request);
            case MONTHLY -> getMonthlyEvent(request);
            case YEARLY -> getYearlyEvent(request);
        };
    }

    private Event getSingleEvent(EventRequest request) {
        var end = request.start.plusSeconds(request.duration);
        var recurrence = new SingleRecurrence(request.start, end, request.zoneId);
        return buildEvent(request, recurrence);
    }

    private Event getDailyEvent(EventRequest request) {
        var end = request.end;

        if (request.occurrences != null) {
            end = request.start.atZone(request.zoneId)
                    .plusDays(request.offset * (request.occurrences - 1))
                    .plusSeconds(request.duration)
                    .toInstant();
        }

        var recurrence = new DailyRecurrence(request.start, end, request.zoneId, request.offset);
        return buildEvent(request, recurrence);
    }

    private Event getWeeklyEvent(EventRequest request) {
        var end = request.end;

        // weekly recurrences are tricky when it comes to the occurrence count
        // it depends on which day of the week we start the recurrence and how many occurrences are requested
        // this makes the calculation of the end date a lot more complicated than other recurrence types
        if (request.occurrences != null) {
            var start = request.start.atZone(request.zoneId);
            // the event start moved to the monday of the week
            var weekStart = start.with(previousOrSame(DayOfWeek.MONDAY));

            // the amount of event occurrences we "skipped" e.g. if we have occurrences on Mon, Tue and Fri
            // and our start date is a Fri, we skipped 2 days (Mon, Tue)
            int skipped = (int) request.weekDays.stream().filter(weekDay -> weekDay.compareTo(start.getDayOfWeek()) < 0).count();

            // added to the weekStart date we get the monday in the week the end date will be in
            // if we have an offset we need to add additional "empty" weeks in between the weeks which contain the events
            int weeksToAdd = (int) Math.ceil((float) (request.occurrences + skipped) / (float) request.weekDays.size());
            weeksToAdd = (int) (request.offset * (weeksToAdd - 1));

            // the weekDay the last occurrence is in
            // putting together the weekDate and the weekDay gives us the last occurrence
            int lastIndex = (request.occurrences + skipped - 1) % request.weekDays.size();
            DayOfWeek lastDay = (DayOfWeek) request.weekDays.toArray()[lastIndex];
            end = weekStart.plusWeeks(weeksToAdd).with(nextOrSame(lastDay)).plusSeconds(request.duration).toInstant();
        }

        var recurrence = new WeeklyRecurrence(request.start, end, request.zoneId, request.offset, request.weekDays);
        return buildEvent(request, recurrence);
    }

    private Event getMonthlyEvent(EventRequest request) {
        var end = request.end;

        if (request.occurrences != null) {
            end = request.start.atZone(request.zoneId)
                    .plusMonths(request.offset * (request.occurrences - 1))
                    .plusSeconds(request.duration)
                    .toInstant();
        }

        var recurrence = new MonthlyRecurrence(request.start, end, request.zoneId, request.offset);
        return buildEvent(request, recurrence);
    }

    private Event getYearlyEvent(EventRequest request) {
        var end = request.end;

        if (request.occurrences != null) {
            end = request.start.atZone(request.zoneId)
                    .plusYears(request.offset * (request.occurrences - 1))
                    .plusSeconds(request.duration)
                    .toInstant();
        }

        var recurrence = new YearlyRecurrence(request.start, end, request.zoneId, request.offset);
        return buildEvent(request, recurrence);
    }

    private Event buildEvent(EventRequest request, Recurrence recurrence) {
        var event = new Event();
        event.content = request.content.toEntity();
        event.duration = request.duration;
        event.fullDay = request.fullDay;
        event.recurrence = recurrence;
        event.user = authenticationProvider.getAuthenticatedUser();

        var categoryOptional = categoryService.getCategory(request.categoryId);
        categoryOptional.ifPresent(category -> event.category = category);

        return event;
    }

    public void clearEvent(Event event) {
        deviationRepo.deleteAll(event.deviations);
        contentRepo.delete(event.content);
        recurrenceRepo.delete(event.recurrence);
    }
}
