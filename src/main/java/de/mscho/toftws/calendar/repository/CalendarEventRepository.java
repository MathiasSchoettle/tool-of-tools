package de.mscho.toftws.calendar.repository;

import de.mscho.toftws.calendar.entity.event.CalendarEvent;
import org.springframework.data.repository.CrudRepository;

import java.time.OffsetDateTime;
import java.util.List;

public interface CalendarEventRepository extends CrudRepository<CalendarEvent, Long> {

    List<CalendarEvent> findAllByCalendarPosition_UntilDateGreaterThanEqualAndCalendarPosition_StartDateLessThan(OffsetDateTime from, OffsetDateTime to);
}
