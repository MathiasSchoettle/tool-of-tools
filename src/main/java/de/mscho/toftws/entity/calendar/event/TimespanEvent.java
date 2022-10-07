package de.mscho.toftws.entity.calendar.event;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Getter
@Setter
public class TimespanEvent extends AbstractEvent {

    @NotNull
    private OffsetDateTime startDateTime;

    @Min(value = 60 * 5)
    private long duration;

    public TimespanEvent() {
    }

    public TimespanEvent(OffsetDateTime startDateTime, long duration) {
        this.startDateTime = startDateTime;
        this.duration = duration;
    }

    @Override
    protected AbstractEvent copy() {
        TimespanEvent event = new TimespanEvent();
        event.setStartDateTime(startDateTime);
        event.setDuration(this.duration);
        return event;
    }

    @Override
    protected void setNewStart(OffsetDateTime start) {
        this.startDateTime = start;
    }
}
