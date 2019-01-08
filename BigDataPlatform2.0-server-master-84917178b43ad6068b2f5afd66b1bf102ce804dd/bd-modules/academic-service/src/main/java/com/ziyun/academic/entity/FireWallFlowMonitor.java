package com.ziyun.academic.entity;

/**
 * 防火墙流量
 *
 * @author dell
 */
public class FireWallFlowMonitor implements Comparable<FireWallFlowMonitor> {

    private String type;      //运营商类型   CMCC-移动，CHINANET-电信
    private String time;  //下行流量
    private String allflow;   //总流量

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAllflow() {
        return allflow;
    }

    public void setAllflow(String allflow) {
        this.allflow = allflow;
    }

    @Override
    public int compareTo(FireWallFlowMonitor o) {
        return this.time.compareTo(o.getTime());
    }


}
