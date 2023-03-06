package de.mscho.toftws.entity.calendar;

import de.mscho.toftws.entity.AbstractTimedEntity;
import de.mscho.toftws.entity.calendar.payload.EventDto;
import de.mscho.toftws.entity.calendar.recurrence.Recurrence;
import de.mscho.toftws.entity.user.User;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.PositiveOrZero;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
public class Event extends AbstractTimedEntity {
    @PositiveOrZero
    public long duration;
    @OneToOne
    public EventContent content;
    @ManyToOne
    public EventCategory category;
    @OneToOne
    public Recurrence recurrence;
    @OneToMany
    public List<RecurrenceDeviation> deviations = new ArrayList<>();
    @ManyToOne
    public User creator;

    /**
     *  Generate eventDto objects from this event which occur between the given timeframe.
     *  Takes care of cancelled or edited events.
     */
    public List<EventDto> getEvents(ZonedDateTime from, ZonedDateTime to) {
        var events = new ArrayList<EventDto>();

        for (ZonedDateTime current : recurrence.generateOccurrences(from, to)) {
            var eventOptional = buildEvent(current);
            eventOptional.ifPresent(events::add);
        }

        return events;
    }

    /**
     * Build an eventDto. Deviations of events are handled here.
     * If the deviation was cancelled this returns an empty optional.
     */
    private Optional<EventDto> buildEvent(ZonedDateTime current) {
        EventDto event;
        var deviationOptional = deviations.stream().filter(d -> d.oldOccurrence.isEqual(current)).findFirst();

        if (deviationOptional.isPresent()) {
            var deviation = deviationOptional.get();
            if (deviation.cancelled) {
                return Optional.empty();
            }

            event = EventDto.build(deviation.newOccurrence, deviation.duration, deviation.content, category);
        }
        else {
            event = EventDto.build(current, duration, content, category);
        }

        return Optional.of(event);
    }
}
