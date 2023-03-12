package de.mscho.toftws.repository.calendar;

import de.mscho.toftws.entity.calendar.Event;
import org.springframework.data.repository.CrudRepository;

import java.time.ZonedDateTime;
import java.util.List;

public interface EventRepo extends CrudRepository<Event, Long> {
    List<Event> findEventsByRecurrenceStartBeforeAndRecurrenceEndAfter(ZonedDateTime to, ZonedDateTime from);
    List<Event> findEventsByCategoryId(long id);
}
