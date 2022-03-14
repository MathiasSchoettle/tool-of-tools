package de.mscho.toftws.calendar.entity.event;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class DayspanEvent extends AbstractEvent {

    @NotNull
    private LocalDate startDate;

    @Min(0)
    private long days;

    public DayspanEvent() {
    }

    public DayspanEvent(LocalDate startDate, long days) {
        this.startDate = startDate;
        this.days = days;
    }

    @Override
    protected AbstractEvent copy() {
        DayspanEvent event = new DayspanEvent();
        event.setStartDate(startDate);
        event.setDays(days);
        return event;
    }

    @Override
    protected void setNewStart(OffsetDateTime start) {
        this.startDate = start.toLocalDate();
    }
}
