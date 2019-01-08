package com.ziyun.auth.service.impl;

import com.ziyun.auth.mapper.DataPermissionMapper;
import com.ziyun.auth.mapper.FuncPermissionMapper;
import com.ziyun.auth.mapper.RoleMapper;
import com.ziyun.auth.mapper.UserMapper;
import com.ziyun.auth.service.IRoleService;
import com.ziyun.common.constant.Constant;
import com.ziyun.common.entity.DataPermission;
import com.ziyun.common.entity.FuncPermission;
import com.ziyun.common.entity.Role;
import com.ziyun.common.entity.User;
import com.ziyun.common.tools.RedisUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author leyangjie
 * @Description: ${todo}
 * @date 2018/4/26 20:55
 */
@Service
@Transactional
public class RoleServiceImpl extends BaseService<Role> implements IRoleService {
    @Autowired
    private FuncPermissionMapper funcPermissionMapper;
    @Autowired
    private DataPermissionMapper dataPermissionMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private ConsumerTokenServices consumerTokenServices;

    @Override
    public int save(Role entity) {
        try {
            //添加角色:分为三步
            //第一步：添加角色
            roleMapper.saveRole(entity);
            //第二步：添加角色数据权限中间表
            if (entity.getDataPermissionId() != null && entity.getDataPermissionId().length > 0) {
                dataPermissionMapper.saveDataPermission(entity);
            }
            //第三步: 添加角色功能权限中间表
            if (entity.getFuncPermissionId() != null && entity.getFuncPermissionId().length > 0) {
                funcPermissionMapper.saveFuncPermission(entity);
            }
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }

    @Override
    public void deleteRoles(Role role) {
        // 删除角色，分为3步(后面几步去掉，因为角色下有用户时不能删除，角色下无用户时不会有子角色)
        // 1:删除角色数据权限中间表
        dataPermissionMapper.deleteByRole(role);
        // 2:删除角色功能权限中间表
        funcPermissionMapper.deleteByRole(role);
        // 3:根据角色id，删除角色（支持批量删除）
        roleMapper.deleteRoles(role);
        /*// 4:定义List接收子角色中所有需要登出的用户
        List<Map<String, Object>> userRoleMapList = new ArrayList<>();
        // 5:递归删除该角色下所有子角色
        Arrays.asList(role.getIds()).stream().forEach(roleId -> {
            deleteChild(roleId, userRoleMapList);
        });
        // 6:根据角色登出对应登录用户
        logoutUserByRoleIds(Arrays.asList(role.getIds()));
        // 7:登出所有子角色相关用户
        logoutUserByMapList(userRoleMapList);*/
    }

    /**
     * 根据角色id删除该角色下的用户和角色(作废)
     *
     * @param roleId
     * @param userRoleMapList
     */
    @Deprecated
    public void deleteChild(Long roleId, List<Map<String, Object>> userRoleMapList) {
        // 1、根据父角色id查询子角色列表
        List<Role> roleList = roleMapper.selectRoleByParentId(roleId);
        if (CollectionUtils.isEmpty(roleList)) {
            return;
        }
        // 2、定义List存放所有子角色ID，用于查询子角色用户对应关系
        List<Long> roleIds = new ArrayList<>();
        // 3、子角色不为空，删除所有子角色相关信息
        roleList.stream().forEach(role -> {
            roleIds.add(role.getRoleId());
            // 3.1：删除子角色数据权限中间表
            dataPermissionMapper.deleteByRole(role);
            // 3.2：删除子角色功能权限中间表
            funcPermissionMapper.deleteByRole(role);
            // 3.3:根据子角色id，删除子角色（支持批量删除）
            roleMapper.deleteRoles(role);
            // 3.4:删除该子角色下的子角色
            deleteChild(role.getRoleId(), userRoleMapList);
        });
        // 4、查询子角色用户对应关系添加到userRoleMapList
        List<Map<String, Object>> userRoleMap = userMapper.selectUserByRoleIds(roleIds);
        if (CollectionUtils.isNotEmpty(userRoleMap)) {
            userRoleMapList.addAll(userRoleMap);
        }
        // 5、根据子角色id集合删除角色用户中间表
        Map<String, Object> params = new HashMap<>(Constant.INT_TWO);
        params.put(Constant.FIELD_ROLE_IDS, roleIds);
        userMapper.deleteUserRoleByParam(params);

        // 6、删除父角色下的所有用户
        userMapper.deleteUserByCreateRoleId(roleId);
    }

    @Override
    public List<Role> listRoles(Role role) {
        List<Role> roleList = roleMapper.listRoles(role);
        if (CollectionUtils.isNotEmpty(roleList)) {
            //查询角色列表下的用户
            roleList.forEach(r -> {
                Long createUserId = r.getCreateUserId();
                User user = userMapper.selectByPrimaryKey(createUserId);
                r.setCreateUsername(user.getUsername());
                //查询角色下的用户
                List<User> userList = userMapper.listUsersByRole(r);
                if (CollectionUtils.isNotEmpty(userList)) {
                    r.setUsernames(userList.stream().map(b -> b.getUsername()).collect(Collectors.toList()).toArray(new String[]{}));
                }
            });
        }
        return roleList;
    }

    @Override
    public void updateUser(Role role) {
        //根据主键修改角色
        roleMapper.updateByPrimaryKey(role);
    }

    /**
     * 查询角色下的用户列表
     */
    @Override
    public List<User> listUser(Role role) {
        return userMapper.listUsersByRole(role);
    }

    /**
     * 角色下添加用户列表
     */
    @Override
    public void addUser(Role role) {
        roleMapper.addUser(role);
    }

    /**
     * 删除角色下用户列表
     *
     * @param role
     */
    @Override
    public void deleteUser(Role role) {

        roleMapper.deleteUser(role);
    }

    /**
     * 查询角色下的用户列表总长度
     */
    @Override
    public int listUserCount(Role role) {

        return userMapper.listUserCount(role);
    }

    /**
     * 查询当前角色账号不存在的用户列表
     *
     * @param role
     * @return
     */
    @Override
    public List<User> listUserNotExistRoleid(Role role) {
        return userMapper.listUserNotExistRoleid(role);
    }

    /**
     * 查询当前角色账号不存在的用户列表总长度
     *
     * @param role
     * @return
     */
    @Override
    public int listUserNotExistRoleidCount(Role role) {
        return userMapper.listUserNotExistRoleidCount(role);
    }

    /**
     * 查询在该用户，该角色下所创建的角色
     *
     * @param user
     * @return
     */
    @Override
    public List<Role> getRoleByUser(User user) {
        return roleMapper.getRoleByUser(user);
    }

    /**
     * 检查 该角色是否是超级管理员角色
     *
     * @param roleId
     * @return
     */
    @Override
    public boolean checkIsSuperRole(Long roleId) {
        Role role = roleMapper.selectByPrimaryKey(roleId);
        return role.getIsSuperRole() == 1;
    }

    /**
     * 查询角色列表总长度
     *
     * @param role
     * @return
     */
    @Override
    public int listRolesSize(Role role) {
        return roleMapper.listRolesSize(role);
    }

    /**
     * 校验当前用户，当前角色下，添加角色时，角色名是否存在：返回1 表示角色名已经存在
     *
     * @param role
     * @return
     */
    @Override
    public int checkRoleIsExist(Role role) {
        List<Role> roleList = roleMapper.selectByRoleByParam(role);
        return CollectionUtils.isNotEmpty(roleList) ? 1 : 0;
    }

    /**
     * 修改角色
     *
     * @param role
     */
    @Override
    public void updateRole(Role role) {
        // 修改角色分为5步
        // 1：修改角色数据权限中间表，先删除，再插入
        dataPermissionMapper.deleteByRole(role);
        if (ArrayUtils.isNotEmpty(role.getDataPermissionId())) {
            dataPermissionMapper.saveDataPermission(role);
        }
        // 2：修改角色功能权限中间表，先删除，再插入
        funcPermissionMapper.deleteByRole(role);
        if (ArrayUtils.isNotEmpty(role.getFuncPermissionId())) {
            funcPermissionMapper.saveFuncPermission(role);
        }
        if (StringUtils.isNotBlank(role.getRoleName())) {
            // 3：修改角色
            roleMapper.updateRole(role);
        }

        List<Long> roleIds = new ArrayList<>();
        roleIds.add(role.getRoleId());
        // 4：递归修改该角色下的子角色权限列表
        updateChild(role, roleIds, true, true);
        // 5：根据角色id列表登出对应用户
        logoutUserByRoleIds(roleIds);
    }

    /**
     * 递归修改角色下所有子角色权限信息
     *
     * @param role     上级角色
     * @param roleIds  调整的角色id集合
     * @param dataFlag 数据权限是否有变化(true:有;false:无)
     * @param funcFlag 功能权限是否有变化(true:有;false:无)
     */
    public void updateChild(Role role, List<Long> roleIds, boolean dataFlag, boolean funcFlag) {
        // 1、获取子角色列表
        List<Role> roleList = roleMapper.selectRoleByParentId(role.getRoleId());
        if (CollectionUtils.isEmpty(roleList)) {
            return;
        }
        // 2、对比角色权限列表是否包含子角色权限列表，不包含则修改子角色权限列表
        List<Long> dataList = role.getDataPermissionId() == null ? null : Arrays.asList(role.getDataPermissionId());
        List<Long> funcList = role.getFuncPermissionId() == null ? null : Arrays.asList(role.getFuncPermissionId());
        roleList.stream().forEach(r -> {
            boolean flagA = false;
            boolean flagB = false;
            // 数据权限
            if (dataFlag) {
                List<Long> dataPermIdList = dataPermissionMapper.selectRoleDataByRole(r);
                // 角色权限列表为空，删除子角色所有权限
                if (CollectionUtils.isEmpty(dataList)) {
                    if (CollectionUtils.isNotEmpty(dataPermIdList)) {
                        dataPermissionMapper.deleteRoleData(r.getRoleId(), null);
                        flagA = true;
                    }
                } else if (!dataList.containsAll(dataPermIdList)) {
                    // 不包含，删除子角色和上级角色权限列表的差集
                    // 1、获取r下面的所有数据权限，递归生成树
                    List<DataPermission> dataPermissionList = dataPermissionMapper.selectDataPermByRole(r);
                    List<DataPermission> dataPermissionTree = getDataTree(dataPermissionList);
                    // 2、取差集(r中需要删除的角色)
                    List<Long> difference = new ArrayList<>(dataPermIdList);
                    difference.removeAll(dataList);
                    /**
                     * 3、递归遍历权限树，如果节点在差集中则删除(包括该节点的所有子节点)，如果非叶子节点下无子节点则删除(这里children字段为null表示叶子节点)，
                     *    最后返回未被删除的节点。
                     */
                    List<Long> resultList = ergodicDataTree(dataPermissionTree, difference);
                    if (CollectionUtils.isNotEmpty(resultList)) {
                        r.setDataPermissionId(resultList.toArray(new Long[resultList.size()]));
                    }

                    // 4、获取需要删除的节点
                    dataPermIdList.removeAll(resultList);

                    // 5、删除
                    dataPermissionMapper.deleteRoleData(r.getRoleId(), dataPermIdList);

                    flagA = true;
                }
            }

            // 功能权限
            if (funcFlag) {
                List<Long> funcPermIdList = funcPermissionMapper.selectRoleFuncByRole(r);
                // 角色权限列表为空，删除子角色所有权限
                if (CollectionUtils.isEmpty(funcList)) {
                    if (CollectionUtils.isNotEmpty(funcPermIdList)) {
                        funcPermissionMapper.deleteRoleFunc(r.getRoleId(), null);
                        flagB = true;
                    }
                } else if (!funcList.containsAll(funcPermIdList)) {
                    // 不包含，删除子角色和上级角色权限列表的差集
                    // 1、获取r下面的所有功能权限，递归生成树
                    List<FuncPermission> funcPermissionList = funcPermissionMapper.selectFuncPermByRole(r);
                    List<FuncPermission> funcPermissionTree = getFuncTree(funcPermissionList);
                    // 2、取差集(r中需要删除的角色)
                    List<Long> difference = new ArrayList<>(funcPermIdList);
                    difference.removeAll(funcList);
                    /**
                     * 3、递归遍历权限树，如果节点在差集中则删除(包括该节点的所有子节点)，如果非叶子节点下无子节点则删除(这里children字段为null表示叶子节点)，
                     *    最后返回未被删除的节点。
                     */
                    List<Long> resultList = ergodicFuncTree(funcPermissionTree, difference);
                    if (CollectionUtils.isNotEmpty(resultList)) {
                        r.setFuncPermissionId(resultList.toArray(new Long[resultList.size()]));
                    }

                    // 4、获取需要删除的节点
                    funcPermIdList.removeAll(resultList);

                    // 5、删除
                    funcPermissionMapper.deleteRoleFunc(r.getRoleId(), funcPermIdList);

                    flagB = true;
                }
            }

            // 对子角色数据权限或功能权限有影响则继续修改子角色
            if (flagA || flagB) {
                roleIds.add(r.getRoleId());
                updateChild(r, roleIds, flagA, flagB);
            }
        });
    }

    /**
     * list转成tree
     *
     * @param list
     * @return
     */
    private List<DataPermission> getDataTree(List<DataPermission> list) {
        if (null == list || list.isEmpty()) {
            return null;
        }
        return getDataChildren(Constant.LONG_ZERO, list);
    }

    /**
     * 递归查找子节点
     *
     * @param parentId
     * @param list
     * @return
     */
    private List<DataPermission> getDataChildren(Long parentId, List<DataPermission> list) {
        // 子节点列表
        List<DataPermission> children = new ArrayList<>();

        for (DataPermission p : list) {
            if (parentId.equals(p.getParentId())) {
                children.add(p);
            }
        }

        if (children.isEmpty()) {
            return null;
        }

        for (DataPermission p : children) {
            p.setChildren(getDataChildren(p.getId(), list));
        }

        return children;
    }

    /**
     * 遍历权限树，如果节点在差集中则删除(包括该节点的所有子节点)，如果非叶子节点下无子节点则删除(这里children字段为null表示叶子节点)
     *
     * @param treeList
     * @param difference
     * @return 返回子节点中不需要删除的节点
     */
    private List<Long> ergodicDataTree(List<DataPermission> treeList, List<Long> difference) {
        List<Long> resultList = new ArrayList<>();
        if (CollectionUtils.isEmpty(treeList)) {
            return resultList;
        }
        Iterator<DataPermission> it = treeList.iterator();
        while (it.hasNext()) {
            DataPermission p = it.next();
            if (difference.contains(p.getId())) {
                it.remove();
                continue;
            }
            if (CollectionUtils.isNotEmpty(p.getChildren())) {
                resultList.addAll(ergodicDataTree(p.getChildren(), difference));
                if (p.getChildren().size() == 0) {
                    it.remove();
                } else {
                    resultList.add(p.getId());
                }
            } else if (p.getChildren() == null) {
                resultList.add(p.getId());
            }
        }

        return resultList;
    }

    /**
     * list转成tree
     *
     * @param list
     * @return
     */
    private List<FuncPermission> getFuncTree(List<FuncPermission> list) {
        if (null == list || list.isEmpty()) {
            return null;
        }
        return getFuncChildren(Constant.LONG_ZERO, list);
    }

    /**
     * 递归查找子节点
     *
     * @param parentId
     * @param list
     * @return
     */
    private List<FuncPermission> getFuncChildren(Long parentId, List<FuncPermission> list) {
        // 子节点列表
        List<FuncPermission> children = new ArrayList<>();

        for (FuncPermission p : list) {
            if (parentId.equals(p.getParentId())) {
                children.add(p);
            }
        }

        if (children.isEmpty()) {
            return null;
        }

        for (FuncPermission p : children) {
            p.setChildren(getFuncChildren(p.getId(), list));
        }

        return children;
    }

    /**
     * 遍历权限树，如果节点在差集中则删除(包括该节点的所有子节点)，如果非叶子节点下无子节点则删除(这里children字段为null表示叶子节点)
     *
     * @param treeList
     * @param difference
     * @return 返回子节点中不需要删除的节点
     */
    private List<Long> ergodicFuncTree(List<FuncPermission> treeList, List<Long> difference) {
        List<Long> resultList = new ArrayList<>();
        if (CollectionUtils.isEmpty(treeList)) {
            return resultList;
        }
        Iterator<FuncPermission> it = treeList.iterator();
        while (it.hasNext()) {
            FuncPermission p = it.next();
            if (difference.contains(p.getId())) {
                it.remove();
                continue;
            }
            if (CollectionUtils.isNotEmpty(p.getChildren())) {
                resultList.addAll(ergodicFuncTree(p.getChildren(), difference));
                if (p.getChildren().size() == 0) {
                    it.remove();
                } else {
                    resultList.add(p.getId());
                }
            } else if (p.getChildren() == null) {
                resultList.add(p.getId());
            }
        }

        return resultList;
    }

    /**
     * 查询当前用户，当前角色创建的角色，与当前类 listRoles()方法不同的是，当前方法查询包含默认模板
     *
     * @param role
     * @return
     */
    @Override
    public List<Role> getRolesById(Role role) {
        return roleMapper.getRolesById(role);
    }

    /**
     * 根据用户名获取所拥有的角色
     *
     * @param user
     * @return
     */
    @Override
    public List<Role> findRoleByUser(User user) {
        return roleMapper.findRoleByUser(user);
    }

    /**
     * 插入角色权限中间表
     *
     * @param role
     * @return
     */
    @Override
    public int insertRoleAndFunction(Role role) {
        try {
            List<FuncPermission> funcPermissionList = funcPermissionMapper.selectAll();
            List<DataPermission> dataPermissionList = dataPermissionMapper.selectAll();
            role.setFuncPermissionId(funcPermissionList.stream().map(FuncPermission::getId).collect(Collectors.toList()).toArray(new Long[]{}));
            role.setDataPermissionId(dataPermissionList.stream().map(DataPermission::getId).collect(Collectors.toList()).toArray(new Long[]{}));
            roleMapper.insertRoleAndDataPermission(role);
            roleMapper.insertRoleAndFuncPermission(role);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    /**
     * 根据角色id列表登出对应用户
     *
     * @param roleIds
     * @return 登出的用户数量
     */
    @Override
    public int logoutUserByRoleIds(List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return 0;
        }
        // 1、查找各角色下的用户
        List<Map<String, Object>> userRoleMapList = userMapper.selectUserByRoleIds(roleIds);
        // 2、注销对应 角色-用户 中登录的用户（token失效）
        return logoutUserByMapList(userRoleMapList);
    }

    /**
     * 根据角色id列表查询角色
     *
     * @param ids
     * @return
     */
    @Override
    public List<Role> selectByIds(Long[] ids) {
        return roleMapper.selectByIds(ids);
    }

    private int logoutUserByMapList(List<Map<String, Object>> userRoleMapList) {
        int result = 0;
        if (CollectionUtils.isEmpty(userRoleMapList)) {
            return result;
        }
        try {
            Object username;
            Object roleId;
            // 注销对应 角色-用户 中登录的用户（token失效）
            for (Map<String, Object> map : userRoleMapList) {
                username = map.get(Constant.FIELD_USERNAME);
                roleId = map.get(Constant.FIELD_ROLE_ID);
                if (null != username && null != roleId) {
                    String accessToken = redisUtils.getAccessTokenCache(username.toString(), Long.valueOf(roleId.toString()));
                    if (null != accessToken) {
                        consumerTokenServices.revokeToken(accessToken);
                        result++;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /***
     *
     * @param role
     * @return
     */
    @Override
    public List<Role> selectByParam(Role role) {
        return roleMapper.selectByRoleByParam(role);
    }
}
