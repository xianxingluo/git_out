package com.ziyun.academic.entity;

import java.util.List;

public class OwnOrgStree {
    private Integer id;

    private String orgCode;

    private String orgName;

    private String parentCode;

    private Integer orgLevel;

    private String oldOrgCode;

    private String oldParentCode;

    private OwnOrgStree orgStree;

    private List<OwnOrgStree> orgStreeList;

    public List<OwnOrgStree> getOrgStreeList() {
        return orgStreeList;
    }

    public void setOrgStreeList(List<OwnOrgStree> orgStreeList) {
        this.orgStreeList = orgStreeList;
    }

    public OwnOrgStree() {
    }

    public Integer getId() {
        return id;
    }

    public OwnOrgStree getOrgStree() {
        return orgStree;
    }

    public void setOrgStree(OwnOrgStree orgStree) {
        this.orgStree = orgStree;
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

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public Integer getOrgLevel() {
        return orgLevel;
    }

    public void setOrgLevel(Integer orgLevel) {
        this.orgLevel = orgLevel;
    }

    public String getOldOrgCode() {
        return oldOrgCode;
    }

    public void setOldOrgCode(String oldOrgCode) {
        this.oldOrgCode = oldOrgCode;
    }

    public String getOldParentCode() {
        return oldParentCode;
    }

    public void setOldParentCode(String oldParentCode) {
        this.oldParentCode = oldParentCode;
    }
}