package de.mscho.toftws.util.validate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ZonedDateBeforeValidator.class)
public @interface ZonedDateBefore {
    String startField();
    String endField();
    boolean inclusive() default true;
    String message() default "Dates are in an invalid order";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
