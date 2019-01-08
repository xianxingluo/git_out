package com.ziyun.academic.enums;

/**
 * Created by linxiaojun on 9/14/17.
 */
public enum CommunityPortraitModuleEnum {

    SOURCE_PRIVINCE("1", "来源省份"),
    AGE_DISTRIBUTION("2", "年龄分布"),
    MALEFEMAL_ERATIO("3", "男女比例"),
    COURSE_TYPE("4", "选课类型"),
    SCORE_PASS_RATE("5", "成绩合格率"),
    ACADEMIC_CREDITS("6", "学分绩点"),
    BORROWING_BEHAVIOR("7", "借阅行为"),
    BORROWING_DURATION("8", "借阅时段"),
    BORROWING_BOOK("9", "借阅书籍"),
    SURF_CLASSIFICATION("10", "上网分类"),
    SURF_PERIOD("11", "上网时段"),
    SURF_DURATION("12", "上网时长"),
    CONSUME_CATEGORY("13", "消费类目占比"),
    CONSUME_DURATION("14", "消费时段"),
    CONSUME_MONEY_CHANGE("15", "消费金额变化");

    private String type;
    private String name;

    CommunityPortraitModuleEnum(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public static String getName(String type) {
        String result = "";
        for (CommunityPortraitModuleEnum module : CommunityPortraitModuleEnum.values()) {
            if (module.getType().equals(type)) {
                result = module.getName();
            }
        }
        return result;
    }
}
