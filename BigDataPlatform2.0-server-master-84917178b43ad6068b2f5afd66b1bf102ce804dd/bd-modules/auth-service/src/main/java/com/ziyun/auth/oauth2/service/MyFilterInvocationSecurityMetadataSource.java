package com.ziyun.auth.oauth2.service;

import com.ziyun.auth.constant.AuthConstant;
import com.ziyun.auth.mapper.FuncPermissionMapper;
import com.ziyun.common.constant.Constant;
import com.ziyun.common.entity.FuncPermission;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * @description: 动态获取url权限配置
 * @author: FubiaoLiu
 * @date: 2018/11/24
 */
@Service
public class MyFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    @Autowired
    private FuncPermissionMapper funcPermissionMapper;

    /**
     * 资源权限
     */
    private volatile HashMap<String, Collection<ConfigAttribute>> urlPermMap = null;

    @PostConstruct
    public void init() {
        loadResourceDefine();
    }

    /**
     * 加载资源，初始化资源变量
     */
    public void loadResourceDefine() {
        urlPermMap = new HashMap<>(Constant.INT_FIVE_HUNDRED_AND_TWELVE);
        Collection<ConfigAttribute> array;
        ConfigAttribute cfg;
        List<FuncPermission> permissions = funcPermissionMapper.getAllValidPermissions();
        for (FuncPermission permission : permissions) {
            array = urlPermMap.get(permission.getUrl());
            if (null == array) {
                array = new ArrayList<>();
            }
            cfg = new SecurityConfig(permission.getCode());
            array.add(cfg);
            urlPermMap.put(permission.getUrl(), array);
        }
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        FilterInvocation fi = (FilterInvocation) object;
        String url = fi.getRequestUrl();
        if (url.contains(Constant.QUESTION_MARK)) {
            url = StringUtils.substringBefore(url, Constant.QUESTION_MARK);
        }
        // 获取资源权限接口直接放行（TODO 服务间可调用,后期需改进服务间的权限控制）
        if (AuthConstant.URL_PERM_CLOUD.equals(url)) {
            return null;
        }

        // 资源权限为空，初始化资源
        if (null == urlPermMap) {
            synchronized (MyFilterInvocationSecurityMetadataSource.class) {
                if (null == urlPermMap) {
                    loadResourceDefine();
                }
            }
        }
        Collection<ConfigAttribute> configAttributes = urlPermMap.get(url);
        if (CollectionUtils.isEmpty(configAttributes)) {
            // 未配置相关权限，直接放行
            return null;
        }

        return configAttributes;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
