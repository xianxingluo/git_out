package com.ziyun.dormitory.service;

import com.ziyun.common.model.Params;
import com.ziyun.common.model.ParamsStatus;

import java.util.List;
import java.util.Map;

/**
 * 宿舍刷卡记录：
 *
 * 在宿舍的时间
 *
 * 在宿舍上网时间
 *
 * @Description:
 * @Created by liquan
 * @date 2017年5月27日 下午9:45:55
 *
 */
public interface IEcardAccessInoutService {

	// -- ｛社群学业分析：成绩提高分析｝4、作息规律统计: -
	// 由于是统计进出时间点，所以不能用按照天拆开的access_record_inout表，因为多了0点的记录
	/**
	 * 4.1、进宿舍时间点汇总:查询条件时间根据：开始时间
	 *
	 * 周几排序、各个小时：汇总平均列表
	 *
	 * @param para
	 * @return
	 */
	List inWeekHourList(Params para)  throws Exception;

	/**
	 * 4.2、出宿舍时间点汇总:查询条件时间根据：开始时间
	 *
	 *  周几排序、各个小时：汇总平均列表
	 *
	 * @param para
	 * @return
	 */
	List outWeekHourList(Params para) throws Exception;
	// --

	/**
	 * ｛行为综合分析｝5.1、本月作息占比:按照天汇总{在宿舍的时间}
	 *
	 * @param para
	 * @return
	 */
	List<Map<String, Object>> mouthRestList(Params para) throws Exception;

	/**
	 * ｛行为综合分析｝5.1、本月作息占比:按照天汇总{在宿舍的上网时间}
	 *
	 * @param para
	 * @return
	 */
	List<Map<String, Object>> mouthRestNetList(Params para) throws Exception;

	/**
	 * 10.1.2、宿舍流量｛用在宿舍流量：显示在宿舍人数｝：按照周几、时间段统计在宿舍的人数
	 *
	 * @param para
	 * @return
	 * @throws Exception
	 */
	List onWeekHourList(Params para) throws Exception;

	/**
	 * 10.2.3、宿舍流量：按照天、小时统计：{进入宿舍人数（into）、离开宿舍人数（out）、在宿舍的人数（on）}
	 *
	 * @param para
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> dayHour(Params para) throws Exception;

	Map getLateToBedroom(Params para) throws Exception;

	/**
	 * 个人画像-宿舍出入情况新需求
	 * @param params
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getStudentInOutDormTimes(Params params) throws Exception;

	/**
	 * 获取学生进出宿舍数据中的年份
	 * @param params
	 * @return
	 */
	List<String> getStudentDormYears(Params params);

	/**
	 * 社群画像 - 宿舍进出流量
	 */
	List<Map<String, Object>> listDormInOutFlows(Params params);

	List<Map> getLateRatio(Params params);

	List<Map<String,Object>> getStudnetLateRatio(ParamsStatus params);

	/**
	 * 查询学生一周的宿舍进出情况
	 * @param params
	 * @returnlist
	 */
	List<Map<String, Object>> listStudentInOutDormWeekView(Params params);

	/**
	 *  宿舍进出流量 -列表，统计进、出总人数
	 * @param params
	 * @return
	 */
	Map<String, Object> listDormInOutFlowsAll(Params params);

	/**
	 * 个人画像-宿舍出入特征-24小时打卡
	 * @param params
	 * @return
	 */
	Integer[] listStudentInOutDormHours(Params params);

	/**
	 * 个人画像 - 宿舍出入特征
	 * @param params
	 * @return
	 */
	Map<String, Object> listStuDormFeature(Params params);

	/**
	 * 宿舍出入-可能晚归
	 * @param params
	 * @return
	 */
	List<Map<String,Object>> getMybeLate(Params params);

	int getMybeLatesize(Params params);

	Map<String,Object> dormSummarize(Params params);

	List<Map> getClockRecord(ParamsStatus params);

	int getClockRecordLength(ParamsStatus params);

	List<Map> getRestRegular(ParamsStatus params);

	/**
	 * 晚归详细人数
	 * @param params
	 * @return
	 */
	List<Map> getLateDetail(Params params);

	/**
	 * 晚归总人数
	 * @param params
	 * @return
	 */
	Map getLateDetailCount(Params params);

    /**
     * 晚归情况
     *
     * @param para
     * @return
     */
    List<Map<String, Object>> personLater(ParamsStatus para);

    Long personLaterSize(ParamsStatus para);

    /**
     * 可能未归情况
     *
     * @param para
     * @return
     */
    List<Map<String, Object>> personNoComeBack(ParamsStatus para);

    Long personNoComeBackSize(ParamsStatus para);
	/**
	 * 根据日期查询可能未归学生
	 * @param para
	 * @return
	 */
	List<Map<String, Object>> queryNoComeBackByDay(ParamsStatus para);
	Long queryNoComeBackByDaySize(ParamsStatus para);
	/**
	 * 根据日期导出可能未归学生到excel
	 * @param para
	 * @return
	 */
	List<Map<String, Object>> excelNoComeBackByDay(ParamsStatus para);
}
