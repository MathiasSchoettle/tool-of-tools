package de.mscho.toftws.calendar.repository;

import de.mscho.toftws.calendar.entity.event.CalendarEvent;
import de.mscho.toftws.calendar.service.CalendarEventService;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;

public interface CalendarEventRepository extends CrudRepository<CalendarEvent, Long> {

    List<CalendarEvent> findAllByCalendarPosition_UntilDateGreaterThanEqualAndCalendarPosition_StartDateLessThan(OffsetDateTime from, OffsetDateTime to);
}
