package de.mscho.toftws.controller;

import de.mscho.toftws.exception.NameFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.time.format.DateTimeParseException;

@ControllerAdvice
public class GlobalCotrollerExceptionHandler {

    @ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="One or more names have an unsupported format")
    @ExceptionHandler(NameFormatException.class)
    public void handleNameFormatException() {
        //
    }

    @ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="One or more names have an unsupported format")
    @ExceptionHandler(DateTimeParseException.class)
    public void handleDateTimeParseException() {
        //
    }

}
