package com.ziyun.report.exception;

/**
 * 鉴权异常
 * <p>
 * Created by Zeng on 2018/05/29.
 */
public class CommonException extends RuntimeException {
    private Integer code;

    public CommonException(String message) {
        super(message);
    }

    public CommonException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
