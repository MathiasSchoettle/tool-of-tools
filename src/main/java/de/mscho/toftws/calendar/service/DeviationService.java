package de.mscho.toftws.calendar.service;

import de.mscho.toftws.configuration.security.AuthenticationProvider;
import de.mscho.toftws.calendar.entity.EventDeviation;
import de.mscho.toftws.calendar.entity.payload.DeviationRequest;
import de.mscho.toftws.calendar.repository.DeviationRepo;
import de.mscho.toftws.calendar.repository.EventContentRepo;
import de.mscho.toftws.calendar.repository.EventRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DeviationService {
    private final Logger logger;
    private final DeviationRepo deviationRepo;
    private final EventRepo eventRepo;
    private final EventContentRepo contentRepo;
    private final AuthenticationProvider authenticationProvider;

    public List<EventDeviation> getDeviations(Instant from, Instant to) {
        var user = authenticationProvider.getAuthenticatedUser();
        return deviationRepo.findEventDeviationByNewOccurrenceAfterAndNewOccurrenceBeforeAndEventUser(from, to, user);
    }

    public void createDeviation(long eventId, DeviationRequest deviationDto) {
        var user = authenticationProvider.getAuthenticatedUser();
        var eventOptional = eventRepo.findByIdAndUser(eventId, user);

        if (eventOptional.isEmpty()) {
            logger.info("Creating deviation not possible as event(id: {}) does not exist for user(id: {})", eventId, user.id);
            return;
        }

        var event = eventOptional.get();
        var deviation = deviationDto.toEntity();

        setContent(deviation, deviationDto);

        deviation.event = event;
        deviation = deviationRepo.save(deviation);

        event.deviations.add(deviation);
        event = eventRepo.save(event);

        logger.info("Created deviation (id: {}) for event(id: {})", deviation.id, event.id);
    }

    public void deleteDeviation(long deviationId) {
        var user = authenticationProvider.getAuthenticatedUser();
        var deviationOptional = deviationRepo.findByIdAndEventUser(deviationId, user);

        if (deviationOptional.isEmpty()) {
            logger.info("Can not delete deviation as deviation(id: {}) does not exist for user(id: {})", deviationId, user.id);
            return;
        }

        var deviation = deviationOptional.get();
        var event = deviation.event;

        event.deviations.remove(deviation);
        deviation.event = null;

        event = eventRepo.save(event);
        deviationRepo.delete(deviation);

        logger.info("Deleted deviation (id: {}) from event(id: {})", deviationId, event.id);
    }

    public void editDeviation(long deviationId, boolean replaceContent, DeviationRequest deviationDto) {
        var user = authenticationProvider.getAuthenticatedUser();
        var deviationOptional = deviationRepo.findByIdAndEventUser(deviationId, user);

        if (deviationOptional.isEmpty()) {
            logger.info("Editing deviation not possible as deviation(id: {}) does not exist for user(id: {})", deviationId, user.id);
            return;
        }

        var deviation = deviationOptional.get();
        deviation.oldOccurrence = deviationDto.oldOccurrence;
        deviation.newOccurrence = deviationDto.newOccurrence;
        deviation.duration = deviationDto.duration;
        deviation.cancelled = deviationDto.cancelled;

        if (replaceContent) {
            setContent(deviation, deviationDto);
        }

        deviationRepo.save(deviation);
        logger.info("Edited deviation(id: {})", deviationId);
    }

    /**
     * Delete potential old content of existing deviation.
     * Create and save potential new content from dto.
     * Set content of deviation.
     */
    private void setContent(EventDeviation deviation, DeviationRequest deviationDto) {
        if (deviation.content != null) {
            var content = deviation.content;
            deviation.content = null;
            contentRepo.delete(content);
        }

        if (deviationDto.content != null) {
            var newContent = deviationDto.content.toEntity();
            newContent = contentRepo.save(newContent);
            deviation.content = newContent;
        }
    }
}
