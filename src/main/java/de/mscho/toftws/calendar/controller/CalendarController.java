package de.mscho.toftws.calendar.controller;

import de.mscho.toftws.calendar.entity.payload.*;
import de.mscho.toftws.calendar.service.CalendarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("calendar/event")
@RequiredArgsConstructor
public class CalendarController {
    private final CalendarService calendarService;

    @GetMapping
    public List<EventDto> getEvents(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to) {
        return calendarService.getEvents(from, to);
    }

    @DeleteMapping("/")
    public void deleteEvent(@RequestParam long eventId) {
        calendarService.deleteEvent(eventId);
    }

    @PostMapping
    public void createEvent(@RequestBody @Valid EventRequest request) {
        calendarService.createEvent(request);
    }

    @PutMapping
    public void editEvent(@RequestParam long eventId, @RequestBody @Valid EventRequest request) {
        calendarService.editEvent(eventId, request);
    }
}
