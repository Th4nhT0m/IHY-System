package com.ihy.app.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // Null properties do not appear in objects.
public class ApiResponse<T> {
    private String code;
    private String message;
    private T data;
    private LocalDateTime timestamp = LocalDateTime.now();
}

