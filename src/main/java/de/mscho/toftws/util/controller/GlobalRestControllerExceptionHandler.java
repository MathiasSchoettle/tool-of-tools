package de.mscho.toftws.util.controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

// TODO maybe extend ResponseEntityExceptionHandler? and only override methods that should return something different?

@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalRestControllerExceptionHandler {

    private final Logger logger;

    @InitBinder
    private void initBinder(DataBinder binder) {
        binder.initDirectFieldAccess();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        try {
            throw e.getCause();
        } catch (InvalidFormatException invalidFormatException) {
            var type = invalidFormatException.getTargetType();
            if (type.isEnum() && !invalidFormatException.getPath().isEmpty()) {
                var fieldName = invalidFormatException.getPath().get(0).getFieldName();
                var value = String.valueOf(invalidFormatException.getValue());
                var allowedValues = Arrays.stream(type.getEnumConstants()).map(Object::toString).collect(Collectors.joining(","));
                var msg = MessageFormat.format("Field [{0}] had value [{1}]. Allowed values are [{2}]", fieldName, value, allowedValues);
                return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
            }
        } catch (Throwable ignore) {
            logger.warn("Unhandled exception", e);
            return new ResponseEntity<>("Unknown error", HttpStatus.BAD_REQUEST);
        }

        logger.warn("Handled exception did not result in a return value", e);
        return new ResponseEntity<>("Json not valid", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        var violations = e.getConstraintViolations();
        var error = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining("\n"));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Throwable.class)
    private ResponseEntity<String> defaultHandler(Throwable e) {
        logger.warn("Unhandled Exception: ", e);
        return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
    }
}
