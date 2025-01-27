package com.wan.commonservice.config;

import com.wan.commonservice.condition.RedisCondition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

@Configuration
@Conditional(RedisCondition.class)
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        try {
            // 设置序列化器
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
            redisTemplate.setHashKeySerializer(new StringRedisSerializer());
            redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

            // 设置连接工厂
            redisTemplate.setConnectionFactory(redisConnectionFactory);

            // 初始化模板
            redisTemplate.afterPropertiesSet();
        } catch (Exception e) {
            // 记录日志并抛出自定义异常
            throw new RuntimeException("RedisTemplate initialization failed", e);
        }
        return redisTemplate;
    }
}
