package de.mscho.toftws.controller.calendar;

import de.mscho.toftws.entity.calendar.payload.DeviationRequest;
import de.mscho.toftws.service.calendar.DeviationService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("calendar/deviation")
@RequiredArgsConstructor
@Validated
public class DeviationController {
    private final DeviationService deviationService;

    /**
     * TODO check all controllers to only load events which belong to authenticated user
     *  especially event methods currently would load events for all users
     *  database migration with flyway is next priority
     *  finish off calendar event endpoints
     */

    @PostMapping
    public void createDeviation(@RequestParam long eventId, @RequestBody DeviationRequest deviation) {
        deviationService.createDeviation(eventId, deviation);
    }

    @DeleteMapping
    public void deleteDeviation(@RequestParam long deviationId) {
        deviationService.deleteDeviation(deviationId);
    }

    @PatchMapping
    public void editDeviation(@RequestParam long deviationId, @RequestParam(required = false) boolean replaceContent, @RequestBody DeviationRequest deviation) {
        deviationService.editDeviation(deviationId, replaceContent, deviation);
    }
}
