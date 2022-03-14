package de.mscho.toftws.calendar.entity.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Setter
@Validated
public class DayEvent extends AbstractEvent {

    @NotNull
    private LocalDate date;

    public DayEvent() {
    }

    public DayEvent(LocalDate date) {
        this.date = date;
    }

    @Override
    protected AbstractEvent copy() {
        DayEvent event = new DayEvent();
        event.setDate(date);
        return event;
    }

    @Override
    protected void setNewStart(OffsetDateTime start) {
        date = start.toLocalDate();
    }
}
