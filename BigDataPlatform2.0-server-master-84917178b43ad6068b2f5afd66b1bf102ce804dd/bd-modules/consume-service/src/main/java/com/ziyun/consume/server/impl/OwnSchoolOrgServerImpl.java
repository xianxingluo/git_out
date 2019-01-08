package com.ziyun.consume.server.impl;


import com.ziyun.consume.dao.OwnSchoolOrgDao;
import com.ziyun.consume.entity.OwnSchoolOrg;
import com.ziyun.consume.server.OwnSchoolOrgServer;
import com.ziyun.consume.vo.Params;
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
    public OwnSchoolOrgDao ownSchoolOrgDao;

    @Cacheable(value = CacheConstant.ORGANIZATION_CACHE, key = "#root.targetClass.hashCode() +'.' +#root.methodName +'.' + #para.hashCode()")
    public List<OwnSchoolOrg> selectBy(Params para) {
        return ownSchoolOrgDao.selectBy(para);
    }

    @Cacheable(value = CacheConstant.ORGANIZATION_CACHE, key = "#root.targetClass.hashCode() +'.' +#root.methodName +'.' + #para.hashCode()")
    @Override
    public Set<String> selectOwnClasscode(Params para) {
        // TODO Auto-generated method stub
        return ownSchoolOrgDao.selectAllClasscode(para);
    }

}
