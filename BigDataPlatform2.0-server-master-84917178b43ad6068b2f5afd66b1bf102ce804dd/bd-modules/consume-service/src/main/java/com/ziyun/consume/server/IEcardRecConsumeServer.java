package com.ziyun.consume.server;


import com.ziyun.consume.vo.ConsumeParams;
import com.ziyun.consume.vo.Params;
import com.ziyun.consume.vo.ResultData;

import java.util.List;
import java.util.Map;

/**
 * @Description: 消费行为分析
 * @Created by liquan
 * @date 2017年5月13日 上午11:39:31
 */
public interface IEcardRecConsumeServer {

    /**
     * 画像：标签{统计消费总金额、人均消费金额、人均消费频次}
     *
     * @param para
     * @return
     * @throws Exception
     */
    Map sumCollect(Params para) throws Exception;


    /**
     * 画像：标签{计算消费总金额、消费天数、日平均消费金额}
     *
     * @param para
     * @return
     * @throws Exception
     */
    public Map avgConsumeByDay(Params para) throws Exception;

    /**
     * 1、 消费大户榜top10 -- 时间段内的合计
     * <p>
     * 新增了：月平均消费
     *
     * @param para
     * @return
     */
    List<ResultData> sumTopList(Params para) throws Exception;

    /**
     * 3、 月节俭榜top10 --还得根据选择的时间计算月，然后算出月平均消费：因为时间是相同的 --只是排序和上面的不一样
     *
     * @param para
     * @return
     */
    List<ResultData> avgBottomList(Params para) throws Exception;

    /**
     * 3.1.2、{图下的明细：就是分页，并且显示多几个字段} 月节俭榜top10
     * <p>
     * --还得根据选择的时间计算月，然后算出月平均消费：因为时间是相同的 --只是排序和上面的不一样
     *
     * @param para
     * @return
     * @throws Exception
     */
    List<ResultData> avgBottomListDetail(Params para) throws Exception;

    /**
     * 3、.1.2、 统计：消费人数
     *
     * @param para
     * @return
     */
    ResultData sumCount(Params para) throws Exception;

    /**
     * 4、 消费总体偏好：top5
     * <p>
     * <!-- 合并考试费的：把考试相关的费用全部合并到考试费这一项中显示 -->
     * <p>
     * <!-- 4、 消费总体偏好 -通过对Java对日期的格式进行再次处理 -->
     * <p>
     * <!-- 7.2、 消费人群分析 -｛消费类型｝ -通过对Java对日期的格式进行再次处理 -->
     * <p>
     * 返回top5
     *
     * @param para
     * @return
     */
    List<Map<String, Object>> preferenceList(Params para) throws Exception;


    /**
     * 4、 消费总体偏好 ++ 显示分类下的商户排名：只显示消费商户，去掉保险、考试等学校统一收费
     *
     * @param para
     * @return
     */
    List<Map<String, Object>> preferenceListTop(Params para) throws Exception;

    /**
     * <!-- 考试费的：把考试相关的费用全部合并到考试费这一项中显示 -->
     * <p>
     * <!-- 4、 消费总体偏好 -通过对Java对日期的格式进行再次处理 -->
     * <p>
     * <!-- 7.2、 消费人群分析 -｛消费类型｝ -通过对Java对日期的格式进行再次处理 -->
     * <p>
     * 返回top5
     *
     * @param para
     * @return
     */
    List<Map<String, Object>> preferenceListTest(Params para) throws Exception;

    /**
     * 10.1.2、 消费类目占比（消费类型、男女：统计消费金额）
     *
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> preferenceSex(Params para) throws Exception;


    /**
     * 4、 消费总体偏好：top5
     * <p>
     * <!-- 不合并考试费的： -->
     * <p>
     * <!-- 4、 消费总体偏好 -通过对Java对日期的格式进行再次处理 -->
     *
     * @param para
     * @return
     */
    List<Map<String, Object>> preferenceListNot(Params para) throws Exception;

    /**
     * 5、 消费金额变化趋势 -- 通过对Java对日期的格式进行再次处理
     * <p>
     * 有效人数日人均、全部人数日人均、有效人数月人均、全部人数月人均
     *
     * @param para
     * @return
     */
    List<Map<String, Object>> timeChangeList(Params para) throws Exception;

    /**
     * 5.1.2、（分页功能的统计总数） 消费金额变化趋势
     *
     * @param para
     * @return
     * @throws Exception
     */
    ResultData timeChangeCount(Params para) throws Exception;


    /**
     * 5、 消费金额变化趋势 --：：个人
     *
     * @param para
     * @return
     */
    List<Map<String, Object>> timeChangeListOne(Params para) throws Exception;

    /**
     * 6、 消费时段分布 -按照周几、小时汇总-平均 -次数
     * <p>
     * 周几排序、各个小时：汇总平均列表
     *
     * @param para
     * @return
     */
    List weekHourList(Params para) throws Exception;

    /**
     * 6.2.0.0.0、 日均消费金额｛查询某个人某一天的消费时段分布｝ -按小时汇总：平均消费{消费金额/消费次数
     *
     * @param para
     * @return
     * @throws Exception
     */
    List hourList(Params para) throws Exception;

    /**
     * 20.2.3、消费明细(考试类)：显示小时、消费类型（最小分类）、金额
     * <p>
     * 20.2.4、消费明细-关联显示商家：(根据扣费终端和商家的关联表)  过滤掉了考试费
     *
     * @param para
     * @return
     * @throws Exception
     */
    public Map<String, List<Map<String, Object>>> detailHour(Params para)
            throws Exception;

    /**
     * 6.1.2、 消费时段分布 -按照周几、小时汇总:消费金额
     * <p>
     * 周几排序、各个小时：汇总平均
     *
     * @param para
     * @return
     */
    List weekHourListNew(Params para) throws Exception;

    /**
     * 6.1.2.1、｛图表下的明细｝ 消费时段分布 -按照周几、小时汇总:消费金额
     * <p>
     * 周几排序、各个小时：汇总平均
     * <p>
     * 按2个小时（或是几个小时）再此汇总
     *
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> weekHourListNewDetail(Params para) throws Exception;


    /**
     * 7.1、消费人群分析 --｛男、女｝按照金额，时段内汇总平均 -
     * <p>
     * 周几排序、男女分成2个列表
     *
     * @param para
     * @return
     */
    Map<String, Object> sexWeekList(Params para) throws Exception;

    // /**
    // * 7.2、消费人群分析:消费总体偏好：top5 --
    // *
    // * 返回top5 ｛新需求去掉其他｝
    // *
    // * @param para
    // * @return
    // */
    // List<Map<String, Object>> preferenceOtherList(Params para) throws
    // Exception;

    /**
     * 8.1、消费开支情况 -消费情况
     *
     * @param para
     * @return
     */
    List<Map<String, Object>> consumeDayList(Params para) throws Exception;

    /**
     * 8.2、消费开支情况 -充值情况
     *
     * @param para
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> rechargeDayList(Params para)
            throws Exception;


    /**
     * 10.1.2.3、 消费类目占比的明细(去掉考试部分的)只统计各个消费分类的商家
     * <p>
     * 10.1.2.2、 消费类目占比的明细(考试部分的)：汇总考试的三级分类的金额
     * <p>
     * 全部分类：包含（餐费、商场购物、考试类、其他类）
     *
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> preferenceDeptnameSex(Params para)
            throws Exception;


    /**
     * 10.1.2.3、 消费类目占比的明细的总条数(去掉考试部分的)只统计各个消费分类的商家
     * <p>
     * 10.1.2.2、 消费类目占比的明细(考试部分的)：汇总考试的三级分类的金额
     * <p>
     * 全部分类：包含（餐费、商场购物、考试类、其他类）
     *
     * @param para
     * @return
     * @throws Exception
     */
    int preferenceDeptnameSexTotal(Params para)
            throws Exception;


    /**
     * 20.1.2.1消费人群分析 --｛明细列表｝
     * <p>
     * 1、计算周几的：各个消费类型的金额、男女金额占比、人均消费
     *
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> WeekTypePersonAvgList(Params para) throws Exception;


    List<Map<String, Object>> getConsumeStudentList(Params params);

    Long getConsumeStudentCount(Params params);


    List<Map<String, Object>> getConsumeCategoryDetail(Params params);

    List<Map<String, Object>> getCanteenConsumeDetail(ConsumeParams params);

    Integer getCanteenConsumeDetailCount(ConsumeParams params);

    List<Map<String, Object>> getShopConsumeDetail(ConsumeParams params);

    Integer getShopConsumeDetailCount(ConsumeParams params);

    List<Map<String, Object>> getExamConsumeDetail(ConsumeParams params);

    Integer getExamConsumeDetailCount(ConsumeParams params);

    List<String> getConsumeDptNames(ConsumeParams params);
}
