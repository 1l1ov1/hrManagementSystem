package com.wan.commonservice.exception.handle;

import com.wan.commonservice.domain.vo.Result;
import com.wan.commonservice.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalException {

    @ExceptionHandler(CommonException.class)
    public Result exceptionHandle(CommonException exception) {
        log.error("异常信息：{}", exception.getMessage());
        return Result.fail(exception.getCode(), exception.getMessage());
    }
    // TODO 这个地方要修改，因为不能所有异常都返回500的响应码，先这样
    @ExceptionHandler(RuntimeException.class)
    public Result exceptionHandle(RuntimeException exception) {
        log.error("异常信息：{}", exception.getMessage());
        return Result.fail(500, exception.getMessage());
    }
}
