package de.mscho.toftws.controller.calendar;

import de.mscho.toftws.entity.calendar.payload.*;
import de.mscho.toftws.service.calendar.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("calendar/event")
@RequiredArgsConstructor
@Validated
public class CalendarController {
    private final CalendarService calendarService;

    @GetMapping
    public List<EventDto> getEvents(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime from, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime to) {
        return calendarService.getEvents(from, to);
    }

    @DeleteMapping
    public void deleteEvent(@RequestParam long id) {
        calendarService.deleteEvent(id);
    }

    @PostMapping("daily")
    public void createDailyEvent(@RequestBody OffsetEventRequest request) {
        calendarService.createDailyEvent(request);
    }

    @PostMapping("weekly")
    public void createWeeklyEvent(@RequestBody WeeklyEventRequest request) {
        calendarService.createWeeklyEvent(request);
    }

    @PostMapping("monthly")
    public void createMonthlyEvent(@RequestBody OffsetEventRequest request) {
        calendarService.createMonthlyEvent(request);
    }

    @PostMapping("yearly")
    public void createYearlyEvent(@RequestBody EventRequest request) {
        calendarService.createYearlyEvent(request);
    }
}
