package com.ziyun.report.interceptor;

import com.ziyun.report.constant.Constants;
import com.ziyun.report.exception.CommonException;
import com.ziyun.utils.common.MD5Util;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 请求拦截器：对前端签名进行鉴权和有效时长判断
 * <p>
 * Created by  Zeng on 2018/05/29.
 */
public class AuthInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);


    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exc) {
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) {
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String sign = request.getParameter("sign");
        String time = request.getParameter("time");
        String outId = request.getParameter("outid");
        if (!verify(sign, time, outId)) {
            throw new CommonException(Constants.CODE_UNAUTHORIZED, Constants.MSG_UNAUTHORIZED);
        }
        return true;
    }

    /**
     * 验证签名有效性
     *
     * @param sign  签名
     * @param time  请求时间
     * @param outId 学号
     * @return
     */
    private boolean verify(String sign, String time, String outId) {
        if (TextUtils.isEmpty(sign) || TextUtils.isEmpty(time) || TextUtils.isEmpty(outId)) {
            return false;
        }
        //验证签名有效性
        if (!sign.equals(MD5Util.MD5(outId + time + Constants.SECRET_KEY))) {
            return false;
        }
        //验证有效时长
        long requestMill = Long.parseLong(time);
        long currentMill = new Date().getTime();
        if ((currentMill - requestMill) > Constants.SING_VALID_PERIOD) {
            return false;
        }
        return true;
    }
}
