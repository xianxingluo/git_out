package com.ziyun.dormitory.mapper;


import com.ziyun.common.model.Params;
import com.ziyun.common.model.ParamsStatus;
import com.ziyun.dormitory.annotation.TargetDataSource;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2017/12/18.
 */
public interface EcardAccessInoutKylinMapper {

    @TargetDataSource("kylin")
    List<Map<String, Object>> lateRatioKylin(Params param);

    @TargetDataSource("kylin")
    List<Map<String, Object>> getLateStudnet(ParamsStatus params);

    /**
     * 宿舍进出流量 -图表
     */
    @TargetDataSource("kylin")
    List<Map<String, Object>> listDormInOutFlows(Params params);

    /**
     * 宿舍进出流量 -列表，统计进、出总人数
     *
     * @param params
     * @return
     */
    @TargetDataSource("kylin")
    List<Map<String, Object>> listDormInOutFlowsAll(Params params);

    @TargetDataSource("kylin")
    List<Map<String, Object>> mybeLate(Params params);

    @TargetDataSource("kylin")
    int mybeLatesize(Params params);

    @TargetDataSource("kylin")
    String getDormPeak(Params params);

    @TargetDataSource("kylin")
    int getDormTotalLate(Params params);

    @TargetDataSource("kylin")
    List<Map<String, Object>> getInOutCount(Params params);

    @TargetDataSource("kylin")
    List<Map<String, Object>> personNoComeBack(Params para);

    @TargetDataSource("kylin")
    Long personNoComeBackSize(ParamsStatus para);

    @TargetDataSource("kylin")
    List<Map<String, Object>> queryNoComeBackByDay(ParamsStatus para);

    @TargetDataSource("kylin")
    Long queryNoComeBackByDaySize(ParamsStatus para);

    @TargetDataSource("kylin")
    List<Map<String, Object>> excelNoComeBackByDay(ParamsStatus para);

}
