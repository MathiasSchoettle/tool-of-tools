package de.mscho.toftws.entity.flashcards;

import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
@NoArgsConstructor
public class FlashcardContent {
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
