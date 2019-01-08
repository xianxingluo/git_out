package com.ziyun.academic.entity;

import java.util.Date;

public class OwnOperateMark {
    private String tableName;

    private Date lastTime;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }
}