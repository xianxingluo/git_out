package com.ziyun.borrow.service;

import com.ziyun.common.model.Params;
import com.ziyun.common.model.ParamsStatus;

import java.text.ParseException;
import java.util.List;
import java.util.Map;


public interface IEduBorrowService {

	/**
	 * 1、 借阅书籍排名top10
	 *
	 * @param para
	 * @return
	 */
	List<Map> bookTopList(Params para) throws Exception;

	/**
	 * 2、 借阅书籍数量top10 -相同的书，也累计次数
	 *
	 * 新增了：月平均借阅
	 *
	 * @param para
	 * @return
	 */
	List<Map> peopleTopList(Params para) throws Exception;

	/**
	 * 3、 借阅频次TOP10 -还得根据选择的时间计算月，然后算出每月频次：
	 *
	 * @param para
	 * @return
	 */
	List<Map> avgPeopleTopList(Params para) throws Exception;

	/**
	 * 4、 借阅书籍类型 :全部分类
	 *
	 * @param para
	 * @return
	 */
	List<Map<String, Object>> preferenceList(Params para) throws Exception;
	/**
	 * 导出excl格式的借阅书籍类型 :全部分类
	 * 支持多班级
	 * @param para
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> exclPreferenceList(Params para) throws Exception;

	/**
	 * 4、 借阅书籍类型 :所有顶级分类:选中某个分类：显示下面借书的排名
	 *
	 * @param para
	 * @return
	 */
    List<Map<String, Object>> preferenceListTop(Params para);

	/**
	 * 4、 借阅书籍类型 :二级的“工业技术”分类下的分类
	 *
	 * @param para
	 * @return
	 */
	List<Map<String, Object>> preferenceListTwo(Params para) throws Exception;

	/**
	 * 4、 借阅书籍类型 :二级的“工业技术”分类下的分类:选中某个分类：显示下面借书的排名
	 *
	 * @param para
	 * @return
	 */
	List<Map> preferenceListTwoTop(Params para);

	/**
	 * 7.2、 借阅人群分析 -｛书籍类型｝：top5
	 *
	 * @param para
	 * @return
	 */
	List<Map<String, Object>> preferenceTopList(Params para) throws Exception;

	// /**
	// * 5、 借阅时段分布-算出借了几天，开始和结束的小时。时间段内的时间次数就是天数，其他时间是天数-1
	// *
	// * 1、已经还书的
	// *
	// * 2、未还书:当前时间作为还书时间，计算到当前
	// *
	// * @param para
	// * @return
	// */
	// Map<Integer, Integer> timeChangeList(Params para) throws Exception;

	/**
	 * new : 只统计借阅的时间点分布 5、 借阅时段分布-
	 *
	 * 群体的要按照月平均。个体的不用平均
	 *
	 *
	 * @param para
	 * @return
	 */
	Map<Integer, Object> hourList(Params para) throws Exception;

	/**
	 * 借阅时段分布-excl导出支持多班级
	 * @param para
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> exclHourList(Params para) throws Exception;
	/**
	 * 6、借阅频次习惯 -按照周几、小时汇总- 通过对Java对日期的格式进行再次处理
	 *
	 * @param para
	 * @return
	 */
	List weekHourList(Params para) throws Exception;

	/**
	 * 7.1、借阅人群分析 -按照｛男、女｝周几:统计人数:去重复
	 *
	 * @param para
	 * @return
	 */
	Map<String, Object> sexWeekList(Params para) throws Exception;

	// /**
	// * 7.2、消费总体偏好：top5+其他 --
	// *
	// * 返回top5+其他
	// *
	// * @param para
	// * @return
	// */
	// List<Map<String, Object>> preferenceOtherList(Params para) throws
	// Exception;

	/**
	 * 8.1、借阅数量变化趋势-按照天 :统计人数:去重复
	 *
	 * @param para
	 * @return
	 */
	List<Map<String, Object>> dayPeopleNum(Params para) throws Exception;

	/**
	 * 8.2、借阅数量变化趋势 :统计按照天：借阅次数
	 *
	 * @param para
	 * @return
	 * @throws Exception
	 */
    List<Map<String, Object>> dayBorrowNum(Params para) throws Exception;

	/**
	 * 9、借阅行为概况 ：借阅人数、借阅书籍数量、人均保有时长、人均借阅数量
	 *
	 * @param para
	 * @return
	 */
	Map analysis(Params para);

	/**
	 * 9、借阅行为概况 ：支持多班级导出
	 * @param para
	 * @return
	 */
	List<Map> exclAnalysis(Params para);

	/**
	 * 画像（借阅特征）：借阅人数、借阅书籍数量、人均保有时长、人均借阅数量、人均借阅频次
	 *
	 * @param para
	 * @return
	 */
	Map analysisShow(Params para) throws ParseException;

	/**
	 * * 9、借阅行为概况 ：个人页面
	 *
	 * 借阅书籍总本数、持有书籍总时长、人均借阅书籍本数（班级的平均）、人均持有书籍时长（班级的平均）
	 *
	 * @param para
	 * @return
	 */
	Map analysisOn(Params para);

	/**
	 * 借阅类型，top7+其他
	 * @param params
	 * @return
	 */
	Map<String, List> getBorrowType(com.ziyun.borrow.vo.Params params);

	/**
	 *根据一级分类查询二级分类中借阅次数最多的书名，借阅次数，人均持有时长，二级分类名
	 * @param params
	 * @return
	 */
	List<Map<String,Object>> getLevelTwoType(ParamsStatus params);

	/**
	 * 借阅图书排行。按图书借阅人次降序排列
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> getBorrowBookList(Params params);

	/**
	 * 借阅图书排行。按图书借阅的人数降序排列
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> getBorrowBookDetail(Params params);
	/**
	 * 借阅图书排行明细分页需要的total
	 * @param params
	 * @return
	 */
	int getBorrowBookDetailLength(Params params);

	/**
	 * 借阅数量排行。其中借阅频次中的时间基数扣除寒暑假的时间
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> getBorrowPeopleDetail(ParamsStatus params);
	/**
	 * 借阅数量排行明细分页需要的total
	 * @param params
	 * @return
	 */
	int getBorrowPeopleDetailLength(Params params);

	List<Map<String, Object>> getBorrowVariationTrend(Params params);

	int getBorrowtrendLength(Params para);

	/**
	 * 借阅频次明细中的总借阅书籍。其中0是星期天
	 * @param params
	 * @return
	 */
	List<Map<String, Object>>getWeekBorrowTotal(Params params) throws Exception;

	/**
	 * 借阅人群中的明细
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> getBorrowPeople(Params params) throws Exception;

    Map<String, Object> getBorrowFeature(Params params)throws Exception;

	List<Map<String, Object>> getBorrowStudentList(Params params);

	Long getBorrowStudentCount(Params params);
}
