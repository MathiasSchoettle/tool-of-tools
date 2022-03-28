package de.mscho.toftws.calendar.controller;

import de.mscho.toftws.calendar.entity.event.*;
import de.mscho.toftws.calendar.entity.recurrence.Recurrence;
import de.mscho.toftws.calendar.service.CalendarEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.xml.ws.Response;
import java.time.OffsetDateTime;
import java.util.List;

@RequiredArgsConstructor

@RestController
@RequestMapping("calendar/event")
public class CalendarEventController {

    private final CalendarEventService calendarEventService;

    @PostMapping(path = "timepoint/{recurrence}")
    public void addTimepointEvent(@RequestBody @Valid TimepointEvent timepointEvent,
                                  @PathVariable Recurrence recurrence,
                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime until) {

        calendarEventService.addTimepointEvent(timepointEvent, recurrence, until);
    }

    @PostMapping(path = "timespan/{recurrence}")
    public void addTimespanEvent(@RequestBody @Valid TimespanEvent timespanEvent,
                                 @PathVariable Recurrence recurrence,
                                 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime until) {

        calendarEventService.addTimespanEvent(timespanEvent, recurrence, until);
    }

    @PostMapping(path = "day/{recurrence}")
    public void addDayEvent(@RequestBody @Valid DayEvent dayEvent,
                            @PathVariable Recurrence recurrence,
                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime until) {

        calendarEventService.addDayEvent(dayEvent, recurrence, until);
    }

    @PostMapping(path = "dayspan/{recurrence}")
    public void addDayspanEvent(@RequestBody @Valid DayspanEvent dayspanEvent,
                                @PathVariable Recurrence recurrence,
                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime until) {

        calendarEventService.addDayspanEvent(dayspanEvent, recurrence, until);
    }

    @GetMapping(path = "")
    public List<AbstractCalendarEvent> getCalendarEventsOfDateTimeSpan(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime from,
                                                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime to) {

        return calendarEventService.getEventsOfTimespan(from, to);
    }

    @DeleteMapping(path = "{id}")
    public void deleteCalendarEvent(@PathVariable long id) {

        calendarEventService.deleteCalendarEvent(id);
    }

    @PatchMapping(path = "{id}")
    public void endCalendarEvent(@PathVariable long id) {

        calendarEventService.endCalendarEvent(id);
    }

    @PutMapping(path = "{id}")
    public void updateCalendarEvent(@PathVariable long id, @RequestBody @Valid TimepointEvent timepointEvent) {
        // TODO
    }
}
