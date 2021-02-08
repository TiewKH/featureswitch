package com.featureswitch.featureswitch.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AddFailedExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = AddFailedException.class)
    protected ResponseEntity<Object> handleAddFailedException() {
        return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

}
