package de.mscho.toftws.calendar.repository;

import de.mscho.toftws.calendar.entity.EventCategory;
import de.mscho.toftws.user.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepo extends CrudRepository<EventCategory, Long> {
    Optional<EventCategory> findByIdAndUser(long id, User user);
    void deleteByIdAndUser(long id, User user);
    boolean existsByIdAndUser(long id, User user);
    List<EventCategory> findByUser(User user);
}
