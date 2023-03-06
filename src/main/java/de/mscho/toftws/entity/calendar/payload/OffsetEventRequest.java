package de.mscho.toftws.entity.calendar.payload;

import javax.validation.constraints.Positive;

public class OffsetEventRequest extends EventRequest {
    @Positive
    public int offset;
}
