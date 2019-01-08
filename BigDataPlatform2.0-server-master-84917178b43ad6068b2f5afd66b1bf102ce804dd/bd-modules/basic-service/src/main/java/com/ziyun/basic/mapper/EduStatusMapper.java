package com.ziyun.basic.mapper;


import com.ziyun.basic.entity.EduStatus;

public interface EduStatusMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EduStatus record);

    int insertSelective(EduStatus record);

    /* EduStatus selectByPrimaryKey(Integer id); */

    /**
     * 根据学号查询：学生学籍信息
     *
     * @param outid 学号
     * @return
     */
    EduStatus selectByOutid(String outid);

    int updateByPrimaryKeySelective(EduStatus record);

    int updateByPrimaryKey(EduStatus record);

}