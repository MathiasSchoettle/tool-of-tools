package de.mscho.toftws.mvc;

import de.mscho.toftws.user.service.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Component
@RequiredArgsConstructor
public class UserDtoValidator implements ConstraintValidator<UserDtoValidator.Constraint, UserDto> {

    private final UserService userService;

    @Override
    public void initialize(Constraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UserDto value, ConstraintValidatorContext context) {

        if (userService.usernameExists(value.getUsername())) {
            context.buildConstraintViolationWithTemplate("username exists")
                    .addPropertyNode("username")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @jakarta.validation.Constraint(validatedBy = UserDtoValidator.class)
    public @interface Constraint {
        String message() default "userdto not valid";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }
}
