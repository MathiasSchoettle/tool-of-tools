package de.mscho.toftws.entity.calendar.payload;

import jakarta.validation.constraints.Positive;

public class OffsetEventRequest extends EventRequest {
    @Positive
    public int offset;
}
