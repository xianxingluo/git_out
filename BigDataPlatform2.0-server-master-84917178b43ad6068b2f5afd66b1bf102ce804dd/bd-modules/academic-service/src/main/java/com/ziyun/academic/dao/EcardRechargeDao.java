package com.ziyun.academic.dao;


import com.ziyun.academic.entity.EcardRecharge;

public interface EcardRechargeDao {
    int deleteByPrimaryKey(Long id);

    int insert(EcardRecharge record);

    int insertSelective(EcardRecharge record);

    EcardRecharge selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EcardRecharge record);

    int updateByPrimaryKey(EcardRecharge record);
}