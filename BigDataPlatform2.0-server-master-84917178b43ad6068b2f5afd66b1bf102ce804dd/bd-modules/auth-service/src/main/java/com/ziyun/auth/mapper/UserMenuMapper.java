package com.ziyun.auth.mapper;

import com.ziyun.auth.model.UserMenu;
import com.ziyun.auth.util.ZyMapper;
import com.ziyun.common.entity.FuncPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @description: 用户自定义菜单mapper
 * @author: FubiaoLiu
 * @date: 2018/9/25
 */
public interface UserMenuMapper extends ZyMapper<UserMenu> {

    /**
     * 查询用户菜单目录列表
     *
     * @param params
     * @return
     */
    List<UserMenu> menuList(Map<String, Object> params);

    /**
     * (分析报告)自定义菜单树
     *
     * @param params
     * @return
     */
    List<UserMenu> menuTreeOfReport(Map<String, Object> params);

    /**
     * 查询用户菜单列表(结果集映射到Permission)
     *
     * @param params
     * @return
     */
    List<FuncPermission> menuToPermission(Map<String, Object> params);

    /**
     * 根据父级ID查询下一个排序值
     *
     * @param params
     * @return
     */
    int getNextOrderNumByParent(Map<String, Object> params);

    /**
     * 批量更新菜单
     *
     * @param list
     */
    void updateBatch(@Param(value = "list") List<UserMenu> list);

    /**
     * 根据id查询用户自定义菜单
     * @param params
     */
    UserMenu queryUserMenuById(Map<String, Object> params);

    /**
     * 根据名称查询菜单
     * @param params
     * @return
     */
    List<UserMenu> queryMenuByName(Map<String, Object> params);

}