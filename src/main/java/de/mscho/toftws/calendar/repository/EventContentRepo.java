package de.mscho.toftws.calendar.repository;

import de.mscho.toftws.calendar.entity.EventContent;
import org.springframework.data.repository.CrudRepository;


public interface EventContentRepo extends CrudRepository<EventContent, Long> {
}
