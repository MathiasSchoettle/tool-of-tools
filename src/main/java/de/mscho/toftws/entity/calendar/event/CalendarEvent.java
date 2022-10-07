package de.mscho.toftws.entity.calendar.event;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;

@Getter
@Setter
@Entity
public class CalendarEvent extends AbstractCalendarEvent {

    @Valid
    @Embedded
    private CalendarPosition calendarPosition;
}
