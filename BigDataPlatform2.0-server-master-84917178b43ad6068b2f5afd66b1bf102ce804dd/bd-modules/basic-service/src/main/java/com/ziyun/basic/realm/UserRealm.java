package com.ziyun.basic.realm;

import com.ziyun.basic.entity.SysUser;
import com.ziyun.basic.tools.LoginManager;
import com.ziyun.common.model.auth.DataPermission;
import com.ziyun.common.model.auth.FuncPermission;
import com.ziyun.common.model.auth.User;
import com.ziyun.utils.api.*;
import com.ziyun.utils.cache.TokenCasheManager;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.codehaus.jackson.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @description:认证与授权
 * @author: yk.tan
 * @since: 2017/8/31
 * @history:
 */
@Component
public class UserRealm extends AuthorizingRealm {
    private static final Logger logger = LoggerFactory
            .getLogger(UserRealm.class);


    /**
     * 权限管理台：请求地址
     */
    @Value(value = "${authority.management.path}")
    private String authpath = "http://192.168.101.211:8101";

//    @Value(value = "${authority.management.path}")
//    private String authpath;
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
     * @param authToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken authToken) throws AuthenticationException {
        String username = (String) authToken.getPrincipal();
        String password = new String((char[]) authToken.getCredentials());
        User user = getAuthUserByName(username);
        if (null == user) {
            throw new AccountException("帐号或密码不正确！");
        }
        //账号是否禁用，0：禁用，1：启用
        if (user.getStatus() == 0) {
            throw new AccountException("该用户没有登录本系统的权限！");
        }
        // 帐号不存在、密码错误
        if (!user.getPassword().equals(password)) {
            throw new AccountException("帐号或密码不正确！");
        }
        //判断该用户是否有登录该平台的权限；如果没有直接返回错误。防止成功登录
        List<FuncPermission> funcPermissionList = getAuthFuncPermission(user.getId());
        List<DataPermission> dataPermissionList = getAuthDataPermission(user.getId());

        //功能权限
        Set<String> funcPermissions = new HashSet<>();
        // 数据权限，放入到session里。
        Set<String> dataPermissions = new HashSet<>();

        if (null != funcPermissionList) {
            for (FuncPermission funcPermission : funcPermissionList) {
                funcPermissions.add(funcPermission.getPerms());
            }
        }

        if (null != dataPermissionList) {
            for (DataPermission permission : dataPermissionList) {
                dataPermissions.add(permission.getPerms());
            }
        }

        if (funcPermissions.size() == 0 || !funcPermissions.contains("university-big-data-login")) {
            throw new AccountException("该用户没有登录本系统的权限！");
        }

        String tokenKey = TokenCasheManager.generateToken(user.getUsername(), user.getPassword());
        TokenCasheManager.setUserCache(tokenKey, user);
        TokenCasheManager.setMenusCache(tokenKey, funcPermissions);
        TokenCasheManager.setPermissionsCache(tokenKey, dataPermissions);

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
        String serviceUrl = ApiUrl.USER_GETUSER;
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
     * 调用权限管理台：根据用户名：获取用户：：用于验证
     *
     * @param username
     * @return
     */
    private User getAuthUserByName(String username) {
        String serviceUrl = ApiUrl.URL_AUTH_GET_USER;
        User user = new User();
        serviceUrl = authpath + serviceUrl;
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("username", username);
        BoResult<JsonNode> boResult = RestInvokeHelper.invokeRestApiGet(serviceUrl, paramMap);
        JsonNode data = boResult.getData();
        if (data != null) {
            user = JsonParams.formJson(data, User.class);
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
        User user = (User) TokenCasheManager.getUserCache(LoginManager.getToken());
        Long userId = user.getId();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        List<FuncPermission> funcPermission = getAuthFuncPermission(userId);
        // 数据权限，放入到session里。
        Set<String> dataPermissions = new HashSet<>();
        if (null != funcPermission) {
            for (FuncPermission dataPermission : funcPermission) {
                dataPermissions.add(dataPermission.getPerms());
            }
        }
        // 根据用户ID查询角色（role），放入到Authorization里。
//        Set<String> roles = new HashSet<>();
//        if (null != rolePermiss && null != rolePermiss.getRoles()) {
//            roles.addAll(rolePermiss.getRoles());
//        }
//        info.setRoles(roles);
        info.setStringPermissions(dataPermissions);
        return info;
    }

    private RoleAndPermiss getRoleAndPermiss(Long userId) {
        RoleAndPermiss rolePermiss = null;
        String serviceUrl = ApiUrl.USER_GET_ROLE_PERMISS;
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

    public List<FuncPermission> getAuthFuncPermission(Long userId) {
        List<FuncPermission> permissionList = new ArrayList<>();
        String serviceUrl = ApiUrl.URL_AUTH_GET_FUNC_PERMISSION;
        serviceUrl = authpath + serviceUrl;
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("userId", "" + userId);
        BoResult<JsonNode> boResult = RestInvokeHelper.invokeRestApiGet(
                serviceUrl, paramMap);
        JsonNode data = boResult.getData();
        if (data != null) {
            permissionList = JsonParams.formJson2List(data.toString(), FuncPermission.class);
            return permissionList;
        }
        return permissionList;
    }

    public List<DataPermission> getAuthDataPermission(Long userId) {
        List<DataPermission> permissionList = new ArrayList<>();

        String serviceUrl = ApiUrl.URL_AUTH_GET_DATA_PERMISSION;
        serviceUrl = authpath + serviceUrl;
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("userId", "" + userId);
        BoResult<JsonNode> boResult = RestInvokeHelper.invokeRestApiGet(serviceUrl, paramMap);
        JsonNode data = boResult.getData();
        if (data != null) {
            permissionList = JsonParams.formJson2List(data.toString(), DataPermission.class);
            return permissionList;
        }
        return permissionList;
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
