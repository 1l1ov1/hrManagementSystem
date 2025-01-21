package com.wan.userservice.utils;


import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.wan.commonservice.constant.UserConstant;
import com.wan.commonservice.exception.UnauthorizedException;

import com.wan.userservice.domain.po.JwtProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
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
        // 当前时间 使用 LocalDateTime 和 Date 类型在处理 JWT 的时间戳时可能会导致不一致。
        // JWT 通常期望的时间格式是标准的 Unix 时间戳（以毫秒为单位），
        // 而 LocalDateTime 默认并不提供这种格式。
        Instant now = Instant.now();
        // 过期时间
        Instant expiresAt = now.plusMillis(jwtProperty.getExpire());
        // 设置jwt的过期时间("exp")的Payload值，这个过期时间必须要大于签发时间
        payload.put(JWTPayload.EXPIRES_AT, expiresAt.getEpochSecond()); // 注意：这里使用秒级时间戳
        // 设置jwt的签发时间
        payload.put(JWTPayload.ISSUED_AT, now.getEpochSecond()); // 注意：这里使用秒级时间戳
        // 生效时间
        payload.put(JWTPayload.NOT_BEFORE, now.getEpochSecond()); // 注意：这里使用秒级时间戳
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

            Object payload = jwt.getPayload(UserConstant.USER_ID);
            // 检查payload是否为Long类型
            if (payload instanceof Long) {
                return (Long) payload;
            } else {
                throw new UnauthorizedException("无效的用户Id");
            }
        } catch (IllegalArgumentException e) {
            throw new UnauthorizedException("无效的token", e);
        } catch (Exception e) {
            throw new UnauthorizedException("token解析失败", e);
        }


    }
}
