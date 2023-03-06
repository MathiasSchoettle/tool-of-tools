package de.mscho.toftws.entity.calendar.payload;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.mscho.toftws.entity.calendar.util.DaySetConverters;

import java.time.DayOfWeek;
import java.util.Set;

public class WeeklyEventRequest extends OffsetEventRequest {
    @JsonDeserialize(converter = DaySetConverters.DaySetConverter.class)
    public Set<DayOfWeek> weekDays;
}
