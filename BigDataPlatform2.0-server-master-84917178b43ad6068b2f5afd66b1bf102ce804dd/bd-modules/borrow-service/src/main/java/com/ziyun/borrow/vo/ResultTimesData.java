package com.ziyun.borrow.vo;

/**
 * sql查询返回结果的封装：专门用于计算时间段
 */
public class ResultTimesData {
    private Long id;//
    private Integer days;// 一共借了多少天书：算上头尾
    private Integer bhour;// 开始借书的小时
    private Integer ehour;// 还书的小时

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Integer getBhour() {
        return bhour;
    }

    public void setBhour(Integer bhour) {
        this.bhour = bhour;
    }

    public Integer getEhour() {
        return ehour;
    }

    public void setEhour(Integer ehour) {
        this.ehour = ehour;
    }

}
