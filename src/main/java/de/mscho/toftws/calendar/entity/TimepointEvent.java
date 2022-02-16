package de.mscho.toftws.calendar.entity;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Getter
@Setter
public class TimepointEvent extends AbstractCalendarEvent {

    @NotNull
    OffsetDateTime dateTime;
}
