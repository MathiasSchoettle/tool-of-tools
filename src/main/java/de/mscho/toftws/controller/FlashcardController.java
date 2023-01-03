package de.mscho.toftws.controller;

import de.mscho.toftws.entity.flashcards.Flashcard;
import de.mscho.toftws.entity.flashcards.FlashcardDeck;
import de.mscho.toftws.entity.flashcards.responses.FlashcardDeckResponse;
import de.mscho.toftws.entity.flashcards.responses.FlashcardResponse;
import de.mscho.toftws.service.impl.FlashcardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("flash")
@RequiredArgsConstructor
@Validated
public class FlashcardController {
    private final FlashcardService flashcardService;

    @PostMapping("deck")
    public void createDeck(@RequestParam @Size(min = 1, max = 32, message = "Name must be between 1 and 32 characters") String name) {
        flashcardService.createFlashcardDeck(name);
    }

    @GetMapping("deck/all")
    public List<FlashcardDeckResponse> allDecks() {
        List<FlashcardDeck> decks = flashcardService.allDecks();
        LocalDateTime current = LocalDateTime.now();
        return decks.stream().map(deck -> FlashcardDeckResponse.build(deck, current)).toList();
    }

    @DeleteMapping("deck")
    public boolean deleteDeck(@RequestParam Long deckId) {
        return flashcardService.deleteFlashcardDeck(deckId);
    }

    @GetMapping("deck/{deckId}/all")
    public List<FlashcardResponse> allCards(@PathVariable Long deckId) {
        List<Flashcard> cards = flashcardService.allCardsOfDeck(deckId);
        return cards.stream().map(FlashcardResponse::build).toList();
    }

    @PostMapping("card")
    public void addCard(@RequestParam Long deckId,
                        @RequestParam @Size(min = 1, max = 1024) String question,
                        @RequestParam @Size(min = 1, max = 1024) String answer) {
        flashcardService.addSimpleFlashcard(deckId, question, answer);
    }

    @PatchMapping("card/{cardId}")
    public void updateCard(@PathVariable Long cardId,
                        @RequestParam @Size(min = 1, max = 1024) String question,
                        @RequestParam @Size(min = 1, max = 1024) String answer) {
        flashcardService.updateFlashCard(cardId, question, answer);
    }

    @DeleteMapping("card/{cardId}")
    public boolean removeCard(@PathVariable Long cardId) {
        return flashcardService.deleteFlashcard(cardId);
    }

    @PostMapping("card/{cardId}/{answer}")
    public void answerCard(@PathVariable Long cardId,
                           @PathVariable Flashcard.FlashcardAnswer answer,
                           @RequestParam @Min(1) @Max(60) Long studiedSeconds) {
        flashcardService.answerFlashcard(cardId, answer, studiedSeconds);
    }

    @GetMapping("card")
    public ResponseEntity<Flashcard> getNextCard(@RequestParam Long deckId) {
        Optional<Flashcard> flashcard = flashcardService.getNextFlashcard(deckId);
        return ResponseEntity.of(flashcard);
    }
}
