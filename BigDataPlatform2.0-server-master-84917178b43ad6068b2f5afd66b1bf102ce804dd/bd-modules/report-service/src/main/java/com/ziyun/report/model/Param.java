package com.ziyun.report.model;

/**
 * 从前台传过来的：查询条件
 */
public class Param {
    private String token;//登录之后，后端返回一个token给前端；后面每个请求，前端需要带着这个token。后台根据这个进行权限验证。

    // 学号
    private String outid;
    // 年级
    private String grade;
    // 班级列表
    private String[] classCodes;
    // 时长
    private Long duration;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOutid() {
        return outid;
    }

    public void setOutid(String outid) {
        this.outid = outid;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String[] getClassCodes() {
        return classCodes;
    }

    public void setClassCodes(String[] classCodes) {
        this.classCodes = classCodes;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }
}
