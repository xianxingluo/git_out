package com.ziyun.academic.dao;


import com.ziyun.academic.entity.EcardRecConsumeCopy;
import com.ziyun.academic.entity.EcardRecConsumeCopyKey;
import com.ziyun.academic.vo.Params;
import com.ziyun.academic.vo.ResultData;

import java.util.List;
import java.util.Map;

/**
 * @Description: 消费行为分析dao
 * @Created by liquan
 * @date 2017年5月13日 上午11:34:15
 */
public interface EcardRecConsumeCopyDao {

    /**
     * 根据查询条件:获取记录的：最早时间、最晚时间：：用于前端没传时间参数的时间，计算有多少天等时间函数
     * <p>
     * 用于按照学号查询的时候：来确定按照日期统计的起始、结束日期
     *
     * @param para
     * @return
     */
    ResultData startEndTimes(Params para);

    /**
     * 新版{群体查询：为提高查询效率：按照天、班级汇总}16
     * <p>
     * 根据查询条件:获取记录的：最早时间、最晚时间：：用于前端没传时间参数的时间，计算有多少天等时间函数
     * <p>
     * 用于按照学号查询的时候：来确定按照日期统计的起始、结束日期
     *
     * @param para
     * @return
     */
    ResultData startEndTimesNew(Params para);

    // ++++++++++业务查询++++++++++++


    /**
     * 画像：标签{统计消费总金额、人均消费金额、人均消费频次}
     *
     * @param para
     * @return
     */
    ResultData sumCollect(Params para);


    /**
     * 个人档案：标签{计算消费总金额、消费天数、日平均消费金额}
     *
     * @param para
     * @return
     */
    ResultData avgConsumeByDay(Params para);

    /**
     * 1.1.2、(分页查询:不传默认top10) 消费大户榜top10 -- 时间段内的合计
     * <p>
     * 2.1.2、(分页查询:不传默认top10) 消费能力榜TOP10 --还得根据选择的时间计算月，然后算出月平均消费：因为时间是相同的
     *
     * @param para
     * @return
     */
    List<ResultData> sumTopList(Params para);

    /**
     * 3.1.2、 (分页查询:不传默认top10)月节俭榜top10 --还得根据选择的时间计算月，然后算出月平均消费：因为时间是相同的 --只是排序和上面的不一样
     *
     * @param para
     * @return
     */
    List<ResultData> sumBottomList(Params para);

//	/**
//	 * 3、.1.2、 (分页查询)月节俭榜top10 --还得根据选择的时间计算月，然后算出月平均消费：因为时间是相同的 --只是排序和上面的不一样
//	 * 
//	 * @param para
//	 * @return
//	 */
//	List<Map> sumBottomPage(Params para);

    /**
     * 3、.1.2、 统计：消费人数
     *
     * @param para
     * @return
     */
    ResultData sumCount(Params para);

    /**
     * <!-- 合并考试费的：把考试相关的费用全部合并到考试费这一项中显示 -->
     * <p>
     * <!-- 4、 消费总体偏好 -通过对Java对日期的格式进行再次处理 -->
     * <p>
     * <!-- 7.2、 消费人群分析 -｛消费类型｝ -通过对Java对日期的格式进行再次处理 -->
     *
     * @param para
     * @return
     */
    List<ResultData> preferenceList(Params para);

    /**
     * 导出(按照班级汇总)
     * <p>
     * <!-- 合并考试费的：把考试相关的费用全部合并到考试费这一项中显示 -->
     * <p>
     * <!-- 4、 消费总体偏好 -通过对Java对日期的格式进行再次处理 -->
     * <p>
     * <!-- 7.2、 消费人群分析 -｛消费类型｝ -通过对Java对日期的格式进行再次处理 -->
     *
     * @param para
     * @return
     */
    List<ResultData> preferenceList4Excel(Params para);

    /**
     * 4、 消费总体偏好 ++ 显示分类下的商户排名：只显示消费商户，去掉保险、考试等学校统一收费
     *
     * @param para
     * @return
     */
    List<ResultData> preferenceListTop(Params para);

    /**
     * <!-- 考试费的：把考试相关的费用全部合并到考试费这一项中显示 -->
     * <p>
     * <!-- 4、 消费总体偏好 -通过对Java对日期的格式进行再次处理 -->
     * <p>
     * <!-- 7.2、 消费人群分析 -｛消费类型｝ -通过对Java对日期的格式进行再次处理 -->
     *
     * @param para
     * @return
     */
    List<ResultData> preferenceListTest(Params para);

    /**
     * 10.1.2、 消费类目占比（消费类型、男女：统计消费金额）
     *
     * @param para
     * @return
     */
    List<ResultData> preferenceSex(Params para);

    //--------------------------------

    /**
     * 10.1.2.2、 消费类目占比的明细(考试部分的)：汇总考试的三级分类的金额
     * <p>
     * -java需要再次处理：按照消费类型（把男女的加到一块，再排序、分页）
     *
     * @param para
     * @return
     */
    List<ResultData> preferenceClass3SexTest(Params para);

    /**
     * 20.2.4、消费明细-关联显示商家：(根据扣费终端和商家的关联表)  过滤掉了考试费
     * <p>
     * 个人消费页面：查询消费明细（不包含考试类）（并显示消费的商家）
     *
     * @param para
     * @return
     */
    List<ResultData> detailHourDeptnameNoTest(Params para);

    /**
     * 20.2.3、 消费明细(考试类)：显示小时、消费类型（最小分类）、金额
     *
     * @param para
     * @return
     */
    List<ResultData> detailHourTypeTest(Params para);
    //--------------------------------


    //----------个人消费页面：查询消费明细（考试类）（并显示消费的最小分类）----------------------

    /**
     * 10.1.2.3、 消费类目占比的明细(去掉考试部分的)只统计各个消费分类的商家：
     * <p>
     * -java需要再次处理：按照消费类型（把男女的加到一块，再排序、分页）
     *
     * @param para
     * @return
     */
    List<ResultData> preferenceDeptnameSexNoTest(Params para);
    //===========================================================

    /**
     * 导出(按照班级汇总)
     * <p>
     * <!-- 考试费的：把考试相关的费用全部合并到考试费这一项中显示 -->
     * <p>
     * <!-- 4、 消费总体偏好 -通过对Java对日期的格式进行再次处理 -->
     * <p>
     * <!-- 7.2、 消费人群分析 -｛消费类型｝ -通过对Java对日期的格式进行再次处理 -->
     *
     * @param para
     * @return
     */
    List<ResultData> preferenceListTest4Excel(Params para);

    /**
     * <!-- 不合并考试费的： -->
     * <p>
     * <!-- 4、 消费总体偏好 -通过对Java对日期的格式进行再次处理 -->
     *
     * @param para
     * @return
     */
    List<ResultData> preferenceListNot(Params para);

    /**
     * 5、 消费金额变化趋势 -- 通过对Java对日期的格式进行再次处理
     *
     * @param para
     * @return
     */
    List<ResultData> timeChangeList(Params para);

    /**
     * 5.2.0.0、 消费者的月份、月消费、月平均每天人数
     *
     * @param para
     * @return
     */
    List<ResultData> monthAvgList(Params para);

    /**
     * 5.1.2、{分页功能的统计总数} 消费金额变化趋势
     *
     * @param para
     * @return
     */
    ResultData timeChangeCount(Params para);

    /**
     * 导出(按照班级汇总) 5、 消费金额变化趋势- 消费者的人均
     *
     * @param para
     * @return
     */
    List<ResultData> timeChangeList4Excel(Params para);

    /**
     * 5、  消费金额变化趋势- 只有金额，没有人数：：个人
     *
     * @param para
     * @return
     */
    List<ResultData> timeChangeListOne(Params para);

    // /**
    // * 5、 消费金额变化趋势 --个人对应的班级
    // *
    // * @param para
    // * @return
    // */
    // List<ResultData> timeChangeListClass(Params para);

    /**
     * 6、 消费时段分布 -按照周几、小时汇总- 通过对Java对日期的格式进行再次处理
     *
     * @param para
     * @return
     */
    List<ResultData> weekHourList(Params para);

    /**
     * 新版{群体查询：为提高查询效率：按照天、班级汇总}
     * <p>
     * 16、 消费时段分布 -按照周几、小时汇总- 通过对Java对日期的格式进行再次处理
     *
     * @param para
     * @return
     */
    List<ResultData> weekHourListNew(Params para);

    /**
     * 6.2.0.0.0、 日均消费金额｛查询某个人某一天的消费时段分布｝ -按小时汇总：平均消费{消费金额/消费次数
     *
     * @param para
     * @return
     */
    List<ResultData> hourList(Params para);

    /**
     * 导出(按照班级汇总){这个周几是1-7格式的}
     * <p>
     * 6、 消费时段分布 -按照周几、小时汇总 通过对Java对日期的格式进行再次处理
     *
     * @param para
     * @return
     */
    List<ResultData> weekHourList4Excel(Params para);

    /**
     * 7.1、消费人群分析 --｛男、女｝按照金额，时段内汇总平均 -通过对Java对日期的格式进行再次处理
     *
     * @param para
     * @return
     */
    List<ResultData> sexWeekList(Params para);

    /**
     * 20.1.2、消费人群分析 -计算周几的人均消费：：｛按照周几统计人数（学号按照天去重）；统计周几的消费总额；｝
     *
     * @param para
     * @return
     */
    List<ResultData> sexWeekSum(Params para);

    /**
     * 20.1.2.1、消费人群分析 -按照｛周几、消费类型｝汇总金额，时段内汇总平均 -通过对Java对日期的格式进行再次处理
     *
     * @param para
     * @return
     */
    List<ResultData> weekConsumeType(Params para);

    /**
     * 8.1、消费开支情况 -消费情况
     *
     * @param para
     * @return
     */
    List<ResultData> consumeDayList(Params para);

    /**
     * 8.2、消费开支情况 -充值情况
     *
     * @param para
     * @return
     */
    List<ResultData> rechargeDayList(Params para);

    /**
     * 根据传人的字段：动态查询
     *
     * @param record
     * @return
     */
    EcardRecConsumeCopy selectBy(EcardRecConsumeCopy record);

    /**
     * 根据id查询：虽然该表是联合主键：但是时间字段只是分表用的，id任然查询的是唯一结果
     *
     * @param id
     * @return
     */
    EcardRecConsumeCopy selectById(Long id);

    // ++++++++++业务查询++++++++++++

    int deleteByPrimaryKey(EcardRecConsumeCopyKey key);

    int insert(EcardRecConsumeCopy record);

    int insertSelective(EcardRecConsumeCopy record);

    EcardRecConsumeCopy selectByPrimaryKey(EcardRecConsumeCopyKey key);

    int updateByPrimaryKeySelective(EcardRecConsumeCopy record);

    int updateByPrimaryKey(EcardRecConsumeCopy record);

    // 成绩提高分析 5、消费
    List<Map<String, Object>> getConsume4Analysis(Params params);

    // 成绩提高分析 5、消费  按学期统计
    List<Map<String, Object>> getConsume4AnalysisBySemester(Params params);

    List<Map<String, Object>> getConsumeCategory4Analysis(Params params);

}