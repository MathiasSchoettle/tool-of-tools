package de.mscho.toftws.flashcard.entity.responses;

import de.mscho.toftws.flashcard.entity.FlashcardDeck;

import java.time.LocalDateTime;

public class FlashcardDeckResponse {
    public Long id;
    public String name;
    public Integer cardCountToStudy;

    private FlashcardDeckResponse() {}

    public static FlashcardDeckResponse build(FlashcardDeck deck, LocalDateTime current) {
        var response = new FlashcardDeckResponse();
        response.id = deck.id;
        response.name = deck.name;
        response.cardCountToStudy = deck.getCardStudyCount(current);
        return response;
    }
}
