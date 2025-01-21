package com.wan.commonservice.config;

import com.wan.commonservice.exception.handle.GlobalException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GlobalExceptionConfig {
    // 由于common-service不是启动类，所以不能扫描到这个全局异常捕获
    // 所以要将其注册到容器中
    @Bean
    public GlobalException globalException() {
        return new GlobalException();
    }
}
