package com.ziyun.academic.dao;


import com.ziyun.academic.vo.NetParams;
import com.ziyun.academic.vo.Params;
import com.ziyun.academic.vo.ResultData;

import java.util.List;
import java.util.Map;

public interface A3RadacctTimeKylinDao {
    /**
     * 上网时长分析： Chart & Table
     *
     * @param para
     * @return
     */
    List<ResultData> selectTotalDurationDistChartAndTable(NetParams para);

    /**
     * 上网时长分析： 记录数
     *
     * @param para
     */
    int selectDurationDistRecordNum(Params para);

    /**
     * 上网人群分析: Chart & Table
     *
     * @param para
     * @return
     */
    List<ResultData> selectCrowdAnalysisChartAndTable(NetParams para);

    /**
     * 上网人群分析： 记录数
     *
     * @param para
     * @return
     */
    int selectCrowdAnalysisRecordNum(Params para);

    /**
     * 上网时长TOP10: Chart & Table
     *
     * @param para
     * @return
     */
    List<ResultData> selectDurationTopChartAndTable(NetParams para);

    /**
     * 上网时长TOP10： 记录数
     *
     * @param para
     */
    int selectDurationTopRecordNum(Params para);

    /**
     * 上网终端： Chart & Table (pc & mobile)
     *
     * @param para
     * @return
     */
    List<ResultData> selectTerminalTypeChartAndTable(NetParams para);

    /**
     * 访问内容： Chart & Table
     *
     * @param para
     * @return
     */
    List<ResultData> selectVisitContextChartAndTable(NetParams para);

    /**
     * 访问内容: 记录数
     *
     * @param para
     * @return
     */
    int selectVisitContextTopRecordNum(Params para);

    /**
     * 上网时段： Chart
     *
     * @param para
     * @return
     */
    List<ResultData> selectPeriodChart(NetParams para);

    /**
     * 上网时段： Table
     *
     * @param para
     * @return
     */
    List<ResultData> selectPeriodTable(NetParams para);

    /**
     * 上网特征 总时长分布、人群分析、时长TOP、时段分析： 获取开始结束时间
     *
     * @param para
     * @return
     */
    ResultData selectStartEndTime(NetParams para);

    /**
     * 总人数： 男 + 女
     *
     * @param para
     * @return
     */
    int selectTotalPeople(Params para);


    /**
     * 总人数： 男
     *
     * @param para
     * @return
     */
    int selectTotalPeopleMale(Params para);

    /**
     * 总人数： 女
     *
     * @param para
     * @return
     */
    int selectTotalPeopleFemale(Params para);

    /**
     * 上网内容热度: 下拉框 serv app类型
     *
     * @param para
     * @return
     */
    List<Map<String, Object>> selectContentHeatServAppType(NetParams para);

    /**
     * 上网内容热度: 下拉框 serv 根据流量倒序排名
     *
     * @param para
     * @return
     */
    List<Map<String, Object>> selectContentHeatServFlux(NetParams para);

    /**
     * 上网内容热度
     *
     * @param para
     * @return
     */
    List<Map<String, Object>> selectContentHeatChartAndTable(NetParams para);

    /**
     * 根据app获取学生列表
     *
     * @param para
     * @return
     */
    List<Map<String, Object>> selectStudentListByApp(NetParams para);

    /**
     * 根据app获取学生列表: 总记录数
     *
     * @param para
     * @return
     */
    int selectAppStudentRecordNum(NetParams para);

    /**
     * 上网内容热度: get min, max of record_date
     *
     * @param para
     * @return
     */
    Map<String, Object> selectStartEndTime4ContentHeat(NetParams para);

    /**
     * 上网内容热度: 总记录数
     *
     * @param para
     * @return
     */
    int selectContentHeatRecordNum(Params para);

    /**
     * 上网流量
     *
     * @param para
     * @return
     */
    List<Map<String, Object>> selectFluxChartAndTable(NetParams para);

    /**
     * 上网流量: 总流量
     *
     * @param para
     * @return
     */
    Long selectFluxTotal4OverviewCommunity(NetParams para);

    /**
     * 上网流量: 总记录数
     *
     * @param para
     * @return
     */
    int selectFluxRecordNum(Params para);

    /**
     * 上网总时长: 个人: Chart
     *
     * @param para
     * @return
     */
    List<Map<String, Object>> selectTotalDurationDistChartAndTablePersonal(NetParams para);

    /**
     * 上网总时长: 个人: 根据outid获取classcode,可能有多个(留级情况)
     *
     * @param para
     * @return
     */
    List<Map<String, String>> selectClassCodeByOutid(NetParams para);

    /**
     * 个人画像: 上网特征: 上网总时长
     * discard in V3.0.0
     *
     * @param para
     * @return
     */
    int selectNetOverviewPersonal(NetParams para);

    /**
     * 社群概述: 人均上网时长
     *
     * @param para
     * @return
     */
    double selectAvgDuration4OverviewCommunity(NetParams para);
}