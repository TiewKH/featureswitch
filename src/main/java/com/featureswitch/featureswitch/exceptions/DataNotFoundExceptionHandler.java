package com.featureswitch.featureswitch.exceptions;

import com.featureswitch.featureswitch.model.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class DataNotFoundExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = DataNotFoundException.class)
    protected ResponseEntity<Object> handleDataNotFoundException(DataNotFoundException ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}
