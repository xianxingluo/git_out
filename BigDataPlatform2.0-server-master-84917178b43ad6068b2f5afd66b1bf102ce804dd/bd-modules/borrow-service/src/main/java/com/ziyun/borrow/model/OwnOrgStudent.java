package com.ziyun.borrow.model;

import java.io.Serializable;

public class OwnOrgStudent implements Serializable {
 
  

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String outid;

    private String schoolCode;

    private String facultyCode;

    private String majorCode;

    private String classCode;

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
}