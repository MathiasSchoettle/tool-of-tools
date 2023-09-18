package de.mscho.toftws.entity.calendar.util;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EventRequestValidator.class)
public @interface EventRequestConstraint {
    String message() default "Event request not valid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
