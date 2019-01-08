package com.ziyun.auth.constant;

/**
 * Constant
 * <p>
 * Created by Zeng on 2018/4/23.
 */
public class AuthConstant {
    //URL version
    public static final String URL_VERSION = "/v2";
    //接口BaseUrl地址
    public static final String BASE_URL = "/api" + URL_VERSION;
    //token加盐
    public static final String SIGNING_KEY = "hw-@Jwt!&Secret^#";
    //token过期时间
    public static final int TOKEN_EXPIRATION_TIME_ = 60 * 60 * 1000;
    //登录用户token参数名
    public static final String PARAM_TOKEN = "token";
    //登录用户名参数名
    public static final String PARAM_USERNAME = "username";
    //登录密码参数名
    public static final String PARAM_PASSWORD = "password";
    //登录接口地址
    public static final String URL_LOGIN = BASE_URL + "/auth/login";
    //登出接口地址
    public static final String URL_LOGOUT = BASE_URL + "/auth/logout";
    //角色登录权限，只要登录就能访问的权限
    public static final String AUTH_ROLE_LOGIN = "ROLE_LOGIN";

    public static final String URL_PERM_CLOUD = URL_VERSION + "/funcPermission/getAllValidPermissions";

}
