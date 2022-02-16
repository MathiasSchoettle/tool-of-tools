package de.mscho.toftws.calendar.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@MappedSuperclass
public abstract class AbstractCalendarEvent {

    @NotBlank
    @Size(max = 64)
    private String title;

    @Size(max = 256)
    private String description;

    @Pattern(regexp = "^#([0-9a-fA-F]{6})$")
    private String hexColor;

    @Size(max = 128)
    private String location;

    @Size(max = 256)
    private String meetingLink;
}
