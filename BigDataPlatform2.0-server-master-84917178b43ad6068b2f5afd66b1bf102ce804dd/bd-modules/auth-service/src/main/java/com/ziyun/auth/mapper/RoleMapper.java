package com.ziyun.auth.mapper;

import com.ziyun.auth.util.ZyMapper;
import com.ziyun.common.entity.Role;
import com.ziyun.common.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper extends ZyMapper<Role> {
    List<Role> listRoles(Role role);

    void deleteRoles(Role role);

    List<Role> listRolesByName(Role role);

    void addUser(Role role);

    void deleteUser(Role role);

    List<Role> userRelationRole(Long id);

    List<Role> getRoleByUser(User user);

    int listRolesSize(Role role);

    int saveRole(Role entity);

    void updateRole(Role role);

    List<Role> getRolesById(Role role);

    List<Role> findRoleByUserid(Long userId);

    List<Role> findRoleByUser(User user);

    void insertRoleAndDataPermission(Role role);

    void insertRoleAndFuncPermission(Role role);

    /**
     * 根据数据权限集合查询所有相关角色
     * @param ids
     * @return
     */
    List<Long> selectRoleByDataPermIds(@Param("ids")List<Long> ids);
    /**
     * 根据功能权限集合查询所有相关角色
     * @param ids
     * @return
     */
    List<Long> selectRoleByFuncPermIds(@Param("ids")List<Long> ids);

    List<Role> selectByRoleByParam(Role role);

    /**
     * 根据父级角色id查询子角色列表
     * @param parentId
     * @return
     */
    List<Role> selectRoleByParentId(@Param("parentId")Long parentId);

    /**
     * 根据角色id列表查询角色
     *
     * @param ids
     * @return
     */
    List<Role> selectByIds(@Param("ids") Long[] ids);
}
