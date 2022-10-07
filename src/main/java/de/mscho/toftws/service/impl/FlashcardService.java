package de.mscho.toftws.service.impl;

import de.mscho.toftws.entity.flashcards.Flashcard;
import de.mscho.toftws.entity.flashcards.FlashcardContent;
import de.mscho.toftws.entity.flashcards.FlashcardDeck;
import de.mscho.toftws.repository.FlashcardDeckRepo;
import de.mscho.toftws.repository.FlashcardRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
public class FlashcardService {
    private final Logger logger;
    private final FlashcardRepo flashcardRepo;
    private final FlashcardDeckRepo flashcardDeckRepo;

    public void createFlashcardDeck(String name) {
        FlashcardDeck deck = new FlashcardDeck(name);
        flashcardDeckRepo.save(deck);
        logger.info("Created flashcardDeck (id: {}, name: {})", deck.id, name);
    }

    public void addSimpleFlashcard(Long deckId, String question, String solution) {
        Optional<FlashcardDeck> deckOptional = flashcardDeckRepo.findById(deckId);

        if (deckOptional.isEmpty()) {
            logger.info("No flashcardDeck found for id {}", deckId);
            return;
        }

        FlashcardDeck deck = deckOptional.get();

        FlashcardContent cardContent = new FlashcardContent(question, solution);
        Flashcard flashcard = new Flashcard(cardContent);
        deck.cards.add(flashcard);
        flashcard.deck = deck;


        logger.info("Added new flashcard to deck (id: {}, name: {})", deck.id, deck.name);

        flashcardDeckRepo.save(deck);
    }

    public boolean deleteFlashcard(Long cardId) {
        return flashcardRepo.deleteFlashcardById(cardId) > 0;
    }

    public Integer getLearnableCount(Long deckId) {
        Optional<FlashcardDeck> deckOptional = flashcardDeckRepo.findById(deckId);

        if (deckOptional.isEmpty()) {
            logger.info("No flashcardDeck found for id {}", deckId);
            return 0;
        }

        FlashcardDeck deck = deckOptional.get();

        return deck.cards.size();
    }

    public Optional<Flashcard> getNextFlashcard(Long deckId) {
        Optional<FlashcardDeck> deckOptional = flashcardDeckRepo.findById(deckId);

        if (deckOptional.isEmpty()) {
            logger.info("No flashcardDeck found for id {}", deckId);
            return Optional.empty();
        }

        return deckOptional.get().getNextFlashcard(LocalDateTime.now());
    }

    public void answerFlashcard(Long cardId, Flashcard.FlashcardAnswer answer, Long studiedSeconds) {

        Optional<Flashcard> flashcardOptional = flashcardRepo.findById(cardId);

        if (flashcardOptional.isEmpty()) {
            logger.info("No flashcard found for id {}", cardId);
            return;
        }

        Flashcard flashcard = flashcardOptional.get();
        flashcard.answer(answer, studiedSeconds);

        flashcardRepo.save(flashcard);
    }
}
