package de.mscho.toftws.flashcard.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.mscho.toftws.entity.AbstractTimedEntity;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class Flashcard extends AbstractTimedEntity {
    public FlashcardContent content;
    @ManyToOne
    @JsonIgnore
    public FlashcardDeck deck;
    @JsonIgnore
    public FlashcardRepetition repetition;
    public long totalStudiedSeconds = 0L;
    public int countAnsweredCorrect = 0;
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
