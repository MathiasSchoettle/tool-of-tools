package de.mscho.toftws.calendar.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;

@Getter
@Setter
@Entity
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class CalendarEvent extends AbstractCalendarEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id;

    @Valid
    @Embedded
    private CalendarPosition calendarPosition;
}
