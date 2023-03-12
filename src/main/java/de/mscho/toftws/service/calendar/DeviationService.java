package de.mscho.toftws.service.calendar;

import de.mscho.toftws.entity.calendar.EventDeviation;
import de.mscho.toftws.entity.calendar.payload.DeviationRequest;
import de.mscho.toftws.repository.calendar.DeviationRepo;
import de.mscho.toftws.repository.calendar.EventContentRepo;
import de.mscho.toftws.repository.calendar.EventRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class DeviationService {
    private final Logger logger;
    private final DeviationRepo deviationRepo;
    private final EventRepo eventRepo;
    private final EventContentRepo contentRepo;

    public void createDeviation(long eventId, DeviationRequest deviationDto) {
        var eventOptional = eventRepo.findById(eventId);

        if (eventOptional.isEmpty()) {
            logger.info("Creating deviation not possible as event(id: {}) does not exist", eventId);
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
        var deviationOptional = deviationRepo.findById(deviationId);

        if (deviationOptional.isEmpty()) {
            logger.info("Can not delete deviation(id: {}). No entity with given id", deviationId);
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
        var deviationOptional = deviationRepo.findById(deviationId);

        if (deviationOptional.isEmpty()) {
            logger.info("Editing deviation not possible as deviation(id: {}) does not exist", deviationId);
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
