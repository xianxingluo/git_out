package com.ziyun.academic.enums;

public enum AcademicEarlyEnum {

    TERM_CREDIT("term_credit"),
    AVG_CREDIT_POINT("avg_credit_point"),
    CLASS_RANKING_CHANGE("class_ranking_change"),
    FAIL_COURSE_NUM("fail_course_num"),
    FAIL_REQUIREDCOURSE_SUM("fail_requiredcourse_sum"),
    FAIL_ELECTIVECOURSE_SUM("fail_electivecourse_sum"),
    REBUILD_COURSE_SUM("rebuild_course_sum"),
    CET4SCORE("CET4score"),
    GRADE_EXAMINATION_TIME("grade_examination_time"),
    PASS_GRADEEXAMINATION_SUM("pass_gradeexamination_sum");


    private String earlyType;

    AcademicEarlyEnum(String earlyType) {
        this.earlyType = earlyType;
    }

    public String getEarlyType() {
        return earlyType;
    }
}
