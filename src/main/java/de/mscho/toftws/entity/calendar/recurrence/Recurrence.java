package de.mscho.toftws.entity.calendar.recurrence;

import de.mscho.toftws.entity.AbstractEntity;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;

@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Recurrence extends AbstractEntity {
    @NotNull
    public ZonedDateTime start;
    @NotNull
    public ZonedDateTime end;

    public Recurrence(ZonedDateTime start, ZonedDateTime end) {
        this.start = start;
        this.end = end;
    }

    public abstract List<ZonedDateTime> generateOccurrences(ZonedDateTime from, ZonedDateTime to);
}
