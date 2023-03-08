package de.mscho.toftws.entity.flashcards;

import lombok.NoArgsConstructor;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Embeddable
@NoArgsConstructor
public class FlashcardRepetition {
    @NotNull
    @DecimalMin("1.3")
    public Double easeFactor = 2.5;
    @NotNull
    @DecimalMin("0")
    public Double dayInterval = 0.0;
    @NotNull
    public LocalDateTime lastAnswered;
    @NotNull
    public LocalDateTime nextPlannedOccurrence;
    @NotNull
    @Enumerated(EnumType.STRING)
    public LearningStep learningStep = LearningStep.INITIAL;

    public FlashcardRepetition(LocalDateTime lastAnswered) {
        this.lastAnswered = lastAnswered;
        this.nextPlannedOccurrence = lastAnswered;
    }

    public void again() {
        lastAnswered = LocalDateTime.now();

        if (learningStep.equals(LearningStep.GRADUATED)) {
            easeFactor -= 0.2;
            dayInterval = 0.0;
            nextPlannedOccurrence = lastAnswered.plusMinutes(10);
            learningStep = LearningStep.INTERMEDIATE;
        } else {
            nextPlannedOccurrence = lastAnswered.plusMinutes(1);
            learningStep = LearningStep.INITIAL;
        }
    }

    public void hard() {
        lastAnswered = LocalDateTime.now();

        if (learningStep.equals(LearningStep.GRADUATED)) {
            easeFactor -= 0.15;
            setStepAndUpdateInterval(LearningStep.GRADUATED, dayInterval * 1.2);
        }
    }

    public void good() {
        lastAnswered = LocalDateTime.now();

        switch (learningStep) {
            case INITIAL -> {
                learningStep = LearningStep.INTERMEDIATE;
                nextPlannedOccurrence = lastAnswered.plusMinutes(10);
            }
            case INTERMEDIATE -> setStepAndUpdateInterval(LearningStep.FINAL, 1.0);
            case FINAL -> setStepAndUpdateInterval(LearningStep.GRADUATED, 2.0);
            case GRADUATED -> setStepAndUpdateInterval(LearningStep.GRADUATED, dayInterval * easeFactor);
        }
    }

    public void easy() {
        lastAnswered = LocalDateTime.now();

        if (learningStep.equals(LearningStep.GRADUATED)) {
            easeFactor += 0.15;
            setStepAndUpdateInterval(LearningStep.GRADUATED, dayInterval * easeFactor);
        } else {
            setStepAndUpdateInterval(LearningStep.GRADUATED, 2.0);
        }
    }

    private void setStepAndUpdateInterval(LearningStep nextStep, Double nextDayInterval) {
        learningStep = nextStep;
        dayInterval = nextDayInterval;
        long daysToAdd = Math.max(dayInterval.longValue(), 1L);
        nextPlannedOccurrence = lastAnswered.plusDays(daysToAdd).withHour(4).withMinute(0).withSecond(0).withNano(0);
    }

    public enum LearningStep {
        INITIAL, INTERMEDIATE, FINAL, GRADUATED
    }
}
