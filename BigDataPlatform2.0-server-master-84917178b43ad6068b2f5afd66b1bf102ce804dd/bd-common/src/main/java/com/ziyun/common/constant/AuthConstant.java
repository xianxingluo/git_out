package com.ziyun.common.constant;

/**
 * @description: 权限常量类
 * @author: FubiaoLiu
 * @date: 2018/9/27
 */
public class AuthConstant {
    /**
     * URL version
     */
    public static final String URL_VERSION = "/v2";
    /**
     * 斜杠
     */
    public static final String SLASH = "/";
    /**
     * 接口BaseUrl地址
     */
    public static final String BASE_URL = "/api" + URL_VERSION;
    /**
     * token加盐
     */
    public static final String SIGNING_KEY = "hw-@Jwt!&Secret^#";
    /**
     * token过期时间
     */
    public static final int TOKEN_EXPIRATION_TIME_ = 60 * 60 * 1000;
    /**
     * 登录用户名参数名
     */
    public static final String PARAM_USERNAME = "username";
    /**
     * 登录密码参数名
     */
    public static final String PARAM_PASSWORD = "password";
    /**
     * 登录接口地址
     */
    public static final String URL_LOGIN = "/auth/authentication/form";
    /**
     * 登出接口地址
     */
    public static final String URL_LOGOUT = "/auth/logout";
    /**
     * 根据用户名查询角色接口地址
     */
    public static final String URL_GET_ROLE_BY_USER = "/auth/role/getRolesByUser";
    /**
     * swagger2 文档路径
     */
    public static final String SWAGGER2_API = "/v2/api-docs";
    /**
     * 角色登录权限，只要登录就能访问的权限
     */
    public static final String AUTH_ROLE_LOGIN = "ROLE_LOGIN";
    /**
     * 角色登录权限，只要登录就能访问的权限
     */
    public static final String AUTH_HTTP_REQUEST_BASIC = "Basic ";


    //---------------------   网关-放行路径  -----------------------

    public static final String[] URL_AUTH_PASS = {
            URL_LOGIN,
            URL_LOGOUT,
            URL_GET_ROLE_BY_USER
    };

    //---------------------   网关-数据权限  -----------------------
    /**
     * 数据权限相关参数数组
     */
    public static final String[] PARAM_DATA_PERMISSION = {"schoolCode", "facultyCode", "majorCode"};

    public static final String SCHOOL_CODE = "schoolCode";
    public static final String FACULTY_CODE = "facultyCode";
    public static final String MAJOR_CODE = "majorCode";
    public static final String CLASS_SELECT = "classSelect";
    public static final String CLASS_CODE = "classCode";
    /**
     * 学历 专科 --3， 本科--1 ,硕士 --2 ,博士 --4
     */
    public static final String EDUCATION = "education";

    public static final String LOGON_PARAM_USERNAME = "logonUsername";
    public static final String LOGON_PARAM_USER_ID = "logonUserId";
    public static final String PARAM_CLASS_CODE = "classCode";
    public static final String LOGON_PARAM_ROLE_ID = "logonRoleId";
    public static final String LOGON_PARAM_IS_SUPER_ADMIN = "logonIsSuperAdmin";

}
