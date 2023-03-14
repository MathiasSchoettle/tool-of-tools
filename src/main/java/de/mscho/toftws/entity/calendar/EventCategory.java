package de.mscho.toftws.entity.calendar;

import de.mscho.toftws.entity.AbstractEntity;
import de.mscho.toftws.entity.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class EventCategory extends AbstractEntity {
    @Column(unique = true)
    public String name;
    public String color;
    @ManyToOne
    public User user;
}
