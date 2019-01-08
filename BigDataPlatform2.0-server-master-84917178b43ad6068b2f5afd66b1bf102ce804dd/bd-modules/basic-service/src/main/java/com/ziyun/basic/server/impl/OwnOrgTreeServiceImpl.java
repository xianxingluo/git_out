package com.ziyun.basic.server.impl;

import com.ziyun.basic.entity.OwnOrgStree;
import com.ziyun.basic.mapper.OwnOrgTreeMapper;
import com.ziyun.basic.server.IOwnOrgTreeService;
import com.ziyun.basic.vo.Params;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 组织机构数，业务实现类
 */
@Service
public class OwnOrgTreeServiceImpl implements IOwnOrgTreeService {

    @Autowired
    private OwnOrgTreeMapper ownOrgTreeMapper;

    /**
     * 根据组织机构编号，查询对应的组织机构信息
     *
     * @param params
     * @return
     */
    @Override
    public OwnOrgStree getPcodeByCcode(Params params) {
        return ownOrgTreeMapper.getPcodeByCcode(params);
    }

    /**
     * 根据父级机构 code，查询子组织机构
     *
     * @param params
     * @return
     */
    @Override
    public List<Map> getChildrenList(Params params) {
        return ownOrgTreeMapper.getChildrenList(params);
    }

    /**
     * 根据父级机构 code，查询子组织机构
     *
     * @param
     * @return
     */
    @Override
    public List<Map> getAllTopCategory(Params params) {
        return ownOrgTreeMapper.getAllTopCategory(params);
    }
}
