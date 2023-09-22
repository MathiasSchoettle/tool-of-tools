package de.mscho.toftws.calendar.entity;

import de.mscho.toftws.entity.AbstractTimedEntity;
import de.mscho.toftws.calendar.entity.payload.EventDto;
import de.mscho.toftws.calendar.entity.recurrence.Recurrence;
import de.mscho.toftws.user.entity.User;

import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
public class Event extends AbstractTimedEntity {
    @PositiveOrZero
    public long duration;
    public boolean fullDay;
    @OneToOne(cascade = CascadeType.ALL, optional = false)
    public EventContent content;
    @ManyToOne
    public EventCategory category;
    @OneToOne(cascade = {CascadeType.ALL})
    public Recurrence recurrence;
    @OneToMany(mappedBy = "event", cascade = CascadeType.REMOVE)
    public List<EventDeviation> deviations = new ArrayList<>();
    @ManyToOne
    public User user;

    /**
     *  Generate eventDto objects from this event which occur between the given timeframe.
     *  Takes care of cancelled or edited events.
     */
    public List<EventDto> getEvents(Instant from, Instant to) {
        var events = new ArrayList<EventDto>();

        for (ZonedDateTime current : recurrence.generateOccurrences(from, to)) {
            var eventOptional = buildEvent(current);
            eventOptional.ifPresent(events::add);
        }

        return events;
    }

    private Optional<EventDto> buildEvent(ZonedDateTime current) {
        var deviationOptional = deviations.stream().
                filter(d -> d.oldOccurrence.atZone(recurrence.zoneId).isEqual(current)).findFirst();

        if (deviationOptional.isPresent()) return Optional.empty();

        return Optional.ofNullable(EventDto.buildForEvent(current, this));
    }

    public void fill(Event event) {
        this.duration = event.duration;
        this.content = event.content;
        this.category = event.category;
        this.recurrence = event.recurrence;
    }
}
