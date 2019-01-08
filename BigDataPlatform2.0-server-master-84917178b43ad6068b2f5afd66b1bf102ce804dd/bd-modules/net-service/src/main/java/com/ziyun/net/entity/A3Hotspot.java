package com.ziyun.net.entity;


import com.ziyun.net.vo.Detail;

import java.util.Date;

public class A3Hotspot {
    private Integer id;

    private String name;

    private String group;

    private String ip;

    private String up;

    private String down;

    private String total;

    private String session;

    private String status;

    private Detail detail;

    private Date date;

    private String target;

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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUp() {
        return up;
    }

    public void setUp(String up) {
        this.up = up;
    }

    public String getDown() {
        return down;
    }

    public void setDown(String down) {
        this.down = down;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Detail getDetail() {
        return detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }

    public Date getDate() {
        return (Date) date.clone();
    }

    public void setDate(Date date) {
        this.date = (Date) date.clone();
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return "A3Hotspot [id=" + id + ", name=" + name + ", group=" + group
                + ", ip=" + ip + ", up=" + up + ", down=" + down + ", total="
                + total + ", session=" + session + ", status=" + status
                + ", detail=" + detail + ", date=" + date + ", target="
                + target + "]";
    }


}