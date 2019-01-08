package com.ziyun.academic.tools;

import com.alibaba.druid.support.json.JSONUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: yk.tan
 * @since: 2017/11/20
 * @history:
 */
public class IntegratedProcessUtil {
    /**
     * 统一处理请求
     * url,ajax
     *
     * @param request
     * @param response
     */
    public static void dealNoPerssion(HttpServletRequest request, HttpServletResponse response) {
        // 判断是否ajax请求
        //if (!ShiroFilterUtils.isAjax(request)) {//前后端分离后，全部返回json,不再有页面跳转
        //    // 如果不是ajax，请求重定向到根目录。
        //    Map<String, Object> map = new HashMap<String, Object>();
        //    try {
        //        response.sendRedirect(request.getContextPath() + "/");
        //    } catch (IOException e) {
        //        e.printStackTrace();
        //    }
        //} else {
        // 如果是ajax请求，JSON格式返回
        try {
            response.setContentType("application/json;charset=UTF-8");
            //这句话的意思，是告诉servlet用UTF-8转码，而不是用默认的ISO8859
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("success", "false");
            map.put("message", "没有数据权限");
            map.put("login_status", String.valueOf(HttpServletResponse.SC_UNAUTHORIZED));
            writer.write(JSONUtils.toJSONString(map));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //}
    }

    /**
     * 统一处理请求
     * url,ajax
     *
     * @param request
     * @param response
     */
    public static void dealNotLogin(HttpServletRequest request, HttpServletResponse response) {
        // 判断是否ajax请求
        //if (!ShiroFilterUtils.isAjax(request)) {//前后端分离后，全部返回json,不再有页面跳转
        //    // 如果不是ajax，请求重定向到根目录。
        //    Map<String, Object> map = new HashMap<String, Object>();
        //    try {
        //        response.sendRedirect(request.getContextPath() + "/");
        //    } catch (IOException e) {
        //        e.printStackTrace();
        //    }
        //} else {
        // 如果是ajax请求，JSON格式返回
        try {
            response.setContentType("application/json;charset=UTF-8");
            //这句话的意思，是告诉servlet用UTF-8转码，而不是用默认的ISO8859
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("success", "false");
            map.put("message", "当前用户没有登录!");
            map.put("login_status", String.valueOf(HttpServletResponse.SC_MULTIPLE_CHOICES));
            writer.write(JSONUtils.toJSONString(map));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //}
    }


    /**
     * 处理ajax
     *
     * @param request
     * @param response
     */
    public static void dealNoPerssionAjax(HttpServletRequest request, HttpServletResponse response) {
        // 判断是否ajax请求
        //if (!ShiroFilterUtils.isAjax(request)) {//前后端分离后，全部返回json,不再有页面跳转
        //    // 如果不是ajax，
        //    //do nothing
        //} else {
        // 如果是ajax请求，JSON格式返回
        try {
            response.setContentType("application/json;charset=UTF-8");
            //这句话的意思，是告诉servlet用UTF-8转码，而不是用默认的ISO8859
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("success", "false");
            map.put("message", "没有数据权限");
            map.put("login_status", String.valueOf(HttpServletResponse.SC_UNAUTHORIZED));
            writer.write(JSONUtils.toJSONString(map));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //}
    }
}
