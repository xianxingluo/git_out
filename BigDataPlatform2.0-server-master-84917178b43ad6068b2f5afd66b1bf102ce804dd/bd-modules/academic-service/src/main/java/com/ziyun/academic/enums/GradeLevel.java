package com.ziyun.academic.enums;

/**
 * @description:
 * @author: yk.tan
 * @since: 2017/5/17
 * @history:
 */
public enum GradeLevel {
    /*FRESHMAN1("1-1", "大一第1学期"),FRESHMAN2("1-2", "大一第2学期"),
    sophomore1("2-1", "大二第1学期"), SOPHOMORE2("2-2", "大二第2学期"),
    JUNIOR1("3-1", "大三第1学期"), JUNIOR2("3-2", "大三第2学期") ,
    SENIOR1("4-1", "大四第1学期"), SENIOR2("4-2", "大四第2学期") ,
    FIVE1("5-1", "大五第1学期"), FIVE2("5-2", "大五第2学期") ,
    SIX1("6-1", "大六第1学期"), SIX2("6-2", "大六第2学期") ,
    SEVEN1("7-1", "大七第1学期"), SEVEN2("7-2", "大七第2学期") ,
    EIGHT1("8-1", "大八第1学期"), EIGHT2("8-2", "大八第2学期") ;*/

/*    FRESHMAN1("1-1", "1-1"),FRESHMAN2("1-2", "1-2"),
    sophomore1("2-1", "2-1"), SOPHOMORE2("2-2", "2-2"),
    JUNIOR1("3-1", "3-1"), JUNIOR2("3-2", "3-2") ,
    SENIOR1("4-1", "4-1"), SENIOR2("4-2", "4-1") ,
    FIVE1("5-1", "5-1"), FIVE2("5-2", "5-2") ,
    SIX1("6-1", "6-1"), SIX2("6-2", "6-2") ,
    SEVEN1("7-1", "7-1"), SEVEN2("7-2", "7-2") ,
    EIGHT1("8-1", "8-1"), EIGHT2("8-2", "8-2") ;*/

    /*FRESHMAN1("1-1", "第一学期"),FRESHMAN2("1-2", "第二学期"),
    sophomore1("2-1", "第三学期"), SOPHOMORE2("2-2", "第四学期"),
    JUNIOR1("3-1", "第五学期"), JUNIOR2("3-2", "第六学期") ,
    SENIOR1("4-1", "第七学期"), SENIOR2("4-2", "第八学期") ,
    FIVE1("5-1", "第九学期"), FIVE2("5-2", "第十学期") ,
    SIX1("6-1", "第十一学期"), SIX2("6-2", "第十二学期") ,
    SEVEN1("7-1", "第十三学期"), SEVEN2("7-2", "第十四学期") ,
    EIGHT1("8-1", "第十五学期"), EIGHT2("8-2", "第十六学期") ;*/

    FRESHMAN1("1", "第一学期"), FRESHMAN2("2", "第二学期"),
    sophomore1("3", "第三学期"), SOPHOMORE2("4", "第四学期"),
    JUNIOR1("5", "第五学期"), JUNIOR2("6", "第六学期"),
    SENIOR1("7", "第七学期"), SENIOR2("8", "第八学期");

    private String type;
    private String name;

    GradeLevel(String type, String name) {
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
        for (GradeLevel gradeLevel : GradeLevel.values()) {
            if (gradeLevel.getType().equals(type)) {
                result = gradeLevel.getName();
            }
        }
        return result;
    }

    public static String getGradename(String type) {
        String result = "";
        for (GradeLevel gradeLevel : GradeLevel.values()) {
            if (gradeLevel.getType().equals(type)) {
                result = gradeLevel.getName();
                break;
            }
        }
        return result;
    }
}
