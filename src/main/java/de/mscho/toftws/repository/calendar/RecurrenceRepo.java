package de.mscho.toftws.repository.calendar;

import de.mscho.toftws.entity.calendar.recurrence.Recurrence;
import org.springframework.data.repository.CrudRepository;

public interface RecurrenceRepo extends CrudRepository<Recurrence, Long> {
}
