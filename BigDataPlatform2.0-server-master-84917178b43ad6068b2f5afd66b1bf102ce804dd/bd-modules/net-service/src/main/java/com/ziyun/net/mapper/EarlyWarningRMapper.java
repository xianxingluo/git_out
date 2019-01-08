package com.ziyun.net.mapper;


import com.ziyun.net.vo.NetParams;
import com.ziyun.net.vo.Params;
import com.ziyun.net.vo.ParamsStatus;

import java.util.List;
import java.util.Map;

public interface EarlyWarningRMapper {

    /**
     * 查询符合预警规则的学生
     *
     * @param params 预警规则
     * @return List
     */
    List<Map<String, Object>> listWarningStudents(Params params);

    Integer countWarningStudents(Params params);

    /**
     * 根据id查询预警规则
     *
     * @param id 预警规则id
     * @return String
     */
    String getWarnRule(String id);

    /*与前台交互部分*/
    List<Map> getEarlyWarnParamShow();

    List<Map> getEarlyWarnRules(Map map);

    int addWarnRule(Map map);

    int delWarnRules(ParamsStatus param);

    /**
     * 用于学业预警
     * 1. 消费金额
     * 2. 上网时长
     *
     * @param para
     * @return
     */
    Double selectTotalResult(NetParams para);
}