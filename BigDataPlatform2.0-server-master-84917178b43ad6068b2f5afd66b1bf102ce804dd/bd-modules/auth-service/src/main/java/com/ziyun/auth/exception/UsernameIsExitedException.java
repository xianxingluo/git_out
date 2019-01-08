package com.ziyun.auth.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by Zeng on 2018/4/26.
 */
public class UsernameIsExitedException extends AuthenticationException {

    public UsernameIsExitedException(String msg) {
        super(msg);
    }

    public UsernameIsExitedException(String msg, Throwable t) {
        super(msg, t);
    }
}