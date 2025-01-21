package com.wan.commonservice.exception;


import com.wan.commonservice.enums.ResponseStatusCodeEnum;
import lombok.Getter;

@Getter
public class CommonException extends RuntimeException {
    // 响应状态码
    private ResponseStatusCodeEnum code;

    public CommonException(String message, ResponseStatusCodeEnum code) {
        super(message);
        this.code = code;
    }

    public CommonException(String message, Throwable cause, ResponseStatusCodeEnum code) {
        super(message, cause);
        this.code = code;
    }

    public CommonException(Throwable cause, ResponseStatusCodeEnum code) {
        super(cause);
        this.code = code;
    }

}
