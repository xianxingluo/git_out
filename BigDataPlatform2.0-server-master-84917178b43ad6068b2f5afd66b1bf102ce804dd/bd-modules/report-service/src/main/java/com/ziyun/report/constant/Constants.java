package com.ziyun.report.constant;

/**
 * Constant
 * <p>
 * Created by Zeng on 2018/4/23.
 */
public class Constants {

    //--------------------------- 鉴权相关 -----------------------
    //签名秘钥
    public static final String SECRET_KEY = "hWp@ssw0rd!";
    //签名有效时间 20 分钟
    public static final long SING_VALID_PERIOD = 20 * 60 * 1000L;
    //签名验证失败返回code
    public static final Integer CODE_UNAUTHORIZED = 401;
    //签名验证失败返回msg
    public static final String MSG_UNAUTHORIZED = "UnAuthorized";
    //拦截url
    public static final String URL_INTERCEPT = "/api/v2/report/**";
    //不拦截url
    public static final String URL_EXCLUDE_INTERCEPT = "/api/v2/report/isGraduationStudent";

    //--------------------------- 学生信息加密相关 -----------------------
    //第一次加密加盐位数
    public static final int OFFSET_SALT_FIRST = 2;
    //第二次加密加盐位数
    public static final int OFFSET_SALT_SECOND = 3;
    //outid（学号）参数名，替代名称
    public static final String NAME_OUT_ID = "?WX-";

    //--------------------------- Others -----------------------
    //...

}
