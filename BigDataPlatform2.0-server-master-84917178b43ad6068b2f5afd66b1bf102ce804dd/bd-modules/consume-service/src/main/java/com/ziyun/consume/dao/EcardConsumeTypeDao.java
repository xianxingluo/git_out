package com.ziyun.consume.dao;


import com.ziyun.consume.entity.EcardConsumeType;

public interface EcardConsumeTypeDao {
    int deleteByPrimaryKey(Long acccode);

    int insert(EcardConsumeType record);

    int insertSelective(EcardConsumeType record);

    EcardConsumeType selectByPrimaryKey(Long acccode);

    int updateByPrimaryKeySelective(EcardConsumeType record);

    int updateByPrimaryKey(EcardConsumeType record);
}