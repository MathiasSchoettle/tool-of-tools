package de.mscho.toftws.entity.calendar;

import de.mscho.toftws.entity.AbstractEntity;
import de.mscho.toftws.entity.user.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class EventCategory extends AbstractEntity {
    @Column(unique = true)
    public String name;
    public String color;
    @ManyToOne
    public User owner;
}
