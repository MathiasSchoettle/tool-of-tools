package de.mscho.toftws.calendar.controller;

import de.mscho.toftws.calendar.entity.*;
import de.mscho.toftws.calendar.service.CalendarEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
public class CalendarEventControllerTest {

    CalendarEventController calendarEventController;

    @Mock
    CalendarEventService calendarEventService;

    OffsetDateTime until = OffsetDateTime.of(LocalDateTime.of(2020, 12, 3, 4, 52, 28), ZoneOffset.UTC);

    @BeforeEach
    public void beforeEach() {
        calendarEventController = new CalendarEventController(calendarEventService);
    }

    @Test
    public void Add_Timepoint_Calls_Service_Add_Timepoint() {

        calendarEventController.addTimepointEvent(new TimepointEvent(), Recurrence.NEVER, until);

        verify(calendarEventService, times(1)).addTimepointEvent(any(TimepointEvent.class), any(Recurrence.class), any(OffsetDateTime.class));
    }

    @Test
    public void Add_Timespan_Calls_Service_Add_Timespan() {

        calendarEventController.addTimespanEvent(new TimespanEvent(), Recurrence.NEVER, until);

        verify(calendarEventService, times(1)).addTimespanEvent(any(TimespanEvent.class), any(Recurrence.class), any(OffsetDateTime.class));
    }

    @Test
    public void Add_Day_Calls_Service_Add_Day() {

        calendarEventController.addDayEvent(new DayEvent(), Recurrence.NEVER, until);

        verify(calendarEventService, times(1)).addDayEvent(any(DayEvent.class), any(Recurrence.class), any(OffsetDateTime.class));
    }

    @Test
    public void Add_Dayspan_Calls_Service_Add_Dayspan() {

        calendarEventController.addDayspanEvent(new DayspanEvent(), Recurrence.NEVER, until);

        verify(calendarEventService, times(1)).addDayspanEvent(any(DayspanEvent.class), any(Recurrence.class), any(OffsetDateTime.class));
    }
}
