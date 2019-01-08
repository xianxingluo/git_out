package com.ziyun.auth.exception;

/**
 * Token校验异常
 * <p>
 * Created by Zeng on 2018/4/26.
 */
public class TokenException extends BaseException {

    private static final long serialVersionUID = 1L;

    public TokenException(String message) {
        super(message);
    }
}
