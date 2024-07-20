package com.demo.M2M_Actor_Movie.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleNoRecordsFoundException(CustomException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(),HttpStatus.NOT_ACCEPTABLE);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
