package de.mscho.toftws.entity.calendar.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.mscho.toftws.entity.calendar.Event;
import de.mscho.toftws.entity.calendar.EventCategory;
import de.mscho.toftws.entity.calendar.EventContent;
import de.mscho.toftws.entity.calendar.EventDeviation;

import java.time.ZonedDateTime;


public class EventDto {
    public Long id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Long deviationId;
    public ZonedDateTime start;
    public long duration;
    public EventContentDto content;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String categoryName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String color;

    private EventDto() {}

    public static EventDto buildForEvent(ZonedDateTime start, Event event) {
        var eventDto = new EventDto();
        eventDto.id = event.id;
        eventDto.start = start;
        eventDto.duration = event.duration;
        return eventDto.fillContentAndCategory(event.content, event.category);
    }

    public static EventDto buildForDeviation(long eventId, EventDeviation deviation, EventContent content, EventCategory category) {
        var eventDto = new EventDto();
        eventDto.id = eventId;
        eventDto.start = deviation.newOccurrence;
        eventDto.duration = deviation.duration;
        eventDto.deviationId = deviation.id;
        return eventDto.fillContentAndCategory(content, category);
    }

    private EventDto fillContentAndCategory(EventContent content, EventCategory category) {
        this.content = EventContentDto.build(content);
        if (category != null) {
            this.categoryName = category.name;
            this.color = category.color;
        }
        return this;
    }
}