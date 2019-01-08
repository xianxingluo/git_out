package com.ziyun.academic.enums;

import java.math.BigDecimal;

/**
 * @description:科目：通过率
 * @author: yk.tan
 * @since: 2017/7/12
 * @history:
 */
public enum PassEnum {
    PASS_90_100("90_100", new BigDecimal(90), new BigDecimal(100)),
    PASS_80_90("80_90", new BigDecimal(80), new BigDecimal(90)),
    PASS_70_80("70_80", new BigDecimal(70), new BigDecimal(80)),
    PASS_60_70("60_70", new BigDecimal(60), new BigDecimal(70)),
    PASS_50_60("50_60", new BigDecimal(50), new BigDecimal(60)),
    PASS_0_50("0_50", new BigDecimal(0), new BigDecimal(50));
    private String type;
    private BigDecimal lower;
    private BigDecimal upper;

    PassEnum(String type, BigDecimal lower, BigDecimal upper) {
        this.type = type;
        this.lower = lower;
        this.upper = upper;
    }

    public String getType() {
        return type;
    }

    public BigDecimal getLower() {
        return lower;
    }

    public BigDecimal getUpper() {
        return upper;
    }
}
