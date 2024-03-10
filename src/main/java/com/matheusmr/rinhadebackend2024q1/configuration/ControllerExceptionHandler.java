package com.matheusmr.rinhadebackend2024q1.configuration;

import com.matheusmr.rinhadebackend2024q1.exception.ClientNotFoundException;
import com.matheusmr.rinhadebackend2024q1.exception.DebitLimitExceededException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebInputException;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({ HttpMessageNotReadableException.class, ServerWebInputException.class })
    public ResponseEntity<Object> handleDeserializationErrors(RuntimeException ex) {
        return ResponseEntity.unprocessableEntity().build();
    }

    @ExceptionHandler({ ClientNotFoundException.class })
    public ResponseEntity<Object> handleClientNotFound(RuntimeException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler({ DebitLimitExceededException.class })
    public ResponseEntity<Object> handleDebitLimitExceeded(RuntimeException ex) {
        return ResponseEntity.unprocessableEntity().build();
    }

    @ExceptionHandler({ IllegalArgumentException.class })
    public ResponseEntity<Object> handleInvalidArgument(RuntimeException ex) {
        return ResponseEntity.unprocessableEntity().build();
    }
}
