package com.ziyun.auth.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "sys_org_tree")
public class OrgTree implements Serializable {
    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * 机构code：：校区、院系2级没有编码；是我们自己编的
     */
    @Column(name = "org_code")
    private String orgCode;

    /**
     * 机构名称
     */
    @Column(name = "org_name")
    private String orgName;

    /**
     * 父级机构 code
     */
    @Column(name = "parent_code")
    private String parentCode;

    /**
     * 机构级别：0、校区-slg:苏理工;jkd:江科大。1、院系。2、专业。3、班级
     */
    @Column(name = "org_level")
    private Integer orgLevel;

    /**
     * 操作时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    private static final long serialVersionUID = 1L;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取机构code：：校区、院系2级没有编码；是我们自己编的
     *
     * @return org_code - 机构code：：校区、院系2级没有编码；是我们自己编的
     */
    public String getOrgCode() {
        return orgCode;
    }

    /**
     * 设置机构code：：校区、院系2级没有编码；是我们自己编的
     *
     * @param orgCode 机构code：：校区、院系2级没有编码；是我们自己编的
     */
    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode == null ? null : orgCode.trim();
    }

    /**
     * 获取机构名称
     *
     * @return org_name - 机构名称
     */
    public String getOrgName() {
        return orgName;
    }

    /**
     * 设置机构名称
     *
     * @param orgName 机构名称
     */
    public void setOrgName(String orgName) {
        this.orgName = orgName == null ? null : orgName.trim();
    }

    /**
     * 获取父级机构 code
     *
     * @return parent_code - 父级机构 code
     */
    public String getParentCode() {
        return parentCode;
    }

    /**
     * 设置父级机构 code
     *
     * @param parentCode 父级机构 code
     */
    public void setParentCode(String parentCode) {
        this.parentCode = parentCode == null ? null : parentCode.trim();
    }

    /**
     * 获取机构级别：0、校区-slg:苏理工;jkd:江科大。1、院系。2、专业。3、班级
     *
     * @return org_level - 机构级别：0、校区-slg:苏理工;jkd:江科大。1、院系。2、专业。3、班级
     */
    public Integer getOrgLevel() {
        return orgLevel;
    }

    /**
     * 设置机构级别：0、校区-slg:苏理工;jkd:江科大。1、院系。2、专业。3、班级
     *
     * @param orgLevel 机构级别：0、校区-slg:苏理工;jkd:江科大。1、院系。2、专业。3、班级
     */
    public void setOrgLevel(Integer orgLevel) {
        this.orgLevel = orgLevel;
    }

    /**
     * 获取操作时间
     *
     * @return update_time - 操作时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置操作时间
     *
     * @param updateTime 操作时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
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
        OrgTree other = (OrgTree) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getOrgCode() == null ? other.getOrgCode() == null : this.getOrgCode().equals(other.getOrgCode()))
                && (this.getOrgName() == null ? other.getOrgName() == null : this.getOrgName().equals(other.getOrgName()))
                && (this.getParentCode() == null ? other.getParentCode() == null : this.getParentCode().equals(other.getParentCode()))
                && (this.getOrgLevel() == null ? other.getOrgLevel() == null : this.getOrgLevel().equals(other.getOrgLevel()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
                && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getOrgCode() == null) ? 0 : getOrgCode().hashCode());
        result = prime * result + ((getOrgName() == null) ? 0 : getOrgName().hashCode());
        result = prime * result + ((getParentCode() == null) ? 0 : getParentCode().hashCode());
        result = prime * result + ((getOrgLevel() == null) ? 0 : getOrgLevel().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", orgCode=").append(orgCode);
        sb.append(", orgName=").append(orgName);
        sb.append(", parentCode=").append(parentCode);
        sb.append(", orgLevel=").append(orgLevel);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", remark=").append(remark);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}