package com.wan.gatewayservice.utils;


import cn.hutool.core.convert.NumberWithFormat;
import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.wan.commonservice.constant.UserConstant;
import com.wan.commonservice.exception.UnauthorizedException;
import com.wan.gatewayservice.domain.po.JwtProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtUtil {


    @Autowired
    private  JwtProperty jwtProperty;

    public  String createToken(Long userId) {
        // 创建负载
        Map<String, Object> payload = new HashMap<>();
        // 当前时间
        Date now = new Date();
        // 过期时间
        Date expiresAt = new Date(now.getTime() + jwtProperty.getExpire());
        // 设置jwt的过期时间("exp")的Payload值，这个过期时间必须要大于签发时间
        payload.put(JWTPayload.EXPIRES_AT, expiresAt);
        // 设置jwt的签发时间
        payload.put(JWTPayload.ISSUED_AT, now);
        // 生效时间
        payload.put(JWTPayload.NOT_BEFORE, now);
        // 存放id
        payload.put(UserConstant.USER_ID, userId);
        JWTSigner signer = JWTSignerUtil.hs256(jwtProperty.getSecureKey().getBytes());
        return JWTUtil.createToken(payload, signer);
    }

    public  Long parseToken(String token) {
        // 如果说传递进来的token为null或空
        if (StrUtil.isBlank(token)) {
            throw new UnauthorizedException("未登录");
        }

        try {
            // 解析Jwt
            JWT jwt = JWTUtil.parseToken(token);
            // 验证Jwt
            JWTValidator.of(token)
                    .validateAlgorithm(JWTSignerUtil.hs256(jwtProperty.getSecureKey().getBytes()))
                    .validateDate();

            NumberWithFormat payload = (NumberWithFormat) jwt.getPayload(UserConstant.USER_ID);

            return  payload.longValue();
        } catch (IllegalArgumentException e) {
            throw new UnauthorizedException("无效的token", e);
        } catch (Exception e) {
            throw new UnauthorizedException("token解析失败", e);
        }


    }
}
