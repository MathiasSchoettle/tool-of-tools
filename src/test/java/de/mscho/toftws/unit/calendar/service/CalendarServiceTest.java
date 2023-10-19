package de.mscho.toftws.unit.calendar.service;

import de.mscho.toftws.calendar.entity.payload.EventRequest;
import de.mscho.toftws.calendar.entity.payload.EventContentDto;
import de.mscho.toftws.calendar.entity.payload.RecurrenceType;
import de.mscho.toftws.calendar.entity.recurrence.*;
import de.mscho.toftws.calendar.service.CategoryService;
import de.mscho.toftws.calendar.service.EventService;
import de.mscho.toftws.configuration.security.AuthenticatedUserProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.mscho.toftws.calendar.entity.payload.RecurrenceType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("unused")
@ExtendWith(MockitoExtension.class)
public class CalendarServiceTest {
    @Mock
    CategoryService categoryService;
    @Mock
    AuthenticatedUserProvider authenticationProvider;
    @InjectMocks
    EventService service;

    static ZoneId GERMAN_ZONE = ZoneId.of("Europe/Berlin");

    EventRequest create(RecurrenceType type, Instant start, Instant end, ZoneId zoneId, Integer duration, Integer offset, Integer occurrences) {
        return new EventRequest(
                type,
                start,
                end,
                zoneId,
                duration == null ? null : duration.longValue(),
                offset == null ? null : offset.longValue(),
                occurrences,
                new EventContentDto(),
                null
        );
    }

    @Test
    void test_CreateSingleEvent() {
        var start = instant(LocalDateTime.of(2020, 2, 1, 12, 0));
        var request = create(SINGLE, start, null, GERMAN_ZONE, 200, null, null);

        var event = service.getEventFromRequest(request);
        assertEquals(200L, event.duration);

        var recurrence = event.recurrence;
        assertEquals(SingleRecurrence.class, recurrence.getClass());
        assertEquals(start, recurrence.start);

        var end = instant(LocalDateTime.of(2020, 2, 1, 12, 3, 20));
        assertEquals(end, recurrence.end);
    }

    @Test
    void test_CreateEvent_With_EndDate() {
        var start = instant(LocalDateTime.of(2020, 2, 1, 12, 0));
        var end = instant(LocalDateTime.of(2020, 4, 1, 12, 0));
        var request = create(DAILY, start, end, GERMAN_ZONE, 200, 1, null);

        var event = service.getEventFromRequest(request);
        assertEquals(200L, event.duration);

        var recurrence = event.recurrence;
        assertEquals(start, recurrence.start);
        assertEquals(end, recurrence.end);
    }

    @Test
    void test_CreateEvent_With_NoEndDate() {
        var start = instant(LocalDateTime.of(2020, 2, 1, 12, 0));
        var request = create(DAILY, start, null, GERMAN_ZONE, 200, 1, null);

        // is it really far in the future?
        var futureDate = instant(LocalDateTime.of(5000, 1, 1, 0, 0));
        var event = service.getEventFromRequest(request);
        assertTrue(event.recurrence.end.isAfter(futureDate));
    }

    @Test
    void test_CreateDailyEvent_With_Occurrences() {
        var start = instant(LocalDateTime.of(2020, 2, 1, 12, 0));
        var request = create(DAILY, start, null, GERMAN_ZONE, 200, 1, 4);

        var event = service.getEventFromRequest(request);
        assertEquals(200L, event.duration);

        var recurrence = event.recurrence;
        assertEquals(DailyRecurrence.class, recurrence.getClass());
        assertEquals(start, recurrence.start);

        var end = instant(LocalDateTime.of(2020, 2, 4, 12, 3, 20));
        assertEquals(end, recurrence.end);
    }

    @Test
    void test_CreateDailyEvent_With_Occurrences_And_Offset() {
        var start = instant(LocalDateTime.of(2020, 2, 1, 12, 0));
        var request = create(DAILY, start, null, GERMAN_ZONE, 200, 4, 4);

        var event = service.getEventFromRequest(request);
        assertEquals(200L, event.duration);

        var recurrence = event.recurrence;
        assertEquals(DailyRecurrence.class, recurrence.getClass());
        assertEquals(start, recurrence.start);

        var end = instant(LocalDateTime.of(2020, 2, 13, 12, 3, 20));
        assertEquals(end, recurrence.end);
    }

    @Test
    void test_CreateWeeklyEvent_With_Occurrences_And_StartDateIsFirstOccurrence() {
        var start = instant(LocalDateTime.of(2023, 9, 4, 12, 0));
        var request = create(WEEKLY, start, null, GERMAN_ZONE, 200, 1, 5);
        request.weekDays = Stream.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY).collect(Collectors.toCollection(TreeSet::new));

        var event = service.getEventFromRequest(request);
        assertEquals(200L, event.duration);

        var recurrence = event.recurrence;
        assertEquals(WeeklyRecurrence.class, recurrence.getClass());
        assertEquals(start, recurrence.start);

        var end = instant(LocalDateTime.of(2023, 9, 18, 12, 3, 20));
        assertEquals(end, recurrence.end);
    }

    @Test
    void test_CreateWeeklyEvent_With_Occurrences_And_StartDateNotFirstOccurrence() {
        var start = instant(LocalDateTime.of(2023, 9, 7, 12, 0));
        var request = create(WEEKLY, start, null, GERMAN_ZONE, 200, 1, 7);
        request.weekDays = Stream.of(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY).collect(Collectors.toCollection(TreeSet::new));

        var event = service.getEventFromRequest(request);
        assertEquals(200L, event.duration);

        var recurrence = event.recurrence;
        assertEquals(WeeklyRecurrence.class, recurrence.getClass());
        assertEquals(start, recurrence.start);

        var end = instant(LocalDateTime.of(2023, 9, 21, 12, 3, 20));
        assertEquals(end, recurrence.end);
    }

    @Test
    void test_CreateWeeklyEvent_With_Occurrences_And_Offset_And_StartDateIsFirstOccurrence() {
        var start = instant(LocalDateTime.of(2023, 9, 4, 12, 0));
        var request = create(WEEKLY, start, null, GERMAN_ZONE, 200, 3, 5);
        request.weekDays = Stream.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY).collect(Collectors.toCollection(TreeSet::new));

        var event = service.getEventFromRequest(request);
        assertEquals(200L, event.duration);

        var recurrence = event.recurrence;
        assertEquals(WeeklyRecurrence.class, recurrence.getClass());
        assertEquals(start, recurrence.start);

        var end = instant(LocalDateTime.of(2023, 10, 16, 12, 3, 20));
        assertEquals(end, recurrence.end);
    }

    @Test
    void test_CreateWeeklyEvent_With_Occurrences_And_Offset_And_StartDateNotFirstOccurrence() {
        var start = instant(LocalDateTime.of(2023, 9, 7, 12, 0));
        var request = create(WEEKLY, start, null, GERMAN_ZONE, 200, 2, 8);
        request.weekDays = Stream.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY).collect(Collectors.toCollection(TreeSet::new));

        var event = service.getEventFromRequest(request);
        assertEquals(200L, event.duration);

        var recurrence = event.recurrence;
        assertEquals(WeeklyRecurrence.class, recurrence.getClass());
        assertEquals(start, recurrence.start);

        var end = instant(LocalDateTime.of(2023, 10, 16, 12, 3, 20));
        assertEquals(end, recurrence.end);
    }

    @Test
    void test_CreateMonthlyEvent_With_Occurrences() {
        var start = instant(LocalDateTime.of(2020, 2, 1, 12, 0));
        var request = create(MONTHLY, start, null, GERMAN_ZONE, 200, 1, 4);

        var event = service.getEventFromRequest(request);
        assertEquals(200L, event.duration);

        var recurrence = event.recurrence;
        assertEquals(MonthlyRecurrence.class, recurrence.getClass());
        assertEquals(start, recurrence.start);

        var end = instant(LocalDateTime.of(2020, 5, 1, 12, 3, 20));
        assertEquals(end, recurrence.end);
    }

    @Test
    void test_CreateMonthlyEvent_With_Occurrences_And_Offset() {
        var start = instant(LocalDateTime.of(2020, 3, 5, 12, 0));
        var request = create(MONTHLY, start, null, GERMAN_ZONE, 200, 3, 3);

        var event = service.getEventFromRequest(request);
        assertEquals(200L, event.duration);

        var recurrence = event.recurrence;
        assertEquals(MonthlyRecurrence.class, recurrence.getClass());
        assertEquals(start, recurrence.start);

        var end = instant(LocalDateTime.of(2020, 9, 5, 12, 3, 20));
        assertEquals(end, recurrence.end);
    }

    @Test
    void test_CreateYearlyEvent_With_Occurrences() {
        var start = instant(LocalDateTime.of(1996, 8, 10, 12, 0));
        var request = create(YEARLY, start, null, GERMAN_ZONE, 0, 1, 28);

        var event = service.getEventFromRequest(request);
        assertEquals(0L, event.duration);

        var recurrence = event.recurrence;
        assertEquals(YearlyRecurrence.class, recurrence.getClass());
        assertEquals(start, recurrence.start);

        var end = instant(LocalDateTime.of(2023, 8, 10, 12, 0, 0));
        assertEquals(end, recurrence.end);
    }

    @Test
    void test_CreateYearlyEvent_With_Occurrences_And_Offset() {
        var start = instant(LocalDateTime.of(1996, 8, 10, 12, 0));
        var request = create(YEARLY, start, null, GERMAN_ZONE, 0, 5, 4);

        var event = service.getEventFromRequest(request);
        assertEquals(0L, event.duration);

        var recurrence = event.recurrence;
        assertEquals(YearlyRecurrence.class, recurrence.getClass());
        assertEquals(start, recurrence.start);

        var end = instant(LocalDateTime.of(2011, 8, 10, 12, 0, 0));
        assertEquals(end, recurrence.end);
    }

    Instant instant(LocalDateTime dateTime) {
        return dateTime.atZone(GERMAN_ZONE).toInstant();
    }
}
