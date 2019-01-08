package com.ziyun.auth.mapper;

import com.ziyun.auth.util.ZyMapper;
import com.ziyun.common.entity.FuncPermission;
import com.ziyun.common.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @description: 功能权限mapper
 * @author: FubiaoLiu
 * @date: 2018/11/20
 */
public interface FuncPermissionMapper extends ZyMapper<FuncPermission> {

    /**
     * 查询所有功能权限
     *
     * @param params
     * @return
     */
    List<FuncPermission> getFuncPermissionTree(Map<String, Object> params);

    /**
     * 根据上级id查询下一个排序号
     *
     * @param parentId
     * @return
     */
    int getNextOrderNumByParentId(Long parentId);

    List<FuncPermission> selectPermissionByUser(Long userId);

    List<FuncPermission> listFuncPermissions(Long id);

    List<FuncPermission> getFuncPermissionByRole(Role role);

    void saveFuncPermission(Role entity);

    /**
     * 根据角色id查询角色功能权限
     *
     * @param role
     * @return
     */
    List<Long> selectRoleFuncByRole(Role role);

    /**
     * 根据角色id查询角色功能权限
     * @param role
     * @return
     */
    List<FuncPermission> selectFuncPermByRole(Role role);

    void deleteByRole(Role role);

    /**
     * 根据角色id，权限列表删除角色功能权限关系
     * @param roleId
     * @param funcList
     */
    void deleteRoleFunc(@Param("roleId") Long roleId, @Param("funcList") List<Long> funcList);

    /**
     * 根据上级id查询权限菜单列表
     *
     * @param params
     * @return
     */
    List<FuncPermission> selectPermissionByParent(Map<String, Object> params);

    /**
     * 查询图表库权限菜单树
     *
     * @param params
     * @return
     */
    List<FuncPermission> selectPermissionOfChart(Map<String, Object> params);

    /**
     * 修改功能权限状态(包括所有子节点)
     *
     * @param permission
     * @return
     */
    int updateStatusIncludeChild(FuncPermission permission);

    /**
     * 根据父级查询所有需要修改状态的权限(包括所有子节点)，和updateStatusIncludeChild操作的数据一致
     *
     * @param permission
     * @return
     */
    List<Long> selectPermByUpdateStatus(FuncPermission permission);

    /**
     * 根据参数修改权限状态（参数包括：状态、权限ID集合）
     *
     * @param params
     * @return
     */
    int updateStatus(Map<String, Object> params);

    /**
     * 查询所有有效权限
     *
     * @return
     */
    List<FuncPermission> getAllValidPermissions();
}
