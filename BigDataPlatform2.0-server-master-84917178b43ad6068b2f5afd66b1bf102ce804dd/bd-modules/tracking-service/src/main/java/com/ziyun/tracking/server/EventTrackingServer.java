package com.ziyun.tracking.server;

import java.util.Map;

/**
 * @description: 埋点服务
 * @author: FubiaoLiu
 * @date: 2018/9/25
 */
public interface EventTrackingServer {

    /**
     * 保存埋点数据
     * @param params
     */
    void save(Map<String, Object> params);

}
