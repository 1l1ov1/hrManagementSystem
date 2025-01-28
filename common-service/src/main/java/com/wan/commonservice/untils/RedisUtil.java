package com.wan.commonservice.untils;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

public class RedisUtil {

    private static final long timeout = 1L;
    private static final TimeUnit timeUnit = TimeUnit.HOURS;

    public static void valueSet(RedisTemplate<String, Object> redisTemplate, String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }


    public static void valueSetWithExpire(RedisTemplate<String, Object> redisTemplate, String key, Object value,
                                        long timeout, TimeUnit unit) {
        valueSet(redisTemplate, key, value);
        expire(redisTemplate, key, timeout, unit);
    }

    public static void valueSetWithExpire(RedisTemplate<String, Object> redisTemplate, String key, Object value) {
        valueSet(redisTemplate, key, value);
        expire(redisTemplate, key);
    }

    public static Object valueGet(RedisTemplate<String, Object> redisTemplate, String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public static void delete(RedisTemplate<String, Object> redisTemplate, String key) {
        redisTemplate.delete(key);
    }

    public static boolean hasKey(RedisTemplate<String, Object> redisTemplate, String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public static void setKey(RedisTemplate<String, Object> redisTemplate, String key, String value) {
        redisTemplate.opsForSet().add(key, value);
    }

    public static void setKeyWithExpire(RedisTemplate<String, Object> redisTemplate, String key, String value,
                              long timeout, TimeUnit unit) {
        redisTemplate.opsForSet().add(key, value);
        expire(redisTemplate, key, timeout, unit);
    }

    public static void setKeyWithExpire(RedisTemplate<String, Object> redisTemplate, String key, String value) {
        redisTemplate.opsForSet().add(key, value);
        expire(redisTemplate, key);
    }

    public static void expire(RedisTemplate<String, Object> redisTemplate, String key, long timeout, TimeUnit unit) {
        redisTemplate.expire(key, timeout, unit);
    }

    public static void expire(RedisTemplate<String, Object> redisTemplate, String key) {
        expire(redisTemplate, key, timeout, timeUnit);
    }
}
