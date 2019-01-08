package com.ziyun.auth.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @description: 功能权限树节点实体类
 * @author: FubiaoLiu
 * @date: 2018/11/20
 */
public class FuncPermissionTree implements Serializable {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 授权标识编号
     */
    private String code;
    /**
     * 权限名称
     */
    private String name;

    private Long parentId;
    /**
     * 权限url地址
     */
    private String url;
    /**
     * 权限描述
     */
    private String description;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    private List<FuncPermissionTree> permissionTree;

    private static final long serialVersionUID = 1L;

}