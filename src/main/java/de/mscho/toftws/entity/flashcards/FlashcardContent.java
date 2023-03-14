package de.mscho.toftws.entity.flashcards;

import lombok.NoArgsConstructor;

import jakarta.persistence.Embeddable;

@Embeddable
@NoArgsConstructor
public class FlashcardContent {
    public String question;
    public String solution;

    public FlashcardContent(String question, String solution) {
        this.question = question;
        this.solution = solution;
    }
}
