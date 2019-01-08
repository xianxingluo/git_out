package com.ziyun.auth.exception;

/**
 * Created by Zeng on 2018/4/26.
 */
public class BaseException extends RuntimeException {

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }
}

