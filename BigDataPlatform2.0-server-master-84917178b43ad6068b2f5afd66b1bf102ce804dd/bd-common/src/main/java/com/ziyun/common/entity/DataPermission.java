package com.ziyun.common.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @description: 数据权限实体类
 * @author: FubiaoLiu
 * @date: 2018/11/20
 */
@Table(name = "auth_data_permission_new")
public class DataPermission implements Serializable {
    /**
     * 主键id
     */
    @Id
    private Long id;
    /**
     * 授权标识编号
     */
    private String code;
    /**
     * 权限名称
     */
    private String name;
    /**
     * 父菜单ID，一级菜单为0
     */
    @Column(name = "parent_id")
    private Long parentId;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 权限描述
     */
    private String description;

    /**
     * 状态(0.禁用;1.启用)
     */
    private Integer status;

    /**
     * 是否删除(0.已删除;1.未删除)
     */
    private Integer deleted;

    /**
     * 创建人(用户名)
     */
    private String creator;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 子节点
     */
    @Transient
    private List<DataPermission> children;

    private static final long serialVersionUID = 1L;

    public List<DataPermission> getChildren() {
        return children;
    }

    public void setChildren(List<DataPermission> children) {
        this.children = children;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}