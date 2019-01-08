package com.ziyun.basic.server.impl;

import com.ziyun.basic.entity.SysLabel;
import com.ziyun.basic.mapper.SysLabelMapper;
import com.ziyun.basic.server.SysLabelServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @description: 系统标签service
 * @author: FubiaoLiu
 * @date: 2018/9/26
 */
@Service
public class SysLabelServerImpl implements SysLabelServer {
    @Autowired
    private SysLabelMapper sysLabelMapper;

    /**
     * 获取标签列表
     *
     * @param params
     * @return
     */
    @Override
    public List<SysLabel> getLabelList(Map<String, Object> params) {
        return sysLabelMapper.getLabelList(params);
    }

    /**
     * 更改标签状态
     *
     * @param params
     */
    @Override
    public void updateStatus(Map<String, Object> params) {
        sysLabelMapper.updateStatus(params);
    }

    /**
     * 启用所有标签
     *
     * @param params
     */
    @Override
    public void enableAll(Map<String, Object> params) {
        sysLabelMapper.enableAll(params);
    }

    /**
     * 获取标签类型列表
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> getLabelTypeList() {
        return sysLabelMapper.getLabelTypeList();
    }
}
