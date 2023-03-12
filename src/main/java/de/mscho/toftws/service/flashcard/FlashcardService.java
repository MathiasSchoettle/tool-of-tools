package de.mscho.toftws.service.flashcard;

import de.mscho.toftws.entity.flashcards.Flashcard;
import de.mscho.toftws.entity.flashcards.FlashcardContent;
import de.mscho.toftws.entity.flashcards.FlashcardDeck;
import de.mscho.toftws.repository.flashcard.FlashcardDeckRepo;
import de.mscho.toftws.repository.flashcard.FlashcardRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public boolean deleteFlashcardDeck(Long deckId) {
        Integer deletedAmount = flashcardDeckRepo.deleteFlashcardDeckById(deckId);
        logger.info("Deleted flashcardDeck (id: {})", deckId);
        return deletedAmount > 0;
    }

    public List<FlashcardDeck> allDecks() {
        return flashcardDeckRepo.findAll();
    }

    public List<Flashcard> allCardsOfDeck(Long deckId) {
        Optional<FlashcardDeck> deckOptional = flashcardDeckRepo.findById(deckId);

        if (deckOptional.isEmpty()) {
            logger.info("No flashcardDeck found for id {}", deckId);
            return new ArrayList<>();
        }

        return deckOptional.get().cards;
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

    public void updateFlashCard(Long cardId, String question, String solution) {
        Optional<Flashcard> cardOptional = flashcardRepo.findById(cardId);

        if (cardOptional.isEmpty()) {
            logger.info("No Flashcard found for id {}", cardId);
            return;
        }

        Flashcard card = cardOptional.get();
        card.content.question = question;
        card.content.solution = solution;

        logger.info("Updated flashcard (id: {})", card.id);

        flashcardRepo.save(card);
    }

    public boolean deleteFlashcard(Long cardId) {
        return flashcardRepo.deleteFlashcardById(cardId) > 0;
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
