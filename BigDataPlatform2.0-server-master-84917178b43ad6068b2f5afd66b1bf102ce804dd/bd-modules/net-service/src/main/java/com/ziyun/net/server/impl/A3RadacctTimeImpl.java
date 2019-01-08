package com.ziyun.net.server.impl;

import com.ziyun.net.entity.A3RadacctTime;
import com.ziyun.net.entity.OwnSchoolOrg;
import com.ziyun.net.enums.SexEnum;
import com.ziyun.net.enums.TerminalEnum;
import com.ziyun.net.enums.WeekdayEnum;
import com.ziyun.net.mapper.A3HotspotMapper;
import com.ziyun.net.mapper.A3RadacctTimeMapper;
import com.ziyun.net.mapper.A3RadacctTimeKylinMapper;
import com.ziyun.net.mapper.EarlyWarningRMapper;
import com.ziyun.net.server.IA3RadacctTimeServer;
import com.ziyun.net.server.StudentServer;
import com.ziyun.net.tools.CalendarUtils;
import com.ziyun.net.tools.Week;
import com.ziyun.net.vo.*;
import com.ziyun.utils.cache.CacheConstant;
import com.ziyun.utils.common.BeanUtil;
import com.ziyun.utils.date.DateUtils;
import com.ziyun.utils.requests.CommResponse;
import com.ziyun.utils.requests.StatusCodeEnum;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class A3RadacctTimeImpl implements IA3RadacctTimeServer {

    private static Logger logger = Logger.getLogger(A3RadacctTimeImpl.class);

    private static BigDecimal zero = new BigDecimal(0.00);
    private static BigDecimal zero_0 = new BigDecimal(0);

    @Autowired
    public A3RadacctTimeMapper radacctTimeMapper;

    @Autowired
    public A3RadacctTimeKylinMapper radacctTimeKylinMapper;

    @Autowired
    public A3HotspotMapper hotspotMapper;

    @Autowired
    public EarlyWarningRMapper earlyWarningRMapper;

    @Autowired
    public StudentServer studentServer;

    /**
     * 设置开始结束时间：用来记录天数等的时间函数需要用到。
     * <p>
     * 在前端没有传入时间参数的时候，设置时间参数
     *
     * @param para
     */
    private void setStartEndTime(Params para) {

        if (StringUtils.isEmpty(para.getBdate())
                || StringUtils.isEmpty(para.getEdate())) {
            String bdate;
            String edate;
            ResultData times = radacctTimeMapper.timeChangeListTimes(para);// 获取最早、最晚：记录的时间
            // 如果没有从查询结果中取到开始、结束时间；则设置为当天
            if (null == times || null == times.getBdate()) {
                bdate = CalendarUtils.toYyyy2MM2dd(new Date());
            } else {
                bdate = CalendarUtils.toYyyy2MM2dd(times.getBdate());
            }
            if (null == times || null == times.getEdate()) {
                edate = CalendarUtils.toYyyy2MM2dd(CalendarUtils.getDateBy(1));
            } else {
                edate = CalendarUtils.toYyyy2MM2dd(CalendarUtils
                        .nextZeroTime(times.getEdate()));// 结束时间：往后推一天，应为查询条件是小于结束时间
            }
            para.setBdate(bdate);
            para.setEdate(edate);
        }
    }

    @Cacheable(value = CacheConstant.NET_CACHE, key = "'net:online:' +#root.methodName +'.' + #para.hashCode()")
    public List<Map<String, Object>> preferenceList(Params para)
            throws Exception {
        List<ResultData> list = hotspotMapper.preferenceList(para);
        List<ResultData> resultlist = new ArrayList<>();// 返回给前台的
        for (ResultData data : list) {
            if (!"其他".equals(data.getName()) && resultlist.size() < 10) {
                resultlist.add(data);
            }
        }
        return BeanUtil.objectsToMapsOffNull(resultlist);
    }


    private void getResultData(ResultData resultData, OwnSchoolOrg ownSchoolOrg) {
        resultData.setSchoolName(ownSchoolOrg.getSchoolName());
        resultData.setFacultyName(ownSchoolOrg.getFacultyName());
        resultData.setMajorName(ownSchoolOrg.getMajorName());
    }

    @Cacheable(value = CacheConstant.NET_CACHE, key = "'net:online:' +#root.methodName +'.' + #para.hashCode()")
    public List<Map<String, Object>> preferenceTypeList(Params para)
            throws Exception {
        List<ResultData> list = hotspotMapper.preferenceTypeList(para);

        List<ResultData> resultlist = new ArrayList<>();// 返回给前台的
        list.stream().forEach(data -> {
            if (!"小说".equals(data.getName()) || data.getSum().intValue() != 0) {
                resultlist.add(data);
            }
        });
        return BeanUtil.objectsToMapsOffNull(resultlist);
    }



    /**
     * 给返回的对象设置：校区、院系、专业：的名称
     *
     * @param orgList
     * @param data
     */
    private void setOrgInfo(List<OwnSchoolOrg> orgList, ResultData data) {
        //把校区、院系、班级的名称赋给每一行，用于excel显示
        for (OwnSchoolOrg org : orgList) {
            if (org.getClassCode().equals(data.getClassCode())) {
                data.setSchoolName(org.getSchoolName());
                data.setFacultyName(org.getFacultyName());
                data.setMajorName(org.getMajorName());
            }
        }
    }

    /**
     * 根据给定的日期；获取查询结果集当中对应的数据项；如果不存在，则新建一个该日期的，数值为0的数据项
     *
     * @param list
     * @param day
     * @return
     */
    private ResultData getDayDataCharge(List<ResultData> list, String day) {
        ResultData dayData;// 指定日期的data;如果结果集中没有，则构造一个
        for (ResultData data : list) {
            if (data.getDatetimeStr().equals(day)) {
                return data;
            }
        }
        dayData = new ResultData();
        dayData.setDatetimeStr(day);
        dayData.setSum(zero);
        dayData.setAvg(zero);
        return dayData;
    }

    /**
     * 根据给定的日期；获取查询结果集当中对应的数据项；如果不存在，则新建一个该日期的，数值为0的数据项
     *
     * @param list
     * @param day
     * @return
     */
    private ResultData getDayData(List<ResultData> list, String day) {
        for (ResultData data : list) {
            if (DateUtils.date2Str("yyyy-MM-dd", data.getAcctstarttime()).equals(day)) {
                data.setDatetimeStr(day);
                return data;
            }
        }
        ResultData dayData = new ResultData();
        dayData.setDatetimeStr(day);
        dayData.setSum(zero);
        return dayData;
    }

    @Cacheable(value = CacheConstant.NET_CACHE, key = "'net:online:' +#root.methodName +'.' + #para.hashCode()")
    public List weekHourList(Params para) throws Exception {
        // //个人查询：保留小数：方便前台显示出数据的大小
        int round = 0;//保留几位小数：默认汇总平均（群体）是0位。个人是2位
        // if(StringUtils.isNotEmpty(para.getOutid())){
        // round=2;
        // }
        List<ResultData> weekHourList = radacctTimeMapper.weekHourList(para);
        if (null == weekHourList || weekHourList.size() == 0) {
            return null;
        }
        // 获取查询时间段内的所有日期：防止查询结果当中，有很多天数据是空的，无法显示
        setStartEndTime(para);
        List<Week> weekNameList = CalendarUtils.getWeekCountBetween(
                para.getBdate(), para.getEdate());

        List<Object> resultMap = new ArrayList<>();
        List<HourData> resultList = null;
        // 时段内汇总平均 - 周几排序、各个小时：汇总平均列表
        for (int i = 0; i < weekNameList.size(); i++) {// 根据周几循环，能将数据按照周几排序
            resultList = new ArrayList<>();
            Week week = weekNameList.get(i);
            for (int j = 0; j < 24; j++) {// 循环24个小时：：数据中也是从0开始的
                String hour = j + "";
                HourData hourData = getWeekHourData(weekHourList, week, hour,
                        round);// 循环获取，每个小时的数据，
                resultList.add(hourData);
            }
            // Map<String, Object> map = new HashMap<String, Object>();
            // map.put(week.getCn_name(), resultList);// 按照周几：返回结果
            resultMap.add(resultList);
        }
        return resultMap;
    }

    /**
     * 根据给定的星期几序号；
     * <p>
     * 获取查询结果集当中对应的数据项；如果不存在，则新建一个该日期的，数值为0的数据项
     *
     * @param list
     * @param week 星期几：序号
     * @param hour %k 小时(0……23)
     * @param hour 保留几位小数
     * @return
     */
    private HourData getWeekHourData(List<ResultData> list, Week week,
                                     String hour, int round) {
        HourData hourData = null;// 指定日期的data;如果结果集中没有，则构造一个
        for (ResultData data : list) {
            if (data.getWeekindex() == week.getEn_index()
                    && hour.equals(data.getHour())) {
                data.setWeek(week.getCn_name());
                if (week.getCount() > 1) {// 周几出现的次数大于1，则需要将该数值除以出现的次数：算平均
                    data.setSum(data.getSum().divide(
                            new BigDecimal(week.getCount()), round,
                            BigDecimal.ROUND_UP));
                    // data.setSum(data.getSum().setScale(2,
                    // BigDecimal.ROUND_HALF_UP)); // 月平均:除以月以后；
                }
                hour = CalendarUtils.hourToAPm(hour);// 24小时：转换成上午下午12时制：如：6a、12a、6p
                hourData = new HourData(hour, data.getSum());
                return hourData;
            }
        }
        hour = CalendarUtils.hourToAPm(hour);// 24小时：转换成上午下午12时制：如：6a、12a、6p
        hourData = new HourData(hour, zero);
        return hourData;
    }

    /**
     * 上网时段分布
     *
     * @param para 1、weekend=weekend 只查询周六、周日的数据
     *             2、weekend=notweekend 只查询周一到周五的数据
     * @return
     * @throws Exception
     */
    @Cacheable(value = CacheConstant.NET_CACHE, key = "'net:online:' +#root.methodName +'.' + #para.hashCode()")
    public Map<Integer, Integer> hourList(Params para) throws Exception {
        String outid = para.getOutid();
        List<ResultData> hourList = new ArrayList<>();
        if (StringUtils.isNotEmpty(para.getOutid())) {
            hourList = radacctTimeMapper.hourListOne(para);
        }

        if (null == hourList || hourList.size() == 0) {
            return null;
        }
        setStartEndTime(para);

        int weekend = 0;//周一到周五的
        int notweekend = 0;//周六周日的
        List<Week> weekNameList = CalendarUtils.getWeekCountBetween(para.getBdate(),
                para.getEdate());
        int i = 0;
        for (Week week : weekNameList) {
            if (i < 5) {//周一到周五的
                notweekend = notweekend + week.getCount();
            } else {//周六周日的
                weekend = weekend + week.getCount();
            }
            i++;
        }
        int dayNum = 0;
        if ("weekend".equals(para.getWeekend())) {//根据当前查询是：周末还是非周末，选择除以对应出现的天数
            dayNum = weekend;
        } else if ("notweekend".equals(para.getWeekend())) {
            dayNum = notweekend;
        }
        if (dayNum == 0) {//防止0作为被除数
            dayNum = 1;
        }
        // 为了和修改前的返回格式一致，前端就不用再修改了
        Map<Integer, Integer> hourmap = new HashMap<>();
        // 时段内汇总平均 - 周几排序、各个小时：汇总平均列表
        for (int j = 0; j < 24; j++) {// 循环24个小时：：数据中也是从0开始的
            String hour = j + "";
            HourData hourData = getHourData(hourList, hour);// 循环获取，每个小时的数据，
            if (null != outid && outid.length() > 0) {
                hourmap.put(j, hourData.getSum().intValue());
            } else {// 个人的，不用平均:取在线天数（个人每天（时段）最多一个人在线 。用这个来当作在线天数）
                BigDecimal avg = hourData.getSum().divide(
                        new BigDecimal(dayNum), 0, BigDecimal.ROUND_HALF_UP);// 人数、人次都进位
                hourmap.put(j, avg.intValue());
            }
        }
        // 把0点变成24点：供给前端显示
        hourmap.put(24, hourmap.get(0));
        hourmap.remove(0);

        return hourmap;
    }

    private String getHourFormat(int hour) {
        int startHuor = hour;
//		startHuor=startHuor+1;
        int endHuor = startHuor + 1;
        String hourstr = "";
        String endhuorstr = "";
        if (startHuor < 10) {
            hourstr = "0" + startHuor;
        } else {
            hourstr = startHuor + "";
        }//
        if (endHuor < 10) {
            endhuorstr = "0" + endHuor;
        } else {
            endhuorstr = endHuor + "";
        }
        hourstr = hourstr + ":00" + "-" + endhuorstr + ":00";
        return hourstr;
    }

    /**
     * 根据给定的星期几序号；
     * <p>
     * 获取查询结果集当中对应的数据项；如果不存在，则新建一个该日期的，数值为0的数据项
     *
     * @param list
     * @param hour %k 小时(0……23)
     * @return
     */
    private HourData getHourData(List<ResultData> list, String hour) {
        HourData hourData = null;// 指定日期的data;如果结果集中没有，则构造一个
        for (ResultData data : list) {
            if (hour.equals(data.getHour())) {
                hourData = new HourData(hour, data.getSum());
                return hourData;
            }
        }
        hourData = new HourData(hour, zero_0);
        return hourData;
    }


    /**
     * 2、为了方便后面的统计：这里把跨天的上网时间都拆成2个，在不同天的；并且计算每条记录的上网时间
     *
     * @param radacct
     * @throws ParseException
     */
    private void everydaySave(A3RadacctTime radacct) throws ParseException {
        // 2、为了方便后面的统计：这里把跨天的上网时间都拆成2个，在不同天的；并且计算每条记录的上网时间
        int days = CalendarUtils.getDateSpace(radacct.getAcctstarttime(),
                radacct.getAcctstoptime());
        if (days == 0) {// 2.1、该记录没有跨天::直接插入数据库
            int seconds = CalendarUtils.secondSpace(radacct.getAcctstarttime(),
                    radacct.getAcctstoptime());
            if (seconds > 0) {
                radacct.setAcctsessiontime(seconds);
                radacctTimeMapper.insertSelective(radacct);
            }
        } else {// 2.1、该记录跨天：拆成多天的多条记录
            A3RadacctTime dayRada = new A3RadacctTime();
            dayRada.setOutid(radacct.getOutid());
            // 为方便后面查询：这里把常用查询条件字段也存入表中
            dayRada.setSchoolCode(radacct.getSchoolCode());
            dayRada.setFacultyCode(radacct.getFacultyCode());
            dayRada.setMajorCode(radacct.getMajorCode());
            dayRada.setClassCode(radacct.getClassCode());
            dayRada.setSex(radacct.getSex());
            //
            Date timeOn = radacct.getAcctstarttime();
            for (int i = 0; i < days; i++) {
                dayRada.setAcctstarttime(timeOn);
                timeOn = CalendarUtils.nextZeroTime(timeOn);
                dayRada.setAcctstoptime(timeOn);
                int seconds = CalendarUtils.secondSpace(
                        dayRada.getAcctstarttime(), dayRada.getAcctstoptime());
                if (seconds > 0) {
                    dayRada.setAcctsessiontime(seconds);
                    radacctTimeMapper.insertSelective(dayRada);
                }
            }
            // 从最后一个0点到结束日期时间
            dayRada.setAcctstarttime(timeOn);
            dayRada.setAcctstoptime(radacct.getAcctstoptime());
            int seconds = CalendarUtils.secondSpace(dayRada.getAcctstarttime(),
                    dayRada.getAcctstoptime());
            if (seconds > 0) {
                dayRada.setAcctsessiontime(seconds);
                radacctTimeMapper.insertSelective(dayRada);
            }
        }
    }

    /**
     * 如果比开始时间早：则把开始时间更新
     *
     * @param max
     * @param startDate
     */
    private void setBeforeStart(A3RadacctTime max, Date startDate) {
        if (max.getAcctstarttime().after(startDate)) {
            max.setAcctstarttime(startDate);
        }
    }

    /**
     * 如果比结束时间晚：则把结束时间更新
     *
     * @param max
     * @param endDate
     */
    private void setAfterEnd(A3RadacctTime max, Date endDate) {
        if (max.getAcctstoptime().before(endDate)) {
            max.setAcctstoptime(endDate);
        }
    }



    /**
     * 根据给定的星期几序号；获取查询结果集当中对应的数据项；如果不存在，则新建一个该日期的，数值为0的数据项
     * <p>
     * //由于返回的是多种类型的数据：所以要多次循环
     *
     * @param list
     * @param week 星期几：序号
     * @return
     */
    private List<ResultData> getWeekData(List<ResultData> list, Week week) {
        List<ResultData> resultlist = new ArrayList<>();// //由于返回的是多种类型的数据
        ResultData dayData = null;// 指定日期的data;如果结果集中没有，则构造一个
        for (ResultData data : list) {
            if (data.getWeekindex().intValue() == week.getEn_index()) {
                // data.setWeek(week.getCn_name());
                if (week.getCount() > 1) {// 周几出现的次数大于1，则需要将该数值除以出现的次数：算平均
                    data.setSum(data.getSum().divide(
                            new BigDecimal(week.getCount()), 2,
                            BigDecimal.ROUND_HALF_UP));
                }
                resultlist.add(data);
            }
        }
        if (resultlist.size() == 0) {
            dayData = new ResultData();
            dayData.setWeekindex(week.getEn_index());
            dayData.setName("");
            // dayData.setWeek(week.getCn_name());
            dayData.setSum(zero);
            resultlist.add(dayData);
        }
        return resultlist;
    }

    /***************************************Add By Linxiaojun*********************************************/

    /***************************************个人*********************************************/
    /**
     * 上网总时长分布： Chart
     *
     * @param para
     * @return
     * @throws ParseException
     */
    @Override
    public Map<String, List<?>> getDurationDistChartPersonal(NetParams para) throws ParseException {
        List<Map<String, String>> classList = radacctTimeKylinMapper.selectClassCodeByOutid(para);
        if (CollectionUtils.isEmpty(classList)) return null;

        StringBuffer strbuf = new StringBuffer();
        classList.stream().forEach(map -> map.entrySet().stream().forEach(entry -> strbuf.append(entry.getValue()).append(",")));
        para.setClassSelect(strbuf.substring(0, strbuf.length() - 1));

        List<Map<String, Object>> list = radacctTimeKylinMapper.selectTotalDurationDistChartAndTablePersonal(para);
        if (CollectionUtils.isEmpty(list)) return null;

        Map<String, List<?>> resultMap = new HashMap<>();
        dealData4TotalDurationDistChartPersonal(resultMap, list);

        return resultMap;
    }

    private void dealData4TotalDurationDistChartPersonal(Map<String, List<?>> resultMap, List<Map<String, Object>> list) {
        List<String> dateList = new ArrayList<>();
        List<Double> personalDurationList = new ArrayList<>();
        List<Double> avgClassDurationList = new ArrayList<>();

        list.stream().forEach(map -> {
            dateList.add(DateUtils.date2Str4GMT16("yyyy-MM-dd", (Date) map.get("acctstarttime")));
            Double personalDuration = 0D;
            if (map.get("personal_duration") != null)
                personalDuration = Double.parseDouble(String.format("%.2f", (Long) map.get("personal_duration") / 3600.00));
            if (personalDuration > 24.00) personalDuration = 24.00;
            personalDurationList.add(personalDuration);

            Long count = (Long) map.get("num");
            Double avgClassDuration = Double.parseDouble(String.format("%.2f", (Long) map.get("total_duration") / (count * 3600.00)));
            if (avgClassDuration > 24.00) avgClassDuration = 24.00;
            avgClassDurationList.add(avgClassDuration);
        });

        resultMap.put("dateList", dateList);
        resultMap.put("personalDurationList", personalDurationList);
        resultMap.put("avgClassDurationList", avgClassDurationList);
    }


    /***************************************群体*********************************************/
    /**
     *
     * 针对上网时长分布、上网人群分析、上网时长TOP三个模块说明：
     * 1.图暂时补零，表不需补零，后期统一在数据源中处理
     * 2.setStartEndTime()目前调的不是kylin，数据会产生偏差，后期数据源统一处理之后这个方法也就不再调用了，所以这里就不改了
     * 3.观察了生产环境radacctlog_*表，从2016-12到2017-11还未出现数据断层（按日期）现象
     *
     */
    /**
     * 上网总时长分布： Chart
     *
     * @param
     * @return
     * @throws ParseException
     */
	@Override
//	@Cacheable(value = CacheConstant.NET_CACHE, key = "'net:online:' +#root.methodName +'.' + #para.hashCode()")
	public Map<String, List<?>> getDurationDistChart(NetParams para) throws ParseException {
        List<ResultData> list = radacctTimeKylinMapper.selectTotalDurationDistChartAndTable(para);
		if (CollectionUtils.isEmpty(list)) return null;

		Map<String, List<?>> resultMap = new HashMap<>();
		dealData4TotalDurationDistChart(resultMap, list);

		return resultMap;
	}
    private void dealData4TotalDurationDistChart(Map<String, List<?>> resultMap, List<ResultData> list) {
        List<String> dateList = new ArrayList<>();
        List<Integer> countList = new ArrayList<>();
        List<Double> avgDurationList = new ArrayList<>();

        list.stream().forEach(b -> {
            dateList.add(DateUtils.date2Str4GMT16("yyyy-MM-dd", b.getAcctstarttime()));
            int count = Math.toIntExact(b.getNum());
            countList.add(count);
            Double avgDuration = Double.parseDouble(String.format("%.2f", b.getTotalDuration().doubleValue() / (count * 3600)));
            if (avgDuration > 24.00) avgDuration = 24.00;
            avgDurationList.add(avgDuration);
        });

        resultMap.put("dateList", dateList);
        resultMap.put("countList", countList);
        resultMap.put("avgDurationList", avgDurationList);
    }

    /**
     * 上网总时长分布： Table 分页
     *
     * @param para
     * @return
     * @throws ParseException
     */
    @Override
//	@Cacheable(value = CacheConstant.NET_CACHE, key = "'net:online:' +#root.methodName +'.' + #para.hashCode()")
    public List<Map<String, Object>> getDurationDistTable(NetParams para) throws ParseException {
        List<ResultData> resultList = radacctTimeKylinMapper.selectTotalDurationDistChartAndTable(para);
        if (CollectionUtils.isEmpty(resultList)) return null;

        // 处理数据
        resultList.stream()
                .forEach(b -> {
                    b.setDatetimeStr(DateUtils.date2Str4GMT16("yyyy-MM-dd", b.getAcctstarttime()));
                    BigDecimal totalDurationByHours = b.getTotalDuration().divide(new BigDecimal(3600), 2, BigDecimal.ROUND_HALF_UP);
                    b.setTotalDuration(totalDurationByHours);
                    b.setAvg(totalDurationByHours.divide(new BigDecimal(b.getNum()), 2, BigDecimal.ROUND_HALF_UP));
                    b.setAcctstarttime(null);
                });

        return BeanUtil.objectsToMapsOffNull(resultList);
    }

    /**
     * 上网总时长分布： 总记录数
     *
     * @param para
     * @return
     */
    @Override
    public int getDurationDistRecordNum(NetParams para) {
        return radacctTimeKylinMapper.selectDurationDistRecordNum(para);
    }

    /**
     * 上网人群分析: Chart
     *
     * @param para
     * @return
     * @throws ParseException
     */
    @Override
    public Map<String, Collection> getCrowdAnalysisChart(NetParams para) throws ParseException {
        List<ResultData> resultSexList = radacctTimeKylinMapper.selectCrowdAnalysisChartAndTable(para);
        if (CollectionUtils.isEmpty(resultSexList)) return null;

        // 获取查询时间段内的所有日期：防止查询结果当中，有很多天数据是空的，无法显示
//		if (StringUtils.isEmpty(para.getBdate()) || StringUtils.isEmpty(para.getEdate())) {
//            getStartEndTime4Kylin(para);
//		}
//		List<String> allDays = CalendarUtils.getAllDatesBetween(para.getBdate(), para.getEdate());

        // 处理数据
        Map<String, Collection> resultMap = new HashMap<>();
        dealData4CrowdAnalysisChart(resultMap, resultSexList);
        return resultMap;
    }

    private void dealData4CrowdAnalysisChart(Map<String, Collection> resultMap, List<ResultData> resultSexList) {
        Set<String> dateList = new LinkedHashSet<>();
        List<Long> maleCountChartList = new ArrayList<>();
        List<Long> femaleCountChartList = new ArrayList<>();
        List<Double> durationChartList = new ArrayList<>();
        Map<Date, Integer> compMap = new HashMap<>();

        double yesterdayDuration = 0;
        double avgDurationTmp = 0;
        for (int i = 0; i < resultSexList.size(); i++) {
            ResultData rd = resultSexList.get(i);

            // 时间
            String realDateStr = DateUtils.date2Str4GMT16("yyyy-MM-dd", rd.getAcctstarttime());
            Date realDate = DateUtils.parse(realDateStr, "yyyy-MM-dd");
            Date realYesterdayDate = DateUtils.addDays(realDate, -1);
            dateList.add(realDateStr);

            // 获取总时长、总人数(男女累加)
            avgDurationTmp += rd.getTotalDuration().divide(new BigDecimal(3600 * rd.getNum()), 10, BigDecimal.ROUND_HALF_UP).doubleValue();
            if (SexEnum.MALE.getValue().equals(rd.getSex())) {
                maleCountChartList.add(rd.getNum());
                if (!compMap.containsKey(realDate)) {
                    if (!compMap.containsKey(realYesterdayDate)) {
                        compMap.put(realDate, 1);
                    } else {
                        if (compMap.containsValue(1)) {
                            femaleCountChartList.add((long) 0);
                            double avgDuration = Double.parseDouble(String.format("%.2f", yesterdayDuration / 2));
                            durationChartList.add(avgDuration);
                        }
                        yesterdayDuration = avgDurationTmp;
                        avgDurationTmp = 0;
                        compMap.remove(realYesterdayDate);
                        compMap.put(realDate, 1);
                    }
                } else {
                    compMap.remove(realDate);
                    compMap.put(realDate, 2);
                    double avgDuration = Double.parseDouble(String.format("%.2f", avgDurationTmp / 2));
                    durationChartList.add(avgDuration);
                    avgDurationTmp = 0;
                }
            } else if (SexEnum.FEMALE.getValue().equals(rd.getSex())) {
                femaleCountChartList.add(rd.getNum());
                if (!compMap.containsKey(realDate)) {
                    if (!compMap.containsKey(realYesterdayDate)) {
                        compMap.put(realDate, 1);
                    } else {
                        if (compMap.containsValue(1)) {
                            femaleCountChartList.add((long) 0);
                            double avgDuration = Double.parseDouble(String.format("%.2f", yesterdayDuration / 2));
                            durationChartList.add(avgDuration);
                        }
                        yesterdayDuration = avgDurationTmp;
                        avgDurationTmp = 0;
                        compMap.remove(realYesterdayDate);
                        compMap.put(realDate, 1);
                    }
                } else {
                    compMap.remove(realDate);
                    compMap.put(realDate, 2);
                    double avgDuration = Double.parseDouble(String.format("%.2f", avgDurationTmp / 2));
                    durationChartList.add(avgDuration);
                    avgDurationTmp = 0;
                }
            }

            if (i == (resultSexList.size() - 1)) {
                if (maleCountChartList.size() != femaleCountChartList.size()) {
                    double avgDuration = Double.parseDouble(String.format("%.2f", yesterdayDuration / 2));
                    durationChartList.add(avgDuration);
                    avgDurationTmp = 0;
                }
                if (maleCountChartList.size() > femaleCountChartList.size()) {
                    femaleCountChartList.add((long) 0);
                } else {
                    maleCountChartList.add((long) 0);
                }
            }
        }

        resultMap.put("dateList", dateList);
        resultMap.put("maleCountChartList", maleCountChartList);
        resultMap.put("femaleCountChartList", femaleCountChartList);
        resultMap.put("durationChartList", durationChartList);
    }

    /**
     * 上网人群分析: Table
     *
     * @param para
     * @return
     */
    @Override
    public List<Map<String, Object>> getCrowdAnalysisTable(NetParams para) throws ParseException {
        List<ResultData> resultList = radacctTimeKylinMapper.selectCrowdAnalysisChartAndTable(para);
        if (CollectionUtils.isEmpty(resultList)) return null;

        // 处理数据
        resultList.stream()
                .forEach(b -> {
                    b.setDatetimeStr(DateUtils.date2Str4GMT16("yyyy-MM-dd", b.getAcctstarttime()));
                    BigDecimal totalDurationByHours = b.getTotalDuration().divide(new BigDecimal(3600), 2, BigDecimal.ROUND_HALF_UP);
                    b.setTotalDuration(totalDurationByHours);
                    b.setAvg(totalDurationByHours.divide(new BigDecimal(b.getNum()), 2, BigDecimal.ROUND_HALF_UP));
                    b.setAcctstarttime(null);
                });

        return BeanUtil.objectsToMapsOffNull(resultList);
    }

    /**
     * 上网人群分析： 总记录数
     *
     * @param para
     * @return
     */
    @Override
    public int getCrowdAnalysisRecordNum(NetParams para) {
        return radacctTimeKylinMapper.selectCrowdAnalysisRecordNum(para);
    }


    /**
     * 上网时长TOP10: Chart
     *
     * @param para
     * @return
     */
    @Override
    public List<Map<String, Object>> getDurationTopChart(NetParams para) throws ParseException {
        List<ResultData> list = radacctTimeKylinMapper.selectDurationTopChartAndTable(para);
        if (CollectionUtils.isEmpty(list)) return null;

        // 如果查询全部，则去数据库检索开始时间和结束时间
        if (StringUtils.isEmpty(para.getBdate()) || StringUtils.isEmpty(para.getEdate())) {
            getStartEndTime4Kylin(para);
        }

        // 计算总天数
        int dayNum = CalendarUtils.getDateSpace(para.getBdate(), para.getEdate());

        list.stream()
                .forEach(b -> {
                    BigDecimal totalHours = b.getTotalDuration().divide(new BigDecimal(3600), 2, BigDecimal.ROUND_HALF_UP);
                    b.setSum(totalHours);
                    b.setAllAvg(totalHours.divide(new BigDecimal(dayNum), 2, BigDecimal.ROUND_HALF_UP));
                    // 有效天数日均时长
                    b.setAvg(totalHours.divide(new BigDecimal(b.getValidDays()), 2, BigDecimal.ROUND_HALF_UP));
                    b.setTotalDuration(null);
                    // 总天数
                    b.setPeopleNum(dayNum);
                });

        return BeanUtil.objectsToMapsOffNull(list);
    }

    /**
     * 上网时长TOP10: Table
     *
     * @param para
     * @return
     */
    @Override
    public List<Map<String, Object>> getDurationTopTable(NetParams para) throws ParseException {
        List<ResultData> list = radacctTimeKylinMapper.selectDurationTopChartAndTable(para);
        if (CollectionUtils.isEmpty(list)) return null;

        // 如果查询全部，则去数据库检索开始时间和结束时间
        if (StringUtils.isEmpty(para.getBdate()) || StringUtils.isEmpty(para.getEdate())) {
            getStartEndTime4Kylin(para);
        }

        // 计算总天数
        int dayNum = CalendarUtils.getDateSpace(para.getBdate(), para.getEdate());

        list.stream()
                .forEach(b -> {
                    BigDecimal totalHours = b.getTotalDuration().divide(new BigDecimal(3600), 2, BigDecimal.ROUND_HALF_UP);
                    b.setSum(totalHours);
                    b.setAllAvg(totalHours.divide(new BigDecimal(dayNum), 2, BigDecimal.ROUND_HALF_UP));
                    // 有效天数日均时长
                    b.setAvg(totalHours.divide(new BigDecimal(b.getValidDays()), 2, BigDecimal.ROUND_HALF_UP));
                    b.setTotalDuration(new BigDecimal(b.getValidDays()));
                    b.setPeopleNum(dayNum);

                    if (b.getTotalDuration1() != null) {
                        BigDecimal gameHours = b.getTotalDuration1().divide(new BigDecimal(3600), 2, BigDecimal.ROUND_HALF_UP);
                        b.setSum1(gameHours);
                        //b.setAllAvg1(gameHours.divide(new BigDecimal(dayNum), 2, BigDecimal.ROUND_HALF_UP));
                        // 有效天数日均时长
                        b.setAvg1(gameHours.divide(new BigDecimal(b.getValidDays1()), 2, BigDecimal.ROUND_HALF_UP));
                    } else {
                        b.setSum1(new BigDecimal(0.00).setScale(2));
                        b.setAvg1(new BigDecimal(0.00).setScale(2));
                        //b.setTotalDuration1(new BigDecimal(0));
                    }
                    b.setTotalDuration1(new BigDecimal(b.getValidDays1()));
                });

        return BeanUtil.objectsToMapsOffNull(list);
    }

    /**
     * 上网时长总记录数
     *
     * @param para
     * @return
     */
    @Override
    public int getDurationTopRecordNum(NetParams para) {
        return radacctTimeKylinMapper.selectDurationTopRecordNum(para);
    }

    /**
     * 上网终端： Chart (pc & mobile)
     *
     * @param para
     * @return
     */
    @Override
    public Map<String, List<Map<String, Object>>> getTerminalTypeChart(NetParams para) {
        List<ResultData> list = radacctTimeKylinMapper.selectTerminalTypeChartAndTable(para);
        if (CollectionUtils.isEmpty(list)) return null;

        return dealData4TerminalTypeChart(list);
    }

    private Map<String, List<Map<String, Object>>> dealData4TerminalTypeChart(List<ResultData> list) {
        List<NVResultData> pcList = new ArrayList<>();
        List<NVResultData> mobileList = new ArrayList<>();
        for (ResultData rd : list) {
            NVResultData data = new NVResultData();
            data.setName(rd.getSex());
            data.setValue(rd.getNum().intValue());
            if (rd.getClienttype().equalsIgnoreCase(TerminalEnum.PC.getValue())) {
                pcList.add(data);
            } else {
                mobileList.add(data);
            }
        }
        Map<String, List<Map<String, Object>>> resultMap = new HashMap<>();
        resultMap.put("pc", BeanUtil.objectsToMapsOffNull(pcList));
        resultMap.put("mobile", BeanUtil.objectsToMapsOffNull(mobileList));
        return resultMap;
    }

    /**
     * 上网终端： Table (pc & mobile)
     *
     * @param para
     * @return
     */
    @Override
    public List<Map<String, Object>> getTerminalTypeTable(NetParams para) {
        List<ResultData> list = radacctTimeKylinMapper.selectTerminalTypeChartAndTable(para);
        if (CollectionUtils.isEmpty(list)) return null;

        List<ResultData> resultList = dealData4TerminalTypeTable(list);
        return BeanUtil.objectsToMapsOffNull(resultList);
    }

    private List<ResultData> dealData4TerminalTypeTable(List<ResultData> list) {
        // 根据终端类型合并男女比例成一条记录
        String pcGenderRatio = null;
        String mobileGenderRatio = null;
        Long pcNum = 0L;
        Long mobileNum = 0L;
        for (ResultData rd : list) {
            if (rd.getClienttype().equalsIgnoreCase(TerminalEnum.PC.getValue())) {
                if (rd.getSex().equalsIgnoreCase(SexEnum.MALE.getValue())) {
                    pcGenderRatio = pcGenderRatio == null ? rd.getNum().toString() : rd.getNum().toString().concat(":").concat(pcGenderRatio);
                } else {
                    pcGenderRatio = pcGenderRatio == null ? rd.getNum().toString() : pcGenderRatio.concat(":").concat(rd.getNum().toString());
                }
                pcNum += rd.getNum();
            } else {
                if (rd.getSex().equalsIgnoreCase(SexEnum.FEMALE.getValue())) {
                    mobileGenderRatio = mobileGenderRatio == null ? rd.getNum().toString() : rd.getNum().toString().concat(":").concat(mobileGenderRatio);
                } else {
                    mobileGenderRatio = mobileGenderRatio == null ? rd.getNum().toString() : mobileGenderRatio.concat(":").concat(rd.getNum().toString());
                }
                mobileNum += rd.getNum();
            }
        }

        List<ResultData> resultList = new ArrayList<>();
        ResultData pcData = new ResultData();
        pcData.setClienttype(TerminalEnum.PC.getValue());
        pcData.setNum(pcNum);
        pcData.setRatio(pcGenderRatio);
        resultList.add(pcData);

        ResultData mobileData = new ResultData();
        mobileData.setClienttype(TerminalEnum.MOBILE.getValue());
        mobileData.setNum(mobileNum);
        mobileData.setRatio(mobileGenderRatio);
        resultList.add(mobileData);
        return resultList;
    }

    /**
     * 访问内容： Chart
     *
     * @param para
     * @return
     */
    @Override
    public List<Map<String, Object>> getVisitContextTopChart(NetParams para) {
        List<ResultData> list = radacctTimeKylinMapper.selectVisitContextChartAndTable(para);
        if (CollectionUtils.isEmpty(list)) return null;
        return BeanUtil.objectsToMapsOffNull(list);
    }

    /**
     * 访问内容： Table
     *
     * @param para
     * @return
     */
    @Override
    public List<Map<String, Object>> getVisitContextTopTable(NetParams para) {
        List<ResultData> list = radacctTimeKylinMapper.selectVisitContextChartAndTable(para);
        if (CollectionUtils.isEmpty(list)) return null;
        return BeanUtil.objectsToMapsOffNull(list);
    }

    /**
     * 访问内容： 总记录数
     *
     * @param para
     * @return
     */
    @Override
    public int getVisitContextTopRecordNum(NetParams para) {
        return radacctTimeKylinMapper.selectVisitContextTopRecordNum(para);
    }

    /**
     * 上网时段： Chart
     *
     * @param para
     * @return
     */
    @Override
    public Map<String, List<NVResultData>> getPeriodChart(NetParams para) {
        Map<String, List<NVResultData>> resultMap = null;
        List<NVResultData> weekdayList = new ArrayList<>();

        para.setWeekday(Integer.parseInt(WeekdayEnum.WEEKDAY.getCode()));
        List<ResultData> list1 = radacctTimeKylinMapper.selectPeriodChart(para);
        if (CollectionUtils.isNotEmpty(list1)) {
            resultMap = new HashMap<>();
            dealData4PeriodChart(weekdayList, list1);
            resultMap.put("weekday", weekdayList);
            weekdayList = new ArrayList<>();
        }

        para.setWeekday(Integer.parseInt(WeekdayEnum.WEEKEND.getCode()));
        List<ResultData> list2 = radacctTimeKylinMapper.selectPeriodChart(para);
        if (CollectionUtils.isNotEmpty(list2)) {
            if (resultMap == null) resultMap = new HashMap<>();
            dealData4PeriodChart(weekdayList, list2);
            resultMap.put("weekend", weekdayList);
        }
        return resultMap;
    }

    private void dealData4PeriodChart(List<NVResultData> weekdayList, List<ResultData> list) {
        int[] hours = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23};
        for (int hour : hours) {
            int value = list.stream()
                    .filter(b -> b.getStartHour() == hour || (b.getStartHour() < hour && b.getStopHour() >= hour))
                    .flatMapToInt(b -> IntStream.of(Math.toIntExact(b.getNum())))
                    .sum();
            String hourStr = String.valueOf(hour);
            String name = hourStr.concat(":00~").concat(hourStr).concat(":59");

            NVResultData data = new NVResultData();
            data.setName(name);
            data.setValue(value);
            weekdayList.add(data);
        }
    }

    /**
     * 上网时段： Table
     *
     * @param para
     * @return
     */
    @Override
    public List<Map<String, Object>> getPeriodTable(NetParams para) {
        List<ResultData> list = radacctTimeKylinMapper.selectPeriodTable(para);
        if (CollectionUtils.isEmpty(list)) return null;

        list.stream().forEach(b -> b.setWeek(WeekdayEnum.getValue(String.valueOf(b.getWeekindex()))));
        return BeanUtil.objectsToMapsOffNull(list);
    }

    /**
     * 社群概述
     *
     * @param para
     * @return
     */
    @Override
    public Map<String, String> getCommunityOverview(NetParams para) {
        Map<String, String> resultMap = new HashMap<>();

        // 上网总时长，人均上网时长
//		List<ResultData> durationList = radacctTimeKylinMapper.selectTotalDurationDistChartAndTable(para);
//        if (CollectionUtils.isEmpty(durationList)) return null;

//        double totalDuration = durationList.stream()
//                .flatMapToDouble(b -> DoubleStream.of(b.getTotalDuration().doubleValue()))
//                .sum();
//        double avgDuration = durationList.stream()
//                .flatMapToDouble(b -> DoubleStream.of(b.getTotalDuration().doubleValue() / b.getNum().doubleValue()))
//                .sum();

        // 人均上网时长
        Double avgDuration = radacctTimeKylinMapper.selectAvgDuration4OverviewCommunity(para);

        if (avgDuration == null) {
            resultMap.put("avgDuration", "0.00");
        } else {
            resultMap.put("avgDuration", String.valueOf(avgDuration));
        }

        // 上网总流量, add in V3.0.0
        Long totalFluxByte = radacctTimeKylinMapper.selectFluxTotal4OverviewCommunity(para);
        if (totalFluxByte == null) {
            resultMap.put("totalFlux", "0.00");
        } else {
            resultMap.put("totalFlux", String.format("%.2f", totalFluxByte / 1024.0 / 1024.0));
        }

        // 上网访问内容, add in V3.0.0
        List<ResultData> list = radacctTimeKylinMapper.selectVisitContextChartAndTable(para);
        if (CollectionUtils.isEmpty(list)) {
            resultMap.put("favor", "");
        } else {
            StringBuffer strbuf = new StringBuffer("");
            list.stream().limit(2).forEach(b -> strbuf.append(b.getName()).append(","));
            resultMap.put("favor", strbuf.substring(0, strbuf.length() - 1));
        }

        // 上网高峰时段
        List<ResultData> periodList = radacctTimeKylinMapper.selectPeriodChart(para);
        if (CollectionUtils.isEmpty(periodList)) {
            resultMap.put("peakHours", "");
        } else {
            List<NVResultData> nvResultDataList = new ArrayList<>();
            dealData4PeriodChart(nvResultDataList, periodList);
            Optional<NVResultData> max = nvResultDataList.stream().max(Comparator.comparing(b -> b.getValue()));
            resultMap.put("peakHours", String.valueOf(max.get().getName()));
        }

        return resultMap;
    }

    /**
     * 上网总时长分布： 获取开始结束时间
     *
     * @param para
     */
    private void getStartEndTime4Kylin(NetParams para) {
        ResultData resultData = radacctTimeKylinMapper.selectStartEndTime(para);

        // 此处无需判断resultData是否null，在调用之前已经判断

        para.setBdate(DateUtils.date2Str4GMT16("yyyy-MM-dd", resultData.getBdate()));
        para.setEdate(DateUtils.date2Str4GMT16("yyyy-MM-dd", DateUtils.addDays(resultData.getEdate(), 1)));
    }

    /**
     * 总人数： 男 + 女
     *
     * @param para
     * @return
     */
    public int getTotalPeople(NetParams para) {
        return radacctTimeKylinMapper.selectTotalPeople(para);
    }


    /**
     * 总人数： 男
     *
     * @param para
     * @return
     */
    public int getTotalPeopleMale(NetParams para) {
        return radacctTimeKylinMapper.selectTotalPeopleMale(para);
    }

    /**
     * 总人数： 女
     *
     * @param para
     * @return
     */
    @Override
    public int getTotalPeopleFemale(NetParams para) {
        return radacctTimeKylinMapper.selectTotalPeopleFemale(para);
    }

    /**
     * Just for test
     *
     * @param para
     * @return
     */
    @Override
    public CommResponse getCommResponses(NetParams para) {
        try {
            Object obj = radacctTimeKylinMapper.selectTerminalTypeChartAndTable(para);
            return new CommResponse(obj, StatusCodeEnum.OK, CommResponse.RESULT_MSG_SUCCESS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new CommResponse(null, StatusCodeEnum.INTERNAL_SERVER_ERROR, CommResponse.RESULT_MSG_FAILURE);
    }

    /**
     * 上网内容热度: 下拉框 serv app类型
     *
     * @param para
     * @return
     */
    @Override
    public List<Map<String, Object>> getContentHeatServAppType(NetParams para) {
        List<Map<String, Object>> list = radacctTimeKylinMapper.selectContentHeatServAppType(para);
        if (CollectionUtils.isEmpty(list)) return null;

        Map<String, List<String>> servMap = new HashMap<>();
        list.stream().forEach(map -> {
            String serv = (String) map.get("serv");
            String app = (String) map.get("app");
            if (servMap.containsKey(serv)) {
                List<String> apps = (List) servMap.get(serv);
                apps.add(app);
            } else {
                List<String> apps = new ArrayList<>();
                apps.add(app);
                servMap.put(serv, apps);
            }
        });

        // order servMap by total_flux
        List<Map<String, Object>> servFluxList = radacctTimeKylinMapper.selectContentHeatServFlux(para);
        Map<String, List<String>> servLinkedMap = new LinkedHashMap<>();
        servFluxList.stream().forEach(map -> {
            for (Iterator<Entry<String, Object>> it = map.entrySet().iterator(); it.hasNext(); ) {
                Entry<String, Object> entry = it.next();
                String value = (String) entry.getValue();
                it.remove();
                servLinkedMap.put(value, servMap.get(value));
                break;
            }
        });

        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Entry map : servLinkedMap.entrySet()) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("serv", map.getKey());
            resultMap.put("child", map.getValue());
            resultList.add(resultMap);
        }
        return resultList;
    }

    /**
     * 上网内容热度: Chart
     * V3.0.0
     *
     * @param para
     * @return
     */
    @Override
    public List<Map<String, Object>> getContentHeatChart(NetParams para) throws ParseException {
        List<Map<String, Object>> list = radacctTimeKylinMapper.selectContentHeatChartAndTable(para);
        if (CollectionUtils.isEmpty(list)) return null;

        list.stream().forEach(map -> {
            Long totalFluxByte = (Long) map.remove("total_flux");
            Double totalFluxMB = Double.valueOf(String.format("%.2f", (double) totalFluxByte / 1024 / 1024));
            map.put("total_flux", totalFluxMB);

            Long seconds = (Long) map.remove("seconds");
            Double hours = Double.valueOf(String.format("%.2f", (double) seconds / 3600));
            map.put("hours", hours);
        });
        return list;
    }

    /**
     * 上网内容热度: 点击Chart弹出学生列表
     *
     * @param para
     * @return
     */
    @Override
    public List<Map<String, Object>> getStudentListByApp(NetParams para) {
        return radacctTimeKylinMapper.selectStudentListByApp(para);
    }

    /**
     * 上网内容热度: 点击Chart弹出学生列表: 获取总记录数
     *
     * @param para
     * @return
     */
    @Override
    public int getAppStudentRecordNum(NetParams para) {
        return radacctTimeKylinMapper.selectAppStudentRecordNum(para);
    }

    // 上网内容热度： get min, max of record_date
    private void getStartEndTime4ContentHeat(NetParams para) {
        Map<String, Object> resultMap = radacctTimeKylinMapper.selectStartEndTime4ContentHeat(para);

        // 此处无需判断resultData是否null，在调用之前已经判断

        para.setBdate(DateUtils.date2Str4GMT16("yyyy-MM-dd", (Date) resultMap.get("bdate")));
        para.setEdate(DateUtils.date2Str4GMT16("yyyy-MM-dd", DateUtils.addDays((Date) resultMap.get("edate"), 1)));
    }

    // generate data for chart and table
    private void dealData4ContentHeatChart(List<Map<String, Object>> resultList, List<Map<String, Object>> list, List<String> allDays, String[] apps) {
        String serv = (String) list.get(0).get("serv");

        Map<String, List<List<Object>>> appMap = new HashMap<>();
        Map<String, Object> dateMap = new HashMap<>();
        Set<String> dateSet = new LinkedHashSet<>();    // 一天中有多个app时,只取当天时间一次

        list.forEach(map -> {
            String realDate = DateUtils.date2Str4GMT16("yyyy-MM-dd", (Date) map.get("record_date"));
            dateSet.add(realDate);
            Long totalFluxByte = (Long) map.get("total_flux");
            Double totalFluxMB = Double.valueOf(String.format("%.2f", (double) totalFluxByte / 1024 / 1024));

            Long seconds = (Long) map.get("seconds");
            Double hours = Double.valueOf(String.format("%.2f", (double) seconds / 3600));

            String app = (String) map.get("app");
            if (appMap.containsKey(app)) {
                List<List<Object>> appDetailList = appMap.get(app);
                List<Object> detailList = new ArrayList<>();
                detailList.add(realDate);
                detailList.add(totalFluxMB);
                detailList.add(hours);
                detailList.add(map.get("visit_count"));
                detailList.add(map.get("people_count"));
                appDetailList.add(detailList);
            } else {
                List<List<Object>> appDetailList = new ArrayList<>();
                List<Object> detailList = new ArrayList<>();
                detailList.add(realDate);
                detailList.add(totalFluxMB);
                detailList.add(hours);
                detailList.add(map.get("visit_count"));
                detailList.add(map.get("people_count"));
                appDetailList.add(detailList);
                appMap.put(app, appDetailList);
            }
        });

        dateMap.put("dateList", dateSet);
        resultList.add(dateMap);
        for (Entry map : appMap.entrySet()) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("serv", serv);
            resultMap.put("app", map.getKey());
            resultMap.put("detail", map.getValue());
            resultList.add(resultMap);
        }
    }

    /**
     * 上网内容热度: Table
     *
     * @param para
     * @return
     */
    @Override
    public List<Map<String, Object>> getContentHeatTable(NetParams para) {
        List<Map<String, Object>> list = radacctTimeKylinMapper.selectContentHeatChartAndTable(para);
        if (CollectionUtils.isEmpty(list)) return null;

        list.stream().forEach(map -> {
            Date recordDate = (Date) map.remove("record_date");
            map.put("record_date", DateUtils.date2Str4GMT16("yyyy-MM-dd", recordDate));

            Long totalFluxByte = (Long) map.remove("total_flux");
            Double totalFluxMB = Double.valueOf(String.format("%.2f", (double) totalFluxByte / 1024 / 1024));
            map.put("total_flux", totalFluxMB);

            Long seconds = (Long) map.remove("seconds");
            Double hours = Double.valueOf(String.format("%.2f", (double) seconds / 3600));
            map.put("hours", hours);
        });
        return list;
    }

    /**
     * 上网内容热度: 总记录数
     *
     * @param para
     * @return
     */
    @Override
    public int getContentHeatRecordNum(NetParams para) {
        return radacctTimeKylinMapper.selectContentHeatRecordNum(para);
    }

    /**
     * 上网流量: Chart
     *
     * @param para
     * @return
     */
    public Map<String, List<String>> getFluxChart(NetParams para) throws ParseException {
        List<Map<String, Object>> list = radacctTimeKylinMapper.selectFluxChartAndTable(para);
        if (CollectionUtils.isEmpty(list)) return null;

        // 处理数据
        Map<String, List<String>> resultMap = new HashMap<>();
        dealData4FluxChart(resultMap, list);
        return resultMap;
    }

    private void dealData4FluxChart(Map<String, List<String>> resultMap, List<Map<String, Object>> list) {
        List<String> servList = new ArrayList<>();
        List<String> downfluxList = new ArrayList<>();
        List<String> upfluxList = new ArrayList<>();

        list.stream().forEach(map -> {
            servList.add((String) map.get("serv"));

            Long downFluxByte = (Long) map.get("down_flux");
            Double downFluxMB = Double.valueOf(String.format("%.2f", (double) downFluxByte / 1024 / 1024));
            downfluxList.add(String.valueOf(downFluxMB));

            Long upFluxByte = (Long) map.get("up_flux");
            Double upFluxMB = Double.valueOf(String.format("%.2f", (double) upFluxByte / 1024 / 1024));
            upfluxList.add(String.valueOf(upFluxMB));
        });
        resultMap.put("serv_list", servList);
        resultMap.put("down_flux", downfluxList);
        resultMap.put("up_flux", upfluxList);
    }

    /**
     * 上网流量: Table
     *
     * @param para
     * @return
     */
    public List<Map<String, Object>> getFluxTable(NetParams para) {
        List<Map<String, Object>> list = radacctTimeKylinMapper.selectFluxChartAndTable(para);
        if (CollectionUtils.isEmpty(list)) return null;

        list.stream().forEach(map -> {
            Long downFluxByte = (Long) map.get("down_flux");
            Double downFluxMB = Double.valueOf(String.format("%.2f", (double) downFluxByte / 1024 / 1024));
            map.put("down_flux", downFluxMB);

            Long upFluxByte = (Long) map.get("up_flux");
            Double upFluxMB = Double.valueOf(String.format("%.2f", (double) upFluxByte / 1024 / 1024));
            map.put("up_flux", upFluxMB);
        });
        return list;
    }

    /**
     * 上网流量: 总记录数
     *
     * @param para
     * @return
     */
    public int getFluxRecordNum(NetParams para) {
        return radacctTimeKylinMapper.selectFluxRecordNum(para);
    }

    /**
     * 个人画像: 上网特征
     *
     * @param para
     * @return
     */
    public Map<String, String> getNetOverviewPersonal(NetParams para) {
        // 为了预警展示的上网总时长数据一致性,直接差预警结果表, early_warning_r
        para.setColumnName("net_time_sum");
        Double totalDuration = earlyWarningRMapper.selectTotalResult(para);

        if (totalDuration == null) return null;

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("totalDuration", String.valueOf(totalDuration));

        // 获取上网高峰时段
        List<ResultData> periodList = radacctTimeKylinMapper.selectPeriodChart(para);
        List<NVResultData> nvResultDataList = new ArrayList<>();
        dealData4PeriodChart(nvResultDataList, periodList);
        Optional<NVResultData> max = nvResultDataList.stream().max(Comparator.comparing(b -> b.getValue()));

        resultMap.put("peakHours", String.valueOf(max.get().getName()));
        //获取平均上网时长
        Map<String, Object> map = radacctTimeKylinMapper.getPersonNetTime(para);
        if (map != null) {
            //总天数
            BigDecimal totalNum = new BigDecimal((Long) map.get("total_num"));
            //总上网时长（单位是 秒）转换成小时
            BigDecimal totalNetTime = new BigDecimal((Long) map.get("total_radacct")).divide(new BigDecimal(3600), 0, BigDecimal.ROUND_HALF_UP);
            BigDecimal avgNetTime = totalNetTime.divide(totalNum, 2, BigDecimal.ROUND_HALF_UP);
            resultMap.put("avgNetTime",avgNetTime.toString());
            resultMap.put("totalNetTime", String.valueOf(totalNum));
        }
        return resultMap;
    }

    /**
     * 当天上网流量Top,用于监控
     *
     * @param para
     * @return
     */
    public Map<String, List<String>> getFluxChartMonitor(NetParams para) {
        return null;
    }

    /**
     * 上网模块：学生列表
     *
     * @param para
     * @return
     */
    @Override
    public List<Map<String, Object>> getNetStudentList(NetParams para) {
        // 获取上网总时长
//		Integer totalDuration = radacctTimeKylinMapper.selectNetOverviewPersonal(para);
        //BeanUtils.copyProperties(para, param);
        // 为了预警展示的上网总时长数据一致性,直接差预警结果表, early_warning_r
        //List<LinkedHashMap<String, Object>> studentList = studentDao.getPageStudent(para);
        List<LinkedHashMap<String, Object>> studentList = this.studentServer.getPageStudent(para);

        if (studentList == null) return null;
        //String[] outidList=String[];
        List<String> outArray = new ArrayList<>();
        for (LinkedHashMap<String, Object> map : studentList) {
            if (map.containsKey("outid")) {
                outArray.add(map.get("outid").toString());
            }
        }
        String[] outidArr = outArray.toArray(new String[outArray.size()]);
        NetParams timeParam = new NetParams();
        timeParam.setOutidArr(outidArr);
        timeParam.setBdate(para.getBdate());
        timeParam.setEdate(para.getEdate());
        List<ResultData> netTimeList = radacctTimeKylinMapper.selectNetTime(timeParam);


        List<Map<String, Object>> resultList = new ArrayList<>();
        // 上网访问内容, add in V3.0.0
        //循环查询上网内容排名：top2
        NetParams param = new NetParams();//因为查询的是个人数据，所以根据学号和时间就可以了，其他条件不用加到sql里面
        param.setStart(0);
        param.setLimit(2);
        param.setBdate(para.getBdate());
        param.setEdate(para.getEdate());
        for (LinkedHashMap<String, Object> stu : studentList) {
            //BeanUtil.objectsToMapsOffNull(resultList);
            String outid = stu.get("outid").toString();
            param.setOutid(outid);
            //Map<String, Object> map =BeanUtil.beanToMapOffNull(stu);
            List<ResultData> list = radacctTimeKylinMapper.selectVisitContextChartAndTable(param);
            if (CollectionUtils.isEmpty(list)) {
                stu.put("favor", "");
            } else {
                StringBuffer strbuf = new StringBuffer("");
                list.stream().forEach(b -> strbuf.append(b.getName()).append(","));
                stu.put("favor", strbuf.substring(0, strbuf.length() - 1));
            }
            //上网时长
            for (ResultData time : netTimeList) {
                String outidTime = time.getOutid();
                if (outidTime.equals(outid)) {
                    BigDecimal totalDuration = time.getTotalDuration().divide(new BigDecimal(3600), 2, BigDecimal.ROUND_HALF_UP);
                    stu.put("totalDuration", totalDuration);
                }
            }
            //
            resultList.add(stu);
        }
        return resultList;
    }

    /**
     * 上网--个人--日均上网总时长列表
     *
     * @param para
     * @return
     */
    @Override
    public List<Map<String, Object>> everyDaySufferInternetTime(NetParams para) {
        List<Map<String, Object>> list = radacctTimeKylinMapper.everyDaySufferInternetTime(para);
        list.forEach(r -> {
            //获取每天上网总时长，默认单位是秒，转换成小时，并重新存储到map集合中
            BigDecimal totalDuration = new BigDecimal((Long) r.get("total_duration")).divide(new BigDecimal(3600), BigDecimal.ROUND_HALF_UP, 2);
            r.put("total_duration", totalDuration);
        });
        return list;
    }

    /**
     * 个人--日均上网时长总记录数
     *
     * @param para
     * @return
     */
    @Override
    public int everyDaySufferInternetTimeCount(NetParams para) {
        return radacctTimeKylinMapper.everyDaySufferInternetTimeCount(para);
    }


    @Override
    public List<Map<String, Object>> getNetEarlywarnList(NetParams para) {
        List<Map<String, Object>> maps = radacctTimeKylinMapper.getNetEarlywarnList(para);
        if (maps == null || maps.size() == 0) {
            return null;
        }
        Params params = new Params();
        //设置outidArr参数
        List<String> outid = maps.stream().map(r -> r.get("outid").toString()).collect(Collectors.toList());
        String[] strings = new String[outid.size()];
        params.setOutidArr(outid.toArray(strings));
        List<Map<String, Object>> students = studentServer.getStudentDetails(params);
        Map<String, Map> studentMap = new HashMap<>();
        //将students 转换成以outid为key的map
        if (students != null && students.size() > 0) {
            students.forEach(r -> studentMap.put(r.get("outid").toString(), r));
        }
        for (Map map : maps) {
            if (studentMap.containsKey(map.get("outid"))) {
                map.putAll(studentMap.get(map.get("outid")));
            }
        }
        return maps;
    }

    @Override
    public int getNetEarlywarnCount(NetParams para) {
        return radacctTimeKylinMapper.getNetEarlywarnCount(para);
    }
}
