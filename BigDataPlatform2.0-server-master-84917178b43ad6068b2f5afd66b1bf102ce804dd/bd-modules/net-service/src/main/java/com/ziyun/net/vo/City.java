package com.ziyun.net.vo;

import java.util.HashMap;
import java.util.Map;

public class City {

    private ItemStyle itemStyle;
    private String name;
    private String[] value = {"0", "0"};
    private String symbolSize = "1";

    private String serNum;

    public String getSerNum() {
        return serNum;
    }


    public void setSerNum(String serNum) {
        this.serNum = serNum;
    }


    public City() {
        Map<String, String> map = new HashMap<String, String>();
        itemStyle = new ItemStyle();
        map.put("color", "#00c486");
        itemStyle.setNormal(map);
    }


    public void setSymbolSize(String symbolSize) {
        this.symbolSize = symbolSize;
    }

    public String getSymbolSize() {
        return symbolSize;
    }


    public ItemStyle getItemStyle() {
        return itemStyle;
    }


    public void setItemStyle(ItemStyle itemStyle) {
        this.itemStyle = itemStyle;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getValue() {
        return value;
    }

    public void setValue(String[] value) {
        this.value = value;
    }


}
