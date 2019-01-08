package com.ziyun.chart.mapper;

import com.ziyun.chart.entity.ChartRelation;
import com.ziyun.chart.util.ZyMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @description: 模块图表关系
 * @author: FubiaoLiu
 * @date: 2018/10/16
 */
public interface ChartRelationMapper extends ZyMapper<ChartRelation> {

    /**
     * 根据code查询关联关系
     *
     * @param code
     * @return
     */
    ChartRelation selectByCode(@Param(value = "code") String code);
}