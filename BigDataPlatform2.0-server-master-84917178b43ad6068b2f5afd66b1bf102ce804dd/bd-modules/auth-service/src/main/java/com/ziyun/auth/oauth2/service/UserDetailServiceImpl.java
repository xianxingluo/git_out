package com.ziyun.auth.oauth2.service;

import com.ziyun.auth.mapper.*;
import com.ziyun.common.constant.CacheConstant;
import com.ziyun.common.constant.Constant;
import com.ziyun.common.entity.DataPermission;
import com.ziyun.common.entity.FuncPermission;
import com.ziyun.common.entity.Role;
import com.ziyun.common.entity.User;
import com.ziyun.common.enums.StatusCodeEnum;
import com.ziyun.common.tools.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author leyangjie
 * @date 2018/11/19 9:13
 */
@Service
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private FuncPermissionMapper funcPermissionMapper;

    @Autowired
    private DataPermissionMapper dataPermissionMapper;

    @Autowired
    private OrgTreeMapper orgTreeMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //获取用户登录选择的角色
        Long roleId = Long.valueOf(request.getParameter("roleId"));
        // 判断是否是无角色
        if (com.ziyun.auth.constant.Constant.NO_ROLE_ID.equals(roleId)) {
            throw new DisabledException(StatusCodeEnum.ACCOUNT_EXCEPTION.getValue());
        }
        log.info("role= {}", roleId);
        User user = userMapper.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(StatusCodeEnum.USER_NOT_EXIST.getValue());
        }
        //查询用户所拥有的角色
        List<Role> roleList = roleMapper.findRoleByUserid(user.getId());
        if (CollectionUtils.isEmpty(roleList)) {
            throw new RuntimeException(StatusCodeEnum.USERNAME_NO_ROLE.getValue());
        }
        List<Role> collect = roleList.stream().filter(r -> r.getId().equals(roleId)).collect(Collectors.toList());
        Role role = null;
        if (CollectionUtils.isNotEmpty(collect)) {
            role = collect.get(0);
        }
        if (role == null) {
            throw new RuntimeException(StatusCodeEnum.USERNAME_NO_ROLE.getValue());
        }
        //存储权限的set集合
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        //获取角色下的权限
        role.setRoleId(role.getId());

        //功能权限
        List<FuncPermission> funcPermissionList;
        //获取数据权限
        List<DataPermission> dataPermissionByRole;

        // 如果是超级管理员，设置权限为所有有效权限，并标记设置isSuperAdmin为1
        if (role.getIsSuperRole() == 1) {
            Map<String, Object> params = new HashMap<>(Constant.INT_TWO);
            params.put("isSuperAdmin", 1);
            funcPermissionList = funcPermissionMapper.getFuncPermissionTree(params);
            dataPermissionByRole = dataPermissionMapper.getDataPermissionTree(params);
            user.setIsSuperAdmin(Constant.INT_ONE);
        } else {
            funcPermissionList = funcPermissionMapper.getFuncPermissionByRole(role);
            dataPermissionByRole = dataPermissionMapper.getDataPermissionByRole(role);
        }

        if (CollectionUtils.isEmpty(funcPermissionList)
                || CollectionUtils.isEmpty(dataPermissionByRole)) {
            throw new UnapprovedClientAuthenticationException(StatusCodeEnum.UNSUPPORTED.getValue());
        }

        if (CollectionUtils.isNotEmpty(funcPermissionList)) {
            //遍历 funcPermissionList
            for (FuncPermission funcPermission : funcPermissionList) {
                GrantedAuthority authority = new SimpleGrantedAuthority(funcPermission.getCode());
                grantedAuthorities.add(authority);
            }
        }
        user.setDataPermissionList(dataPermissionByRole);
        user.setFuncPermissionsList(funcPermissionList);

        //获取数据权限内的班级列表
        Set<String> dataPermissionSet = new HashSet<>();
        dataPermissionByRole.stream().forEach(perm -> {
            dataPermissionSet.add(perm.getCode());
        });
        Set<String> classSet = orgTreeMapper.selectAllClassCode();
        if (CollectionUtils.isNotEmpty(classSet) && CollectionUtils.isNotEmpty(dataPermissionSet)) {
            classSet.retainAll(dataPermissionSet);
            user.setClassPermissionSet(classSet);
        }
        user.setRoleId(roleId);

        //将用户添加到redis
        redisUtils.setUserCache(user.getUsername(), role.getRoleId(), user, null);
        // 可用性 :true:可用 false:不可用, isDelete字段为0 表示未删除
        boolean enabled = user.getStatus() == 1 ? true : false;
        if (com.ziyun.auth.constant.Constant.NO_ROLE_ID.equals(roleId)) {
            //如果用户选择无角色，账号不可用
            enabled = false;
        }
        // 过期性 :true:没过期 false:过期
        boolean accountNonExpired = true;
        // 有效性 :true:凭证有效 false:凭证无效
        boolean credentialsNonExpired = true;
        // 锁定性 :true:未锁定 false:已锁定
        boolean accountNonLocked = true;

        return new org.springframework.security.core.userdetails.User(user.getUsername() + CacheConstant.DELIMITER_CACHE + role.getRoleId(), passwordEncoder.encode(user.getPassword()), enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, grantedAuthorities);
    }
}
