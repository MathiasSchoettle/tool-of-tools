package de.mscho.toftws.entity.flashcards;

import de.mscho.toftws.entity.AbstractEntity;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor
public class FlashcardContent extends AbstractEntity {

    @NotNull
    @Length(max = 1024)
    public String question;

    @NotNull
    @Length(max = 1024)
    public String solution;

    public FlashcardContent(String question, String solution) {
        this.question = question;
        this.solution = solution;
    }
}
