package com.ziyun.tracking.enums;

/**
 * @description: 点击元素类型枚举类
 * @author: FubiaoLiu
 * @date: 2018/9/25
 */
public enum EventType {
    MENU("00", "菜单"),
    BUTTON("01", "按钮"),
    CHART("02", "图"),
    TABLE("03", "表");

    private String code;
    private String value;

    EventType(String code, String value) {
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

    public static String getValue(String code) {
        String result = "";
        for (EventType sex : EventType.values()) {
            if (sex.getCode().equals(code)) {
                result = sex.getValue();
            }
        }
        return result;
    }
}
