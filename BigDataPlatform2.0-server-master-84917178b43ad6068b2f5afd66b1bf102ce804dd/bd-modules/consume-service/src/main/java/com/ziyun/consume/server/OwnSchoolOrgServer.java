package com.ziyun.consume.server;


import com.ziyun.consume.entity.OwnSchoolOrg;
import com.ziyun.consume.vo.Params;

import java.util.List;
import java.util.Set;

public interface OwnSchoolOrgServer {

    /**
     * 根据条件：查询,获取班级的全部信息
     *
     * @param para 前台传过来的查询条件
     * @return 根据查询条件：获取该条件对应的班级列表
     */
    List<OwnSchoolOrg> selectBy(Params para);

    /**
     * 根据当前参数获得该参数的班级集合
     *
     * @param para 参数中的schoolCode、facultyCode、majorCode、classSelect
     * @return classCode的set集
     */
    Set<String> selectOwnClasscode(Params para);
}
