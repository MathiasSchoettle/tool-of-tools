package de.mscho.toftws.calendar.service;

import de.mscho.toftws.calendar.entity.event.*;
import de.mscho.toftws.calendar.entity.recurrence.Recurrence;
import de.mscho.toftws.calendar.repository.CalendarEventRepository;
import de.mscho.toftws.calendar.service.impl.DefaultCalenderEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.mockito.Mockito.*;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
public class DefaultCalendarEventServiceTest {

    CalendarEventService service;

    @Mock
    CalendarEventRepository repository;

    @Mock
    Logger logger;

    LocalDate startDate;

    OffsetDateTime startDateTime;

    OffsetDateTime untilDateTime;

    OffsetDateTime from;

    OffsetDateTime to;

    @BeforeEach
    public void beforeEach() {
        this.service = new DefaultCalenderEventService(repository, logger);
        this.startDate = LocalDate.of(2020, 3, 12);
        this.startDateTime = OffsetDateTime.of(LocalDateTime.of(2019, 4, 23, 12, 43, 23), ZoneOffset.UTC);
        this.untilDateTime = OffsetDateTime.of(LocalDateTime.of(2020, 4, 12, 0, 0, 0), ZoneOffset.UTC);
        this.from =  OffsetDateTime.of(LocalDateTime.of(2020, 1, 3, 4, 52, 28), ZoneOffset.UTC);
        this.to = OffsetDateTime.of(LocalDateTime.of(2022, 5, 7, 23, 5, 12), ZoneOffset.UTC);
    }

    @Test
    public void Add_Timepoint_Event_Calls_Repository_Save_Once() {

        TimepointEvent event = new TimepointEvent();
        event.setDateTime(startDateTime);

        this.service.addTimepointEvent(event, Recurrence.NEVER, untilDateTime);

        verify(repository, times(1)).save(any(CalendarEvent.class));
    }

    @Test
    public void Add_Timespan_Event_Calls_Repository_Save_Once() {

        TimespanEvent event = new TimespanEvent();
        event.setStartDateTime(startDateTime);
        event.setDuration(2034);

        this.service.addTimespanEvent(event, Recurrence.NEVER, untilDateTime);

        verify(repository, times(1)).save(any(CalendarEvent.class));
    }

    @Test
    public void Add_Day_Event_Calls_Repository_Once() {

        DayEvent event = new DayEvent();
        event.setDate(startDate);

        this.service.addDayEvent(event, Recurrence.NEVER, untilDateTime);

        verify(repository, times(1)).save(any(CalendarEvent.class));
    }

    @Test
    public void Add_Dayspan_Event_Calls_Repository_Once() {

        DayspanEvent event = new DayspanEvent();
        event.setStartDate(startDate);
        event.setDays(23);

        this.service.addDayspanEvent(event, Recurrence.NEVER, untilDateTime);

        verify(repository, times(1)).save(any(CalendarEvent.class));
    }

    @Test
    public void Get_Events_of_Timespan_Calls_Repository_Once() {

        this.service.getEventsOfTimespan(from, to);

        verify(repository, times(1)).findAllByCalendarPosition_UntilDateGreaterThanEqualAndCalendarPosition_StartDateLessThan(any(OffsetDateTime.class), any(OffsetDateTime.class));
    }
}
