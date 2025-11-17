package com.ihy.app.common.exception;

import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import com.ihy.app.common.constant.ErrorCode;
import com.ihy.app.common.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
        apiResponse.setMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = ValueInstantiationException.class)
    ResponseEntity<ApiResponse> handlingValueInstantiationException(ValueInstantiationException exception) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setCode(String.valueOf(HttpStatus.BAD_GATEWAY.value()));
        apiResponse.setMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingException(RuntimeException exception) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setCode(String.valueOf(HttpStatus.EXPECTATION_FAILED.value()));
        apiResponse.setMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        String error = exception.getFieldError().getCode();
        ErrorCode errorCode = ErrorCode.INVALID_MESSAGE_CODE;
        try {
            errorCode = ErrorCode.valueOf(error);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }


}

