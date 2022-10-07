package de.mscho.toftws.repository;

import de.mscho.toftws.entity.flashcards.FlashcardDeck;
import org.springframework.data.repository.CrudRepository;

public interface FlashcardDeckRepo extends CrudRepository<FlashcardDeck, Long> {
}
