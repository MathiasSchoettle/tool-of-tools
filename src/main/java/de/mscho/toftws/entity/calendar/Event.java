package de.mscho.toftws.entity.calendar;

import de.mscho.toftws.entity.AbstractTimedEntity;
import de.mscho.toftws.entity.calendar.payload.EventDto;
import de.mscho.toftws.entity.calendar.recurrence.Recurrence;
import de.mscho.toftws.entity.user.User;

import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
public class Event extends AbstractTimedEntity {
    @PositiveOrZero
    public long duration;
    @OneToOne(cascade = CascadeType.REMOVE)
    public EventContent content;
    @ManyToOne
    public EventCategory category;
    @OneToOne(cascade = CascadeType.REMOVE)
    public Recurrence recurrence;
    @OneToMany(mappedBy = "event", cascade = CascadeType.REMOVE)
    public List<EventDeviation> deviations = new ArrayList<>();
    @ManyToOne
    public User user;

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

            // use base event content if deviation's content is empty
            var content = deviation.content == null ? this.content : deviation.content;
            event = EventDto.buildForDeviation(id, deviation, content, category);
        }
        else {
            event = EventDto.buildForEvent(current, this);
        }

        return Optional.of(event);
    }
}
