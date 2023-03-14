package de.mscho.toftws.entity;

import org.springframework.data.annotation.CreatedDate;

import java.time.ZonedDateTime;

public abstract class AbstractTimedEntity extends AbstractEntity {
    @CreatedDate
    public ZonedDateTime created;
}
