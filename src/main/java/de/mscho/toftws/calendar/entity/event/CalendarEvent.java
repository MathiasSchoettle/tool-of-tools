package de.mscho.toftws.calendar.entity.event;

import lombok.EqualsAndHashCode;
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
