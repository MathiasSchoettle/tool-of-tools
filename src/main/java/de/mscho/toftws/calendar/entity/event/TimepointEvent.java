package de.mscho.toftws.calendar.entity.event;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Getter
@Setter
public class TimepointEvent extends AbstractEvent {

    @NotNull
    OffsetDateTime dateTime;

    public TimepointEvent() {
        
    }

    public TimepointEvent(OffsetDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    protected AbstractEvent copy() {
        TimepointEvent event = new TimepointEvent();
        event.setDateTime(dateTime);
        return event;
    }

    @Override
    protected void setNewStart(OffsetDateTime start) {
        this.dateTime = start;
    }
}
