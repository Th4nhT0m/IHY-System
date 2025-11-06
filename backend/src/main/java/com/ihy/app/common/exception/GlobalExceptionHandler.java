package com.ihy.app.common.exception;

import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import com.ihy.app.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
        apiResponse.setMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = ValueInstantiationException.class)
    ResponseEntity<ApiResponse> handlingValueInstantiationException(ValueInstantiationException exception){
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setCode(String.valueOf(HttpStatus.BAD_GATEWAY.value()));
        apiResponse.setMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

}

