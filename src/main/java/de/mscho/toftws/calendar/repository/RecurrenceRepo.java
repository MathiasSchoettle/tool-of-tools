package de.mscho.toftws.calendar.repository;

import de.mscho.toftws.calendar.entity.recurrence.Recurrence;
import org.springframework.data.repository.CrudRepository;

public interface RecurrenceRepo extends CrudRepository<Recurrence, Long> {
}
