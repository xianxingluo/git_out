package com.ziyun.net.mapper;


import com.ziyun.net.annotation.TargetDataSource;
import com.ziyun.net.entity.OnLineNum;

import java.util.List;
import java.util.Map;

public interface OnLineNumMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OnLineNum record);

    int insertSelective(OnLineNum record);

    OnLineNum selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OnLineNum record);

    int updateByPrimaryKey(OnLineNum record);

    @TargetDataSource("mysql")
    void insertOnLineNum();

    List<Map> listOnLineNum();
}