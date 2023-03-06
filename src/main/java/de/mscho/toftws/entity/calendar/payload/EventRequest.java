package de.mscho.toftws.entity.calendar.payload;

import de.mscho.toftws.util.validate.ZonedDateBefore;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.ZonedDateTime;

@ZonedDateBefore(startField = "start", endField = "end")
public class EventRequest {
    @NotNull
    public ZonedDateTime start;
    @NotNull
    public ZonedDateTime end;
    @NotNull
    @PositiveOrZero
    public Long duration;
    @NotNull
    @Valid
    public EventContentDto content;
    public Long categoryId;
}
