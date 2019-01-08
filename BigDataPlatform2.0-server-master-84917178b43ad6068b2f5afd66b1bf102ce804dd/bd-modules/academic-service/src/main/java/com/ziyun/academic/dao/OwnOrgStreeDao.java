package com.ziyun.academic.dao;


import com.ziyun.academic.entity.OwnOrgStree;
import com.ziyun.academic.vo.Params;

import java.util.List;
import java.util.Map;

public interface OwnOrgStreeDao {
    int deleteByPrimaryKey(Integer id);

    int insert(OwnOrgStree record);

    int insertSelective(OwnOrgStree record);

    OwnOrgStree selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OwnOrgStree record);

    int updateByPrimaryKey(OwnOrgStree record);

    List<Map> getTopCategory(Params params);

    List<Map> getTopCategoryByFunction(Params params);

    List<Map> getAllTopCategory(Params params);

    //获得所有父级id
    OwnOrgStree getPcodeByCcode(Params params);

    //获得单独子级数据
    List<Map> getChildrenList(Params params);

}