package com.ziyun.academic.dao;


import com.ziyun.academic.entity.OwnOrgStudent;
import com.ziyun.academic.entity.OwnOrgStudentType;
import com.ziyun.academic.vo.Params;

import java.util.List;

public interface OwnOrgStudentDao {
    int deleteByPrimaryKey(String outid);

    int insert(OwnOrgStudent record);

    int insertSelective(OwnOrgStudent record);

    OwnOrgStudent selectByPrimaryKey(String outid);

    int updateByPrimaryKeySelective(OwnOrgStudent record);

    int updateByPrimaryKey(OwnOrgStudent record);

    OwnOrgStudentType selectDetail(String outid);

    /**
     * 获取全部学生的：人员相关信息
     *
     * @param para
     * @return
     */
    List<OwnOrgStudentType> selectAllDetail(Params para);

    /**
     * 获取：全部学生的人数
     *
     * @param para
     * @return
     */
    int getCount(Params para);
}