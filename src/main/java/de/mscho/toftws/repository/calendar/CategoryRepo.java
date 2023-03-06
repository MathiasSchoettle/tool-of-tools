package de.mscho.toftws.repository.calendar;

import de.mscho.toftws.entity.calendar.EventCategory;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepo extends CrudRepository<EventCategory, Long> {
}
