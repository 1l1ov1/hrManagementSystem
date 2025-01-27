package com.wan.commonservice.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class RedisCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        // 根据实际情况编写条件逻辑
        // 例如，检查配置文件中的某个属性是否存在或具有特定值
        String redisHost = context.getEnvironment().getProperty("redis.host");
        return redisHost != null && !redisHost.trim().isEmpty();
    }
}
