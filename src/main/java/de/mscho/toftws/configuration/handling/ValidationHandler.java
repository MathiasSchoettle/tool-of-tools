package de.mscho.toftws.configuration.handling;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

// TODO rework this

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class ValidationHandler {
    private final Logger logger;

    @InitBinder
    private void initBinder(DataBinder binder) {
        binder.initDirectFieldAccess();
    }

//    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        var cause = e.getCause();

        if (cause.getClass().isAssignableFrom(InvalidFormatException.class)) {
            var invalidFormatException = (InvalidFormatException) cause;
            var type = invalidFormatException.getTargetType();

            if (type.isEnum() && !invalidFormatException.getPath().isEmpty()) {
                var fieldName = invalidFormatException.getPath().get(0).getFieldName();
                var value = String.valueOf(invalidFormatException.getValue());
                var allowedValues = Arrays.stream(type.getEnumConstants()).map(Object::toString).collect(Collectors.joining(","));
                var msg = "Field [%s] had value [%s]. Allowed values are [%s]".formatted(fieldName, value, allowedValues);
                return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
            }
        }

        logger.warn("Handled HttpMessageNotReadableException did not map to a result value", e);
        return new ResponseEntity<>("Response not readable", HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        var bindingResult = e.getBindingResult();
        var fieldErrors = new HashMap<String, String>();

        for (var error : bindingResult.getFieldErrors()) {
            String field = error.getField();
            String message = error.getDefaultMessage();
            fieldErrors.put(field, message);
        }

        return new ResponseEntity<>(fieldErrors, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        var violations = e.getConstraintViolations();
        var error = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining("\n"));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
