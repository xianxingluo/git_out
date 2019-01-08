package com.ziyun.consume.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 小时统计时：返回结果封装
 */
public class HourDataExport implements Serializable {

    public HourDataExport(String weekstr, String hour, String hourstr,
                          BigDecimal sum, BigDecimal totalsum) {
        super();
        this.weekstr = weekstr;
        this.hour = hour;
        this.hourstr = hourstr;
        this.sum = sum;
        this.totalsum = totalsum;
    }

    /**
     *
     */
    private static final long serialVersionUID = 7019273035015282366L;

    private String weekstr;// 小时
    private String hour;// 小时
    private String hourstr;// 小时
    private BigDecimal sum;// 浮点数：：例如-金额
    private BigDecimal totalsum;// 后加的：没有除以周几出现次数的总值// 浮点数：：例如-金额

    public HourDataExport(String hour, BigDecimal sum) {
        super();
        this.hour = hour;
        this.sum = sum;
    }

    public HourDataExport(String hour, BigDecimal sum, BigDecimal totalsum) {
        super();
        this.hour = hour;
        this.sum = sum;
        this.totalsum = totalsum;
    }


    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public BigDecimal getTotalsum() {
        return totalsum;
    }

    public void setTotalsum(BigDecimal totalsum) {
        this.totalsum = totalsum;
    }

    public String getWeekstr() {
        return weekstr;
    }

    public void setWeekstr(String weekstr) {
        this.weekstr = weekstr;
    }

    public String getHourstr() {
        return hourstr;
    }

    public void setHourstr(String hourstr) {
        this.hourstr = hourstr;
    }

}
