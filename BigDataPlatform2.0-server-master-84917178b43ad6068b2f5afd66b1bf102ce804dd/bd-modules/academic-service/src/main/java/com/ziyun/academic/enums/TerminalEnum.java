package com.ziyun.academic.enums;

/**
 * Created by linxiaojun on 11/10/17 6:50 PM.
 */
public enum TerminalEnum {
    PC("0", "pc"),
    MOBILE("1", "mobile");

    private String code;
    private String value;

    TerminalEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    public static String getValue(String code) {
        String result = "";
        for (TerminalEnum terminal : TerminalEnum.values()) {
            if (terminal.getCode().equals(code)) {
                result = terminal.getValue();
            }
        }
        return result;
    }
}
