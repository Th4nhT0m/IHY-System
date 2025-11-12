package com.ihy.app.common.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {

    USER_EXISTS("300", "User already exists"),
    USER_NOT_EXISTS("300", "User does not exist"),
    INVALID_INPUT("300", "Invalid data, please try again"),
    INVALID_PASSWORD("401", "Invalid password, please try again"),
    INVALID_MESSAGE_CODE("300", "Invalid message key"),
    SERVER_ERROR("500", "System error, please contact admin"),
    PASS_MINIUM_LIMIT("300","Password must be more than 8 characters")

    ;


    String code;
    String message;


}
