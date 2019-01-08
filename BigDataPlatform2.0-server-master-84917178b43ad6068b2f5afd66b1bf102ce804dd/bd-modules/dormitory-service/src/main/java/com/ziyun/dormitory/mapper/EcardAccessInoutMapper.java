package com.ziyun.dormitory.mapper;

import com.ziyun.common.model.Params;
import com.ziyun.common.model.ParamsStatus;
import com.ziyun.common.model.ResultData;
import com.ziyun.dormitory.annotation.TargetDataSource;
import com.ziyun.dormitory.model.EcardAccessInout;

import java.util.List;
import java.util.Map;

public interface EcardAccessInoutMapper {

    // ++++++++++业务查询++++++++++++

    // -- ｛社群学业分析：成绩提高分析｝4、作息规律统计: -
    // 由于是统计进出时间点，所以不能用按照天拆开的access_record_inout表，因为多了0点的记录

    /**
     * 4.1.0、进宿舍时间点汇总：：最早时间、最晚时间
     * <p>
     * 用于按照学号查询的时候：来确定按照日期统计的起始、结束日期
     *
     * @param para
     * @return
     */
    @TargetDataSource("mysql")
    ResultData inWeekHourListTimes(Params para);


    /**
     * (新版)4.1.0、进宿舍时间点汇总：：最早时间、最晚时间
     * <p>
     * <!-- 10.3.1、宿舍流量:按照周几、时间段：统计进入宿舍人数：：最早最晚时间 -->
     * <p>
     * 用于按照学号查询的时候：来确定按照日期统计的起始、结束日期
     *
     * @param para
     * @return
     */
    @TargetDataSource("mysql")
    ResultData inWeekHourListTimesNew(Params para);

    /**
     * 4.1、进宿舍时间点汇总:查询条件时间根据：开始时间
     *
     * @param para
     * @return
     */
    @TargetDataSource("mysql")
    List<ResultData> inWeekHourList(Params para);

    /**
     * (新版)4.1、进宿舍时间点汇总:查询条件时间根据：开始时间
     * <p>
     * <!-- 10.3.1、宿舍流量:按照周几、时间段：统计进入宿舍人数 -->
     * <p>
     * {这里是按照周几统计人数。所以不能直接count(DISTINCT(b.outid))，而是按照每一天统计人数，再除以出现了多少次周几}
     *
     * @param para
     * @return
     */
    @TargetDataSource("mysql")
    List<ResultData> inWeekHourListNew(Params para);


    /**
     * 4.2.0、出宿舍时间点汇总：：最早时间、最晚时间
     * <p>
     * 用于按照学号查询的时候：来确定按照日期统计的起始、结束日期
     *
     * @param para
     * @return
     */
    @TargetDataSource("mysql")
    ResultData outWeekHourListTimes(Params para);

    /**
     * (新版)4.2.0、出宿舍时间点汇总：：最早时间、最晚时间
     * <p>
     * <!-- 10.3.1、宿舍流量:按照周几、时间段：统计离开宿舍人数：：最早最晚时间 -->
     * <p>
     * 用于按照学号查询的时候：来确定按照日期统计的起始、结束日期
     *
     * @param para
     * @return
     */
    @TargetDataSource("mysql")
    ResultData outWeekHourListTimesNew(Params para);

    /**
     * 4.2、出宿舍时间点汇总:查询条件时间根据：开始时间
     *
     * @param para
     * @return
     */
    @TargetDataSource("mysql")
    List<ResultData> outWeekHourList(Params para);

    /**
     * （新版）4.2、出宿舍时间点汇总:查询条件时间根据：开始时间
     * <p>
     * <!-- 10.3.1、宿舍流量:按照周几、时间段：统计离开宿舍人数-->
     * <p>
     * {这里是按照周几统计人数。所以不能直接count(DISTINCT(b.outid))，而是按照每一天统计人数，再除以出现了多少次周几}
     *
     * @param para
     * @return
     */
    @TargetDataSource("mysql")
    List<ResultData> outWeekHourListNew(Params para);
    // --

    /**
     * ｛行为综合分析｝5.1、本月作息占比:按照天汇总{在宿舍的时间}
     *
     * @param para
     * @return
     */
    @TargetDataSource("mysql")
    List<ResultData> mouthRestList(Params para);

    /**
     * ｛行为综合分析｝5.1、本月作息占比:按照天汇总{在宿舍的上网时间}
     *
     * @param para
     * @return
     */
    @TargetDataSource("mysql")
    List<ResultData> mouthRestNetList(Params para);


    ///<!-- 为提高查询效率：按照每个人、周几、时间段：统计在宿舍的人数。｛用在宿舍流量：显示在宿舍人数｝ -->

    /**
     * 10.1.1、宿舍流量｛用在宿舍流量：显示在宿舍人数｝：按照周几、时间段统计在宿舍的人数：：查询最早最晚时间
     *
     * @param para
     * @return
     */
    @TargetDataSource("mysql")
    ResultData onWeekHourListTimes(Params para);


    /**
     * 10.1.2、宿舍流量｛用在宿舍流量：显示在宿舍人数｝：按照周几、时间段统计在宿舍的人数
     *
     * @param para
     * @return
     */
    @TargetDataSource("mysql")
    List<ResultData> onWeekHourList(Params para);
    ///<!-- 为提高查询效率：按照每个人、周几、时间段：统计在宿舍的人数。｛用在宿舍流量：显示在宿舍人数｝ -->


    /// 宿舍流量：按照天、小时统计

    /**
     * 10.2.1、宿舍流量：按照天、小时统计：进入宿舍人数
     *
     * @param para
     * @return
     */
    @TargetDataSource("mysql")
    List<ResultData> dayHourIn(Params para);

    /**
     * 10.2.2、宿舍流量：按照天、小时统计：离开宿舍人数
     *
     * @param para
     * @return
     */
    @TargetDataSource("mysql")
    List<ResultData> dayHourOut(Params para);

    /**
     * 10.2.3、宿舍流量：按照天、小时统计：呆在宿舍人数
     *
     * @param para
     * @return
     */
    @TargetDataSource("mysql")
    List<ResultData> dayHourOn(Params para);
    /// 宿舍流量：按照天、小时统计

    /**
     * 获取学生进出宿舍数据中的年份
     *
     * @param params
     * @return
     */
    @TargetDataSource("mysql")
    List<String> getStudentDormYears(Params params);

    /**
     * 查询学生一年中每一天的进出宿舍次数
     *
     * @param params
     * @return
     */
    @TargetDataSource("mysql")
    List<Map<String, Object>> getStudentInOutDormTimes(Params params);

    /**
     * 查询学生一周的宿舍进出情况
     *
     * @param params
     * @return
     */
    @TargetDataSource("mysql")
    List<Map<String, Object>> listStudentInOutDormWeekView(Params params);

    /**
     * 个人画像-宿舍出入特征-24小时打卡
     *
     * @param params
     * @return
     */
    @TargetDataSource("mysql")
    List<Map<String, Object>> listStudentInOutDormHours(Params params);

    /**
     * 24小时打卡，对23:00-24:00这个小时分两个半小时处理，23:30以后的算晚归
     *
     * @param params
     * @return
     */
    @TargetDataSource("mysql")
    Map<String, Long> getInOutDormTimesHalfHour(Params params);

    /**
     * 获取个人晚归次数
     *
     * @param params
     * @return
     */
    @TargetDataSource("mysql")
    Integer lateTimesBackToDorm(Params params);

    // ++++++++++业务查询++++++++++++

    @TargetDataSource("mysql")
    int deleteByPrimaryKey(Long id);

    @TargetDataSource("mysql")
    int insert(EcardAccessInout record);

    @TargetDataSource("mysql")
    int insertSelective(EcardAccessInout record);

    @TargetDataSource("mysql")
    EcardAccessInout selectByPrimaryKey(Long id);

    @TargetDataSource("mysql")
    int updateByPrimaryKeySelective(EcardAccessInout record);

    @TargetDataSource("mysql")
    int updateByPrimaryKey(EcardAccessInout record);

    @TargetDataSource("mysql")
    Map getLateToBedroom(Params param);

    @TargetDataSource("mysql")
    int getLateToBedroomNum(Params param);

    @TargetDataSource("mysql")
    List<Map> getClockRecord(ParamsStatus params);

    @TargetDataSource("mysql")
    int getClockRecordLength(ParamsStatus params);

    @TargetDataSource("mysql")
    List<Map> getRestRegular(ParamsStatus params);

    @TargetDataSource("mysql")
    List<Map> getLateDetail(Params params);

    @TargetDataSource("mysql")
    Map getLateDetailCount(Params params);

    @TargetDataSource("mysql")
    List<Map<String, Object>> personLater(Params para);

    @TargetDataSource("mysql")
    Long personLaterSize(ParamsStatus para);

    @TargetDataSource("kylin")
    Long personNoComeBackSize(ParamsStatus para);
}