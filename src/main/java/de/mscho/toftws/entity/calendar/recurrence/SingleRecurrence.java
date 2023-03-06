package de.mscho.toftws.entity.calendar.recurrence;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class SingleRecurrence extends Recurrence {
    @Override
    public List<ZonedDateTime> generateOccurrences(ZonedDateTime from, ZonedDateTime to) {
        if (!start.isBefore(from) && start.isBefore(to)) {
            return List.of(start);
        }
        return new ArrayList<>();
    }
}
