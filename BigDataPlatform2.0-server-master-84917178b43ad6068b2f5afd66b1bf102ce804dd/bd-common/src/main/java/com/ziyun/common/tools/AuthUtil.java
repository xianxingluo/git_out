package com.ziyun.common.tools;

import com.ziyun.common.constant.Constant;
import org.apache.commons.lang.StringUtils;

import java.util.Base64;

/**
 * @description: 权限工具类
 * @author: FubiaoLiu
 * @date: 2018/9/26
 */
public class AuthUtil {
    public static String getUsername(String token) {
        String key = new String(Base64.getDecoder().decode(token));
        return StringUtils.substringBefore(key, Constant.COLON_CHART);
    }
}
