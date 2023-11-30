package de.mscho.toftws.calendar.entity.payload;

import de.mscho.toftws.calendar.entity.EventCategory;

public class EventCategoryPayload {
    public Long id;
    public String color;

    public static EventCategoryPayload fromEntity(EventCategory category) {
        var result = new EventCategoryPayload();
        result.id = category.id;
        result.color = category.color;
        return result;
    }
}
