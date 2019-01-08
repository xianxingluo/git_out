package com.ziyun.net.mapper;


import com.ziyun.net.annotation.TargetDataSource;
import com.ziyun.net.vo.NetParams;
import com.ziyun.net.vo.Params;
import com.ziyun.net.vo.ResultData;

import java.util.List;
import java.util.Map;

public interface A3RadacctTimeKylinMapper {
    /**
     * 上网时长分析： Chart & Table
     *
     * @param para
     * @return
     */
    @TargetDataSource("kylin")
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
    @TargetDataSource("kylin")
    List<ResultData> selectDurationTopChartAndTable(NetParams para);

    /**
     * 上网时长TOP10： 记录数
     *
     * @param para
     */
    @TargetDataSource("kylin")
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
    @TargetDataSource("kylin")
    List<ResultData> selectVisitContextChartAndTable(NetParams para);

    /**
     * 访问内容: 记录数
     *
     * @param para
     * @return
     */
    @TargetDataSource("kylin")
    int selectVisitContextTopRecordNum(Params para);

    /**
     * 上网时段： Chart
     *
     * @param para
     * @return
     */
    @TargetDataSource("kylin")
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
    @TargetDataSource("kylin")
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
    @TargetDataSource("kylin")
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
    @TargetDataSource("kylin")
    List<Map<String, Object>> selectFluxChartAndTable(NetParams para);

    /**
     * 上网流量: 总流量
     *
     * @param para
     * @return
     */
    @TargetDataSource("kylin")
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
    @TargetDataSource("kylin")
    List<Map<String, Object>> selectTotalDurationDistChartAndTablePersonal(NetParams para);

    /**
     * 上网总时长: 个人: 根据outid获取classcode,可能有多个(留级情况)
     *
     * @param para
     * @return
     */
    @TargetDataSource("kylin")
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
    @TargetDataSource("kylin")
    double selectAvgDuration4OverviewCommunity(NetParams para);

    /**
     * 按照学号分组：查询上网总时长
     *
     * @param para
     * @return
     */
    @TargetDataSource("kylin")
    List<ResultData> selectNetTime(NetParams para);

    /**
     * 上网模块-- 个人档案--个人上网时长
     *
     * @return
     */
    @TargetDataSource("kylin")
    Map<String, Object> getPersonNetTime(NetParams params);

    @TargetDataSource("kylin")
    List<Map<String, Object>> everyDaySufferInternetTime(NetParams para);

    @TargetDataSource("kylin")
    int everyDaySufferInternetTimeCount(NetParams para);

    List<Map<String, Object>> getNetEarlywarnList(NetParams para);

    int getNetEarlywarnCount(NetParams para);
}
