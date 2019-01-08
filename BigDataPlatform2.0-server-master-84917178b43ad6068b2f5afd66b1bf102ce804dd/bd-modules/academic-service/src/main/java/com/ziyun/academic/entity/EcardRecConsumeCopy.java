package com.ziyun.academic.entity;

public class EcardRecConsumeCopy extends EcardRecConsumeCopyKey {
    private String ecode;

    private Long sourcetable;//数据抽取来源（0主表，1补助表）

    private String outid;

    private Long cardsn;

    private Long opcount;

    private Long oddfare;

    private Long opfare;

    private Long sumfare;

    private Long acccode;

    private String schoolCode;

    private String facultyCode;

    private String majorCode;

    private String classCode;

    private String sex;

    public String getEcode() {
        return ecode;
    }

    public void setEcode(String ecode) {
        this.ecode = ecode;
    }

    public String getOutid() {
        return outid;
    }

    public void setOutid(String outid) {
        this.outid = outid;
    }

    public Long getOpcount() {
        return opcount;
    }

    public void setOpcount(Long opcount) {
        this.opcount = opcount;
    }

    public Long getOddfare() {
        return oddfare;
    }

    public void setOddfare(Long oddfare) {
        this.oddfare = oddfare;
    }

    public Long getOpfare() {
        return opfare;
    }

    public void setOpfare(Long opfare) {
        this.opfare = opfare;
    }

    public Long getSumfare() {
        return sumfare;
    }

    public void setSumfare(Long sumfare) {
        this.sumfare = sumfare;
    }

    public Long getAcccode() {
        return acccode;
    }

    public void setAcccode(Long acccode) {
        this.acccode = acccode;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Long getCardsn() {
        return cardsn;
    }

    public void setCardsn(Long cardsn) {
        this.cardsn = cardsn;
    }

    public Long getSourcetable() {
        return sourcetable;
    }

    public void setSourcetable(Long sourcetable) {
        this.sourcetable = sourcetable;
    }


}