package com.ziyun.academic.dao;


import com.ziyun.academic.entity.OnLineNum;

import java.util.List;
import java.util.Map;

public interface OnLineNumDao {
    int deleteByPrimaryKey(Integer id);

    int insert(OnLineNum record);

    int insertSelective(OnLineNum record);

    OnLineNum selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OnLineNum record);

    int updateByPrimaryKey(OnLineNum record);

    void insertOnLineNum();

    List<Map> listOnLineNum();
}