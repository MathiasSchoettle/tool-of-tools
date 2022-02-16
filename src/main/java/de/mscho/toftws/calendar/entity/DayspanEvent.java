package de.mscho.toftws.calendar.entity;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class DayspanEvent extends AbstractCalendarEvent{

    @NotNull
    private LocalDate startDate;

    @Min(0)
    private long days;
}
