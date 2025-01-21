package com.wan.commonservice.exception;

import com.wan.commonservice.enums.ResponseStatusCodeEnum;

public class AuthenticationException extends CommonException {

    public AuthenticationException(ResponseStatusCodeEnum code, String message) {
        super(message, code);
    }

    public AuthenticationException(String message, ResponseStatusCodeEnum code) {
        super(message, code);
    }

    public AuthenticationException(String message, Throwable cause, ResponseStatusCodeEnum code) {
        super(message, cause, code);
    }

    public AuthenticationException(Throwable cause, ResponseStatusCodeEnum code) {
        super(cause, code);
    }
}
