package de.mscho.toftws.calendar.controller;

import de.mscho.toftws.calendar.entity.*;
import de.mscho.toftws.calendar.service.CalendarEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.OffsetDateTime;

@RequiredArgsConstructor

@RestController
@RequestMapping("calendar/event")
public class CalendarEventController {

    private final CalendarEventService calendarEventService;

    @PostMapping(path = "timepoint/{recurrence}")
    public void addTimepointEvent(@RequestBody @Valid TimepointEvent timepointEvent,
                                  @PathVariable Recurrence recurrence,
                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime untilDate) {

        calendarEventService.addTimepointEvent(timepointEvent, recurrence, untilDate);
    }

    @PostMapping(path = "timespan/{recurrence}")
    public void addTimespanEvent(@RequestBody @Valid TimespanEvent timespanEvent,
                                 @PathVariable Recurrence recurrence,
                                 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime untilDate) {

        calendarEventService.addTimespanEvent(timespanEvent, recurrence, untilDate);
    }

    @PostMapping(path = "day/{recurrence}")
    public void addDayEvent(@RequestBody @Valid DayEvent dayEvent,
                            @PathVariable Recurrence recurrence,
                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime untilDate) {

        calendarEventService.addDayEvent(dayEvent, recurrence, untilDate);
    }

    @PostMapping(path = "dayspan/{recurrence}")
    public void addDayspanEvent(@RequestBody @Valid DayspanEvent dayspanEvent,
                                @PathVariable Recurrence recurrence,
                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime untilDate) {

        calendarEventService.addDayspanEvent(dayspanEvent, recurrence, untilDate);
    }
}
