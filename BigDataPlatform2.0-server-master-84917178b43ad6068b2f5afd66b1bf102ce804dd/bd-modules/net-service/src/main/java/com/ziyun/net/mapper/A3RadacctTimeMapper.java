package com.ziyun.net.mapper;


import com.ziyun.net.annotation.TargetDataSource;
import com.ziyun.net.entity.A3RadacctTime;
import com.ziyun.net.vo.Params;
import com.ziyun.net.vo.ResultData;

import java.util.List;
import java.util.Map;

public interface A3RadacctTimeMapper {

    // ++++++++++业务查询++++++++++++

    /**
     * 2、上网总时长top10
     *
     * @param para
     * @return
     */
    List<ResultData> sumTopList(Params para);

    /**
     * 3、上网日平均时长top10 ：：num 上网的天数
     *
     * @param para
     * @return
     */
    List<Map> avgTopList(Params para);

    /**
     * 4、 消费总体偏好 -- 通过对Java对日期的格式进行再次处理
     *
     * @param para
     * @return
     */
    List<ResultData> preferenceList(Params para);

    /**
     * 5.0、上网总时长分析（日人均总时长和上网人数统计）：：最早时间、最晚时间
     * <p>
     * 用于查询的时候：来确定按照日期统计的起始、结束日期
     *
     * @param para
     * @return
     */
    ResultData timeChangeListTimes(Params para);

    /**
     * 5、上网总时长分析（日人均总时长和上网人数统计）：：每天的：上网人数 、人均总时长
     *
     * @param para
     * @return
     */
    List<ResultData> timeChangeList(Params para);

    List<ResultData> timeChangeList4Excel(Params para);

    /**
     * 5、上网总时长分析 ::个人页面：个人每日上网时间、班级平均每日上网时间
     *
     * @param param
     * @return
     */
    List<ResultData> timeChangeListOne(Params param);


    /**
     * 6、 上网时段分布 -按照周几、小时汇总- 通过对Java对日期的格式进行再次处理
     *
     * @param para
     * @return
     */
    List<ResultData> weekHourList(Params para);

    /**
     * 16、 新版上网时段分布：｛周末、周一到周五2个24小时图｝
     * <p>
     * {按照班级汇总：用于群体查询}先按照天、小时、班级汇总在线人数;再按照小时统计总人数
     *
     * @param para 1、weekend=weekend 只查询周六、周日的数据
     *             2、weekend=notweekend 只查询周一到周五的数据
     * @return
     */
    List<ResultData> hourList(Params para);

    List<ResultData> hourList4Excel(Params para);

    /**
     * 16、 新版上网时段分布：｛周末、周一到周五2个24小时图｝
     * <p>
     * ｛用于个人查询｝先按照天、小时、每个人统计在线人数;再按照小时统计总人数
     *
     * @param para 1、weekend=weekend 只查询周六、周日的数据
     *             2、weekend=notweekend 只查询周一到周五的数据
     * @return
     */
    @TargetDataSource("mysql")
    List<ResultData> hourListOne(Params para);

    /**
     * 7.1、上网人群分析：：上网人数｛男、女｝
     *
     * @param para
     * @return
     */
    List<ResultData> sexDayList(Params para);

    /**
     * 7.2、上网人群分析：：PC、移动端
     *
     * @param para
     * @return
     */
    List<ResultData> clientTypeList(Params para);

    // ++++++ 定时任务++++++

    /**
     * 获得数据库5分钟前的时间：用于定时任务
     *
     * @param para
     * @return
     */
    @TargetDataSource("mysql")
    ResultData fiveMinAgo(Params para);

    /**
     * 获取到和新log记录，时间重合的radacct_time记录：该记录需要合并
     * <p>
     * 可能会出现多个重复的：比如出现一个好几个小时的上网记录；就会合并前面N次短的上网记录
     *
     * @param para
     * @return
     */
    @TargetDataSource("mysql")
    default List<A3RadacctTime> getTimeSame(Params para) {
        return null;
    }

    /**
     * 查询radacctlog_all上网时间表：指定时间内的新数据
     * <p>
     * 定时任务：计算上网时间，去重复radacctlog_all存入到：radacct_time
     *
     * @param para
     * @return
     */
    @TargetDataSource("mysql")
    List<A3RadacctTime> taskNewData(Params para);

    /**
     * 查询radacctlog_all上网时间表：指定时间内的新数据 :的总条数
     *
     * @param para
     * @return
     */
    @TargetDataSource("mysql")
    ResultData countTaskNewData(Params para);

    // ++++++ 定时任务++++++

    // ++++++++++业务查询++++++++++++

    @TargetDataSource("mysql")
    int deleteByPrimaryKey(Long id);

    int insert(A3RadacctTime record);

    int insertSelective(A3RadacctTime record);

    A3RadacctTime selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(A3RadacctTime record);

    int updateByPrimaryKey(A3RadacctTime record);

    // 成绩提高分析 3、上网
    List<Map<String, Object>> getNetwork4Analysis(Params params);
}