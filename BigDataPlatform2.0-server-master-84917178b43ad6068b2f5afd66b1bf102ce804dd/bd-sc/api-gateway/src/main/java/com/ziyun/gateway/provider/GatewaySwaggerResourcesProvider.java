package com.ziyun.gateway.provider;

import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: swagger2整合
 * @author: FubiaoLiu
 * @date: 2018/9/27
 */
@Component
@Primary
public class GatewaySwaggerResourcesProvider implements SwaggerResourcesProvider {
    private final RouteLocator routeLocator;

    public GatewaySwaggerResourcesProvider(RouteLocator routeLocator) {
        this.routeLocator = routeLocator;
    }

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        resources.add(swaggerResource("权限管理服务", "/auth/v2/api-docs"));
        resources.add(swaggerResource("数据埋点服务", "/tracking/v2/api-docs"));
        resources.add(swaggerResource("毕业报告服务", "/report/v2/api-docs"));
        resources.add(swaggerResource("基础信息服务", "/basic/v2/api-docs"));
        resources.add(swaggerResource("学业特征服务", "/academic/v2/api-docs"));
        resources.add(swaggerResource("消费特征服务", "/consume/v2/api-docs"));
        resources.add(swaggerResource("上网特征服务", "/net/v2/api-docs"));
        resources.add(swaggerResource("宿舍出入服务", "/dormitory/v2/api-docs"));
        resources.add(swaggerResource("借阅特征服务", "/borrow/v2/api-docs"));
        resources.add(swaggerResource("图表库服务", "/chart/v2/api-docs"));
        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion("2.0");
        return swaggerResource;
    }
}