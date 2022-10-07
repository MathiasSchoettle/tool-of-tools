package de.mscho.toftws.repository;

import de.mscho.toftws.entity.calendar.event.CalendarEvent;
import org.springframework.data.repository.CrudRepository;

import java.time.OffsetDateTime;
import java.util.List;

public interface CalendarEventRepo extends CrudRepository<CalendarEvent, Long> {

    List<CalendarEvent> findAllByCalendarPosition_UntilDateGreaterThanEqualAndCalendarPosition_StartDateLessThan(OffsetDateTime from, OffsetDateTime to);
}
