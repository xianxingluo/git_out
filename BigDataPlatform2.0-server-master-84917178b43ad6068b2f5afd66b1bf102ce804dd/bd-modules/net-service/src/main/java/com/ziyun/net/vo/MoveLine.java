package com.ziyun.net.vo;

import java.util.ArrayList;
import java.util.List;

public class MoveLine {
    private List coords;
    private String[] fromCoords = {"0", "0"};
    private String[] toCoords = {"0", "0"};
    private String fromName;
    private String toName;
    private String serNum;

    public String getSerNum() {
        return serNum;
    }

    public void setSerNum(String serNum) {
        this.serNum = serNum;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public List getCoords() {
        List coordsList = new ArrayList();
        coordsList.add(fromCoords);
        coordsList.add(toCoords);
        return coordsList;
    }

    public void setFromCoords(String[] fromCoords) {
        this.fromCoords = fromCoords;
    }

    public void setToCoords(String[] toCoords) {
        this.toCoords = toCoords;
    }

}
