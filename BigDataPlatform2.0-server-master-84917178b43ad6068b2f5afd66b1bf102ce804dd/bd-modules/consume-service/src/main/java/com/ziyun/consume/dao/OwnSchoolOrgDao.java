package com.ziyun.consume.dao;


import com.ziyun.consume.entity.OwnSchoolOrg;
import com.ziyun.consume.vo.Params;

import java.util.List;
import java.util.Set;

public interface OwnSchoolOrgDao {
    int deleteByPrimaryKey(Integer id);

    int insert(OwnSchoolOrg record);

    int insertSelective(OwnSchoolOrg record);

    OwnSchoolOrg selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OwnSchoolOrg record);

    int updateByPrimaryKey(OwnSchoolOrg record);

    List<OwnSchoolOrg> list();

    /**
     * 根据条件：查询,获取班级的全部信息
     *
     * @param para 前台传过来的查询条件
     * @return 根据查询条件：获取该条件对应的班级列表
     */
    List<OwnSchoolOrg> selectBy(Params para);

    Set<String> selectAllClasscode(Params para);
}