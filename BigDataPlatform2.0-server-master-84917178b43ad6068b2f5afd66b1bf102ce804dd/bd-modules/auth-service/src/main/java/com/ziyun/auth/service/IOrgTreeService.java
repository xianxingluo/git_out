package com.ziyun.auth.service;

import com.ziyun.auth.model.OrgTree;

import java.util.List;
import java.util.Map;

/**
 * @author leyangjie
 * @Description: 组织机构树
 * @date 2018/4/27 16:11
 */
public interface IOrgTreeService extends IService<OrgTree> {
    List<Map<String, Object>> listOrgSys(OrgTree orgTree);

    void deleteOrg(String id);

    List<Map<String, Object>> getParentOrgs(Integer orgTree);

    void update(OrgTree orgTree);
}
