package com.ziyun.net.vo;

import java.io.Serializable;

/**
 * Created by linxiaojun on 11/16/17 11:07 PM.
 */
public class NVResultData implements Serializable {

    private String name;

    private int value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
