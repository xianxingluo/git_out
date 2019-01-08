package com.ziyun.net.entity;

import java.util.Date;

public class A3RadacctTime {
    private Long id;

    private String outid;

    private Date acctstarttime;

    private Date acctstoptime;

    private Integer acctsessiontime;

    private String schoolCode;

    private String facultyCode;

    private String majorCode;

    private String classCode;

    private String sex;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOutid() {
        return outid;
    }

    public void setOutid(String outid) {
        this.outid = outid;
    }

    public Date getAcctstarttime() {
        return acctstarttime;
    }

    public void setAcctstarttime(Date acctstarttime) {
        this.acctstarttime = acctstarttime;
    }

    public Date getAcctstoptime() {
        return acctstoptime;
    }

    public void setAcctstoptime(Date acctstoptime) {
        this.acctstoptime = acctstoptime;
    }

    public Integer getAcctsessiontime() {
        return acctsessiontime;
    }

    public void setAcctsessiontime(Integer acctsessiontime) {
        this.acctsessiontime = acctsessiontime;
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
}