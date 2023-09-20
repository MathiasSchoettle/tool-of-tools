package unit.calendar.service;

import de.mscho.toftws.calendar.entity.payload.CalendarEventRequest;
import de.mscho.toftws.calendar.entity.payload.EventContentDto;
import de.mscho.toftws.calendar.entity.payload.RecurrenceType;
import de.mscho.toftws.calendar.entity.recurrence.*;
import de.mscho.toftws.calendar.service.CalendarService;
import de.mscho.toftws.calendar.service.CategoryService;
import de.mscho.toftws.configuration.security.AuthenticationProvider;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.mscho.toftws.calendar.entity.payload.RecurrenceType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("unused")
@Tag("unit")
@ExtendWith(MockitoExtension.class)
public class CalendarServiceTest {
    @Mock
    CategoryService categoryService;
    @Mock
    AuthenticationProvider authenticationProvider;
    @InjectMocks
    CalendarService service;

    static ZoneId GERMAN_ZONE = ZoneId.of("Europe/Berlin");

    @Test
    void test_CreateSingleEvent() {
        var start = LocalDateTime.of(2020, 2, 1, 12, 0);
        var request = new CalendarEventRequest(
                SINGLE,
                ZonedDateTime.of(start, GERMAN_ZONE),
                null,
                GERMAN_ZONE,
                200L,
                null,
                null,
                new EventContentDto(),
                null
        );

        var event = service.getEventFromRequest(request);
        assertEquals(200L, event.duration);

        var recurrence = event.recurrence;
        assertEquals(SingleRecurrence.class, recurrence.getClass());
        assertEquals(recurrence.start, ZonedDateTime.of(start, GERMAN_ZONE));

        var end = LocalDateTime.of(2020, 2, 1, 12, 3, 20);
        assertEquals(ZonedDateTime.of(end, GERMAN_ZONE), recurrence.end);
    }

    @Test
    void test_CreateEvent_With_EndDate() {
        var start = LocalDateTime.of(2020, 2, 1, 12, 0);
        var end = LocalDateTime.of(2020, 4, 1, 12, 0);
        var request = new CalendarEventRequest(
                DAILY,
                ZonedDateTime.of(start, GERMAN_ZONE),
                ZonedDateTime.of(end, GERMAN_ZONE),
                GERMAN_ZONE,
                200L,
                1L,
                null,
                new EventContentDto(),
                null
        );

        var event = service.getEventFromRequest(request);
        assertEquals(200L, event.duration);

        var recurrence = event.recurrence;
        assertEquals(recurrence.start, ZonedDateTime.of(start, GERMAN_ZONE));
        assertEquals(ZonedDateTime.of(end, GERMAN_ZONE), recurrence.end);
    }

    @Test
    void test_CreateEvent_With_NoEndDate() {
        var start = LocalDateTime.of(2020, 2, 1, 12, 0);
        var request = new CalendarEventRequest(
                DAILY,
                ZonedDateTime.of(start, GERMAN_ZONE),
                null,
                GERMAN_ZONE,
                200L,
                1L,
                null,
                new EventContentDto(),
                null
        );

        // is it really far in the future?
        var futureDate = LocalDateTime.of(5000, 1, 1, 0, 0);
        var event = service.getEventFromRequest(request);
        assertTrue(event.recurrence.end.isAfter(ZonedDateTime.of(futureDate, GERMAN_ZONE)));
    }

    @Test
    void test_CreateDailyEvent_With_Occurrences() {
        var start = LocalDateTime.of(2020, 2, 1, 12, 0);
        var request = new CalendarEventRequest(
                DAILY,
                ZonedDateTime.of(start, GERMAN_ZONE),
                null,
                GERMAN_ZONE,
                200L,
                1L,
                4,
                new EventContentDto(),
                null
        );

        var event = service.getEventFromRequest(request);
        assertEquals(200L, event.duration);

        var recurrence = event.recurrence;
        assertEquals(DailyRecurrence.class, recurrence.getClass());
        assertEquals(recurrence.start, ZonedDateTime.of(start, GERMAN_ZONE));

        var end = LocalDateTime.of(2020, 2, 4, 12, 3, 20);
        assertEquals(ZonedDateTime.of(end, GERMAN_ZONE), recurrence.end);
    }

    @Test
    void test_CreateDailyEvent_With_Occurrences_And_Offset() {
        var start = LocalDateTime.of(2020, 2, 1, 12, 0);
        var request = new CalendarEventRequest(
                DAILY,
                ZonedDateTime.of(start, GERMAN_ZONE),
                null,
                GERMAN_ZONE,
                200L,
                4L,
                4,
                new EventContentDto(),
                null
        );

        var event = service.getEventFromRequest(request);
        assertEquals(200L, event.duration);

        var recurrence = event.recurrence;
        assertEquals(DailyRecurrence.class, recurrence.getClass());
        assertEquals(recurrence.start, ZonedDateTime.of(start, GERMAN_ZONE));

        var end = LocalDateTime.of(2020, 2, 13, 12, 3, 20);
        assertEquals(ZonedDateTime.of(end, GERMAN_ZONE), recurrence.end);
    }

    @Test
    void test_CreateWeeklyEvent_With_Occurrences_And_StartDateIsFirstOccurrence() {
        var start = LocalDateTime.of(2023, 9, 4, 12, 0);
        var request = new CalendarEventRequest(
                WEEKLY,
                ZonedDateTime.of(start, GERMAN_ZONE),
                null,
                GERMAN_ZONE,
                200L,
                1L,
                5,
                new EventContentDto(),
                null
        );
        request.weekDays = Stream.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY).collect(Collectors.toCollection(TreeSet::new));

        var event = service.getEventFromRequest(request);
        assertEquals(200L, event.duration);

        var recurrence = event.recurrence;
        assertEquals(WeeklyRecurrence.class, recurrence.getClass());
        assertEquals(recurrence.start, ZonedDateTime.of(start, GERMAN_ZONE));

        var end = LocalDateTime.of(2023, 9, 18, 12, 3, 20);
        assertEquals(ZonedDateTime.of(end, GERMAN_ZONE), recurrence.end);
    }

    @Test
    void test_CreateWeeklyEvent_With_Occurrences_And_StartDateNotFirstOccurrence() {
        var start = LocalDateTime.of(2023, 9, 7, 12, 0);
        var request = new CalendarEventRequest(
                WEEKLY,
                ZonedDateTime.of(start, GERMAN_ZONE),
                null,
                GERMAN_ZONE,
                200L,
                1L,
                7,
                new EventContentDto(),
                null
        );
        request.weekDays = Stream.of(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY).collect(Collectors.toCollection(TreeSet::new));

        var event = service.getEventFromRequest(request);
        assertEquals(200L, event.duration);

        var recurrence = event.recurrence;
        assertEquals(WeeklyRecurrence.class, recurrence.getClass());
        assertEquals(recurrence.start, ZonedDateTime.of(start, GERMAN_ZONE));

        var end = LocalDateTime.of(2023, 9, 21, 12, 3, 20);
        assertEquals(ZonedDateTime.of(end, GERMAN_ZONE), recurrence.end);
    }

    @Test
    void test_CreateWeeklyEvent_With_Occurrences_And_Offset_And_StartDateIsFirstOccurrence() {
        var start = LocalDateTime.of(2023, 9, 4, 12, 0);
        var request = new CalendarEventRequest(
                WEEKLY,
                ZonedDateTime.of(start, GERMAN_ZONE),
                null,
                GERMAN_ZONE,
                200L,
                3L,
                5,
                new EventContentDto(),
                null
        );
        request.weekDays = Stream.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY).collect(Collectors.toCollection(TreeSet::new));

        var event = service.getEventFromRequest(request);
        assertEquals(200L, event.duration);

        var recurrence = event.recurrence;
        assertEquals(WeeklyRecurrence.class, recurrence.getClass());
        assertEquals(recurrence.start, ZonedDateTime.of(start, GERMAN_ZONE));

        var end = LocalDateTime.of(2023, 10, 16, 12, 3, 20);
        assertEquals(ZonedDateTime.of(end, GERMAN_ZONE), recurrence.end);
    }

    @Test
    void test_CreateWeeklyEvent_With_Occurrences_And_Offset_And_StartDateNotFirstOccurrence() {
        var start = LocalDateTime.of(2023, 9, 7, 12, 0);
        var request = new CalendarEventRequest(
                WEEKLY,
                ZonedDateTime.of(start, GERMAN_ZONE),
                null,
                GERMAN_ZONE,
                200L,
                2L,
                8,
                new EventContentDto(),
                null
        );
        request.weekDays = Stream.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY).collect(Collectors.toCollection(TreeSet::new));

        var event = service.getEventFromRequest(request);
        assertEquals(200L, event.duration);

        var recurrence = event.recurrence;
        assertEquals(WeeklyRecurrence.class, recurrence.getClass());
        assertEquals(recurrence.start, ZonedDateTime.of(start, GERMAN_ZONE));

        var end = LocalDateTime.of(2023, 10, 16, 12, 3, 20);
        assertEquals(ZonedDateTime.of(end, GERMAN_ZONE), recurrence.end);
    }

    @Test
    void test_CreateMonthlyEvent_With_Occurrences() {
        var start = LocalDateTime.of(2020, 2, 1, 12, 0);
        var request = new CalendarEventRequest(
                MONTHLY,
                ZonedDateTime.of(start, GERMAN_ZONE),
                null,
                GERMAN_ZONE,
                200L,
                1L,
                4,
                new EventContentDto(),
                null
        );

        var event = service.getEventFromRequest(request);
        assertEquals(200L, event.duration);

        var recurrence = event.recurrence;
        assertEquals(MonthlyRecurrence.class, recurrence.getClass());
        assertEquals(recurrence.start, ZonedDateTime.of(start, GERMAN_ZONE));

        var end = LocalDateTime.of(2020, 5, 1, 12, 3, 20);
        assertEquals(ZonedDateTime.of(end, GERMAN_ZONE), recurrence.end);
    }

    @Test
    void test_CreateMonthlyEvent_With_Occurrences_And_Offset() {
        var start = LocalDateTime.of(2020, 3, 5, 12, 0);
        var request = new CalendarEventRequest(
                MONTHLY,
                ZonedDateTime.of(start, GERMAN_ZONE),
                null,
                GERMAN_ZONE,
                200L,
                3L,
                3,
                new EventContentDto(),
                null
        );

        var event = service.getEventFromRequest(request);
        assertEquals(200L, event.duration);

        var recurrence = event.recurrence;
        assertEquals(MonthlyRecurrence.class, recurrence.getClass());
        assertEquals(recurrence.start, ZonedDateTime.of(start, GERMAN_ZONE));

        var end = LocalDateTime.of(2020, 9, 5, 12, 3, 20);
        assertEquals(ZonedDateTime.of(end, GERMAN_ZONE), recurrence.end);
    }

    @Test
    void test_CreateYearlyEvent_With_Occurrences() {
        var start = LocalDateTime.of(1996, 8, 10, 12, 0);
        var request = new CalendarEventRequest(
                YEARLY,
                ZonedDateTime.of(start, GERMAN_ZONE),
                null,
                GERMAN_ZONE,
                0L,
                1L,
                28,
                new EventContentDto(),
                null
        );

        var event = service.getEventFromRequest(request);
        assertEquals(0L, event.duration);

        var recurrence = event.recurrence;
        assertEquals(YearlyRecurrence.class, recurrence.getClass());
        assertEquals(recurrence.start, ZonedDateTime.of(start, GERMAN_ZONE));

        var end = LocalDateTime.of(2023, 8, 10, 12, 0, 0);
        assertEquals(ZonedDateTime.of(end, GERMAN_ZONE), recurrence.end);
    }

    @Test
    void test_CreateYearlyEvent_With_Occurrences_And_Offset() {
        var start = LocalDateTime.of(1996, 8, 10, 12, 0);
        var request = new CalendarEventRequest(
                YEARLY,
                ZonedDateTime.of(start, GERMAN_ZONE),
                null,
                GERMAN_ZONE,
                0L,
                5L,
                4,
                new EventContentDto(),
                null
        );

        var event = service.getEventFromRequest(request);
        assertEquals(0L, event.duration);

        var recurrence = event.recurrence;
        assertEquals(YearlyRecurrence.class, recurrence.getClass());
        assertEquals(recurrence.start, ZonedDateTime.of(start, GERMAN_ZONE));

        var end = LocalDateTime.of(2011, 8, 10, 12, 0, 0);
        assertEquals(ZonedDateTime.of(end, GERMAN_ZONE), recurrence.end);
    }
}
