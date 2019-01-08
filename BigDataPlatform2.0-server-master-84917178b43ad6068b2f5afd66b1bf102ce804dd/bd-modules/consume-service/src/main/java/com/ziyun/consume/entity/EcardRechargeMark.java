package com.ziyun.consume.entity;

import java.util.Date;

public class EcardRechargeMark {
    private Long id;

    private Long consumeid;

    private String outid;

    private Long cardsn;

    private Long opcount;

    private Date opdt;

    private Long oddfare;

    private Long opfare;

    private Long sumfare;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getConsumeid() {
        return consumeid;
    }

    public void setConsumeid(Long consumeid) {
        this.consumeid = consumeid;
    }

    public String getOutid() {
        return outid;
    }

    public void setOutid(String outid) {
        this.outid = outid;
    }

    public Long getCardsn() {
        return cardsn;
    }

    public void setCardsn(Long cardsn) {
        this.cardsn = cardsn;
    }

    public Long getOpcount() {
        return opcount;
    }

    public void setOpcount(Long opcount) {
        this.opcount = opcount;
    }

    public Date getOpdt() {
        return opdt;
    }

    public void setOpdt(Date opdt) {
        this.opdt = opdt;
    }

    public Long getOddfare() {
        return oddfare;
    }

    public void setOddfare(Long oddfare) {
        this.oddfare = oddfare;
    }

    public Long getOpfare() {
        return opfare;
    }

    public void setOpfare(Long opfare) {
        this.opfare = opfare;
    }

    public Long getSumfare() {
        return sumfare;
    }

    public void setSumfare(Long sumfare) {
        this.sumfare = sumfare;
    }
}