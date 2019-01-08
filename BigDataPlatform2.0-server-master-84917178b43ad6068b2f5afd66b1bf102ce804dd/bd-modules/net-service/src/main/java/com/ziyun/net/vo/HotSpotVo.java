package com.ziyun.net.vo;

public class HotSpotVo implements Comparable<HotSpotVo> {
    /**
     * 周日期
     */
    String week;
    /**
     * 热点类型
     */
    String type;
    /**
     * 数值
     */
    Integer num;

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    /*
     * 排序
     * (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(HotSpotVo o) {
        return this.num.compareTo(o.getNum()) * -1;
    }

}
