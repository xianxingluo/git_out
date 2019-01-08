package com.ziyun.academic.filter;

import com.ziyun.academic.tools.LoggerUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * <p>
 * 权限校验 Filter， 已启用
 * <p>
 * <p>
 */
public class PermsFilter extends PermissionsAuthorizationFilter {
    final static Class<PermsFilter> CLASS = PermsFilter.class;

    public PermsFilter() {
        super();
    }

    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
        //原代码逻辑，来自于父类的isAccessAllowed
        Subject subject = this.getSubject(request, response);
        String[] perms = (String[]) ((String[]) mappedValue);
        boolean isPermitted = true;
        if (perms != null && perms.length > 0) {
            if (perms.length == 1) {
                if (!subject.isPermitted(perms[0])) {
                    isPermitted = false;
                }
            } else if (!subject.isPermittedAll(perms)) {
                isPermitted = false;
            }
        }

        //新增逻辑。此逻辑写于onAccessDenied() 亦可。
        if (!isPermitted) {// && ShiroFilterUtils.isAjax(request)前后端分离后，全部返回json,不再有页面跳转
            LoggerUtils.debug(getClass(), "当前用户没有权限，并且是Ajax请求！");
            String message = "当前用户没有权限!";
            ShiroFilterUtils.returnJsonResult(response, message, String.valueOf(HttpServletResponse.SC_UNAUTHORIZED));
        }
        return isPermitted;
    }
}
