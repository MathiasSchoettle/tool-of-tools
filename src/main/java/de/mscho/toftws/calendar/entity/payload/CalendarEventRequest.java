package de.mscho.toftws.calendar.entity.payload;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.mscho.toftws.calendar.entity.util.DaySetConverters;
import de.mscho.toftws.calendar.entity.util.EventRequestValidator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.util.SortedSet;

@EventRequestValidator.Constraint
public class CalendarEventRequest {
    @NotNull
    public RecurrenceType type;
    @NotNull
    public ZonedDateTime start;
    public ZonedDateTime end;
    @NotNull
    @PositiveOrZero
    public Long duration;
    @Positive
    public Long offset;
    // TODO could we just use a json array instead of weird string parsing?
    @JsonDeserialize(converter = DaySetConverters.DaySetConverter.class)
    public SortedSet<DayOfWeek> weekDays;
    @Positive
    public Integer occurrences;
    public boolean fullDay;
    @NotNull
    @Valid
    public EventContentDto content;
    @NotNull
    public Long categoryId;
}
