package de.mscho.toftws.calendar.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@Validated
public class DayEvent extends AbstractCalendarEvent {

    @NotNull
    private LocalDate date;
}
