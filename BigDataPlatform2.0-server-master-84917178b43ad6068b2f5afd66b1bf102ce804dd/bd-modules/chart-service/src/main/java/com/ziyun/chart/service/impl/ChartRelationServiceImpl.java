package com.ziyun.chart.service.impl;


import com.ziyun.chart.entity.ChartRelation;
import com.ziyun.chart.mapper.ChartRelationMapper;
import com.ziyun.chart.service.ChartRelationService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @description: 模块图表关系
 * @author: FubiaoLiu
 * @date: 2018/10/16
 */
@Service
public class ChartRelationServiceImpl implements ChartRelationService {

    private static Logger logger = Logger.getLogger(ChartRelationServiceImpl.class);

    @Autowired
    public ChartRelationMapper chartRelationMapper;


    /**
     * 保存模块图表关系
     *
     * @param relation
     */
    @Override
    public void save(ChartRelation relation) {
        ChartRelation chartRelation = chartRelationMapper.selectByCode(relation.getCode());
        if (null == chartRelation) {
            relation.setCreateTime(new Date());
            relation.setUpdateTime(new Date());
            chartRelationMapper.insert(relation);
        } else if (!relation.getChartType().equals(chartRelation.getChartType())) {
            relation.setId(chartRelation.getId());
            chartRelationMapper.updateByPrimaryKeySelective(relation);
        }
    }

    /**
     * 根据code查询关联关系
     *
     * @param code
     * @return
     */
    @Override
    public ChartRelation selectByCode(String code) {
        return chartRelationMapper.selectByCode(code);
    }

}
