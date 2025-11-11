package com.ihy.app.common.exception;

import com.ihy.app.common.constant.ErrorCode;
import lombok.Data;


@Data
public class AppException extends RuntimeException{
    private ErrorCode errorCode;

    public AppException(ErrorCode errorCode){
        super();
        this.errorCode = errorCode;
    }

}
