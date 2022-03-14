package de.mscho.toftws.calendar.entity.event;

import de.mscho.toftws.calendar.entity.recurrence.Recurrence;
import de.mscho.toftws.calendar.entity.validate.constraint.CalendarPositionConstraint;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Getter
@Setter
@CalendarPositionConstraint
@Embeddable
public class CalendarPosition {

    @NotNull
    private OffsetDateTime startDate;

    @NotNull
    private OffsetDateTime untilDate;

    @Min(0)
    private long duration;

    private boolean allDay;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Recurrence recurrence = Recurrence.NEVER;
}
