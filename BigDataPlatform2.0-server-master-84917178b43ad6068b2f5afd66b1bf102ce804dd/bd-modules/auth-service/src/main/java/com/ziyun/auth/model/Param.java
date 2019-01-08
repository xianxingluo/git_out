package com.ziyun.auth.model;

import javax.persistence.Column;
import javax.persistence.Transient;

public class Param {

    /**
     * 创建者ID
     */
    @Column(name = "create_user_id")
    private Long createUserId;

    @Transient
    private String access_token;
    /**
     * 创建角色id
     */
    @Column(name = "create_role_id")
    private Long createRoleId;

    @Transient
    private Integer start;
    @Transient
    private Integer limit;

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Long getCreateRoleId() {
        return createRoleId;
    }

    public void setCreateRoleId(Long createRoleId) {
        this.createRoleId = createRoleId;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}
