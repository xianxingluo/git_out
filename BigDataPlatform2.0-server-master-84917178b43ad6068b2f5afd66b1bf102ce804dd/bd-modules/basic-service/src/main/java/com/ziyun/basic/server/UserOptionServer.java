package com.ziyun.basic.server;

import java.util.List;
import java.util.Map;

public interface UserOptionServer {
    /**
     * 新增或者修改用户标签。修改是删除前端传过来的id，并新增信息
     *
     * @param map
     * @return 如果操作成功返回1失败或者没有影响的行数返回0
     */
    int saveUserLabel(Map<String, Object> map);

    /**
     * 根据id删除对应的数据
     *
     * @return
     */
    int deleteUserLabel(Map<String, Object> map);

    /**
     * 获取当前用户所有的标签
     *
     * @return
     */
    List<Map<String, Object>> getUserLabel(String base);

    /**
     * 获取当前用户的对比标签
     *
     * @param base
     * @return
     */
    List<Map<String, Object>> getUserReferenceLabel(String base);

    /**
     * 获取当前id的信息
     *
     * @param id
     * @return
     */
    Map<String, Object> getLabelById(Long id);
}
