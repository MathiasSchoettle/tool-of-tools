package de.mscho.toftws.repository.calendar;

import de.mscho.toftws.entity.calendar.EventDeviation;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DeviationRepo extends CrudRepository<EventDeviation, Long> {
    List<EventDeviation> findByEventId(long eventId);
}
