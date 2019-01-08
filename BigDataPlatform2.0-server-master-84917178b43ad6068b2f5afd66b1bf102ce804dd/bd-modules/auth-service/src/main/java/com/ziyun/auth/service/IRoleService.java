package com.ziyun.auth.service;

import com.ziyun.common.entity.Role;
import com.ziyun.common.entity.User;

import java.util.List;

/**
 * @author leyangjie
 * @Description: ${todo}
 * @date 2018/4/26 20:55
 */
public interface IRoleService extends IService<Role> {
    @Override
    int save(Role role);

    void updateUser(Role role);

    void deleteRoles(Role role);

    List<Role> listRoles(Role role);


    List<User> listUser(Role role);

    void addUser(Role role);

    void deleteUser(Role role);

    int listUserCount(Role role);

    List<User> listUserNotExistRoleid(Role role);

    int listUserNotExistRoleidCount(Role role);

    List<Role> getRoleByUser(User user);

    boolean checkIsSuperRole(Long roleId);

    int listRolesSize(Role role);

    int checkRoleIsExist(Role role);

    void updateRole(Role role);

    List<Role> getRolesById(Role role);

    List<Role> findRoleByUser(User user);


    int insertRoleAndFunction(Role role);

    /**
     * 根据角色id列表登出对应用户
     *
     * @param roleIds
     * @return 登出的用户数量
     */
    int logoutUserByRoleIds(List<Long> roleIds);

    /**
     * 根据角色id列表查询角色
     *
     * @param ids
     * @return
     */
    List<Role> selectByIds(Long[] ids);

    List<Role> selectByParam(Role role);
}
