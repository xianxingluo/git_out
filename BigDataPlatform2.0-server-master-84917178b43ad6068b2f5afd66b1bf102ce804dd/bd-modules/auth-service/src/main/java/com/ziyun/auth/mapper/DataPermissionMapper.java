package com.ziyun.auth.mapper;

import com.ziyun.auth.util.ZyMapper;
import com.ziyun.common.entity.DataPermission;
import com.ziyun.common.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @description: 数据权限mapper
 * @author: FubiaoLiu
 * @date: 2018/11/20
 */
public interface DataPermissionMapper extends ZyMapper<DataPermission> {

    /**
     * 查询所有数据权限
     * @param params
     * @return
     */
    List<DataPermission> getAllDataPermission(Map<String,Object> params);

    /**
     * 查询数据权限(可带状态查询条件)
     * @param params
     * @return
     */
    List<DataPermission> getDataPermissionTree(Map<String,Object> params);

    List<DataPermission> listDataPerm(Long userId);

    List<DataPermission> getDataPermissionByRole(Role role);

    /**
     * 根据角色id查询角色数据权限
     * @param entity
     */
    void saveDataPermission(Role entity);

    /**
     * 根据角色id查询角色数据权限id
     * @param role
     * @return
     */
    List<Long> selectRoleDataByRole(Role role);

    /**
     * 根据角色id查询角色数据权限
     * @param role
     * @return
     */
    List<DataPermission> selectDataPermByRole(Role role);

    /**
     * 根据角色id删除角色数据权限关系
     * @param role
     */
    void deleteByRole(Role role);

    /**
     * 根据角色id，权限列表删除角色数据权限关系
     * @param roleId
     * @param dataList
     */
    void deleteRoleData(@Param("roleId") Long roleId, @Param("dataList") List<Long> dataList);

    /**
     * 修改数据权限状态(包括所有子节点)
     * @param permission
     * @return
     */
    int updateStatusIncludeChild(DataPermission permission);

    /**
     * 修改数据权限状态(根据id列表)
     * @param params
     * @return
     */
    int updateStatus(Map<String,Object> params);
}
