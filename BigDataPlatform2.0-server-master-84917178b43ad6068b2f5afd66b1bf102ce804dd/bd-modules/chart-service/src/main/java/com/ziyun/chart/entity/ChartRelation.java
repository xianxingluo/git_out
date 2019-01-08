package com.ziyun.chart.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @description: 模块图表关系实体类
 * @author: FubiaoLiu
 * @date: 2018/10/16
 */
@Table(name = "sys_chart_relation")
public class ChartRelation implements Serializable {
    /**
     * 主键id
     */
    @Id
    private Long id;

    /**
     * 模块编码
     */
    private String code;

    /**
     * 图表类型(00. 柱状图;01. 折线图;02. 饼状图;03. 仪表盘)
     */
    @Column(name = "chart_type")
    private String chartType;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    /**
     * 获取主键id
     *
     * @return id - 主键id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键id
     *
     * @param id 主键id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取模块编码
     *
     * @return code - 模块编码
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置模块编码
     *
     * @param code 模块编码
     */
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    /**
     * 获取图表类型(00. 柱状图;01. 折线图;02. 饼状图;03. 仪表盘)
     *
     * @return chart_type - 图表类型(00. 柱状图;01. 折线图;02. 饼状图;03. 仪表盘)
     */
    public String getChartType() {
        return chartType;
    }

    /**
     * 设置图表类型(00. 柱状图;01. 折线图;02. 饼状图;03. 仪表盘)
     *
     * @param chartType 图表类型(00. 柱状图;01. 折线图;02. 饼状图;03. 仪表盘)
     */
    public void setChartType(String chartType) {
        this.chartType = chartType == null ? null : chartType.trim();
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        ChartRelation other = (ChartRelation) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getCode() == null ? other.getCode() == null : this.getCode().equals(other.getCode()))
                && (this.getChartType() == null ? other.getChartType() == null : this.getChartType().equals(other.getChartType()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCode() == null) ? 0 : getCode().hashCode());
        result = prime * result + ((getChartType() == null) ? 0 : getChartType().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", code=").append(code);
        sb.append(", chartType=").append(chartType);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}