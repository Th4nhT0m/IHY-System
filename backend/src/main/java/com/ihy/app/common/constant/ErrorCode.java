package com.ihy.app.common.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {

    //Input error
    USER_EXISTS("E1001", "User already exists", HttpStatus.BAD_REQUEST),
    INVALID_INPUT("E1002", "Invalid data, please try again", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD("E1003", "Invalid password, please try again",HttpStatus.BAD_REQUEST),
    INVALID_MESSAGE_CODE("E1004", "Invalid message key", HttpStatus.BAD_REQUEST),
    PASS_MINIUM_LIMIT("E1005", "Password must be more than 8 characters", HttpStatus.BAD_REQUEST),

    USER_NOT_EXISTS("E1006", "User does not exist", HttpStatus.NOT_FOUND),

    UNAUTHENTICATE("E1007", "Unauthenticated user", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED("E1008", "You do not permission", HttpStatus.FORBIDDEN),
    USER_NOT_ACTIVE("E1008", "Current user is invisible", HttpStatus.FORBIDDEN),

    //Server error
    SERVER_ERROR("E1009", "System error, please contact admin", HttpStatus.INTERNAL_SERVER_ERROR),
    ;




    String code;
    String message;
    HttpStatusCode statusCode;


}
