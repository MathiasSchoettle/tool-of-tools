package de.mscho.toftws.entity.calendar;

import de.mscho.toftws.entity.AbstractTimedEntity;

import javax.persistence.Entity;

@Entity
public class EventContent extends AbstractTimedEntity {
    public String title;
    public String description;
    public String location;
    public String link;
}
