package com.ziyun.basic.server;

import com.ziyun.basic.entity.OwnOrgStree;
import com.ziyun.basic.vo.Params;


import java.util.List;
import java.util.Map;

/**
 * 组织结构树，业务service接口
 */
public interface IOwnOrgTreeService {

    OwnOrgStree getPcodeByCcode(Params params);

    List<Map> getChildrenList(Params params);

    List<Map> getAllTopCategory(Params params);
}
