package de.mscho.toftws.calendar.controller;

import de.mscho.toftws.calendar.entity.payload.*;
import de.mscho.toftws.calendar.service.CalendarService;
import de.mscho.toftws.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("calendar/event")
@RequiredArgsConstructor
public class CalendarController {
    private final CalendarService calendarService;

    @GetMapping
    public ResponseEntity<ApiResponse> getEvents(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to) {
        var events = calendarService.getEvents(from, to);
        return new ResponseEntity<>(ApiResponse.success(events), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse> deleteEvent(@RequestParam long eventId) {
        calendarService.deleteEvent(eventId);
        return new ResponseEntity<>(ApiResponse.success(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createEvent(@RequestBody @Valid EventRequest request) {
        calendarService.createEvent(request);
        return new ResponseEntity<>(ApiResponse.success(), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ApiResponse> editEvent(@RequestParam long eventId, @RequestBody @Valid EventRequest request) {
        calendarService.editEvent(eventId, request);
        return new ResponseEntity<>(ApiResponse.success(), HttpStatus.OK);
    }
}
