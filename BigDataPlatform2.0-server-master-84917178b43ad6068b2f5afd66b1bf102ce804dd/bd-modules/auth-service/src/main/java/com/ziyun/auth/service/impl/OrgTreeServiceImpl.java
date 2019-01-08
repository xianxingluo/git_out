package com.ziyun.auth.service.impl;

import com.ziyun.auth.mapper.OrgTreeMapper;
import com.ziyun.auth.model.OrgTree;
import com.ziyun.auth.service.IOrgTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author leyangjie
 * @Description: 组织机构树
 * @date 2018/4/27 16:12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OrgTreeServiceImpl extends BaseService<OrgTree> implements IOrgTreeService {

    @Autowired
    private OrgTreeMapper orgTreeMapper;

    /**
     * 获取组织机构树
     *
     * @param orgTree
     * @return
     */
    @Override
    public List<Map<String, Object>> listOrgSys(OrgTree orgTree) {
        List<Map<String, Object>> orgTreeList = orgTreeMapper.listOrgSys(orgTree);
        recursion(orgTreeList, orgTree);
        return orgTreeList;
    }

    public void recursion(List<Map<String, Object>> list, OrgTree orgTree) {
        list.forEach(r -> {
            String orgCode = r.get("orgCode").toString();
            orgTree.setParentCode(orgCode);
            List<Map<String, Object>> orgTreeList = orgTreeMapper.listOrgSys(orgTree);
            r.put("son", orgTreeList);
            recursion(orgTreeList, orgTree);
        });
    }

    /**
     * 根据orgCode删除组织机构树
     *
     * @param id
     */
    @Override
    public void deleteOrg(String id) {
        orgTreeMapper.deleteOrg(id);
    }

    /**
     * 根据org_level父组织机构和父组织机构以上节点
     *
     * @param orgLevel
     * @return
     */
    @Override
    public List<Map<String, Object>> getParentOrgs(Integer orgLevel) {
        return orgTreeMapper.getParentOrgs(orgLevel);
    }

    /**
     * 根据组织机构id，修改组织机构树
     *
     * @param orgTree
     */
    @Override
    public void update(OrgTree orgTree) {

        orgTreeMapper.update(orgTree);
    }
}
