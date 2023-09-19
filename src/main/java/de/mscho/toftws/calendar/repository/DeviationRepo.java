package de.mscho.toftws.calendar.repository;

import de.mscho.toftws.calendar.entity.EventDeviation;
import de.mscho.toftws.user.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface DeviationRepo extends CrudRepository<EventDeviation, Long> {
    Optional<EventDeviation> findByIdAndEventUser(long eventId, User user);
}
