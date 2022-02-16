package de.mscho.toftws.calendar.entity.validate;

import de.mscho.toftws.calendar.entity.CalendarPosition;
import de.mscho.toftws.calendar.entity.Recurrence;
import de.mscho.toftws.calendar.entity.validate.constraint.CalendarPositionConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Duration;
import java.time.OffsetDateTime;

public class CalendarPositionValidator implements ConstraintValidator<CalendarPositionConstraint, CalendarPosition> {

    @Override
    public void initialize(CalendarPositionConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(CalendarPosition calendarPosition, ConstraintValidatorContext constraintValidatorContext) {

        boolean datesCorrect = correctDateOrder(calendarPosition);
        boolean durationCorrect = correctDuration(calendarPosition);

        return datesCorrect && durationCorrect;
    }

    private static boolean correctDateOrder(CalendarPosition calendarPosition) {

        OffsetDateTime startDate = calendarPosition.getStartDate();
        OffsetDateTime untilDate = calendarPosition.getUntilDate();

        return !untilDate.isBefore(startDate);
    }

    private static boolean correctDuration(CalendarPosition calendarPosition) {

        return durationNotLongerThanRecurrence(calendarPosition) && untilDateNotBeforeEndOfDuration(calendarPosition);
    }

    private static boolean durationNotLongerThanRecurrence(CalendarPosition calendarPosition) {

        long duration = calendarPosition.getDuration();
        Recurrence recurrence = calendarPosition.getRecurrence();

        return duration <= recurrence.seconds;
    }

    private static boolean untilDateNotBeforeEndOfDuration(CalendarPosition calendarPosition) {

        OffsetDateTime startDate = calendarPosition.getStartDate();
        OffsetDateTime untilDate = calendarPosition.getUntilDate();

        long durationInSeconds = calendarPosition.getDuration();
        Duration duration = Duration.between(startDate, untilDate);

        return durationInSeconds <= duration.getSeconds();
    }
}
