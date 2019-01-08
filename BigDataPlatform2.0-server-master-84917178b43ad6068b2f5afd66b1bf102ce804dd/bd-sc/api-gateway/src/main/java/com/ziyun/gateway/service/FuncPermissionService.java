package com.ziyun.gateway.service;

import com.ziyun.common.entity.FuncPermission;
import com.ziyun.gateway.config.FeignConfig;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @description: 功能权限service
 * @author: FubiaoLiu
 * @date: 2018/12/3
 */
@FeignClient(name = "auth-service", configuration = FeignConfig.class)
@RequestMapping("/v2/funcPermission")
public interface FuncPermissionService {

    /**
     * 获取所有有效权限
     *
     * @return
     */
    @RequestMapping(value = "/getAllValidPermissions", method = RequestMethod.POST)
    List<FuncPermission> getAllValidPermissions();

}
