package com.ziyun.net.vo;

import java.io.Serializable;

/**
 * 防火墙标签对象
 */
public class Spot implements Serializable {
    private static final long serialVersionUID = 1L;

    // "id":1,"app":"P2P:P2P行为","line":1,"percent":"47","up":448,"down":360,"total":808
    /**
     * 应用名称
     */
    private String id;

    /**
     *
     */
    private String line;

    /**
     *
     */
    private String percent;

    /**
     * 应用名称
     */
    private String app;
    /**
     * 上行流量
     */
    private long up;
    /**
     * 下行流量
     */
    private long down;
    /**
     * 总流量
     */
    private long total;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public long getUp() {
        return up;
    }

    public void setUp(long up) {
        this.up = up;
    }

    public long getDown() {
        return down;
    }

    public void setDown(long down) {
        this.down = down;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
