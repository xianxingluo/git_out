package com.ziyun.report.handler;

import com.ziyun.report.exception.CommonException;
import com.ziyun.report.response.CommonResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 异常创建对应的处理
 * <p>
 * Created by Zeng on 2018/05/29.
 */
@RestController
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = CommonException.class)
    public CommonResponse commonExceptionHandler(HttpServletRequest req, CommonException e) throws Exception {
        CommonResponse response = new CommonResponse();
        response.setMessage(e.getMessage());
        response.setStatusCode(e.getCode());
        return response;
    }
}

