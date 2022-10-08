package de.mscho.toftws.entity.flashcards;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.mscho.toftws.entity.AbstractEntity;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class Flashcard extends AbstractEntity {
    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    public FlashcardContent content;

    @NotNull
    @ManyToOne
    @JsonIgnore
    public FlashcardDeck deck;
    @NotNull
    @JsonIgnore
    public FlashcardRepetition repetition;
    @Min(0)
    @NotNull
    public Long totalStudiedSeconds = 0L;
    @Min(0)
    public int countAnsweredCorrect = 0;
    @Min(0)
    public int countAnsweredTotal = 0;

    public Flashcard(FlashcardContent content) {
        this.content = content;
        repetition = new FlashcardRepetition(LocalDateTime.now());
    }

    public void answer(FlashcardAnswer answer, Long studiedSeconds) {

        totalStudiedSeconds += studiedSeconds;
        countAnsweredTotal++;

        if (!answer.equals(FlashcardAnswer.AGAIN)) {
            countAnsweredCorrect++;
        }

        switch (answer) {
            case AGAIN -> repetition.again();
            case HARD -> repetition.hard();
            case GOOD -> repetition.good();
            case EASY -> repetition.easy();
        }
    }

    public enum FlashcardAnswer {
        AGAIN, HARD, GOOD, EASY
    }
}
