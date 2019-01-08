package com.ziyun.auth.service;

import com.ziyun.auth.model.UserMenu;

import java.util.List;
import java.util.Map;

/**
 * @description: 用户自定义菜单
 * @author: FubiaoLiu
 * @date: 2018/9/19
 */
public interface IUserMenuService extends IService<UserMenu> {
    /**
     * (新增文件夹)查询用户自定义菜单目录树
     * @param params
     * @return
     */
    List<UserMenu> userMenuTree(Map<String, Object> params);
    /**
     * (新增文件)查询用户自定义菜单目录树
     * @param params
     * @return
     */
    List<UserMenu> menuTreeOfFile(Map<String, Object> params);
    /**
     * (分析报告)查询用户自定义菜单树
     * @param params
     * @return
     */
    List<UserMenu> menuTreeOfReport(Map<String, Object> params);

    /**
     * 保存自定义菜单
     * @param params
     */
    void save(Map<String, Object> params);

    /**
     * 拖拽修改菜单目录、顺序
     * @param params
     */
    void change(Map<String, Object> params);

    /**
     * 根据id查询用户自定义菜单
     * @param params
     * @return
     */
    UserMenu queryUserMenuById(Map<String, Object> params);

    /**
     * 根据名称查询菜单
     * @param params
     * @return
     */
    List<UserMenu> queryMenuByName(Map<String, Object> params);

}
