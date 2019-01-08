package com.ziyun.academic.entity;

import java.io.Serializable;

/**
 * 预警参数类
 */
public class EarlyParam implements Serializable {
    /**
     * 自增长id
     */
    private Long id;
    /**
     * early_warn_rule2的id
     */
    private Long warnId;
    /**
     * 预警参数描述：如挂科门数
     */
    private String warnDesc;
    /**
     * 预警参数
     */
    private String warnParam;
    /**
     * 最小值
     */
    private String minNum;
    /**
     * 最大值
     */
    private String maxNum;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWarnId() {
        return warnId;
    }

    public void setWarnId(Long warnId) {
        this.warnId = warnId;
    }

    public String getWarnDesc() {
        return warnDesc;
    }

    public void setWarnDesc(String warnDesc) {
        this.warnDesc = warnDesc;
    }

    public String getWarnParam() {
        return warnParam;
    }

    public void setWarnParam(String warnParam) {
        this.warnParam = warnParam;
    }

    public String getMinNum() {
        return minNum;
    }

    public void setMinNum(String minNum) {
        this.minNum = minNum;
    }

    public String getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(String maxNum) {
        this.maxNum = maxNum;
    }
}
