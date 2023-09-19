package de.mscho.toftws.calendar.entity.recurrence;

import de.mscho.toftws.entity.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.NoArgsConstructor;

import java.time.*;
import java.util.List;

@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Recurrence extends AbstractEntity {
    public ZonedDateTime start;
    public ZonedDateTime end;

    // TODO move this somewhere else
    private static final ZonedDateTime MAX_DATE_TIME_SQL = ZonedDateTime.of(LocalDateTime.of(LocalDate.of(9999, 12, 31), LocalTime.MIN), ZoneId.of("UTC"));

    /**
     * If end is not set, set it to the max.
     */
    public Recurrence(ZonedDateTime start, ZonedDateTime end) {
        this.start = start;
        this.end = end != null ? end : MAX_DATE_TIME_SQL;
    }

    public abstract List<ZonedDateTime> generateOccurrences(ZonedDateTime from, ZonedDateTime to);
}
