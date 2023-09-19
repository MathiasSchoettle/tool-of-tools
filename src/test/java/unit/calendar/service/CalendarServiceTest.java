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
        var request = new CalendarEventRequest();

        request.type = RecurrenceType.SINGLE;
        request.start = ZonedDateTime.of(start, GERMAN_ZONE);
        request.duration = 200L;
        request.content = new EventContentDto();

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
        var request = new CalendarEventRequest();

        request.type = RecurrenceType.DAILY;
        request.start = ZonedDateTime.of(start, GERMAN_ZONE);
        request.end = ZonedDateTime.of(end, GERMAN_ZONE);
        request.duration = 200L;
        request.content = new EventContentDto();
        request.offset = 1L;

        var event = service.getEventFromRequest(request);
        assertEquals(200L, event.duration);

        var recurrence = event.recurrence;
        assertEquals(recurrence.start, ZonedDateTime.of(start, GERMAN_ZONE));
        assertEquals(ZonedDateTime.of(end, GERMAN_ZONE), recurrence.end);
    }

    @Test
    void test_CreateEvent_With_NoEndDate() {
        var start = LocalDateTime.of(2020, 2, 1, 12, 0);
        var request = new CalendarEventRequest();

        request.type = RecurrenceType.DAILY;
        request.start = ZonedDateTime.of(start, GERMAN_ZONE);
        request.duration = 200L;
        request.content = new EventContentDto();
        request.offset = 1L;

        // is it really far in the future?
        var futureDate = LocalDateTime.of(5000, 1, 1, 0, 0);
        var event = service.getEventFromRequest(request);
        assertTrue(event.recurrence.end.isAfter(ZonedDateTime.of(futureDate, GERMAN_ZONE)));
    }

    @Test
    void test_CreateDailyEvent_With_Occurrences() {
        var start = LocalDateTime.of(2020, 2, 1, 12, 0);
        var request = new CalendarEventRequest();

        request.type = RecurrenceType.DAILY;
        request.start = ZonedDateTime.of(start, GERMAN_ZONE);
        request.occurrences = 4;
        request.duration = 200L;
        request.content = new EventContentDto();
        request.offset = 1L;

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
        var request = new CalendarEventRequest();

        request.type = RecurrenceType.DAILY;
        request.start = ZonedDateTime.of(start, GERMAN_ZONE);
        request.occurrences = 4;
        request.duration = 200L;
        request.content = new EventContentDto();
        request.offset = 4L;

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
        var request = new CalendarEventRequest();

        request.type = RecurrenceType.WEEKLY;
        request.start = ZonedDateTime.of(start, GERMAN_ZONE);
        request.occurrences = 5;
        request.duration = 200L;
        request.weekDays = Stream.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY).collect(Collectors.toCollection(TreeSet::new));
        request.content = new EventContentDto();
        request.offset = 1L;

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
        var request = new CalendarEventRequest();

        request.type = RecurrenceType.WEEKLY;
        request.start = ZonedDateTime.of(start, GERMAN_ZONE);
        request.occurrences = 7;
        request.duration = 200L;
        request.weekDays = Stream.of(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY).collect(Collectors.toCollection(TreeSet::new));
        request.content = new EventContentDto();
        request.offset = 1L;

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
        var request = new CalendarEventRequest();

        request.type = RecurrenceType.WEEKLY;
        request.start = ZonedDateTime.of(start, GERMAN_ZONE);
        request.occurrences = 5;
        request.duration = 200L;
        request.weekDays = Stream.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY).collect(Collectors.toCollection(TreeSet::new));
        request.content = new EventContentDto();
        request.offset = 3L;

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
        var request = new CalendarEventRequest();

        request.type = RecurrenceType.WEEKLY;
        request.start = ZonedDateTime.of(start, GERMAN_ZONE);
        request.occurrences = 8;
        request.duration = 200L;
        request.weekDays = Stream.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY).collect(Collectors.toCollection(TreeSet::new));
        request.content = new EventContentDto();
        request.offset = 2L;

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
        var request = new CalendarEventRequest();

        request.type = RecurrenceType.MONTHLY;
        request.start = ZonedDateTime.of(start, GERMAN_ZONE);
        request.occurrences = 4;
        request.duration = 200L;
        request.content = new EventContentDto();
        request.offset = 1L;

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
        var request = new CalendarEventRequest();

        request.type = RecurrenceType.MONTHLY;
        request.start = ZonedDateTime.of(start, GERMAN_ZONE);
        request.occurrences = 3;
        request.duration = 200L;
        request.content = new EventContentDto();
        request.offset = 3L;

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
        var request = new CalendarEventRequest();

        request.type = RecurrenceType.YEARLY;
        request.start = ZonedDateTime.of(start, GERMAN_ZONE);
        request.occurrences = 28;
        request.duration = 0L;
        request.content = new EventContentDto();
        request.offset = 1L;

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
        var request = new CalendarEventRequest();

        request.type = RecurrenceType.YEARLY;
        request.start = ZonedDateTime.of(start, GERMAN_ZONE);
        request.occurrences = 4;
        request.duration = 0L;
        request.content = new EventContentDto();
        request.offset = 5L;

        var event = service.getEventFromRequest(request);
        assertEquals(0L, event.duration);

        var recurrence = event.recurrence;
        assertEquals(YearlyRecurrence.class, recurrence.getClass());
        assertEquals(recurrence.start, ZonedDateTime.of(start, GERMAN_ZONE));

        var end = LocalDateTime.of(2011, 8, 10, 12, 0, 0);
        assertEquals(ZonedDateTime.of(end, GERMAN_ZONE), recurrence.end);
    }
}
