package de.mscho.toftws.controller;

import de.mscho.toftws.exception.NameFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.format.DateTimeParseException;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="One or more names have an unsupported format")
    @ExceptionHandler(NameFormatException.class)
    public void handleNameFormatException() {
        //
    }

    @ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="One or more dates have an unsupported format")
    @ExceptionHandler(DateTimeParseException.class)
    public void handleDateTimeParseException() {
        //
    }

}
