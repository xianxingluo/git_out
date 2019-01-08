package com.ziyun.academic.filter;

import com.ziyun.academic.tools.LoggerUtils;
import net.sf.json.JSONObject;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * <p>
 * Shiro Filter 工具类
 * <p>
 * <p>
 * <p>
 * <p>
 * 创建　 　2016年5月27日 　<br/>
 * <p>
 * *******
 * <p>
 *
 * @author
 * @version 1.0, 2016年5月27日 <br/>
 */
public class ShiroFilterUtils {
    final static Class<? extends ShiroFilterUtils> CLAZZ = ShiroFilterUtils.class;
    //登录页面
    static final String LOGIN_URL = "/";
    /*//踢出登录提示
    final static String KICKED_OUT = "/open/kickedOut.shtml";*/
    //没有权限提醒
    final static String UNAUTHORIZED = "/";

    /**
     * 是否是Ajax请求
     *
     * @param servletRequest
     * @return
     */
    public static boolean isAjax(ServletRequest servletRequest) {
        //return "XMLHttpRequest".equalsIgnoreCase(((HttpServletRequest) servletRequest).getHeader("X-Requested-With"));
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        return request.getHeader("accept").indexOf("application/json") > -1
                || (request.getHeader("X-Requested-With") != null
                && request.getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1);
    }

    /**
     * response 输出JSON
     *
     * @param response
     * @param resultMap
     * @throws IOException
     */
    public static void out(ServletResponse response, Map<String, String> resultMap) {

        PrintWriter out = null;
        try {
            response.setContentType("application/json;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(JSONObject.fromObject(resultMap).toString());
        } catch (Exception e) {
            LoggerUtils.fmtError(CLAZZ, e, "输出JSON报错。");
        } finally {
            if (null != out) {
                out.flush();
                out.close();
            }
        }
    }

    /**
     * @param response
     * @param message
     * @param loginStatus 401没权限、300未登录
     */
    public static void returnJsonResult(ServletResponse response, String message, String loginStatus) {
        Map<String, String> resultMap = new HashMap<String, String>();
        resultMap.put("login_status", loginStatus);
        resultMap.put("message", message);//当前用户没有登录！
        resultMap.put("success", "false");
        ShiroFilterUtils.out(response, resultMap);
    }


}
