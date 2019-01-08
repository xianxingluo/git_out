package com.ziyun.tracking.server.impl;


import com.ziyun.common.constant.AuthConstant;
import com.ziyun.common.tools.MapUtil;
import com.ziyun.tracking.entity.EventTracking;
import com.ziyun.tracking.mapper.EventTrackingMapper;
import com.ziyun.tracking.server.EventTrackingServer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @description: 埋点服务
 * @author: FubiaoLiu
 * @date: 2018/9/25
 */
@Service
public class EventTrackingServerImpl implements EventTrackingServer {

    private static Logger logger = Logger.getLogger(EventTrackingServerImpl.class);

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    public EventTrackingMapper eventTrackingMapper;


    /**
     * 保存埋点数据
     *
     * @param params
     */
    @Override
    public void save(Map<String, Object> params) {
        EventTracking eventTracking = new EventTracking();
        MapUtil.copyPropertiesInclude(params, eventTracking);
        eventTracking.setUsername(params.get(AuthConstant.LOGON_PARAM_USERNAME).toString());
        eventTracking.setCreateTime(new Date());
        eventTrackingMapper.insert(eventTracking);
    }

}
