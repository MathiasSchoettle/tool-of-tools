package de.mscho.toftws.repository.calendar;

import de.mscho.toftws.entity.calendar.EventContent;
import org.springframework.data.repository.CrudRepository;


public interface EventContentRepo extends CrudRepository<EventContent, Long> {
}
