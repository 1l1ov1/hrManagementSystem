package com.wan.departmentservice;

import com.wan.apiservice.config.DefaultFeignConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
// 启用 Feign 客户端支持。扫描 com.wan.apiservice.client 包及其子包中的 Feign 客户端接口。
// 应用 DefaultFeignConfig 类中的默认配置到所有扫描到的 Feign 客户端。
@EnableFeignClients(basePackages = "com.wan.apiservice.client", defaultConfiguration = DefaultFeignConfig.class)
public class DepartmentServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DepartmentServiceApplication.class, args);
    }

}
