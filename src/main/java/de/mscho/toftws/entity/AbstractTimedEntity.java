package de.mscho.toftws.entity;

import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

@MappedSuperclass
public abstract class AbstractTimedEntity extends AbstractEntity {
    @CreationTimestamp
    public ZonedDateTime created;
}
