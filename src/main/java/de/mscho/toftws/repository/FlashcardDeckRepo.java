package de.mscho.toftws.repository;

import de.mscho.toftws.entity.flashcards.FlashcardDeck;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FlashcardDeckRepo extends CrudRepository<FlashcardDeck, Long> {
    @Override
    List<FlashcardDeck> findAll();
    Integer deleteFlashcardDeckById(Long id);
}
