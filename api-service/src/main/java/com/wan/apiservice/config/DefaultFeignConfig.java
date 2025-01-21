package com.wan.apiservice.config;

import com.wan.commonservice.constant.UserConstant;
import com.wan.commonservice.untils.UserContext;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;

/**
 * 默认的Feign配置类，提供全局的Feign客户端配置
 */
public class DefaultFeignConfig {

    /**
     * 配置Feign客户端的日志级别
     *
     * @return Logger.Level.FULL 返回完整的日志级别，记录所有调用细节
     */
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    /**
     * 创建一个请求拦截器，用于在发送请求前添加用户信息到请求头
     *
     * @return RequestInterceptor 一个请求拦截器实例，用于拦截和修改请求
     */
    @Bean
    public RequestInterceptor userInfoRequestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                // 从用户上下文中获取当前用户的ID
                Long userId = UserContext.getUserId();
                // 如果用户ID不为空，则将其添加到请求头中
                if (userId != null) {
                    requestTemplate.header(UserConstant.USER_ID, userId.toString());
                }
            }
        };
    }

}
