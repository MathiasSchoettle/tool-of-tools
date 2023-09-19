package de.mscho.toftws.calendar.entity.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.mscho.toftws.calendar.entity.EventContent;

import jakarta.validation.constraints.NotBlank;

public class EventContentDto {
    @NotBlank
    public String title;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String description;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String location;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String link;

    private EventContentDto() {}

    public static EventContentDto build(EventContent content) {
        var dto = new EventContentDto();
        dto.title = content.title;
        dto.description = content.description;
        dto.location = content.location;
        dto.link = content.link;
        return dto;
    }

    public EventContent toEntity() {
        var content = new EventContent();
        content.title = title;
        content.description = description;
        content.location = location;
        content.link = link;
        return content;
    }
}
