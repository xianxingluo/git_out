package com.ziyun.gateway.config.custom;

import com.ziyun.common.enums.StatusCodeEnum;
import com.ziyun.gateway.response.SimpleResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author leyangjie
 * @date 2018/11/29 15:07
 */
@Slf4j
public class CustomAuth2EntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("Spring Securtiy异常", authException);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();
        SimpleResponse simpleResponse = new SimpleResponse();
        simpleResponse.setMessage(StatusCodeEnum.NO_LOGGED.getValue());
        simpleResponse.setStatusCode(StatusCodeEnum.NO_LOGGED.getKey());
    }
}
