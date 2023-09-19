package de.mscho.toftws.flashcard.repository;

import de.mscho.toftws.flashcard.entity.Flashcard;
import org.springframework.data.repository.CrudRepository;

public interface FlashcardRepo extends CrudRepository<Flashcard, Long> {

    Integer deleteFlashcardById(Long id);
}
