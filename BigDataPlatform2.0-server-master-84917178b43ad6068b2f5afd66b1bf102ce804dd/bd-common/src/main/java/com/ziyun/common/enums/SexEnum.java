package com.ziyun.common.enums;

/**
 * @author leyangjie
 * @date 2018/12/6 10:11
 * 性别
 */
public enum SexEnum {
    MALE("0", "男"),
    FEMALE("1", "女");
    private String code;
    private String value;

    SexEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
