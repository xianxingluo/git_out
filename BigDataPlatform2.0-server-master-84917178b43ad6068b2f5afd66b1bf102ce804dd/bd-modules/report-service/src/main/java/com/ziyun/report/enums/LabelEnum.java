package com.ziyun.report.enums;

/**
 * @description:毕业生标签
 */
public enum LabelEnum {
    MALE_GOD(1,"男神", "翩翩少年"),
    GODDESS(2,"女神", "秀外慧中"),
    SUPER_SCHOLAR(3,"学霸", "智慧如你"),
    WINNER_OF_LIFE(4,"人生赢家", "出类拔萃"),
    WELL_READ(5,"博览群书", "学富五车"),
    STUDENT_LEADER(6,"学生领袖", "恪尽职守"),
    SCHOOL_ACTIVE_MAN(7,"校园活动达人", "活动达人"),
    INTERNET_ENTHUSIASTS(8,"网络爱好者", "网络达人"),
    INDOORSMAN(9,"宅男", "宅族"),
    FEMALE_OTAKU(10,"宅女", "宅族"),
    NONE_OF_MY_BUSINESS(11,"与世无争", "云淡风轻");
    private int key;
    private String label;
    private String describe;

    LabelEnum(int key,String label, String describe) {
        this.key = key;
        this.label = label;
        this.describe = describe;
    }

    public static LabelEnum get(int key) {
        LabelEnum[] values = LabelEnum.values();
        for (LabelEnum object : values) {
            if (object.key == key) {
                return object;
            }
        }
        return null;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
