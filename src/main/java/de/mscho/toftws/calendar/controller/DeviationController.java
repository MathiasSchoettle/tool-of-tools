package de.mscho.toftws.calendar.controller;

import de.mscho.toftws.calendar.entity.payload.DeviationRequest;
import de.mscho.toftws.calendar.service.DeviationService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("calendar/deviation")
@RequiredArgsConstructor
@Validated
public class DeviationController {
    private final DeviationService deviationService;

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
