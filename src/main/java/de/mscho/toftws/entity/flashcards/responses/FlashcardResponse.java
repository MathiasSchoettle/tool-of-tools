package de.mscho.toftws.entity.flashcards.responses;

import de.mscho.toftws.entity.flashcards.Flashcard;
import de.mscho.toftws.entity.flashcards.FlashcardContent;

public class FlashcardResponse {
    public Long id;
    public FlashcardContent content;

    public static FlashcardResponse build(Flashcard flashcard) {
        var response = new FlashcardResponse();
        response.id = flashcard.id;
        response.content = flashcard.content;
        return response;
    }
}
