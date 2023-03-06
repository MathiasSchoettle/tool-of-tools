package de.mscho.toftws.entity.calendar.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.mscho.toftws.entity.calendar.EventCategory;
import de.mscho.toftws.entity.calendar.EventContent;

import java.time.ZonedDateTime;


public class EventDto {
    public ZonedDateTime start;
    public long duration;
    public EventContentDto content;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String categoryName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String color;

    private EventDto() {}

    public static EventDto build(ZonedDateTime start, long duration, EventContent content, EventCategory category) {
        var event = new EventDto();
        event.start = start;
        event.duration = duration;
        event.content = EventContentDto.build(content);

        if (category != null) {
            event.categoryName = category.name;
            event.color = category.color;
        }

        return event;
    }
}
