package com.wan.commonservice.untils;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;


import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;

import java.util.Set;
import java.util.List;
;

public class RedisUtil {

    private static RedisTemplate<String, Object> redisTemplate;

    public static void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        RedisUtil.redisTemplate = redisTemplate;
    }
    /**
     * 设置键值对，不设置过期时间
     * @param key 键
     * @param value 值
     */
    public static void set(String key, Object value) {
        ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
        valueOps.set(key, value);
    }

    /**
     * 设置键值对，并设置过期时间
     * @param key 键
     * @param value 值
     * @param timeout 过期时间
     * @param unit 时间单位
     */
    public static void set(String key, Object value, long timeout, TimeUnit unit) {
        ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
        valueOps.set(key, value, timeout, unit);
    }

    /**
     * 获取键值对
     * @param key 键
     * @return 对应的值
     */
    public static Object get(String key) {
        ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
        return valueOps.get(key);
    }

    /**
     * 删除键
     * @param key 要删除的键
     */
    public static void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 检查键是否存在
     * @param key 要检查的键
     * @return 如果键存在返回true，否则返回false
     */
    public static boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 设置过期时间
     * @param key 键
     * @param timeout 过期时间
     * @param unit 时间单位
     * @return 如果设置成功返回true，否则返回false
     */
    public static boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 获取过期时间
     * @param key 键
     * @param unit 时间单位
     * @return 过期时间
     */
    public static Long getExpire(String key, TimeUnit unit) {
        return redisTemplate.getExpire(key, unit);
    }

    /**
     * 增加键的值（适用于数值类型）
     * @param key 键
     * @param delta 增加的值
     * @return 增加后的值
     */
    public static Long increment(String key, long delta) {
        ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
        return valueOps.increment(key, delta);
    }

    /**
     * 减少键的值（适用于数值类型）
     * @param key 键
     * @param delta 减少的值
     * @return 减少后的值
     */
    public static Long decrement(String key, long delta) {
        ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
        return valueOps.decrement(key, delta);
    }

    /**
     * 设置 Set
     * @param key 键
     * @param values 值
     */
    public static void setSet(String key, Object... values) {
        SetOperations<String, Object> setOps = redisTemplate.opsForSet();
        setOps.add(key, values);
    }

    /**
     * 获取 Set
     * @param key 键
     * @return Set集合
     */
    public static Set<Object> getSet(String key) {
        SetOperations<String, Object> setOps = redisTemplate.opsForSet();
        return setOps.members(key);
    }

    /**
     * 设置 ZSet
     * @param key 键
     * @param value 值
     * @param score 分数
     */
    public static void setZSet(String key, Object value, double score) {
        ZSetOperations<String, Object> zSetOps = redisTemplate.opsForZSet();
        zSetOps.add(key, value, score);
    }

    /**
     * 获取 ZSet
     * @param key 键
     * @param start 开始位置
     * @param end 结束位置
     * @return ZSet集合
     */
    public static Set<Object> getZSet(String key, long start, long end) {
        ZSetOperations<String, Object> zSetOps = redisTemplate.opsForZSet();
        return zSetOps.range(key, start, end);
    }

    /**
     * 设置 Hash
     * @param key 键
     * @param hashKey Hash的键
     * @param value 值
     */
    public static void setHash(String key, String hashKey, Object value) {
        HashOperations<String, Object, Object> hashOps = redisTemplate.opsForHash();
        hashOps.put(key, hashKey, value);
    }

    /**
     * 获取 Hash
     * @param key 键
     * @param hashKey Hash的键
     * @return 对应的值
     */
    public static Object getHash(String key, String hashKey) {
        HashOperations<String, Object, Object> hashOps = redisTemplate.opsForHash();
        return hashOps.get(key, hashKey);
    }

    /**
     * 设置 List
     * @param key 键
     * @param value 值
     */
    public static void setList(String key, Object value) {
        ListOperations<String, Object> listOps = redisTemplate.opsForList();
        listOps.rightPush(key, value);
    }

    /**
     * 获取 List
     * @param key 键
     * @param start 开始位置
     * @param end 结束位置
     * @return List集合
     */
    public static List<Object> getList(String key, long start, long end) {
        ListOperations<String, Object> listOps = redisTemplate.opsForList();
        return listOps.range(key, start, end);
    }
}
