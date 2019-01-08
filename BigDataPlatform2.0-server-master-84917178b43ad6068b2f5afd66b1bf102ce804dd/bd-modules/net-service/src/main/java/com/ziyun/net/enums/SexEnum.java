package com.ziyun.net.enums;

/**
 * Created by linxiaojun on 11/10/17 6:50 PM.
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

    public String getValue() {
        return value;
    }

    public static String getValue(String code) {
        String result = "";
        for (SexEnum sex : SexEnum.values()) {
            if (sex.getCode().equals(code)) {
                result = sex.getValue();
            }
        }
        return result;
    }
}
