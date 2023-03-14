package de.mscho.toftws.repository.calendar;

import de.mscho.toftws.entity.calendar.EventDeviation;
import de.mscho.toftws.entity.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface DeviationRepo extends CrudRepository<EventDeviation, Long> {
    Optional<EventDeviation> findByIdAndEventUser(long eventId, User user);
}
