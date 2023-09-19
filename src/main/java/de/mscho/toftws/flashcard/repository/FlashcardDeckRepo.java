package de.mscho.toftws.flashcard.repository;

import de.mscho.toftws.flashcard.entity.FlashcardDeck;
import jakarta.annotation.Nonnull;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FlashcardDeckRepo extends CrudRepository<FlashcardDeck, Long> {
    @Override
    @Nonnull
    List<FlashcardDeck> findAll();
    Integer deleteFlashcardDeckById(Long id);
}
