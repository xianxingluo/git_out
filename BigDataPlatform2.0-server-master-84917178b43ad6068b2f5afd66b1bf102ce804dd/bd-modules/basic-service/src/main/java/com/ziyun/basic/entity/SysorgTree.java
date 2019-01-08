package com.ziyun.basic.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SysorgTree implements Serializable {

    private static final long serialVersionUID = -4181105905866779732L;

    public SysorgTree() {
        super();
    }

    private Integer id;

    private String orgCode;

    private String orgName;

    private Long parentCode;

    private Integer orgLevel;

    private Date updateTime;

    private String remark;

    private long userId;

    private Integer cid;
    private List<SysorgTree> sysorgtree = new ArrayList<SysorgTree>();
    private List<SysUser> user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Long getParentCode() {
        return parentCode;
    }

    public void setParentCode(Long parentCode) {
        this.parentCode = parentCode;
    }

    public Integer getOrgLevel() {
        return orgLevel;
    }

    public void setOrgLevel(Integer orgLevel) {
        this.orgLevel = orgLevel;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<SysorgTree> getSysorgtree() {
        return sysorgtree;
    }

    public void setSysorgtree(List<SysorgTree> sysorgtree) {
        this.sysorgtree = sysorgtree;
    }

    public List<SysUser> getUser() {
        return user;
    }

    public void setUser(List<SysUser> user) {
        this.user = user;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }


}