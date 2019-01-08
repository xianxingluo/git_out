package com.ziyun.auth.service.impl;

import com.ziyun.auth.enums.MenuType;
import com.ziyun.auth.mapper.FuncPermissionMapper;
import com.ziyun.auth.mapper.RoleMapper;
import com.ziyun.auth.mapper.UserMapper;
import com.ziyun.auth.mapper.UserMenuMapper;
import com.ziyun.auth.service.IFuncPermissionService;
import com.ziyun.auth.service.IRoleService;
import com.ziyun.auth.service.IUserService;
import com.ziyun.common.constant.Constant;
import com.ziyun.common.entity.FuncPermission;
import com.ziyun.common.entity.Role;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 功能权限serviceImpl
 * Created by Zeng on 2018/4/23.
 */
@Service("funcPermissionService")
public class FuncPermissionServiceImpl extends BaseService<FuncPermission> implements IFuncPermissionService {
    @Autowired
    FuncPermissionMapper funcPermissionMapper;
    @Autowired
    private UserMenuMapper userMenuMapper;
    @Autowired
    private IUserService userService;
    @Autowired
    IRoleService roleService;
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    UserMapper userMapper;

    /**
     * 查询所有有效权限
     *
     * @return
     */
    @Override
    public List<FuncPermission> getAllValidPermissions() {
        return funcPermissionMapper.getAllValidPermissions();
    }

    @Override
    public List<FuncPermission> getFuncPermissionTree(Map<String, Object> params) {
        return funcPermissionMapper.getFuncPermissionTree(params);
    }

    @Override
    public int save(FuncPermission entity) {
        entity.setOrderNum(funcPermissionMapper.getNextOrderNumByParentId(entity.getParentId()));
        entity.setCreateTime(new Date());
        return super.save(entity);
    }

    @Override
    public List<FuncPermission> listFuncPermissionByUserId(Long userId) {
        return null;
    }

    /**
     * 查看某个用户所拥有的功能权限
     *
     * @param userId
     * @return
     */
    @Override
    public List<FuncPermission> selectPermissionByUser(Long userId) {
        return funcPermissionMapper.selectPermissionByUser(userId);
    }

    @Override
    public List<FuncPermission> listAllFuncPermissions() {
        return mapper.selectAll();
    }

    /**
     * 查询该角色下的功能权限
     *
     * @param role
     * @return
     */
    @Override
    public List<FuncPermission> getFuncPermissionByRole(Role role) {
        return funcPermissionMapper.getFuncPermissionByRole(role);
    }

    /**
     * 查询模块权限菜单树
     *
     * @param params
     * @return
     */
    @Override
    public List<FuncPermission> getPermissionByModule(Map<String, Object> params) {
        // 获取系统权限列表并生成树
        List<FuncPermission> permissionList = funcPermissionMapper.selectPermissionByParent(params);
        List<FuncPermission> permissionResultList = getTree(permissionList, Long.parseLong(params.get(Constant.FIELD_PARENT_ID).toString()));

        // 获取用户自定义菜单列表并生成树
        List<FuncPermission> menuList = userMenuMapper.menuToPermission(params);
        List<FuncPermission> menuResultList = getTree(menuList, Constant.LONG_ZERO);

        // 合并系统权限树和用户自定义菜单树
        List<FuncPermission> resultList;
        if (null != permissionResultList && !permissionResultList.isEmpty()) {
            resultList = new ArrayList<>();
            // 遍历整合一级子节点中文件夹的子节点(有一级文件夹无实际意义不显示，例如 数据图表 - 默认图表)
            for (FuncPermission fp : permissionResultList) {
                if (fp.getMenuType() == MenuType.FOLDER.getKey() && null != fp.getChildren()) {
                    resultList.addAll(fp.getChildren());
                }
            }

            if (null != menuResultList && !menuResultList.isEmpty()) {
                resultList.addAll(menuResultList);
            }
        } else {
            resultList = menuResultList;
        }

        // 使用菜单搜索时，删除不匹配的树节点
        Object name = params.get(Constant.FIELD_NAME);
        if (null != name) {
            searchForName(resultList, name.toString().toLowerCase());
        }

        return resultList;
    }

    /**
     * 查询图表库权限菜单树
     *
     * @param params
     * @return
     */
    @Override
    public List<FuncPermission> getPermissionOfChart(Map<String, Object> params) {
        // 获取系统权限列表并生成树
        List<FuncPermission> permissionList = funcPermissionMapper.selectPermissionOfChart(params);
        List<FuncPermission> permissionResultList = getTree(permissionList, Long.parseLong(params.get(Constant.FIELD_PARENT_ID).toString()));

        List<FuncPermission> resultList = new ArrayList<>();
        for (FuncPermission p : permissionResultList) {
            if (null != p.getChildren()) {
                resultList.addAll(p.getChildren());
            }
        }

        return resultList;
    }

    /**
     * 修改功能权限状态
     *
     * @param permission
     * @return
     */
    @Override
    public int changeStatus(FuncPermission permission) {
        List<Long> idList = funcPermissionMapper.selectPermByUpdateStatus(permission);
        Map<String, Object> params = new HashMap<>(Constant.INT_FOUR);
        params.put("status", permission.getStatus());
        params.put("roleIds", idList);
        int result = funcPermissionMapper.updateStatus(params);
        // 注销权限相关登录用户
        logoutByDataPerm(idList);
        return result;
    }

    /**
     * list转成tree
     *
     * @param list
     * @return
     */
    private List<FuncPermission> getTree(List<FuncPermission> list, Long parentId) {
        if (null == list || list.isEmpty()) {
            return null;
        }
        return getChildren(parentId, list, Constant.INT_ZERO);
    }

    /**
     * 递归查找子节点
     *
     * @param parentId
     * @param list
     * @return
     */
    private List<FuncPermission> getChildren(Long parentId, List<FuncPermission> list, int level) {
        level++;
        // 子节点列表
        List<FuncPermission> children = new ArrayList<>();

        for (FuncPermission p : list) {
            if (parentId.equals(p.getParentId())) {
                p.setLevel(level);
                children.add(p);
            }
        }

        if (children.isEmpty()) {
            return null;
        }

        for (FuncPermission p : children) {
            // 如果是文件夹，查找子节点
            if (p.getMenuType() == MenuType.FOLDER.getKey()) {
                p.setChildren(getChildren(p.getId(), list, level));
            }
        }

        return children;
    }

    /**
     * 递归遍历树节点，根据name模糊匹配（不区分大小写），删除不匹配的节点（匹配的节点保留其 父节点和所有子节点）
     *
     * @param list
     * @param name
     * @return
     */
    private boolean searchForName(List<FuncPermission> list, String name) {
        Iterator<FuncPermission> it = list.iterator();
        boolean flag = false;
        while (it.hasNext()) {
            FuncPermission p = it.next();
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
     * 注销这些权限相关的登录用户
     *
     * @param ids
     */
    private void logoutByDataPerm(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        // 1、根据权限集合查询所有相关角色
        List<Long> roleIds = roleMapper.selectRoleByFuncPermIds(ids);
        // 2、根据角色id列表登出对应用户
        roleService.logoutUserByRoleIds(roleIds);
    }
}
