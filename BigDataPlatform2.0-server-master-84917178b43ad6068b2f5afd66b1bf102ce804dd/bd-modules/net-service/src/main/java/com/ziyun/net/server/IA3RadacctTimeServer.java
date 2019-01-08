package com.ziyun.net.server;


import com.ziyun.net.vo.NVResultData;
import com.ziyun.net.vo.NetParams;
import com.ziyun.net.vo.Params;
import com.ziyun.utils.requests.CommResponse;

import java.text.ParseException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Description: 上网行为分析
 * @Created by liquan
 * @date 2017年5月13日 上午11:39:31
 */
public interface IA3RadacctTimeServer {

    /**
     * 16、 新版上网时段分布：｛周末、周一到周五2个24小时图｝
     * <p>
     * 个人的，不用平均:取在线天数（个人每天（时段）最多一个人在线 。用这个来当作在线天数）
     *
     * @param para 1、weekend=weekend 只查询周六、周日的数据
     *             2、weekend=notweekend 只查询周一到周五的数据
     * @return
     */
    Map<Integer, Integer> hourList(Params para) throws Exception;

    /**
     * {上网行为分析}8、上网内容类型{修改为按照人数：统计具体应用}:top10显示，
     * <p>
     * ｛去掉其他，改成top10｝
     *
     * @param para
     * @return
     */
    List<Map<String, Object>> preferenceList(Params para) throws Exception;

    /**
     * 新增的 8、上网内容类型{修改为按照人数：统计学校关注的几个分类：例如游戏、炒股等}
     * <p>
     * 新的需求：按照上面《新的上网热力》新增了几个分类
     *
     * @param para
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> preferenceTypeList(Params para) throws Exception;


    /***********************************Add By Linxiaojun*********************************************/
    /**
     * 上网总时长分布: 个人: Chart
     *
     * @param para
     * @return
     * @throws ParseException
     */
    Map<String, List<?>> getDurationDistChartPersonal(NetParams para) throws ParseException;

    /**
     * 上网总时长分布： Chart
     * @param para
     * @return
     * @throws ParseException
     */
	Map<String, List<?>> getDurationDistChart(NetParams para) throws ParseException;

    /**
     * 上网总时长分布： Table
     *
     * @param para
     * @return
     * @throws ParseException
     */
    List<Map<String, Object>> getDurationDistTable(NetParams para) throws ParseException;

    /**
     * 上网总时长分布： 总记录数
     *
     * @param para
     * @return
     */
    int getDurationDistRecordNum(NetParams para);

    /**
     * 上网人群分析: Chart
     *
     * @param para
     * @return
     */
    Map<String, Collection> getCrowdAnalysisChart(NetParams para) throws ParseException;

    /**
     * 上网人群分析: Table
     *
     * @param para
     * @return
     */
    List<Map<String, Object>> getCrowdAnalysisTable(NetParams para) throws ParseException;

    /**
     * 上网人群分析： 总记录数
     *
     * @param para
     * @return
     */
    int getCrowdAnalysisRecordNum(NetParams para);

    /**
     * 上网时长TOP10: Chart
     *
     * @param para
     * @return
     */
    List<Map<String, Object>> getDurationTopChart(NetParams para) throws ParseException;

    /**
     * 上网时长TOP10: Table
     *
     * @param para
     * @return
     */
    List<Map<String, Object>> getDurationTopTable(NetParams para) throws ParseException;

    /**
     * 上网时长: 总记录数
     *
     * @param para
     * @return
     */
    int getDurationTopRecordNum(NetParams para);

    /**
     * 上网终端： Chart (pc & mobile)
     *
     * @param para
     * @return
     */
    Map<String, List<Map<String, Object>>> getTerminalTypeChart(NetParams para);

    /**
     * 上网终端： Table (pc & mobile)
     *
     * @param para
     * @return
     */
    List<Map<String, Object>> getTerminalTypeTable(NetParams para);

    /**
     * 访问内容： Chart
     *
     * @param para
     * @return
     */
    List<Map<String, Object>> getVisitContextTopChart(NetParams para);

    /**
     * 访问内容： Table
     *
     * @param para
     * @return
     */
    List<Map<String, Object>> getVisitContextTopTable(NetParams para);

    /**
     * 访问内容： 总记录数
     *
     * @param para
     * @return
     */
    int getVisitContextTopRecordNum(NetParams para);

    /**
     * 上网时段： Chart
     *
     * @param para
     * @return
     */
    Map<String, List<NVResultData>> getPeriodChart(NetParams para);

    /**
     * 上网时段： Table
     *
     * @param para
     * @return
     */
    List<Map<String, Object>> getPeriodTable(NetParams para);

    /**
     * 社群概述
     *
     * @param para
     * @return
     */
    Map<String, String> getCommunityOverview(NetParams para);

    /**
     * 总人数： 男 + 女
     * @param para
     * @return
     */
     int getTotalPeople(NetParams para);

    /**
     * 总人数： 男
     * @param para
     * @return
     */
	int getTotalPeopleMale(NetParams para);

    /**
     * 总人数： 女
     *
     * @param para
     * @return
     */
    int getTotalPeopleFemale(NetParams para);

    // Just for test
    CommResponse getCommResponses(NetParams para);

    /**
     * 上网内容热度: 下拉框  serv app类型
     *
     * @param para
     * @return
     */
    List<Map<String, Object>> getContentHeatServAppType(NetParams para);

    /**
     * 上网内容热度: Chart
     *
     * @param para
     * @return
     */
    List<Map<String, Object>> getContentHeatChart(NetParams para) throws ParseException;

    /**
     * 根据app获取学生列表
     *
     * @param para
     * @return
     */
    List<Map<String, Object>> getStudentListByApp(NetParams para);

    /**
     * 根据app获取学生列表: 总记录数
     *
     * @param para
     * @return
     */
    int getAppStudentRecordNum(NetParams para);

    /**
     * 上网内容热度: Table
     *
     * @param para
     * @return
     */
    List<Map<String, Object>> getContentHeatTable(NetParams para);

    /**
     * 上网内容热度: 总记录数
     *
     * @param para
     * @return
     */
    int getContentHeatRecordNum(NetParams para);

    /**
     * 上网流量: Chart
     * @param para
     * @return
     */
    //Map<String, List<String>> getFluxChart(NetParams para) throws ParseException;

    /**
     * 上网流量: Table
     * @param para
     * @return
     */
	List<Map<String, Object>> getFluxTable(NetParams para);

    /**
     * 上网浏览: 总记录数
     * @param para
     * @return
     */
	int getFluxRecordNum(NetParams para);

    /**
     * 个人画像: 上网特征
     * @param para
     * @return
     */
	Map<String, String> getNetOverviewPersonal(NetParams para);

    /**
     * 当天上网流量Top,用于监控
     * @param para
     * @return
     */
	Map<String, List<String>> getFluxChartMonitor(NetParams para);

    /**
     * 上网流量: Chart
     *
     * @param para
     * @return
     */
    Map<String, List<String>> getFluxChart(NetParams para) throws ParseException;

    /**
     * 上网模块：学生列表
     *
     * @param para
     * @return
     */
    List<Map<String, Object>> getNetStudentList(NetParams para);

    List<Map<String, Object>> everyDaySufferInternetTime(NetParams para);

    int everyDaySufferInternetTimeCount(NetParams para);

    List<Map<String, Object>> getNetEarlywarnList(NetParams para);


    int getNetEarlywarnCount(NetParams para);


}
