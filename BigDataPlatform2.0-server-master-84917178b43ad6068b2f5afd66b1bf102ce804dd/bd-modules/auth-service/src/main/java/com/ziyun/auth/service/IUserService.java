package com.ziyun.auth.service;

import com.ziyun.common.entity.DataPermission;
import com.ziyun.common.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author leyangjie
 * @Description: ${todo}
 * @date 2018/4/26 15:14
 */
public interface IUserService extends IService<User> {


    void deleteUser(User user);

    List<User> listUsers(User user);

    void updateUser(User user);

    int saveUser(User user);

    User getUser(String username);

    String importUserByExcel(MultipartFile file, String createUserId);

    int queryUserByusername(User user);

    List<DataPermission> listDataPerfuncs();

    /**
     * 获取当前登录用户名
     *
     * @param token
     * @return
     */
    String getUsername(String token);

    /**
     * 设置username到参数列表
     *
     * @param params
     */
    void setParamOfUsername(Map<String, Object> params);

    int listUserSize(User user);

    int updateUserStatus(User user);

    List<User> selectUserByParam(User user);

    int deleteUserUnderRole(User user);
}
