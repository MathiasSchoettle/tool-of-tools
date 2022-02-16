package de.mscho.toftws.calendar.repository;

import de.mscho.toftws.calendar.entity.CalendarEvent;
import org.springframework.data.repository.CrudRepository;

public interface CalendarEventRepository extends CrudRepository<CalendarEvent, Long> {

}
