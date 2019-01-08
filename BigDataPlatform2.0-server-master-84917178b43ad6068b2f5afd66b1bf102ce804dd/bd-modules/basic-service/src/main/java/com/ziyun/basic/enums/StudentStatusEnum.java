package com.ziyun.basic.enums;

/**
 * Created by admin on 2018/1/24.
 */
public enum StudentStatusEnum {
    Freshman("1", "新生"),
    NotFreshman("2", "老生"),
    Repeater("3", "留级"),
    Suspension("4", "休学"),
    Graduate("5", "毕业生"),
    NotGraduate("6", "肄业"),
    Other("7", "其他");


    private String code;
    private String value;

    StudentStatusEnum(String code, String value) {
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
        for (StudentStatusEnum student : StudentStatusEnum.values()) {
            if (student.getCode().equals(code)) {
                result = student.getValue();
            }
        }
        return result;
    }
}
