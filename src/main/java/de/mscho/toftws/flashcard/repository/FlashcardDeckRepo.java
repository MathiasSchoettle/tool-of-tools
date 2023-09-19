package de.mscho.toftws.flashcard.repository;

import de.mscho.toftws.flashcard.entity.FlashcardDeck;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FlashcardDeckRepo extends CrudRepository<FlashcardDeck, Long> {
    @Override
    List<FlashcardDeck> findAll();
    Integer deleteFlashcardDeckById(Long id);
}
