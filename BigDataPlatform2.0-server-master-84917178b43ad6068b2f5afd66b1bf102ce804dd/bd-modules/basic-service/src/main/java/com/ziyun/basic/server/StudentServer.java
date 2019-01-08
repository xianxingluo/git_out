package com.ziyun.basic.server;

import com.ziyun.basic.entity.OwnOrgStudent;
import com.ziyun.basic.entity.OwnOrgStudentType;
import com.ziyun.basic.vo.Params;
import com.ziyun.basic.vo.ParamsStatus;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface StudentServer {

	List<LinkedHashMap<String, Object>> getSource(Params para);

	List<LinkedHashMap<String, Object>> getBorrowhabits(Params para);

	// *********平台未调用，先隐藏**********
	//Map<String, List<HotSpotVo>> internet(Params para, String type);

	List<LinkedHashMap<String, Object>> getConsume(Params para);

	List<LinkedHashMap<String, Object>> getMajor(Params para);

	List<LinkedHashMap<String, Object>> getOrgTree();

	/**
	 * 根据参数：分页查询：学生信息：
	 *
	 * @param para
	 * @return
	 */
	List<LinkedHashMap<String, Object>> getPageStudent(Params para);

	/**
	 * 个人查询条件页面：显示学生信息总条数
	 *
	 * @param para
	 * @return
	 */
	Integer countStudent(Params para);

	List<LinkedHashMap<String, Object>> getArchive(Params para);

	Map<String, Object> getStatus(ParamsStatus para);

	/**
	 * 奖励与惩罚
	 * @param para
	 * @return
	 */
	Map<String, Object> getPunishAndReward(ParamsStatus para);

	List<String> getPersonalStatus(ParamsStatus para);

	/**热点信息  // *********平台未调用，先隐藏**********
	 * @param para
	 * @return
	 */
	//Map<String, Object> getHotsport(ParamsStatus para);

	/**性别统计
	 * @param para
	 * @return
	 */
	List<LinkedHashMap<String, Object>> countBySex(ParamsStatus para);

	/**
	 * 根据获奖次数来统计男女的数量，比如获奖一次男XX女XX
	 * @param params
	 * @return 获奖次数对应男女数量及相应比例。比例是占总数的比例
	 */
	List<Map<String, Object>> getScholarshipCountBysex(ParamsStatus params);

	/**
	 * 根据获奖类型统计数量。每个类型获奖人次。只统计前三，后面的归为其他
	 * @param params
	 * @return
	 */
	List<Map<String,Object>> getScholarshipTypeCount(ParamsStatus params);

	/**
	 * 男女比例按学期统计
	 * @param para
	 * @return
	 */
	List<Map<String, Object>> getCountBySex(Params para);
	/**出生年月统计
	 * @param para
	 * @return
	 */
	List<LinkedHashMap<String, Object>> countByBirthday(ParamsStatus para);

	/**
	 * 奖励列表
	 * @param
	 * @return
	 */
	List<LinkedHashMap<String, Object>> scholarshipPageList(ParamsStatus params);

	/**
	 * 惩罚列表
	 * @param para
	 * @return
	 */
	List<LinkedHashMap<String, Object>> punishPageList(ParamsStatus para);

	Long scholarshipPageListLength(ParamsStatus para);

	Long punishPageListLength(ParamsStatus para);

	/**
	 * 统计每个处罚类别的数量
	 * @param para
	 * @return
	 */
	List<Map<String, Object>> getPunishCount(ParamsStatus para);

	/**
	 * 来源省份根据班级汇总导出
	 * @param para
	 * @return
	 */
	List<Map<String, Object>> proviceFrom(Params para);

	/**性别统计 Excel导出
	 * @param para
	 * @return
	 */
	List<Map<String, Object>> genderStatistics(Params para);

	/**出生年月统计 Excel导出
	 * @param para
	 * @return
	 */
	List<Map<String, Object>> birthdayDistribution(Params para);


	List<LinkedHashMap> getTermDate();

    List<Map<String, Object>> getgetScholarshipStudentList(ParamsStatus paramsStatus);

    List<Map<String, Object>> getPunishStudentList(ParamsStatus param);

    List<Map<String, Object>> getScholarshipTypeStudentList(ParamsStatus param);

    /**
     * 提供为消费微服务--获取学生消费列表总长度
     *
     * @param params
     * @return
     */
    List<Map<String, Object>> getStudents(Params params);

    /**
     * 提供为消费微服务--获取学生消费列表总长度
     *
     * @param params
     * @return
     */
    Long getStudentSize(Params params);

    /**
     * 提供给消费微服务  获取所有的学生基本详情
     *
     * @param params
     * @return
     */
    List<OwnOrgStudentType> selectAllDetail(Params params);

    OwnOrgStudent selectByPrimaryKey(String outid);

    List<Map<String, Object>> sourceStudentList(Params para);

    int sourceStudentListCount(Params para);

	List<Map<String, Object>> sexRatioStudentList(Params param);

	int sexRatioStudentListCount(Params param);

	/**
	 * 判断是否是毕业生
	 *
	 * @param outid
	 * @return
	 */
	boolean isGraduationStudent(String outid);

	List<Map<String, Object>> countByBrithdryStudentList(ParamsStatus para);

	int countByBrithdryStudentListCount(ParamsStatus para);

	String getEnrollmentYearById(Params params);

	List<Map<String, Object>> getScholarshipPunishStudentList(ParamsStatus paramsStatus);

	Long getScholarshipPunishStudentCount(ParamsStatus paramsStatus);

	Map selectByOutid(String outid);

	List<Map<String, Object>> getActiveRanking(ParamsStatus param);

	int getActiveRankingSize(ParamsStatus param);

    List<String> getPunishType(Params params);
}
