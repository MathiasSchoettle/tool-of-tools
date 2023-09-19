package de.mscho.toftws.calendar.entity.payload;

import de.mscho.toftws.calendar.entity.EventDeviation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.ZonedDateTime;

public class DeviationRequest {
    @NotNull
    public ZonedDateTime oldOccurrence;
    @NotNull
    public ZonedDateTime newOccurrence;
    @PositiveOrZero
    public long duration;
    public boolean cancelled;
    @Valid
    public EventContentDto content;

    public EventDeviation toEntity() {
        var deviation = new EventDeviation();
        deviation.oldOccurrence = oldOccurrence;
        deviation.newOccurrence = newOccurrence;
        deviation.duration = duration;
        deviation.cancelled = cancelled;
        return deviation;
    }
}
