package com.ziyun.net.server;


import com.ziyun.net.entity.FireWallFlowMonitor;
import com.ziyun.net.vo.HotSpotVo;
import com.ziyun.net.vo.Params;
import com.ziyun.net.vo.ParamsStatus;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页大屏幕支撑
 *
 * @author 何亚鹏
 * @Date 2017年5月26日
 */
public interface MonitorServer {

    /**
     * 在线人数
     *
     * @return
     */
    int getOnLineNum();

    /**
     * 在线人员信息
     *
     * @return
     */
    List<LinkedHashMap<String, Object>> getOnLinePersion();

    /**
     * ip地理位置信息
     *
     * @return
     */
    Map<String, Object> getIpgeo();

    /**
     * 今日流量数据
     *
     * @return
     */
    List<FireWallFlowMonitor> getRate();

    /**
     * 热点信息
     *
     * @param para
     * @return List<HotSpotVo>
     */
    List<HotSpotVo> getTopHotSpot(Params para);

    /**
     * 执行在线人数取样方法
     */
    void insertOnLineNum();

    /**
     * 查询历史在线人数
     *
     * @return
     */
    List<Map> getOnLineNumHis();

    /**
     * 实现今日上线人数
     * 根据前端传递的时间来判断是否与缓存中的时间一致，不同则重新读取数据库。
     *
     * @return
     */
    Map getOnlineStudent(ParamsStatus param);

    Map<String, Object> getWorldIp();

}
