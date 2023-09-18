package de.mscho.toftws.entity.calendar;

import de.mscho.toftws.entity.AbstractTimedEntity;
import jakarta.persistence.*;

import java.time.ZonedDateTime;

@Entity
public class EventDeviation extends AbstractTimedEntity {
    @ManyToOne(optional = false)
    public Event event;
    public ZonedDateTime oldOccurrence;
    public ZonedDateTime newOccurrence;
    public long duration;
    public boolean cancelled;
    @OneToOne(cascade = CascadeType.ALL)
    public EventContent content;
}
