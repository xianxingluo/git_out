package com.ziyun.academic.filter;

import com.ziyun.academic.tools.LoggerUtils;
import com.ziyun.utils.cache.CacheConstant;
import com.ziyun.utils.cache.TokenCasheManager;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * <p>
 * 判断登录，已启用
 * <p>
 * <p>
 * <p>
 * <p>
 *
 * @author
 * @version <br/>
 * @email
 */
public class AuthcFilter extends AccessControlFilter {
    final static Class<AuthcFilter> CLASS = AuthcFilter.class;


    // 表示是否允许访问；mappedValue就是[urls]配置中拦截器参数部分，如果允许访问返回true，否则false；
    @Override
    protected boolean isAccessAllowed(ServletRequest request,
                                      ServletResponse response, Object mappedValue) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        //SysUser token = (SysUser) httpServletRequest.getSession().getAttribute("user");
        String token = String.valueOf(httpServletRequest.getParameter(CacheConstant.TOKEN_CACHE));
        boolean isLogined = false;
        if (com.ziyun.utils.common.StringUtils.isNotEmpty(token)) {
            isLogined = TokenCasheManager.isLoginedByCache(token);
            if (isLogined) {//每次登录判断时，刷新缓存失效时间为20分钟。相当于session过期时间是20分钟。
                TokenCasheManager.expireTimeAllLoginData(token);
            }
            //if(!isLogined&&(!isLoginRequest(request, response))){//如果没有缓存key，并且不是登录请求，则是没有登录或者已经登录超时
            //
            //}
        }
        /*super.isAccessAllowed(request, response, mappedValue) || !this.isLoginRequest(request, response) && this.isPermissive(mappedValue)*/
        //null != token表示已经登录，  isLoginRequest()是否是登录请求。登录请求并未配置authc.subject.isAuthenticated()
        if (isLogined || isLoginRequest(request, response)) {// && isEnabled()
            return Boolean.TRUE;
        }
        //if (ShiroFilterUtils.isAjax(request)) {// ajax请求//前后端分离后，全部返回json,不再有页面跳转
        LoggerUtils.debug(getClass(), "当前用户没有登录，并且是Ajax请求！");
        String message = "当前用户没有登录!";
        ShiroFilterUtils.returnJsonResult(response, message, String.valueOf(HttpServletResponse.SC_MULTIPLE_CHOICES));
        //}
        return Boolean.FALSE;

    }

    // 表示当访问拒绝时是否已经处理了；如果返回true表示需要继续处理；如果返回false表示该拦截器实例已经处理了，将直接返回即可。
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
            throws Exception {
        //Subject subject = getSubject(request, response);
        //if (subject.getPrincipal() == null) {
        //    saveRequestAndRedirectToLogin(request, response);
        //}else {
        //    String unauthorizedUrl = ShiroFilterUtils.UNAUTHORIZED;
        //    if (StringUtils.hasText(unauthorizedUrl)) {
        //        WebUtils.issueRedirect(request, response, unauthorizedUrl);
        //    } else {
        //        WebUtils.toHttp(response).sendError(401);
        //    }
        //}

        return Boolean.FALSE;
    }


}
