package de.mscho.toftws.calendar.entity;

import de.mscho.toftws.entity.AbstractTimedEntity;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class EventDeviation extends AbstractTimedEntity {
    @ManyToOne(optional = false)
    public Event event;
    public Instant oldOccurrence;
    public Instant newOccurrence;
    public long duration;
    public boolean cancelled;
    @OneToOne(cascade = CascadeType.ALL)
    public EventContent content;
}
