package de.mscho.toftws.repository.calendar;

import de.mscho.toftws.entity.calendar.Event;
import de.mscho.toftws.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepo extends JpaRepository<Event, Long> {
    Optional<Event> findByIdAndUser(long id, User user);
    List<Event> findEventsByRecurrenceStartBeforeAndRecurrenceEndAfterAndUser(ZonedDateTime to, ZonedDateTime from, User user);
    List<Event> findEventsByCategoryIdAndUser(long categoryId, User user);
    long deleteByIdAndUser(long id, User user);
}
