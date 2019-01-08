package com.ziyun.common.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @description: 功能权限实体类
 * @author: FubiaoLiu
 * @date: 2018/11/20
 */
@Table(name = "auth_func_permission_copy")
public class FuncPermission implements Serializable {
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

    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 权限级别
     */
    private Integer level;

    /**
     * 权限类型(0.系统权限;1.用户权限)
     */
    @Transient
    private Integer type;
    /**
     * 菜单类型(0.目录;1.菜单;2.按钮;3.图表;9.列表总数)
     */
    @Column(name = "menu_type")
    private Integer menuType;

    /**
     * 展示类型(0.图表；1.卡片)
     */
    @Column(name = "show_type")
    private Integer showType;

    /**
     * 权限url地址
     */
    private String url;

    /**
     * 权限描述
     */
    private String description;

    /**
     * 排序号(从1开始)
     */
    @Column(name = "order_num")
    private Integer orderNum;

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
     * 子节点列表
     */
    @Transient
    private List<FuncPermission> children;
    /**
     * 权限名称(前端需要，代替name)
     */
    @Transient
    private String label;

    /**
     * 权限编号(前端需要，代替code)
     */
    @Transient
    private String value;

    private static final long serialVersionUID = 1L;

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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getMenuType() {
        return menuType;
    }

    public void setMenuType(Integer menuType) {
        this.menuType = menuType;
    }

    public Integer getShowType() {
        return showType;
    }

    public void setShowType(Integer showType) {
        this.showType = showType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
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

    public List<FuncPermission> getChildren() {
        return children;
    }

    public void setChildren(List<FuncPermission> children) {
        this.children = children;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}