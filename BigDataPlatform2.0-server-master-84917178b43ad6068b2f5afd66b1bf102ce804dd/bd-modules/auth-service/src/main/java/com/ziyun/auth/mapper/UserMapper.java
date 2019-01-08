package com.ziyun.auth.mapper;

import com.ziyun.auth.util.ZyMapper;
import com.ziyun.common.entity.Role;
import com.ziyun.common.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserMapper extends ZyMapper<User> {
    void insertUserAndRole(User entity);

    void insertUserAndDate(User entity);

    void deleteUserAndRoles(User user);

    void deleteUserAndDatas(User user);

    void deleteUsers(User user);

    List<User> listUsers(User user);


    void updateUser(User user);

    List<User> listUsersByRole(Role role);

    int listUserCount(Role role);

    List<User> listUserNotExistRoleid(Role role);

    int listUserNotExistRoleidCount(Role role);

    String selectOrganname(String organCode);

    int listUserSize(User user);


    int saveUser(User entity);

    User findUserByUsername(String username);

    /**
     * 根据角色集合查询所有相关用户
     *
     * @param roleIds
     * @return
     */
    List<Map<String, Object>> selectUserByRoleIds(@Param("roleIds") List<Long> roleIds);

    /**
     * 根据用户ID数组查询所有用户名
     *
     * @param ids
     * @return
     */
    List<User> selectUserByIds(@Param("ids") Long[] ids);

    List<User> selectByParam(User user);

    /**
     * 这个方法今后修改
     *
     * @param user
     * @return
     */
    List<User> selectByParam2(User user);


    int updateUserStatus(User user);

    List<User> selectUserByParam(User user);

    /**
     * 根据创建角色id删除用户
     *
     * @param createRoleId
     * @return
     */
    int deleteUserByCreateRoleId(@Param("createRoleId") Long createRoleId);

    /**
     * 根据条件删除用户角色中间表
     *
     * @return
     */
    int deleteUserRoleByParam(Map<String, Object> params);
}
