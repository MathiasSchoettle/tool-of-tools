package de.mscho.toftws.entity.flashcards;

import de.mscho.toftws.entity.AbstractEntity;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
        List<Flashcard> possibleCards = getPossibleCards(current);

        if (possibleCards.isEmpty()) {
            return Optional.empty();
        }

        Flashcard nextCard = possibleCards.get(new Random().nextInt(possibleCards.size()));
        return Optional.of(nextCard);
    }

    public Integer getCardStudyCount(LocalDateTime current) {
        List<Flashcard> possibleCards = getPossibleCards(current);
        return possibleCards.size();
    }

    private List<Flashcard> getPossibleCards(LocalDateTime current) {
        return cards.stream().filter(card -> card.repetition.nextPlannedOccurrence.isBefore(current.plusMinutes(20))).toList();
    }
}
