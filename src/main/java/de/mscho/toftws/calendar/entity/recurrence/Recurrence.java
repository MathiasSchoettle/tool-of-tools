package de.mscho.toftws.calendar.entity.recurrence;

import de.mscho.toftws.entity.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

import java.time.*;
import java.util.List;

import static de.mscho.toftws.utils.DateTimeUtils.MAX_INSTANT_TIME_SQL;

@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Recurrence extends AbstractEntity {
    @NotNull
    public Instant start;
    @NotNull
    public Instant end;
    @NotNull
    public ZoneId zoneId;

    /**
     * If end is not set, set it to the max.
     */
    public Recurrence(Instant start, Instant end, ZoneId zoneId) {
        this.start = start;
        this.end = end != null ? end : MAX_INSTANT_TIME_SQL;
        this.zoneId = zoneId;
    }

    public abstract List<ZonedDateTime> generateOccurrences(Instant from, Instant to);
}
