package com.ziyun.borrow.mapper;

import com.ziyun.borrow.model.EduBorrow;
import com.ziyun.common.model.Params;
import com.ziyun.common.model.ParamsStatus;
import com.ziyun.borrow.vo.ResultData;

import java.util.List;
import java.util.Map;

public interface EduBorrowMapper {

	// ++++++++++业务查询++++++++++++
	/**
	 * 1、 借阅书籍排名top10
	 *
	 * @param para
	 * @return
	 */
	List<Map> bookTopList(Params para);

	/**
	 * 2、 借阅书籍数量top10 -相同的书，也累计次数
	 *
	 * 3、 借阅频次TOP10 -还得根据选择的时间计算月，然后算出每月频次：因为时间是相同的 只是排序和上面的不一样
	 *
	 * @param para
	 * @return
	 */
	List<Map> peopleTopList(Params para);

	/**
	 * 4、 借阅书籍类型 :全部分类
	 *
	 * @param para
	 * @return
	 */
	List<ResultData> preferenceList(Params para);

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
	List<ResultData> preferenceListTwo(Params para);

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
	List<ResultData> preferenceTopList(Params para);

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
	// List<ResultTimesData> timeChangeList(Params para);

	/**
	 * new : 只统计借阅的时间点分布 5、 借阅时段分布-
	 *
	 * @param para
	 * @return
	 */
	List<ResultData> hourList(Params para);

	/**
	 * new : 只统计借阅的时间点分布 5、 借阅时段分布-
	 *
	 * @param para
	 * @return
	 */
	List<ResultData> exclHourList(Params para);

	/**
	 * 6、借阅频次习惯 -按照周几、小时汇总- 通过对Java对日期的格式进行再次处理
	 *
	 * @param para
	 * @return
	 */
	List<ResultData> weekHourList(Params para);

	/**
	 * 6、借阅频次习惯 -按照周几、小时汇总:最早、最晚记录时间（用于计算周几的平均）
	 *
	 * @param para
	 * @return
	 */
	ResultData weekHourListTimes(Params para);

	/**
	 * 7.1、借阅人群分析 -按照｛男、女｝周几:统计人数:去重复
	 *
	 * @param para
	 * @return
	 */
	List<ResultData> sexWeekList(Params para);

	/**
	 * 8.1、借阅数量变化趋势-按照天 :统计人数:去重复
	 *
	 * @param para
	 * @return
	 */
	List<ResultData> dayPeopleNum(Params para);

	//借阅数量变化趋势中的表的长度
	int borrowtrendLength(Params para);

	/**
	 * 8.2、借阅数量变化趋势 :统计按照天：借阅次数
	 *
	 * @param para
	 * @return
	 */
	List<ResultData> dayBorrowNum(Params para);

	/**
	 * 9、借阅行为概况 ：借阅人数、借阅书籍数量、人均保有时长、人均借阅数量
	 *
	 * 新增了一个上网总时长（timeSum）
	 *
	 * @param para
	 * @return
	 */
	Map analysis(Params para);

	/**
	 * 9、excl导出版本的借阅行为概况 ：借阅人数、借阅书籍数量、人均保有时长、人均借阅数量
	 * 支持多班级导出
	 * 新增了一个上网总时长（timeSum）
	 *
	 * @param para
	 * @return
	 */
	List<Map> exclAnalysis(Params para);

	// ++++++ 定时任务++++++
	/**
	 * 定时任务:存在学籍的学生
	 *
	 * @return
	 */
	int taskStudent();

	/**
	 * 定时任务:定时任务:不存在学籍的往届学生、或者教师
	 *
	 * @return
	 */
	int taskNotStudent();

	// ++++++ 定时任务++++++

	// ++++++++++业务查询++++++++++++

	int deleteByPrimaryKey(Integer id);

	int insert(EduBorrow record);

	int insertSelective(EduBorrow record);

	EduBorrow selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(EduBorrow record);

	int updateByPrimaryKey(EduBorrow record);

	// 成绩提高分析 4、借阅
	List<Map<String, Object>> getBorrow4Analysis(Params params);
	// 成绩提高分析 4、借阅 人均借阅时长
	List<Map<String, Object>> getBorrowTime4Analysis(Params params);

	List<Map<String, Object>> getBorrowType(com.ziyun.borrow.vo.Params params);

	List<Map<String,Object>> getBorrowTypeLevelTwo(ParamsStatus params);

	List<Map<String,Object>> getLevelTwoType(ParamsStatus params);

	Long getTime(ParamsStatus params);

	Float getTimes(ParamsStatus params);

	List<Map<String, Object>> getBorrowBookList(Params params);

	List<Map<String, Object>> getBorrowBookDetail(Params params);


	int getBorrowBookDetailLength(Params params);

	List<Map<String, Object>> getPeopleDetail(Params params);

	List<Map<String, Object>> getBorrowPeopleDetail(Params params);
	int getBorrowPeopleDetailLength(Params params);

	List<Map<String, Object>>getWeekBorrowTotal(Params params);

	List<Map<String, Object>> getWeekListBysex(Params params);

	List<Map<String, Object>> getWeekBorrowDetails(Params params);

	Long getTimeByParams(Params params);

	Long getTimesByParams(Params params);

	Map<String, Object> borrowfeature(Params params);

	Map<String,String> getDateByYearTerm(Params params);

	float getYearDate(Params params);

	String getNowTerm();

	List<Map<String, Object>> getBorrowStudentList(Params params);

	Long getBorrowStudentCount(Params params);
}
