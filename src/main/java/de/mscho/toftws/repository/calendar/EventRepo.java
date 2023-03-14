package de.mscho.toftws.repository.calendar;

import de.mscho.toftws.entity.calendar.Event;
import de.mscho.toftws.entity.user.User;
import org.springframework.data.repository.CrudRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepo extends CrudRepository<Event, Long> {
    Optional<Event> findByIdAndUser(long id, User user);
    List<Event> findEventsByRecurrenceStartBeforeAndRecurrenceEndAfterAndUser(ZonedDateTime to, ZonedDateTime from, User user);
    List<Event> findEventsByCategoryIdAndUser(long categoryId, User user);
    void deleteByIdAndUser(long id, User user);
}
