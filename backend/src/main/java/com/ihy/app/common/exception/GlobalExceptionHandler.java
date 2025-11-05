package com.ihy.app.common.exception;

import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<String> handlingRuntimeException(RuntimeException exception){
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(value = ValueInstantiationException.class)
    ResponseEntity<String> handlingValueInstantiationException(ValueInstantiationException exception){
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

}

