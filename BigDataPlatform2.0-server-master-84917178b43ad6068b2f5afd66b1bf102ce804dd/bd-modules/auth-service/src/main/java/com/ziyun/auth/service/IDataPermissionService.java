package com.ziyun.auth.service;

import com.ziyun.common.entity.DataPermission;
import com.ziyun.common.entity.Role;

import java.util.List;
import java.util.Map;

/**
 * @description: 数据权限service
 * @author: FubiaoLiu
 * @date: 2018/11/20
 */
public interface IDataPermissionService extends IService<DataPermission> {

    /**
     * 获取所有数据权限
     * @param params
     * @return
     */
    List<DataPermission> getDataPermissionTree(Map<String, Object> params);

    List<DataPermission> listDataPerm(Long userId);

    List<DataPermission> getDataPermissionByRole(Role role);

    /**
     * 修改数据权限状态
     * @param params
     * @return
     */
    int changeStatus(Map<String, Object> params);
}
