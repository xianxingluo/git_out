package com.ziyun.basic.entity;

import java.io.Serializable;

public class OwnOrgStudentType implements Serializable {


    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String outid;

    private String schoolCode;

    private String facultyCode;

    private String majorCode;

    private String classCode;

    private String schoolName;

    private String facultyName;

    private String majorName;

    //
    private String name;
    private String sex;
    private Integer impoverish;
    private Integer scholarship;
    private Integer politicalCodeAll;
    private Integer politicalCodeThisterm;
    private Integer politicalCodeLastterm;
    private Integer politicalCodeLastyear;

    public String getOutid() {
        return outid;
    }

    public void setOutid(String outid) {
        this.outid = outid;
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

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getImpoverish() {
        return impoverish;
    }

    public void setImpoverish(Integer impoverish) {
        this.impoverish = impoverish;
    }

    public Integer getScholarship() {
        return scholarship;
    }

    public void setScholarship(Integer scholarship) {
        this.scholarship = scholarship;
    }

    public Integer getPoliticalCodeAll() {
        return politicalCodeAll;
    }

    public void setPoliticalCodeAll(Integer politicalCodeAll) {
        this.politicalCodeAll = politicalCodeAll;
    }

    public Integer getPoliticalCodeThisterm() {
        return politicalCodeThisterm;
    }

    public void setPoliticalCodeThisterm(Integer politicalCodeThisterm) {
        this.politicalCodeThisterm = politicalCodeThisterm;
    }

    public Integer getPoliticalCodeLastterm() {
        return politicalCodeLastterm;
    }

    public void setPoliticalCodeLastterm(Integer politicalCodeLastterm) {
        this.politicalCodeLastterm = politicalCodeLastterm;
    }

    public Integer getPoliticalCodeLastyear() {
        return politicalCodeLastyear;
    }

    public void setPoliticalCodeLastyear(Integer politicalCodeLastyear) {
        this.politicalCodeLastyear = politicalCodeLastyear;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}