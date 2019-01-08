package com.ziyun.auth.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @description: 用户自定义菜单
 * @author: FubiaoLiu
 * @date: 2018/9/25
 */
@Table(name = "auth_user_menu_copy")
public class UserMenu implements Serializable {
    /**
     * 主键id
     */
    @Id
    private Long id;

    /**
     * 权限编号
     */
    private String code;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 模块(00.驾驶舱；01.数据图表；02.智慧标签；03.分析报告；04.智能预警；06.大屏；07.图表库)(后面根据权限code进行调整)
     */
    private String module;

    /**
     * 父级编号
     */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 权限类型(0.系统权限;1.用户权限)
     */
    private Integer type;

    /**
     * 菜单类型(0.目录;1.菜单;2.按钮;3.图表)
     */
    @Column(name = "menu_type")
    private Integer menuType;

    /**
     * url地址
     */
    private String url;

    /**
     * 用户名
     */
    private String username;
    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 描述
     */
    private String description;

    /**
     * 排序号
     */
    @Column(name = "order_num")
    private Integer orderNum;

    /**
     * 是否删除(0.未删除;1.已删除)
     */
    private Integer deleted;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;
    /**
     * 子节点列表
     */
    @Transient
    private List<UserMenu> children;
    /**
     * 权限级别
     */
    @Transient
    private Integer level;

    private static final long serialVersionUID = 1L;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public List<UserMenu> getChildren() {
        return children;
    }

    public void setChildren(List<UserMenu> children) {
        this.children = children;
    }

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
     * 获取权限编号
     *
     * @return code - 权限编号
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置权限编号
     *
     * @param code 权限编号
     */
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    /**
     * 获取权限名称
     *
     * @return name - 权限名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置权限名称
     *
     * @param name 权限名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取父级编号
     *
     * @return parent_id - 父级编号
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * 设置父级编号
     *
     * @param parentId 父级编号
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取权限类型(0.系统权限;1.用户权限)
     *
     * @return type - 权限类型(0.系统权限;1.用户权限)
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置权限类型(0.系统权限;1.用户权限)
     *
     * @param type 权限类型(0.系统权限;1.用户权限)
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取菜单类型(0.目录;1.菜单;2.按钮;3.图表)
     *
     * @return menu_type - 菜单类型(0.目录;1.菜单;2.按钮;3.图表)
     */
    public Integer getMenuType() {
        return menuType;
    }

    /**
     * 设置菜单类型(0.目录;1.菜单;2.按钮;3.图表)
     *
     * @param menuType 菜单类型(0.目录;1.菜单;2.按钮;3.图表)
     */
    public void setMenuType(Integer menuType) {
        this.menuType = menuType;
    }

    /**
     * 获取url地址
     *
     * @return url - url地址
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置url地址
     *
     * @param url url地址
     */
    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    /**
     * 获取所属用户
     *
     * @return username - 所属用户
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置所属用户
     *
     * @param username 所属用户
     */
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    /**
     * 获取描述
     *
     * @return description - 描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置描述
     *
     * @param description 描述
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * 获取排序号
     *
     * @return order_num - 排序号
     */
    public Integer getOrderNum() {
        return orderNum;
    }

    /**
     * 设置排序号
     *
     * @param orderNum 排序号
     */
    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    /**
     * 获取是否删除(0.未删除;1.已删除)
     *
     * @return deleted - 是否删除(0.未删除;1.已删除)
     */
    public Integer getDeleted() {
        return deleted;
    }

    /**
     * 设置是否删除(0.未删除;1.已删除)
     *
     * @param deleted 是否删除(0.未删除;1.已删除)
     */
    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
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
     * 获取修改时间
     *
     * @return update_time - 修改时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置修改时间
     *
     * @param updateTime 修改时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
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
        UserMenu other = (UserMenu) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getCode() == null ? other.getCode() == null : this.getCode().equals(other.getCode()))
                && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
                && (this.getParentId() == null ? other.getParentId() == null : this.getParentId().equals(other.getParentId()))
                && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
                && (this.getMenuType() == null ? other.getMenuType() == null : this.getMenuType().equals(other.getMenuType()))
                && (this.getUrl() == null ? other.getUrl() == null : this.getUrl().equals(other.getUrl()))
                && (this.getUsername() == null ? other.getUsername() == null : this.getUsername().equals(other.getUsername()))
                && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
                && (this.getOrderNum() == null ? other.getOrderNum() == null : this.getOrderNum().equals(other.getOrderNum()))
                && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCode() == null) ? 0 : getCode().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getParentId() == null) ? 0 : getParentId().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getMenuType() == null) ? 0 : getMenuType().hashCode());
        result = prime * result + ((getUrl() == null) ? 0 : getUrl().hashCode());
        result = prime * result + ((getUsername() == null) ? 0 : getUsername().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getOrderNum() == null) ? 0 : getOrderNum().hashCode());
        result = prime * result + ((getDeleted() == null) ? 0 : getDeleted().hashCode());
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
        sb.append(", name=").append(name);
        sb.append(", parentId=").append(parentId);
        sb.append(", type=").append(type);
        sb.append(", menuType=").append(menuType);
        sb.append(", url=").append(url);
        sb.append(", username=").append(username);
        sb.append(", desc=").append(description);
        sb.append(", orderNum=").append(orderNum);
        sb.append(", deleted=").append(deleted);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}