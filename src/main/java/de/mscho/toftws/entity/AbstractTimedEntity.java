package de.mscho.toftws.entity;

import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

public abstract class AbstractTimedEntity extends AbstractEntity {
    @CreatedDate
    public LocalDateTime created;
}
