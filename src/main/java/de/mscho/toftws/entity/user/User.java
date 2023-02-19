package de.mscho.toftws.entity.user;

import de.mscho.toftws.entity.AbstractTimedEntity;

import javax.persistence.Entity;

@Entity
public class User extends AbstractTimedEntity {
    public String name;
}
