package com.ziyun.academic.vo;


import com.ziyun.academic.entity.EcardRechargeMark;

public class EcardRechargeMarkVo extends EcardRechargeMark {

    /**
     * student_outid是关联查询出的edu_status表的outid学号字段 ：如果mark表没有该学号的记录，通过该字段给新增一个默认的
     */
    private String student_outid;

    private String schoolCode;

    private String facultyCode;

    private String majorCode;

    private String classCode;

    public String getStudent_outid() {
        return student_outid;
    }

    public void setStudent_outid(String student_outid) {
        this.student_outid = student_outid;
    }

    public String getSchoolCode() {
        return schoolCode;
    }

    public void setSchoolCode(String schoolCode) {
        this.schoolCode = schoolCode;
    }

    public String getFacultyCode() {
        return facultyCode;
    }

    public void setFacultyCode(String facultyCode) {
        this.facultyCode = facultyCode;
    }

    public String getMajorCode() {
        return majorCode;
    }

    public void setMajorCode(String majorCode) {
        this.majorCode = majorCode;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

}