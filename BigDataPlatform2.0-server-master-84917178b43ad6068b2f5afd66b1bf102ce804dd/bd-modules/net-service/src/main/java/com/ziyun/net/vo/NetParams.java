package com.ziyun.net.vo;

/**
 * Created by linxiaojun on 1/25/18 11:29 AM.
 */
public class NetParams extends Params {
    /**
     * 周几
     */
    private int weekday;

    /**
     * App 一级分类
     */
    private String serv;

    /**
     * App 二级分类
     */
    private String[] apps;

    /**
     * 上网时长排名： 排序列-->上网总时长
     */
    private Integer netDurationTopOrder;

    /**
     * 区分群体、个人, 主要用于防止群体查询映射到个人cube
     * 0： 个人， 1： 群体
     */
    private Integer periodFlag;

    public String getServ() {
        return serv;
    }

    public void setServ(String serv) {
        this.serv = serv;
    }

    public String[] getApps() {
        return apps;
    }

    public void setApps(String[] apps) {
        this.apps = apps;
    }

    public int getWeekday() {
        return weekday;
    }

    public void setWeekday(int weekday) {
        this.weekday = weekday;
    }

    public Integer getNetDurationTopOrder() {
        return netDurationTopOrder;
    }

    public void setNetDurationTopOrder(Integer netDurationTopOrder) {
        if (netDurationTopOrder == null) {
            this.netDurationTopOrder = 1;
        } else {
            this.netDurationTopOrder = netDurationTopOrder;
        }
    }

    public Integer getPeriodFlag() {
        return periodFlag;
    }

    public void setPeriodFlag(Integer periodFlag) {
        this.periodFlag = periodFlag;
    }
}
