package com.wan.commonservice.exception;

import com.wan.commonservice.enums.ResponseStatusCodeEnum;

public class UnauthorizedException extends CommonException {

    public UnauthorizedException() {
        super("", ResponseStatusCodeEnum.UNAUTHORIZED);
    }

    public UnauthorizedException(String message) {
        super(message, ResponseStatusCodeEnum.UNAUTHORIZED);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause, ResponseStatusCodeEnum.UNAUTHORIZED);
    }

    public UnauthorizedException(Throwable cause) {
        super(cause, ResponseStatusCodeEnum.UNAUTHORIZED);
    }
}
