package de.mscho.toftws.entity.flashcards;

import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalDateTime;

@Embeddable
@NoArgsConstructor
public class FlashcardRepetition {

    @DecimalMin("1.3")
    public double easeFactor = 2.5;

    @NotNull
    public Duration learnInterval = Duration.ofMinutes(1);

    @NotNull
    public LocalDateTime lastAnswered;

    @NotNull
    @Enumerated(EnumType.STRING)
    public LearningStep learningStep = LearningStep.INITIAL;

    public FlashcardRepetition(LocalDateTime lastAnswered) {
        this.lastAnswered = lastAnswered;
    }

    public void again() {
        lastAnswered = LocalDateTime.now();

        if (this.learningStep.equals(LearningStep.GRADUATED)) {
            easeFactor -= 0.2;
            this.learnInterval = Duration.ofMinutes(10);
            this.learningStep = LearningStep.INTERMEDIATE;
        } else {
            this.learnInterval = Duration.ofMinutes(1);
            this.learningStep = LearningStep.INITIAL;
        }
    }

    public void hard() {
        lastAnswered = LocalDateTime.now();

        if (learningStep.equals(LearningStep.GRADUATED)) {
            easeFactor -= 0.15;
            learnInterval = Duration.ofDays((long) (learnInterval.toDays() * 1.2));
        }
    }

    public void good() {
        lastAnswered = LocalDateTime.now();

        switch (learningStep) {
            case INITIAL -> {
                learningStep = LearningStep.INTERMEDIATE;
                learnInterval = Duration.ofMinutes(10);
            }
            case INTERMEDIATE -> {
                learningStep = LearningStep.FINAL;
                learnInterval = Duration.ofDays(1);
            }
            case FINAL -> learningStep = LearningStep.GRADUATED;
            case GRADUATED -> learnInterval = Duration.ofDays((long) (learnInterval.toDays() * easeFactor));
        }
    }

    public void easy() {
        lastAnswered = LocalDateTime.now();

        if (learningStep.equals(LearningStep.GRADUATED)) {
            learnInterval = Duration.ofDays((long) (learnInterval.toDays() * easeFactor));
            easeFactor += 0.15;
        } else {
            learningStep = LearningStep.GRADUATED;
        }
    }

    public enum LearningStep {
        INITIAL, INTERMEDIATE, FINAL, GRADUATED
    }
}
