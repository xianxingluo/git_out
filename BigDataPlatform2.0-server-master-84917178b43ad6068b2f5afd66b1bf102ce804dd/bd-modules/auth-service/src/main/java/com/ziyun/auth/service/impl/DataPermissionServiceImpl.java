package com.ziyun.auth.service.impl;

import com.ziyun.auth.mapper.DataPermissionMapper;
import com.ziyun.auth.mapper.RoleMapper;
import com.ziyun.auth.mapper.UserMapper;
import com.ziyun.auth.service.IDataPermissionService;
import com.ziyun.auth.service.IRoleService;
import com.ziyun.common.constant.Constant;
import com.ziyun.common.entity.DataPermission;
import com.ziyun.common.entity.Role;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description: 数据权限service
 * @author: FubiaoLiu
 * @date: 2018/11/20
 */
@Service("dataPermissionService")
public class DataPermissionServiceImpl extends BaseService<DataPermission> implements IDataPermissionService {

    @Autowired
    DataPermissionMapper dataPermissionMapper;
    @Autowired
    IRoleService roleService;
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    UserMapper userMapper;

    /**
     * 获取所有数据权限
     *
     * @param params
     * @return
     */
    @Override
    public List<DataPermission> getDataPermissionTree(Map<String, Object> params) {
        return dataPermissionMapper.getDataPermissionTree(params);
    }

    @Override
    public List<DataPermission> listDataPerm(Long userId) {
        return dataPermissionMapper.listDataPerm(userId);
    }

    /**
     * 获取当前角色下的数据权限
     *
     * @param role
     * @return
     */
    @Override
    public List<DataPermission> getDataPermissionByRole(Role role) {
        return dataPermissionMapper.getDataPermissionByRole(role);
    }

    /**
     * 修改数据权限状态
     *
     * @param params
     * @return
     */
    @Override
    public int changeStatus(Map<String, Object> params) {
        List<DataPermission> permissionList = dataPermissionMapper.getAllDataPermission(params);
        if (null == permissionList || permissionList.isEmpty()) {
            // TODO 此处应该抛出异常
            return 0;
        }
        List<Long> idList = new ArrayList<>();
        Long parentId = Long.parseLong(params.get(Constant.FIELD_ID).toString());
        idList.add(parentId);
        getChildren(parentId, permissionList, idList);
        params.put("ids", StringUtils.join(idList.toArray(), ","));
        int result = dataPermissionMapper.updateStatus(params);
        // 注销权限相关登录用户
        logoutByDataPerm(idList);
        return result;
    }

    /**
     * 递归查找子节点
     *
     * @param parentId
     * @param list
     * @param idList
     */
    private void getChildren(Long parentId, List<DataPermission> list, List<Long> idList) {
        // 子节点列表
        List<Long> children = new ArrayList<>();

        for (DataPermission p : list) {
            if (parentId.equals(p.getParentId())) {
                children.add(p.getId());
            }
        }

        if (children.isEmpty()) {
            return;
        }

        idList.addAll(children);

        for (Long id : children) {
            getChildren(id, list, idList);
        }

        return;
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
        List<Long> roleIds = roleMapper.selectRoleByDataPermIds(ids);
        // 2、根据角色id列表登出对应用户
        roleService.logoutUserByRoleIds(roleIds);
    }
}
