package com.ziyun.report.model;

public class GraduateEndings {
    // 主键
    private Long id;
    // 学号
    private String outid;
    // 寄语
    private String endings;

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

    public String getEndings() {
        return endings;
    }

    public void setEndings(String endings) {
        this.endings = endings;
    }
}
