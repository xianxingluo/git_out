package com.ziyun.auth.service.impl;

import com.ziyun.auth.constant.Constant;
import com.ziyun.auth.enums.MenuType;
import com.ziyun.auth.mapper.UserMenuMapper;
import com.ziyun.auth.model.UserMenu;
import com.ziyun.auth.service.IUserMenuService;
import com.ziyun.auth.service.IUserService;
import com.ziyun.common.constant.AuthConstant;
import com.ziyun.common.tools.MapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @description: 用户自定义菜单
 * @author: FubiaoLiu
 * @date: 2018/9/19
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserMenuServiceImpl extends BaseService<UserMenu> implements IUserMenuService {

    @Autowired
    private IUserService userService;
    @Autowired
    private UserMenuMapper userMenuMapper;

    /**
     * (新增文件夹)查询用户自定义菜单目录树
     *
     * @param params
     * @return
     */
    @Override
    public List<UserMenu> userMenuTree(Map<String, Object> params) {
        params.put("parentId", Constant.LONG_ZERO);
        List<UserMenu> menuList = userMenuMapper.menuList(params);
        List<UserMenu> resultList = new ArrayList<>();
        UserMenu userMenu = new UserMenu();
        userMenu.setId(Constant.LONG_ZERO);
        userMenu.setName(Constant.TREE_ROOT_NAME);
        if (null != menuList && !menuList.isEmpty()) {
            userMenu.setChildren(menuList);
        }
        resultList.add(userMenu);
        return resultList;
    }

    /**
     * (新增文件)查询用户自定义菜单目录树
     *
     * @param params
     * @return
     */
    @Override
    public List<UserMenu> menuTreeOfFile(Map<String, Object> params) {
        List<UserMenu> menuList = userMenuMapper.menuList(params);
        List<UserMenu> resultList = new ArrayList<>();
        UserMenu userMenu = new UserMenu();
        userMenu.setId(Constant.LONG_ZERO);
        userMenu.setName(Constant.TREE_ROOT_NAME);
        if (null != menuList && !menuList.isEmpty()) {
            userMenu.setChildren(getTree(menuList));
        }
        resultList.add(userMenu);
        return resultList;
    }

    /**
     * (分析报告)查询用户自定义菜单树
     *
     * @param params
     * @return
     */
    @Override
    public List<UserMenu> menuTreeOfReport(Map<String, Object> params) {
        List<UserMenu> menuList = userMenuMapper.menuTreeOfReport(params);
        List<UserMenu> resultList = getTree(menuList);
        if (null == resultList) {
            return new ArrayList<>();
        }

        // 使用菜单搜索时，删除不匹配的树节点
        Object name = params.get(Constant.FIELD_NAME);
        if (null != name) {
            searchForName(resultList, name.toString().toLowerCase());
        }
        return resultList;
    }

    /**
     * 递归遍历树节点，根据name模糊匹配（不区分大小写），删除不匹配的节点（匹配的节点保留其 父节点和所有子节点）
     *
     * @param list
     * @param name
     * @return
     */
    private boolean searchForName(List<UserMenu> list, String name) {
        Iterator<UserMenu> it = list.iterator();
        boolean flag = false;
        while (it.hasNext()) {
            UserMenu p = it.next();
            // 如果匹配，则跳过不处理
            if (p.getName().toLowerCase().contains(name)) {
                flag = true;
                continue;
            }

            if (p.getMenuType() == MenuType.FOLDER.getKey() && null != p.getChildren() && !p.getChildren().isEmpty()) {
                // 如果该节点是目录，并且子节点不为空，则遍历子节点
                if (searchForName(p.getChildren(), name)) {
                    // 子节点存在匹配值，保留节点
                    flag = true;
                } else {
                    // 子节点不存在匹配值，删除节点
                    it.remove();
                }
            } else {
                // 否则删除节点
                it.remove();
            }
        }

        return flag;
    }

    /**
     * 保存自定义菜单
     *
     * @param params
     */
    @Override
    public void save(Map<String, Object> params) {
        UserMenu menu = new UserMenu();
        MapUtil.copyPropertiesInclude(params, menu);
        if (null == params.get(Constant.FIELD_ID)) {
            menu.setType(Constant.INT_ONE);
            if (null != params.get(AuthConstant.LOGON_PARAM_USERNAME)) {
                menu.setUsername(params.get(AuthConstant.LOGON_PARAM_USERNAME).toString());
            }
            if (null != params.get(AuthConstant.LOGON_PARAM_ROLE_ID)) {
                menu.setRoleId(Long.parseLong(params.get(AuthConstant.LOGON_PARAM_ROLE_ID).toString()));
            }
            menu.setDeleted(Constant.INT_ONE);
            menu.setCreateTime(new Date());
            menu.setOrderNum(userMenuMapper.getNextOrderNumByParent(params));
            super.save(menu);
        } else {
            menu.setUpdateTime(new Date());
            super.updateNotNull(menu);
        }
    }

    /**
     * 拖拽修改菜单目录、顺序
     *
     * @param params
     */
    @Override
    public void change(Map<String, Object> params) {
        UserMenu userMenu = super.selectByKey(Long.valueOf(params.get(Constant.FIELD_ID).toString()));
        if (null == userMenu) {
            return;
        }

        // 更换目录
        Long parentId = Long.valueOf(params.get(Constant.FIELD_PARENT_ID).toString());
        if (!parentId.equals(userMenu.getParentId())) {
            userMenu.setParentId(parentId);
            userMenu.setOrderNum(userMenuMapper.getNextOrderNumByParent(params));
            super.updateNotNull(userMenu);
        }

        // 目的排序位置 为空或与原排序位置相同 不做处理
        Object oToSeq = params.get(Constant.FIELD_TO_SEQ);
        if (null == oToSeq) {
            return;
        }
        int toSeq = Integer.parseInt(oToSeq.toString());
        if (toSeq == userMenu.getOrderNum()) {
            return;
        }
        params.put("bOrder", toSeq < userMenu.getOrderNum() ? toSeq : userMenu.getOrderNum() + 1);
        params.put("eOrder", toSeq < userMenu.getOrderNum() ? userMenu.getOrderNum() - 1 : toSeq);

        List<UserMenu> list = userMenuMapper.menuList(params);
        if (null == list || list.isEmpty()) {
            return;
        }
        for (UserMenu menu : list) {
            menu.setOrderNum((toSeq < userMenu.getOrderNum()) ? menu.getOrderNum() + 1 : menu.getOrderNum() - 1);
        }
        userMenu.setOrderNum(toSeq);
        list.add(userMenu);
        userMenuMapper.updateBatch(list);
    }

    /**
     * 根据id查询用户自定义菜单
     *
     * @param params
     * @return
     */
    @Override
    public UserMenu queryUserMenuById(Map<String, Object> params) {
        return userMenuMapper.queryUserMenuById(params);
    }

    /**
     * 根据名称查询菜单
     *
     * @param params
     * @return
     */
    @Override
    public List<UserMenu> queryMenuByName(Map<String, Object> params) {
        return userMenuMapper.queryMenuByName(params);
    }

    /**
     * list转成tree
     *
     * @param list
     * @return
     */
    private List<UserMenu> getTree(List<UserMenu> list) {
        if (null == list || list.isEmpty()) {
            return null;
        }
        return getChildren(Constant.LONG_ZERO, list, Constant.INT_ZERO);
    }

    /**
     * 递归查找子节点
     *
     * @param parentId
     * @param list
     * @return
     */
    private List<UserMenu> getChildren(Long parentId, List<UserMenu> list, int level) {
        level++;
        // 子节点列表
        List<UserMenu> children = new ArrayList<>();

        for (UserMenu p : list) {
            if (parentId.equals(p.getParentId())) {
                p.setLevel(level);
                children.add(p);
            }
        }

        if (children.isEmpty()) {
            return null;
        }

        for (UserMenu p : children) {
            // 如果是文件夹，查找子节点
            if (p.getMenuType() == MenuType.FOLDER.getKey()) {
                p.setChildren(getChildren(p.getId(), list, level));
            }
        }

        return children;
    }


}
