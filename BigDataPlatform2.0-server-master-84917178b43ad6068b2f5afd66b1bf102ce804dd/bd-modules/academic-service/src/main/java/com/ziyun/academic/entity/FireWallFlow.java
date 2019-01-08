package com.ziyun.academic.entity;

import java.util.Date;

/**
 * 防火墙流量
 *
 * @author dell
 */
public class FireWallFlow {

    private Integer id;
    private String name;      //接口名称
    private String state;     //状态
    private String type;      //运营商类型   CMCC-移动，CHINANET-电信
    private String ip;        //IP
    private String upflow;    //上行流量
    private String downflow;  //下行流量
    private String allflow;   //总流量
    private Date date;        //日期

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUpflow() {
        return upflow;
    }

    public void setUpflow(String upflow) {
        this.upflow = upflow;
    }

    public String getDownflow() {
        return downflow;
    }

    public void setDownflow(String downflow) {
        this.downflow = downflow;
    }

    public String getAllflow() {
        return allflow;
    }

    public void setAllflow(String allflow) {
        this.allflow = allflow;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


}
