package com.ziyun.auth.oauth2.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ziyun.common.enums.StatusCodeEnum;
import com.ziyun.common.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author leyangjie
 * @date 2018/11/19 15:05
 */
@Component
@Slf4j
public class MyAuthenticationFailureHandle implements AuthenticationFailureHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        CommonResponse commonResponse = new CommonResponse();

        // 错误信息处理
        if (exception instanceof UsernameNotFoundException) {
            // 用户不存在
            commonResponse.setStatusCode(StatusCodeEnum.USER_NOT_EXIST.getKey());
            commonResponse.setMessage(StatusCodeEnum.USER_NOT_EXIST.getValue());
        } else if (exception instanceof DisabledException) {
            // 账号异常(禁用)
            commonResponse.setStatusCode(StatusCodeEnum.ACCOUNT_EXCEPTION.getKey());
            commonResponse.setMessage(StatusCodeEnum.ACCOUNT_EXCEPTION.getValue());
        } else if (exception instanceof AccountExpiredException) {
            // 账号已过期(赞无此项)
            commonResponse.setStatusCode(StatusCodeEnum.ACCOUNT_EXPIRE.getKey());
            commonResponse.setMessage(StatusCodeEnum.ACCOUNT_EXPIRE.getValue());
        } else if (exception instanceof BadCredentialsException) {
            // 用户名或密码错误
            commonResponse.setStatusCode(StatusCodeEnum.USERNAME_OR_PWD_WRONG.getKey());
            commonResponse.setMessage(StatusCodeEnum.USERNAME_OR_PWD_WRONG.getValue());
        } else if (exception instanceof UnapprovedClientAuthenticationException) {
            // 未经授权的
            commonResponse.setStatusCode(StatusCodeEnum.UNAUTHORIZED.getKey());
            commonResponse.setMessage(StatusCodeEnum.UNAUTHORIZED.getValue());
        } else if (exception instanceof InternalAuthenticationServiceException) {
            // 无权限
            //自定义异常处理
            commonResponse.setStatusCode(StatusCodeEnum.UNSUPPORTED.getKey());
            commonResponse.setMessage(exception.getMessage());
        }
        writer.write(objectMapper.writeValueAsString(commonResponse));
    }
}
