package de.mscho.toftws.controller;

import de.mscho.toftws.entity.flashcards.Flashcard;
import de.mscho.toftws.service.impl.FlashcardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.Optional;

@RestController
@RequestMapping("flash")
@RequiredArgsConstructor
@Validated
public class FlashcardController {
    private final FlashcardService flashcardService;

    @PostMapping("deck")
    public void createDeck(@RequestParam @Size(min = 1, max = 32, message = "Name must be between 1 and 32 characters") String name) {
        flashcardService.createFlashcardDeck(name);
    }

    @GetMapping("deck/count")
    public Integer getLearnableCount(@RequestParam Long deckId) {
        return flashcardService.getLearnableCount(deckId);
    }

    @PostMapping("card")
    public void addCard(@RequestParam Long deckId,
                        @RequestParam @Size(min = 1, max = 1024) String question,
                        @RequestParam @Size(min = 1, max = 1024) String answer) {
        flashcardService.addSimpleFlashcard(deckId, question, answer);
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
