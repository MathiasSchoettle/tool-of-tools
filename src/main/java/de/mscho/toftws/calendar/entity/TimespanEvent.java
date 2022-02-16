package de.mscho.toftws.calendar.entity;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Getter
@Setter
public class TimespanEvent extends AbstractCalendarEvent {

    @NotNull
    private OffsetDateTime startDateTime;

    @Min(value = 60 * 5)
    private long duration;
}
