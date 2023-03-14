package de.mscho.toftws.repository.calendar;

import de.mscho.toftws.entity.calendar.EventCategory;
import de.mscho.toftws.entity.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CategoryRepo extends CrudRepository<EventCategory, Long> {
    Optional<EventCategory> findByIdAndUser(long id, User user);
    void deleteByIdAndUser(long id, User user);
}
