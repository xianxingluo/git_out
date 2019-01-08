package com.ziyun.auth.mapper;

import com.ziyun.auth.model.OrgTree;
import com.ziyun.auth.util.ZyMapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface OrgTreeMapper extends ZyMapper<OrgTree> {
    /**
     * 根据父级编码获取组织机构树
     * @param orgTree
     * @return
     */
    List<Map<String, Object>> listOrgSys(OrgTree orgTree);

    /**
     * 根据orgCode删除组织机构树
     * @param orgCode
     */
    void deleteOrg(String orgCode);

    /**
     * 获取level小于orgLevel的组织机构
     * @param orgLevel
     * @return
     */
    List<Map<String, Object>> getParentOrgs(Integer orgLevel);

    /**
     * 根据组织机构id，修改组织机构树
     * @param orgTree
     */
    void update(OrgTree orgTree);

    /**
     * 查询所有班级信息
     *
     * @return
     */
    Set<String> selectAllClassCode();
}