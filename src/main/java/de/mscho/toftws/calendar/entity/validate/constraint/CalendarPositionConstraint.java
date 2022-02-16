package de.mscho.toftws.calendar.entity.validate.constraint;

import de.mscho.toftws.calendar.entity.validate.CalendarPositionValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CalendarPositionValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CalendarPositionConstraint {
    String message() default "Invalid duration or until date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
