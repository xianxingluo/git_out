package com.ziyun.academic.enums;

/**
 * @description:
 * @author: yk.tan
 * @since: 2017/7/13
 * @history:
 */
public enum ExamNaturesEnum {
    NORMAL("正常考试", "正常考试通过"), MAKEUP("补考一", "补考通过"), REBUILD("重修一", "重修通过"), UNPASS("未通过", "未通过");
    private String type;
    private String name;

    ExamNaturesEnum(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
