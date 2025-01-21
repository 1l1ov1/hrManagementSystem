package com.wan.commonservice.exception;

import com.wan.commonservice.enums.ResponseStatusCodeEnum;

public class ObjectExistedException extends CommonException{

    public ObjectExistedException(String message, ResponseStatusCodeEnum code) {
        super(message, ResponseStatusCodeEnum.OBJECT_IS_EXIST);
    }

    public ObjectExistedException(String message, Throwable cause, ResponseStatusCodeEnum code) {
        super(message, cause, ResponseStatusCodeEnum.OBJECT_IS_EXIST);
    }

    public ObjectExistedException(Throwable cause, ResponseStatusCodeEnum code) {
        super(cause, ResponseStatusCodeEnum.OBJECT_IS_EXIST);
    }

    public ObjectExistedException(String message) {
        super(message, ResponseStatusCodeEnum.OBJECT_IS_EXIST);
    }
}
