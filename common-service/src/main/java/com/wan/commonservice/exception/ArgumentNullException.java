package com.wan.commonservice.exception;

import com.wan.commonservice.enums.ResponseStatusCodeEnum;

public class ArgumentNullException extends CommonException{

    public ArgumentNullException() {
        super("", ResponseStatusCodeEnum.ARGUMENT_NULL);
    }

    public ArgumentNullException(String message, ResponseStatusCodeEnum code) {
        super(message, ResponseStatusCodeEnum.ARGUMENT_NULL);
    }

    public ArgumentNullException(String message, Throwable cause, ResponseStatusCodeEnum code) {
        super(message, cause, ResponseStatusCodeEnum.ARGUMENT_NULL);
    }

    public ArgumentNullException(Throwable cause, ResponseStatusCodeEnum code) {
        super(cause, ResponseStatusCodeEnum.ARGUMENT_NULL);
    }

    public ArgumentNullException(String message) {
        super(message, ResponseStatusCodeEnum.ARGUMENT_NULL);
    }
}
