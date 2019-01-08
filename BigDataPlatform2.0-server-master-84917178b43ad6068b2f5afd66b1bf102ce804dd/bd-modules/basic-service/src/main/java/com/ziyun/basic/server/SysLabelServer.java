package com.ziyun.basic.server;

import com.ziyun.basic.entity.SysLabel;

import java.util.List;
import java.util.Map;

/**
 * @description: 系统标签service
 * @author: FubiaoLiu
 * @date: 2018/9/26
 */
public interface SysLabelServer {

    /**
     * 获取标签列表
     *
     * @param params
     * @return
     */
    List<SysLabel> getLabelList(Map<String, Object> params);

    /**
     * 更改标签状态
     *
     * @param params
     */
    void updateStatus(Map<String, Object> params);

    /**
     * 启用所有标签
     *
     * @param params
     */
    void enableAll(Map<String, Object> params);

    /**
     * 获取标签类型列表
     *
     * @return
     */
    List<Map<String, Object>> getLabelTypeList();
}
