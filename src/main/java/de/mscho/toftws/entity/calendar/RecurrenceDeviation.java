package de.mscho.toftws.entity.calendar;

import de.mscho.toftws.entity.AbstractTimedEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import java.time.ZonedDateTime;

@Entity
public class RecurrenceDeviation extends AbstractTimedEntity {
    public ZonedDateTime oldOccurrence;
    public ZonedDateTime newOccurrence;
    public long duration;
    public boolean cancelled;
    @OneToOne
    public EventContent content;
}
