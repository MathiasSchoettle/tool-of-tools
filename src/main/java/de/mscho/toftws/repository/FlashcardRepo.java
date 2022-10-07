package de.mscho.toftws.repository;

import de.mscho.toftws.entity.flashcards.Flashcard;
import org.springframework.data.repository.CrudRepository;

public interface FlashcardRepo extends CrudRepository<Flashcard, Long> {

    Integer deleteFlashcardById(Long id);
}
