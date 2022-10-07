package de.mscho.toftws.util.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor

@RestControllerAdvice
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class GlobalRestControllerExceptionHandler {

    private final Logger logger;

    @ExceptionHandler({UnsatisfiedServletRequestParameterException.class, IllegalArgumentException.class})
    public String handleExceptionByForwardingMessage(Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrorList = bindingResult.getFieldErrors();
        return buildMessageFromFieldErrors(fieldErrorList);
    }

    private String buildMessageFromFieldErrors(List<FieldError> fieldErrors) {
        StringBuilder sb = new StringBuilder();
        for (var fieldError : fieldErrors) {
            sb.append(fieldError.getField()).append(": ").append(fieldError.getDefaultMessage()).append("\n");
        }
        return sb.toString();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public String handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return "The given JSON was not parseable";
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public String handleConstraintViolationException(ConstraintViolationException e) {
        var violations = e.getConstraintViolations();
        return buildConstraintViolationMessage(violations);
    }

    private String buildConstraintViolationMessage(Set<ConstraintViolation<?>> violations) {
        StringBuilder sb = new StringBuilder();
        for( var violation : violations ) {
            sb.append(violation.getMessage()).append("\n");
        }
        return sb.toString();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    private String handleNumberFormatException(MethodArgumentTypeMismatchException e) {

        int parameterIndex = e.getParameter().getParameterIndex();
        String parameterName = e.getName();
        String parameterValue = String.valueOf(e.getValue());
        String requiredType = String.valueOf(e.getRequiredType());

        return String.format("Wrong type for parameter %d (%s) with value '%s'. Required type: %s", parameterIndex, parameterName, parameterValue, requiredType);
    }
}
