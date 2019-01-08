package com.ziyun.auth.enums;

/**
 * @description: 菜单类型枚举类
 * @author: FubiaoLiu
 * @date: 2018/9/25
 */
public enum MenuType {
    /**
     * 文件夹
     */
    FOLDER(0, "文件夹"),
    /**
     * 文件
     */
    FILE(1, "文件"),
    /**
     * 按钮
     */
    BUTTON(2, "按钮"),
    /**
     * 图表
     */
    CHART(3, "图表");

    private int key;
    private String value;

    MenuType(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static String getValue(int key) {
        String result = "";
        for (MenuType menu : MenuType.values()) {
            if (menu.getKey() == key) {
                result = menu.getValue();
            }
        }
        return result;
    }
}
