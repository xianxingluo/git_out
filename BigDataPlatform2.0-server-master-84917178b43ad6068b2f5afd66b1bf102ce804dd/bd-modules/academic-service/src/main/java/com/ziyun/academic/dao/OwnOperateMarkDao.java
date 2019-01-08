package com.ziyun.academic.dao;


import com.ziyun.academic.entity.OwnOperateMark;

public interface OwnOperateMarkDao {
    int deleteByPrimaryKey(String tableName);

    int insert(OwnOperateMark record);

    int insertSelective(OwnOperateMark record);

    OwnOperateMark selectByPrimaryKey(String tableName);

    int updateByPrimaryKeySelective(OwnOperateMark record);

    int updateByPrimaryKey(OwnOperateMark record);
}