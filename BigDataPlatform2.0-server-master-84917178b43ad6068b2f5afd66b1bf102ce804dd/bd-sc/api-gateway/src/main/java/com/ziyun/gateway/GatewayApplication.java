package com.ziyun.gateway;

import com.ziyun.gateway.filter.AuthFilter;
import com.ziyun.gateway.filter.DataPermissionFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

/**
 * @description: 网关启动类
 * @author: FubiaoLiu
 * @date: 2018/11/28
 */
@EnableZuulProxy
@EnableEurekaClient
@SpringBootApplication
@EnableFeignClients
@ComponentScans({
        @ComponentScan("com.ziyun.common.tools")
})
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    @RefreshScope
    @ConfigurationProperties("zuul")
    public ZuulProperties zuulProperties() {
        return new ZuulProperties();
    }

    @Bean
    public AuthFilter authFilter() {
        return new AuthFilter();
    }

    @Bean
    public DataPermissionFilter dataPermissionFilter() {
        return new DataPermissionFilter();
    }
}
