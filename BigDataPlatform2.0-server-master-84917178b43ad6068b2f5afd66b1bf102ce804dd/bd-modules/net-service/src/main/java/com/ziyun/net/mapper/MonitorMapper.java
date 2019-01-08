package com.ziyun.net.mapper;


import com.ziyun.net.entity.FireWallFlowMonitor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface MonitorMapper {
    /**
     * 实时在线人数
     */
    int getOnLineNum();

    /**
     * 当前在线人员
     *
     * @return List<LinkedHashMap<String,Object>>
     */
    List<LinkedHashMap<String, Object>> getOnLinePersion();

    /**
     * 获取城市列表
     */
    List<LinkedHashMap<String, String>> getCityList();

    /**
     * 网络速率
     *
     * @return List<FireWallFlowMonitor>
     */
    List<FireWallFlowMonitor> getRate();

    /**
     * 获得当前上线人数
     *
     * @return
     */
    List<Map<String, Object>> getOnlineStudent();
}