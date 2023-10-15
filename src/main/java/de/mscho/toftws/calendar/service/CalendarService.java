package de.mscho.toftws.calendar.service;

import de.mscho.toftws.configuration.security.AuthenticatedUserProvider;
import de.mscho.toftws.calendar.entity.payload.EventRequest;
import de.mscho.toftws.calendar.entity.payload.EventDto;
import de.mscho.toftws.calendar.repository.EventRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CalendarService {
    private final Logger logger;
    private final EventService eventService;
    private final DeviationService deviationService;
    private final EventRepo eventRepo;
    private final AuthenticatedUserProvider authenticationProvider;

    public List<EventDto> getEvents(Instant from, Instant to) {
        var user = authenticationProvider.getAuthenticatedUser();
        var events = eventRepo.findEventsByRecurrenceEndAfterAndRecurrenceStartBeforeAndUser(from, to, user);
        var eventDtos = events.stream().map(e -> e.getEvents(from, to)).flatMap(Collection::stream).collect(Collectors.toList());

        var deviationEventDtos = deviationService.getDeviations(from, to).stream()
                .filter(deviation -> !deviation.cancelled)
                .map(EventDto::buildForDeviation).toList();

        eventDtos.addAll(deviationEventDtos);

        return eventDtos;
    }

    public void createEvent(EventRequest request) {
        var event = eventService.getEventFromRequest(request);
        event = eventRepo.save(event);
        logger.info("created event(id:{})", event.id);
    }

    public void deleteEvent(long eventId) {
        var user = authenticationProvider.getAuthenticatedUser();
        long deleted = eventRepo.deleteByIdAndUser(eventId, user);

        if (deleted > 0) logger.info("Deleted Event. id:{}", eventId);
    }

    public void editEvent(long eventId, EventRequest request) {
        var user = authenticationProvider.getAuthenticatedUser();
        var eventOptional = eventRepo.findByIdAndUser(eventId, user);

        if (eventOptional.isEmpty()) {
            logger.info("No event(id: {}) found for update", eventId);
            return;
        }

        var existingEvent = eventOptional.get();
        eventService.clearEvent(existingEvent);

        var otherEvent = eventService.getEventFromRequest(request);
        existingEvent.fill(otherEvent);

        existingEvent = eventRepo.save(existingEvent);
        logger.info("edited event(id:{})", existingEvent.id);
    }
}
