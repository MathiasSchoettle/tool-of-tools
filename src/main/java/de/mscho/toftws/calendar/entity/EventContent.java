package de.mscho.toftws.calendar.entity;

import de.mscho.toftws.entity.AbstractTimedEntity;

import jakarta.persistence.Entity;

@Entity
public class EventContent extends AbstractTimedEntity {
    public String title;
    public String description;
    public String location;
    public String link;
}
