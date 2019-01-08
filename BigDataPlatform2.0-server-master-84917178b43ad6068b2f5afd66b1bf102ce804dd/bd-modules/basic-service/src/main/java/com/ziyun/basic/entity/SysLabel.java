package com.ziyun.basic.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @description: 系统标签实体类
 * @author: FubiaoLiu
 * @date: 2018/9/26
 */
@Table(name = "sys_label")
public class SysLabel implements Serializable {
    /**
     * 主键id
     */
    @Id
    private Long id;

    /**
     * 标签名称
     */
    private String name;

    /**
     * 参数名
     */
    @Column(name = "param_type")
    private String paramType;

    /**
     * 参数值
     */
    private String value;

    /**
     * 标签详情
     */
    private String detail;
    /**
     * 标签类型(0.规则类;1.算法类)
     */
    private Integer type;
    /**
     * 标签类型描述(0.规则类;1.算法类)
     */
    private String typeDesc;
    /**
     * 状态(0.禁用;1.启用)
     */
    private Integer status;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    /**
     * 获取标签名称
     *
     * @return name - 标签名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置标签名称
     *
     * @param name 标签名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取参数名
     *
     * @return param_type - 参数名
     */
    public String getParamType() {
        return paramType;
    }

    /**
     * 设置参数名
     *
     * @param paramType 参数名
     */
    public void setParamType(String paramType) {
        this.paramType = paramType == null ? null : paramType.trim();
    }

    /**
     * 获取参数值
     *
     * @return value - 参数值
     */
    public String getValue() {
        return value;
    }

    /**
     * 设置参数值
     *
     * @param value 参数值
     */
    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }

    /**
     * 获取标签详情
     *
     * @return detail - 标签详情
     */
    public String getDetail() {
        return detail;
    }

    /**
     * 设置标签详情
     *
     * @param detail 标签详情
     */
    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }

    /**
     * 获取状态(0.启用;1.禁用)
     *
     * @return status - 状态(0.启用;1.禁用)
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态(0.启用;1.禁用)
     *
     * @param status 状态(0.启用;1.禁用)
     */
    public void setStatus(Integer status) {
        this.status = status;
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
        SysLabel other = (SysLabel) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
                && (this.getParamType() == null ? other.getParamType() == null : this.getParamType().equals(other.getParamType()))
                && (this.getValue() == null ? other.getValue() == null : this.getValue().equals(other.getValue()))
                && (this.getDetail() == null ? other.getDetail() == null : this.getDetail().equals(other.getDetail()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getParamType() == null) ? 0 : getParamType().hashCode());
        result = prime * result + ((getValue() == null) ? 0 : getValue().hashCode());
        result = prime * result + ((getDetail() == null) ? 0 : getDetail().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", paramType=").append(paramType);
        sb.append(", value=").append(value);
        sb.append(", detail=").append(detail);
        sb.append(", status=").append(status);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}