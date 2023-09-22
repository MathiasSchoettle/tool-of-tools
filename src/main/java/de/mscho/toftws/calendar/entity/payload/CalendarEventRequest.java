package de.mscho.toftws.calendar.entity.payload;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.mscho.toftws.calendar.entity.util.DaySetConverters;
import de.mscho.toftws.calendar.entity.util.EventRequestValidator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.*;
import java.util.SortedSet;

@EventRequestValidator.Constraint
public class CalendarEventRequest {
    @NotNull
    public RecurrenceType type;
    @NotNull
    public Instant start;
    public Instant end;
    @NotNull
    public ZoneId zoneId;
    @NotNull
    @PositiveOrZero
    public Long duration;
    @Positive
    public Long offset;
    @JsonDeserialize(converter = DaySetConverters.DaySetConverter.class)
    public SortedSet<DayOfWeek> weekDays;
    @Positive
    public Integer occurrences;
    public boolean fullDay;
    @NotNull
    @Valid
    public EventContentDto content;
    public Long categoryId;

    public CalendarEventRequest(RecurrenceType type, Instant start, Instant end, ZoneId zoneId, Long duration, Long offset, Integer occurrences, EventContentDto content, Long categoryId) {
        this.type = type;
        this.start = start;
        this.end = end;
        this.zoneId = zoneId;
        this.duration = duration;
        this.offset = offset;
        this.occurrences = occurrences;
        this.content = content;
        this.categoryId = categoryId;
    }
}
