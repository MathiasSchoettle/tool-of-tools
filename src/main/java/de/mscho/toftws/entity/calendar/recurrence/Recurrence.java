package de.mscho.toftws.entity.calendar.recurrence;

import de.mscho.toftws.entity.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Recurrence extends AbstractEntity {
    public ZonedDateTime start;
    public ZonedDateTime end;

    public Recurrence(ZonedDateTime start, ZonedDateTime end) {
        this.start = start;
        this.end = end;
    }

    public abstract List<ZonedDateTime> generateOccurrences(ZonedDateTime from, ZonedDateTime to);
}
