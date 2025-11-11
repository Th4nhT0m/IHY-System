package com.ihy.app.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    USER_EXISTS("300", "User already exists"),
    INVALID_INPUT("300", "Invalid data, please try again"),
    INVALID_PASSWORD("401", "Invalid password, please try again"),
    INVALID_MESSAGE_CODE("300", "Invalid message key"),
    SERVER_ERROR("500", "System error, please contact admin"),
    PASS_MINIUM_LIMIT("300","Password must be more than 8 characters")

    ;


    private String code;
    private String message;


}
