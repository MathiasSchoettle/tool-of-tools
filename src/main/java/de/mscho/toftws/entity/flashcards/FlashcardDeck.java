package de.mscho.toftws.entity.flashcards;

import de.mscho.toftws.entity.AbstractEntity;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@NoArgsConstructor
public class FlashcardDeck extends AbstractEntity {
    @NotNull
    @Size(max = 64)
    public String name;
    @OneToMany(mappedBy = "deck")
    @Cascade(CascadeType.ALL)
    @NotNull
    public List<Flashcard> cards = new ArrayList<>();

    public FlashcardDeck(String name) {
        this.name = name;
    }

    public Optional<Flashcard> getNextFlashcard(LocalDateTime current) {
        List<Flashcard> possibleCards = cards.stream().filter(card -> card.repetition.nextPlannedOccurrence.isBefore(current.plusMinutes(20))).toList();

        if (possibleCards.isEmpty()) {
            return Optional.empty();
        }

        Flashcard nextCard = possibleCards.get(new Random().nextInt(possibleCards.size()));
        return Optional.of(nextCard);
    }
}
