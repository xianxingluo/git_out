package com.ziyun.consume.enums;

/**
 * Created by linxiaojun on 11/10/17 6:50 PM.
 */
public enum WeekdayEnum {
    Monday("1", "周一"),
    Tuesday("2", "周二"),
    Wednesday("3", "周三"),
    Thursday("4", "周四"),
    Friday("5", "周五"),
    Saturday("6", "周六"),
    Sunday("7", "周日"),
    WEEKDAY("8", "周一～周五"),
    WEEKEND("9", "周六～周日");

    private String code;
    private String value;

    WeekdayEnum(String code, String value) {
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
        for (WeekdayEnum weekday : WeekdayEnum.values()) {
            if (weekday.getCode().equals(code)) {
                result = weekday.getValue();
            }
        }
        return result;
    }
}
