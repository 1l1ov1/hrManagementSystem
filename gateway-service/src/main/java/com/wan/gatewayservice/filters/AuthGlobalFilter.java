package com.wan.gatewayservice.filters;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.AntPathMatcher;
import com.wan.commonservice.exception.UnauthorizedException;
import com.wan.gatewayservice.constant.AuthConstant;
import com.wan.gatewayservice.domain.po.AuthProperty;
import com.wan.gatewayservice.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
@Component
@RequiredArgsConstructor
public class AuthGlobalFilter implements GlobalFilter, Ordered {


    private final AuthProperty authProperty;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private final JwtUtil jwtUtil;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 得到请求
        ServerHttpRequest request = exchange.getRequest();
        // 先判断是否需要检验token
        if (isExclude(request.getPath().toString())) {
            return chain.filter(exchange);
        }
        // 从头中得到token
        String token = null;
        List<String> headers = request.getHeaders().get(AuthConstant.AUTHORIZATION_HEADER);
        // 如果不空
        if (!CollUtil.isEmpty(headers)) {
            // 赋值token
            token = headers.get(0);
        }
        // 然后解析token
        Long userId = null;
       try {
            userId = jwtUtil.parseToken(token);
       } catch (UnauthorizedException e) {
            // 如果说抛出了异常
           // 结束请求，并回一个响应
           ServerHttpResponse response = exchange.getResponse();
           // 设置响应码
           response.setStatusCode(HttpStatus.UNAUTHORIZED);
           return response.setComplete();
       }
        // 如果说解析成功，就重新放到请求中
        String userIdToString = userId.toString();
        ServerWebExchange build = exchange.mutate().request(
                builder -> builder.header(AuthConstant.AUTHORIZATION_HEADER, userIdToString)
        ).build();
        return chain.filter(build);
    }

    private boolean isExclude(String path) {
        for (String excludePath : authProperty.getExcludePaths()) {
            if (antPathMatcher.match(excludePath, path)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
