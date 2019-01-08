package com.ziyun.report.model;

public class RedirectParam extends Param{
    //非毕业生重定向Url
    private String redirectUrl;
    //是否自动弹出，登录认证系统：如果是报告发布后第一次登陆自动弹出。 1：自动弹出。 0：不是自动弹出（点击按钮弹出）
    private Integer autoPop = 0;

    public Integer getAutoPop() {
        return autoPop;
    }

    public void setAutoPop(Integer autoPop) {
        this.autoPop = autoPop;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
