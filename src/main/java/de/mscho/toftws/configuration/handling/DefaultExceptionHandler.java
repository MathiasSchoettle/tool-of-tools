package de.mscho.toftws.configuration.handling;

import de.mscho.toftws.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
@RequiredArgsConstructor
public class DefaultExceptionHandler {

    private final Logger logger;

    @ExceptionHandler(Throwable.class)
    private ResponseEntity<ApiResponse> defaultHandler(Throwable e) {
        logger.warn("Unhandled Exception: ", e);
        var response = ApiResponse.error(e.getMessage(), e.getClass());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
