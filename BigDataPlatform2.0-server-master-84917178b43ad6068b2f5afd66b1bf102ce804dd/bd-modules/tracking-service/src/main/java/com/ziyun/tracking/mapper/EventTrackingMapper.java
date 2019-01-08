package com.ziyun.tracking.mapper;

import com.ziyun.tracking.entity.EventTracking;

/**
 * @description: 埋点mapper
 * @author: FubiaoLiu
 * @date: 2018/9/25
 */
public interface EventTrackingMapper{

    /**
     * 保存埋点数据
     * @param eventTracking
     */
    void insert(EventTracking eventTracking);
}