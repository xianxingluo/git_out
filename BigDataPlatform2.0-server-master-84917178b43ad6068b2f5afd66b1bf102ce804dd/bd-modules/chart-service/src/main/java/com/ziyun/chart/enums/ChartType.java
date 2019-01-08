package com.ziyun.chart.enums;

/**
 * @description: 图表类型枚举类
 * @author: FubiaoLiu
 * @date: 2018/10/16
 */
public enum ChartType {
    HISTOGRAM("00", "柱状图"),
    LINE_HART("01", "折线图"),
    PIE_CHART("02", "饼状图"),
    DASHBOARD("03", "仪表盘");

    private String code;
    private String value;

    ChartType(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static String getValue(String code) {
        String result = "";
        for (ChartType sex : ChartType.values()) {
            if (sex.getCode().equals(code)) {
                result = sex.getValue();
            }
        }
        return result;
    }
}
