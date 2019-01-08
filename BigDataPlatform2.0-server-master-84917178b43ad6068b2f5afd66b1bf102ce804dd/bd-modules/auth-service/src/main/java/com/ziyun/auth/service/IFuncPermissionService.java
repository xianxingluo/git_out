package com.ziyun.auth.service;

import com.ziyun.common.entity.FuncPermission;
import com.ziyun.common.entity.Role;

import java.util.List;
import java.util.Map;

/**
 * 功能权限service
 * Created by Zeng on 2018/4/25.
 */

public interface IFuncPermissionService extends IService<FuncPermission> {
    /**
     * 查询所有有效权限
     *
     * @return
     */
    List<FuncPermission> getAllValidPermissions();

    /**
     * 获取所有功能权限树
     *
     * @param params
     * @return
     */
    List<FuncPermission> getFuncPermissionTree(Map<String, Object> params);

    /**
     * 获取该用户所拥有的数据权限
     *
     * @return 用户所拥有的数据权限
     */
    List<FuncPermission> listFuncPermissionByUserId(Long userId);


    /**
     * 获取所有功能权限。
     *
     * @return 所有功能权限
     */
    List<FuncPermission> listAllFuncPermissions();

    List<FuncPermission> selectPermissionByUser(Long userId);

    List<FuncPermission> getFuncPermissionByRole(Role role);

    /**
     * 查询模块权限菜单树
     *
     * @param params
     * @return
     */
    List<FuncPermission> getPermissionByModule(Map<String, Object> params);

    /**
     * 查询图表库权限菜单树
     *
     * @param params
     * @return
     */
    List<FuncPermission> getPermissionOfChart(Map<String, Object> params);

    /**
     * 修改功能权限状态
     *
     * @param permission
     * @return
     */
    int changeStatus(FuncPermission permission);
}
