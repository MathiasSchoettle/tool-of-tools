package de.mscho.toftws.calendar.entity.recurrence;

import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
public class SingleRecurrence extends Recurrence {

    public SingleRecurrence(Instant start, Instant end, ZoneId zoneId) {
        super(start, end, zoneId);
    }

    @Override
    public List<ZonedDateTime> generateOccurrences(Instant from, Instant to) {
        if (!start.isBefore(from) && start.isBefore(to)) {
            return List.of(start.atZone(zoneId));
        }
        return new ArrayList<>();
    }
}
