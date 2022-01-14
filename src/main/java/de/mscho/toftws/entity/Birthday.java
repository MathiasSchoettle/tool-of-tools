package de.mscho.toftws.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Birthday {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @JsonIgnore
    private Long id;

    private String firstname;

    private String surname;

    private LocalDate date;

    public Birthday(String firstname, String surname, LocalDate date) {
        this.firstname = firstname;
        this.surname = surname;
        this.date = date;
    }
}
