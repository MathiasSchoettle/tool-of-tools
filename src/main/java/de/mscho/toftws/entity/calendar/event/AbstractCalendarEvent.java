package de.mscho.toftws.entity.calendar.event;

import de.mscho.toftws.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@MappedSuperclass
public abstract class AbstractCalendarEvent extends AbstractEntity {

    @NotBlank
    @Size(max = 64)
    protected String title;

    @Size(max = 256)
    protected String description;

    @Pattern(regexp = "^#([0-9a-fA-F]{6})$")
    protected String hexColor;

    @Size(max = 128)
    protected String location;

    @Size(max = 256)
    protected String meetingLink;
}
