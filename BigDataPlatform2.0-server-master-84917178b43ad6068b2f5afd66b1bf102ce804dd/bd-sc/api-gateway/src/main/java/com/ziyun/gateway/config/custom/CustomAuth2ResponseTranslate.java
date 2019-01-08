package com.ziyun.gateway.config.custom;

import com.ziyun.common.enums.StatusCodeEnum;
import com.ziyun.gateway.response.SimpleResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;

/**
 * @author leyangjie
 * @date 2018/11/29 14:
 * 自定义异常转换类
 */
@Slf4j
public class CustomAuth2ResponseTranslate implements WebResponseExceptionTranslator {
    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
        SimpleResponse simpleResponse = new SimpleResponse();
        log.error("Auth2异常", e);
        Throwable throwable = e.getCause();
        if (throwable instanceof InvalidTokenException) {
            log.info("token失效:{}", throwable);
            simpleResponse.setStatusCode(StatusCodeEnum.LOGON_FAILURE.getKey());
            simpleResponse.setMessage(StatusCodeEnum.LOGON_FAILURE.getValue());
            return new ResponseEntity(simpleResponse, HttpStatus.OK);
        }
        simpleResponse.setMessage(e.getMessage());
        return new ResponseEntity(simpleResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }
}
