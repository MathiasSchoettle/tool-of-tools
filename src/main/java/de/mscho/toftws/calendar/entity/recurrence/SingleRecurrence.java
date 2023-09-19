package de.mscho.toftws.calendar.entity.recurrence;

import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
public class SingleRecurrence extends Recurrence {

    public SingleRecurrence(ZonedDateTime start, ZonedDateTime end) {
        super(start, end);
    }

    @Override
    public List<ZonedDateTime> generateOccurrences(ZonedDateTime from, ZonedDateTime to) {
        if (!start.isBefore(from) && start.isBefore(to)) {
            return List.of(start);
        }
        return new ArrayList<>();
    }
}
