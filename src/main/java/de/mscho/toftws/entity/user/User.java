package de.mscho.toftws.entity.user;

import de.mscho.toftws.entity.AbstractTimedEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

import javax.persistence.Entity;

@Entity
public class User extends AbstractTimedEntity {
    public String name;
}
