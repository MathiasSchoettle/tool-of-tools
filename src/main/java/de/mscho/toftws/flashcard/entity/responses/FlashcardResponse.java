package de.mscho.toftws.flashcard.entity.responses;

import de.mscho.toftws.flashcard.entity.Flashcard;
import de.mscho.toftws.flashcard.entity.FlashcardContent;

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
