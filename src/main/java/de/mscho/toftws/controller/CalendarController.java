package de.mscho.toftws.controller;

import de.mscho.toftws.entity.calendar.payload.EventDto;
import de.mscho.toftws.entity.calendar.payload.EventRequest;
import de.mscho.toftws.entity.calendar.payload.OffsetEventRequest;
import de.mscho.toftws.entity.calendar.payload.WeeklyEventRequest;
import de.mscho.toftws.service.CalendarService;
import de.mscho.toftws.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("calendar")
@RequiredArgsConstructor
@Validated
public class CalendarController {
    private final CalendarService calendarService;
    private final CategoryService categoryService;

    @GetMapping("event")
    public List<EventDto> getEvents(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime from, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime to) {
        return calendarService.getEvents(from, to);
    }

    @DeleteMapping("event")
    public void deleteEvent(@RequestParam long id) {
        calendarService.deleteEvent(id);
    }

    @PostMapping("daily")
    public void createDailyEvent(@RequestBody @Valid OffsetEventRequest request) {
        calendarService.createDailyEvent(request);
    }

    @PostMapping("weekly")
    public void createWeeklyEvent(@RequestBody @Valid WeeklyEventRequest request) {
        calendarService.createWeeklyEvent(request);
    }

    @PostMapping("monthly")
    public void createMonthlyEvent(@RequestBody @Valid OffsetEventRequest request) {
        calendarService.createMonthlyEvent(request);
    }

    @PostMapping("yearly")
    public void createYearlyEvent(@RequestBody @Valid EventRequest request) {
        calendarService.createYearlyEvent(request);
    }

    @PostMapping("category")
    public void createCategory(@RequestParam String name, @RequestParam @Pattern(regexp = "#\\d{6}") String color) {
        categoryService.createCategory(name, color);
    }

    @DeleteMapping("category")
    public void deleteCategory(@RequestParam Long id) {
        categoryService.deleteCategory(id);
    }
}
