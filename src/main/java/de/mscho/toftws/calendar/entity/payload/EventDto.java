package de.mscho.toftws.calendar.entity.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.mscho.toftws.calendar.entity.Event;
import de.mscho.toftws.calendar.entity.EventCategory;
import de.mscho.toftws.calendar.entity.EventContent;
import de.mscho.toftws.calendar.entity.EventDeviation;
import de.mscho.toftws.utils.DateTimeUtils;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;


@NoArgsConstructor
public class EventDto {
    public Long id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Long deviationId;
    public ZonedDateTime start;
    public long duration;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public Boolean fullDay;
    public EventContentDto content;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String categoryName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String color;

    public static EventDto buildForEvent(ZonedDateTime start, Event event) {
        var eventDto = new EventDto();
        eventDto.id = event.id;
        eventDto.start = start.withZoneSameInstant(DateTimeUtils.UTC);
        eventDto.duration = event.duration;
        if (event.fullDay) eventDto.fullDay = true;
        return eventDto.fillContentAndCategory(event.content, event.category);
    }

    public static EventDto buildForDeviation(EventDeviation deviation) {
        var eventDto = new EventDto();
        eventDto.id = deviation.event.id;
        eventDto.start = deviation.newOccurrence.atZone(DateTimeUtils.UTC);
        eventDto.duration = deviation.duration;
        eventDto.deviationId = deviation.id;
        var content = deviation.content == null ? deviation.event.content : deviation.content;
        return eventDto.fillContentAndCategory(content, deviation.event.category);
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
