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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
        return cards.stream().min(Comparator.comparing(flashcard -> flashcard.getDurationOffset(current)));
    }
}
