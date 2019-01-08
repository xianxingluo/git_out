package com.ziyun.chart.service;

import com.ziyun.chart.entity.ChartRelation;

/**
 * @description: 模块图表关系
 * @author: FubiaoLiu
 * @date: 2018/10/16
 */
public interface ChartRelationService {

    /**
     * 保存模块图表关系
     *
     * @param relation
     */
    void save(ChartRelation relation);

    /**
     * 根据code查询关联关系
     *
     * @param code
     * @return
     */
    ChartRelation selectByCode(String code);
}
