package com.ziyun.academic.dao;

import com.ziyun.academic.vo.Params;
import com.ziyun.academic.vo.ParamsStatus;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2017/12/18.
 */
public interface EcardAccessInoutKylinDao {

    List<Map<String, Object>> lateRatioKylin(Params param);

    List<Map<String, Object>> getLateStudnet(ParamsStatus params);

    /**
     * 宿舍进出流量 -图表
     */
    List<Map<String, Object>> listDormInOutFlows(Params params);

    /**
     * 宿舍进出流量 -列表，统计进、出总人数
     *
     * @param params
     * @return
     */
    List<Map<String, Object>> listDormInOutFlowsAll(Params params);

    List<Map<String, Object>> mybeLate(Params params);

    int mybeLatesize(Params params);

    String getDormPeak(Params params);

    int getDormTotalLate(Params params);

}
