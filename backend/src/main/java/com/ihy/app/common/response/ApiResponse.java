package com.ihy.app.common.response;

import lombok.Data;

@Data
public class ApiResponse<T> {
    private String code;
    private String message;
    private T data;
}

