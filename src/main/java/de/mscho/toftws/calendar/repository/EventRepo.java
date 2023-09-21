package de.mscho.toftws.calendar.repository;

import de.mscho.toftws.calendar.entity.Event;
import de.mscho.toftws.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface EventRepo extends JpaRepository<Event, Long> {
    Optional<Event> findByIdAndUser(long id, User user);
    List<Event> findEventsByRecurrenceStartBeforeAndRecurrenceEndAfterAndUser(Instant to, Instant from, User user);
    List<Event> findEventsByCategoryIdAndUser(long categoryId, User user);
    long deleteByIdAndUser(long id, User user);
}
