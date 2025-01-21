package com.wan.commonservice.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({MybatisPlusInterceptor.class, BaseMapper.class})
public class MyBatisConfig {
    /**
     * 创建并配置MybatisPlus拦截器
     *
     * 此方法通过Spring的@Bean注解定义了一个Bean，使得MybatisPlusInterceptor实例
     * 能够被Spring容器管理和自动注入到需要的地方此外，通过@ConditionalOnMissingBean注解，
     * 只有在Spring容器中没有找到相同类型的Bean时，才会创建这个Bean
     *
     * @return MybatisPlusInterceptor 返回配置好的MybatisPlus拦截器实例
     */
    @Bean
    @ConditionalOnMissingBean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        // 创建MybatisPlus拦截器实例
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 1.分页拦截器
        // 配置分页拦截器，指定数据库类型为MySQL
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        // 设置最大单页限制数量，防止查询过多数据导致内存溢出
        paginationInnerInterceptor.setMaxLimit(1000L);
        // 将分页拦截器添加到MybatisPlus拦截器中
        interceptor.addInnerInterceptor(paginationInnerInterceptor);

        // 返回配置好的MybatisPlus拦截器实例
        return interceptor;
    }
}