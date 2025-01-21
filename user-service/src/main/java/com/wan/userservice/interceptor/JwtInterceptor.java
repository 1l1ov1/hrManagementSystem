package com.wan.userservice.interceptor;

import com.wan.commonservice.constant.RequestHeaderConstant;
import com.wan.commonservice.untils.UserContext;
import com.wan.userservice.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从请求中得到请求头中的token
        String token = request.getHeader(RequestHeaderConstant.REQUEST_HEADER_TOKEN);
        // 然后校验token
        Long userId = jwtUtil.parseToken(token);
        // 存放到上下文中
        UserContext.setUserId(userId);
        // 放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清除上下文，防止内存泄露
        UserContext.clear();
    }
}
