package com.ziyun.basic.server.impl;


import com.ziyun.basic.mapper.OwnSchoolOrgMapper;
import com.ziyun.basic.entity.OwnSchoolOrg;
import com.ziyun.basic.server.OwnSchoolOrgServer;
import com.ziyun.basic.vo.Params;
import com.ziyun.utils.cache.CacheConstant;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class OwnSchoolOrgServerImpl implements OwnSchoolOrgServer {

    private static Logger logger = Logger
            .getLogger(OwnSchoolOrgServerImpl.class);

    @Autowired
    public OwnSchoolOrgMapper ownSchoolOrgMapper;

    @Cacheable(value = CacheConstant.BASIC_CACHE, key = "'base:organization:' +#root.methodName +'.' + #para.hashCode()")
    public List<OwnSchoolOrg> selectBy(Params para) {
        return ownSchoolOrgMapper.selectBy(para);
    }

    @Cacheable(value = CacheConstant.BASIC_CACHE, key = "'base:organization:' +#root.methodName +'.' + #para.hashCode()")
    @Override
    public Set<String> selectOwnClasscode(Params para) {
        return ownSchoolOrgMapper.selectAllClasscode(para);
    }

}
