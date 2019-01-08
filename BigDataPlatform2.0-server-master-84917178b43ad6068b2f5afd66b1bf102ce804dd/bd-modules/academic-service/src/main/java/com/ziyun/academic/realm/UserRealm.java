package com.ziyun.academic.realm;


import com.ziyun.academic.entity.SysUser;
import com.ziyun.academic.tools.LoginManager;
import com.ziyun.utils.api.*;
import com.ziyun.utils.cache.RedisUtils;
import com.ziyun.utils.cache.TokenCasheManager;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.codehaus.jackson.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashSet;
import java.util.Set;

/**
 * @description:认证与授权
 * @author: yk.tan
 * @since: 2017/8/31
 * @history:
 */
public class UserRealm extends AuthorizingRealm {
    private static final Logger logger = LoggerFactory
            .getLogger(UserRealm.class);

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 权限管理台：请求地址
     */
    @Value(value = "${authority.management.path}")
    private String authpath;

    // /**
    // * 字符串返回值:通过用户名、密码：加密得到密码
    // * @param email
    // * @param pswd
    // * @return
    // */
    // public static String md5Pswd(String username ,String pswd){
    // pswd = String.format("%s#%s", username,pswd);
    // pswd = MD5Util.MD5(pswd);
    // return pswd;
    // }

    /**
     * 认证
     *
     * @param authcToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken authcToken) throws AuthenticationException {
        String username = (String) authcToken.getPrincipal();
        //logger.info("username:" + username);
        // 密码
        String password = new String((char[]) authcToken.getCredentials());
        //logger.info("password:" + password);
        // password=this.md5Pswd(username, password);

        SysUser user = null;
        user = getUserByname(username, user);
        // 用户信息
        // SysUser user = sysUserService.queryByUserName(token.getUsername());

        if (null == user) {
            throw new AccountException("帐号或密码不正确！");
            // 如果用户的status为禁用。那么就抛出<code>DisabledAccountException</code>
        } else if (SysUser.LONG_0.equals(user.getStatus())) {
            throw new DisabledAccountException("帐号已经禁止登录！");
        } else {
            // 更新登录时间 last login time
            // user.setLastLoginTime(new Date());
            // sysUserService.updateNotCheck(user);
        }
        // // 加密获取
        // String password = UserManager.md5Pswd(user.getUsername(),
        // user.getPassword());
        // 帐号不存在、密码错误
        if (!user.getPassword().equals(password)) {
            throw new AccountException("帐号或密码不正确！");
        }
        //判断该用户是否有登录该平台的权限；如果没有直接返回错误。防止成功登录
        RoleAndPermiss rolePermiss = getRoleAndPermiss(user.getUserId());
        // 根据用户ID查询权限（permission），放入到session里。
        Set<String> permissions = new HashSet<>();
        //只包含菜单，按钮，图表的权限
        Set<String> menus = new HashSet<>();

        if (null != rolePermiss) {
            //判断所有权限是否为空
            if (null != rolePermiss.getPermissions()) {
                permissions.addAll(rolePermiss.getPermissions());
            }
            //判断菜单权限是否为空
            if (null != rolePermiss.getMenus()) {
                menus.addAll(rolePermiss.getMenus());
            }
        }
        if (null == permissions || permissions.size() == 0 || (!permissions.contains("university-big-data-login"))) {
            throw new AccountException("该用户没有登录本系统的权限！");
        }
        //
        String tokenKey = TokenCasheManager.generateToken(user.getUsername(), user.getPassword());
        TokenCasheManager.setUserCache(tokenKey, user);
        TokenCasheManager.setMenusCache(tokenKey, menus);
        TokenCasheManager.setPermissionsCache(tokenKey, permissions);
        //tokenKey作为认证的用户名,后面操作全部都根据tokenKey来查找缓存，权限等
        return new SimpleAuthenticationInfo(tokenKey, password, getName());
    }

    /**
     * 调用权限管理台：根据用户名：获取用户：：用于验证
     *
     * @param username
     * @param user
     * @return
     */
    private SysUser getUserByname(String username, SysUser user) {
        String serviceUrl = com.ziyun.utils.api.ApiUrl.USER_GETUSER;
//		PropertyValue path=new PropertyValue();
        serviceUrl = authpath + serviceUrl;
        BoResult<JsonNode> boResult = RestInvokeHelper.invokeRestApiPost(
                Constant.C_TYPE_JSON, serviceUrl, username);
        JsonNode data = boResult.getData();
        if (data != null) {
            user = JsonParams.formJson(data, SysUser.class);

        }
        return user;
    }

    /**
     * 授权
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(
            PrincipalCollection principalCollection) {
        //if (!SecurityUtils.getSubject().isAuthenticated()) {
        //	doClearCache(principalCollection);
        //	SecurityUtils.getSubject().logout();
        //	return null;
        //}
        /*
         * String userName = (String)
         * principalCollection.fromRealm(getName()).iterator().next(); User user
         * = accountManager.findUserByUserName(userName); if (user != null) {
         * SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(); for
         * (Group group : user.getGroupList()) {
         * info.addStringPermissions(group.getPermissionList()); } return info;
         * } else { return null; }
         */
        //SysUser token = (SysUser) SecurityUtils.getSubject().getPrincipal();
        SysUser user = (SysUser) TokenCasheManager.getUserCache(LoginManager.getToken());
        Long userId = user.getUserId();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        RoleAndPermiss rolePermiss = getRoleAndPermiss(userId);
        // 根据用户ID查询角色（role），放入到Authorization里。
        Set<String> roles = new HashSet<>();
        if (null != rolePermiss && null != rolePermiss.getRoles()) {
            roles.addAll(rolePermiss.getRoles());
        }
        info.setRoles(roles);
        // 根据用户ID查询权限（permission），放入到Authorization里。
        Set<String> permissions = new HashSet<>();
        if (null != rolePermiss && null != rolePermiss.getPermissions()) {
            permissions.addAll(rolePermiss.getPermissions());
        }
        info.setStringPermissions(permissions);
        return info;
    }

    private RoleAndPermiss getRoleAndPermiss(Long userId) {
        RoleAndPermiss rolePermiss = null;
        String serviceUrl = com.ziyun.utils.api.ApiUrl.USER_GET_ROLE_PERMISS;
//		PropertyValue path=new PropertyValue();
        serviceUrl = authpath + serviceUrl;
        BoResult<JsonNode> boResult = RestInvokeHelper.invokeRestApiPost(
                Constant.C_TYPE_JSON, serviceUrl, userId);
        JsonNode data = boResult.getData();
        if (data != null) {
            rolePermiss = JsonParams.formJson(data, RoleAndPermiss.class);
            return rolePermiss;
        }
        return rolePermiss;
    }


    ///**
    // * 清空当前用户权限信息
    // */
    //public void clearCachedAuthorizationInfo() {
    //	PrincipalCollection principalCollection = SecurityUtils.getSubject()
    //			.getPrincipals();
    //	SimplePrincipalCollection principals = new SimplePrincipalCollection(
    //			principalCollection, getName());
    //	super.clearCachedAuthorizationInfo(principals);
    //}

    ///**
    // * 指定principalCollection 清除
    // */
    //public void clearCachedAuthorizationInfo(
    //		PrincipalCollection principalCollection) {
    //	SimplePrincipalCollection principals = new SimplePrincipalCollection(
    //			principalCollection, getName());
    //	super.clearCachedAuthorizationInfo(principals);
    //}
}
