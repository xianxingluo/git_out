package com.ziyun.dormitory.service.impl;

import com.ziyun.common.model.*;
import com.ziyun.dormitory.mapper.EcardAccessInoutKylinMapper;
import com.ziyun.dormitory.mapper.EcardAccessInoutMapper;
import com.ziyun.dormitory.service.IEcardAccessInoutService;
import com.ziyun.utils.cache.CacheConstant;
import com.ziyun.utils.common.BeanUtil;
import com.ziyun.utils.common.CalendarUtils;
import com.ziyun.utils.date.DateUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

@Service
public class EcardAccessInoutServiceImpl implements IEcardAccessInoutService {

	private static Logger logger = Logger
			.getLogger(EcardAccessInoutServiceImpl.class);


	private static BigDecimal zero = new BigDecimal(0.00);

	private static BigDecimal zero_0 = new BigDecimal(0);

    @Autowired
    public EcardAccessInoutMapper accessInoutMapper;

	//麒麟的dao
    @Autowired
    public EcardAccessInoutKylinMapper accessInoutKylinMapper;

	/**
	 * 根据给定的日期；获取查询结果集当中对应的数据项；如果不存在，则新建一个该日期的，数值为0的数据项
     *
	 * @param list
	 * @param day
	 * @return
	 */
	private ResultData getDayData(List<ResultData> list, String day) {
		ResultData dayData = null;// 指定日期的data;如果结果集中没有，则构造一个
		for (ResultData data : list) {
			if (data.getDatetimeStr().equals(day)) {
				return data;
			}
		}
		dayData = new ResultData();
		dayData.setDatetimeStr(day);
		dayData.setNum(0L);// 如果没有数据，默认人数
		dayData.setSum(zero);
		return dayData;
	}

	@Override
    @Cacheable(value = CacheConstant.DORM_CACHE, key = "'dorm:dormitory:' +#root.methodName +'.' + #para.hashCode()")
	public List<Map<String, Object>> mouthRestList(Params para)
			throws Exception {
		// 此报表全部返回当月数据：所以在这里把开始结束时间重新设置
		String bdate;
		String edate;
		if (CalendarUtils.isFirstDayOfMonth()) {
			// 如果是当月一号，则显示上个月的数据
			bdate = CalendarUtils.toYyyy2MM2dd(CalendarUtils
					.getFirstDayOfMonth(CalendarUtils.getDateBy(-1)));
			edate = CalendarUtils.toYyyy2MM2dd(new Date());// 考勤当天的数据还算不出来，这里不用传第二天的日期
		} else {
			bdate = CalendarUtils.toYyyy2MM2dd(CalendarUtils
					.getFirstDayOfMonth(new Date()));
			edate = CalendarUtils.toYyyy2MM2dd(new Date());// 考勤当天的数据还算不出来，这里不用传第二天的日期
		}
		para.setBdate(bdate);
		para.setEdate(edate);
		// 获取查询时间段内的所有日期：防止查询结果当中，有很多天数据是空的，无法显示
		List<String> allDays = CalendarUtils.getAllDatesBetween(bdate, edate);
		//
        List<ResultData> list = accessInoutMapper.mouthRestList(para);
		if (null == list || list.size() == 0) {
			return null;
		}
		List<ResultData> resultList = new ArrayList<ResultData>();
		for (String day : allDays) {
			ResultData dayData = getDayData(list, day);
			resultList.add(dayData);
		}
		// 在宿舍上网时间
        List<ResultData> netList = accessInoutMapper.mouthRestNetList(para);
		List<ResultData> resultNetList = new ArrayList<ResultData>();
		for (String day : allDays) {
			ResultData dayData = getDayData(netList, day);
			resultNetList.add(dayData);
		}
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		//
		BigDecimal allDay = new BigDecimal(24);
		// 查询出来的时间是秒：这里要转化成小时
		BigDecimal cos = new BigDecimal(3600);
		for (ResultData data : resultList) {
			for (ResultData netData : resultNetList) {
				if (data.getDatetimeStr().equals(netData.getDatetimeStr())) {
					BigDecimal count = new BigDecimal(data.getNum());// 每天的上网人数：：一般来说在宿舍的人数比上网人数多。这里取平均按照最大人数算
					if (count.compareTo(new BigDecimal(0)) == 0) {
						count = new BigDecimal(1);
					}
					count = count.multiply(cos);// 把秒转换成小时
					BigDecimal avgBig = data.getSum().divide(count, 2,
							BigDecimal.ROUND_HALF_UP);// 每天的上网总时长，除以上网人数
					// data.setSum(avgBig); // 平均上网时间
					BigDecimal avgBig2 = netData.getSum().divide(count, 2,
							BigDecimal.ROUND_HALF_UP);// 每天的上网总时长，除以上网人数
					// netData.setSum(avgBig2); // 平均上网时间
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("dateStr", data.getDatetimeStr());
					map.put("restTime", avgBig.subtract(avgBig2));// 休息时间=在宿舍时间-在宿舍上网时间
					map.put("netTime", avgBig2);
					map.put("otherTime", allDay.subtract(avgBig));
					mapList.add(map);
				}
			}
		}

		return mapList;
	}

	@Override
	public List<Map<String, Object>> mouthRestNetList(Params para)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
    @Cacheable(value = CacheConstant.DORM_CACHE, key = "'dorm:dormitory:' +#root.methodName +'.' + #para.hashCode()")
	public List inWeekHourList(Params para) throws Exception {

        List<ResultData> weekHourList = accessInoutMapper.inWeekHourListNew(para);
		if (null == weekHourList || weekHourList.size() == 0) {
			return null;
		}
		// 获取查询时间段内的所有日期：防止查询结果当中，有很多天数据是空的，无法显示
		setStartEndTime(para);
		List<Week> weekNameList = CalendarUtils.getWeekCountBetween(
				para.getBdate(), para.getEdate());

		List<Object> resultMap = new ArrayList<Object>();
		List<HourData> resultList = null;
		// 时段内汇总平均 - 周几排序、各个小时：汇总平均列表
		for (int i = 0; i < weekNameList.size(); i++) {// 根据周几循环，能将数据按照周几排序
			resultList = new ArrayList<HourData>();
			Week week = weekNameList.get(i);
			for (int j = 0; j < 24; j++) {// 循环24个小时：：数据中也是从0开始的
				String hour = j + "";
				HourData hourData = getWeekHourData(weekHourList, week, hour);// 循环获取，每个小时的数据，
				resultList.add(hourData);
			}
			// Map<String, Object> map = new HashMap<String, Object>();
			// map.put(week.getCn_name(), resultList);// 按照周几：返回结果
			resultMap.add(resultList);
		}
		return resultMap;
	}

	@Override
    @Cacheable(value = CacheConstant.DORM_CACHE, key = "'dorm:dormitory:' +#root.methodName +'.' + #para.hashCode()")
	public List onWeekHourList(Params para) throws Exception {

        List<ResultData> weekHourList = accessInoutMapper.onWeekHourList(para);
		if (null == weekHourList || weekHourList.size() == 0) {
			return null;
		}
		// 获取查询时间段内的所有日期：防止查询结果当中，有很多天数据是空的，无法显示
		setStartEndTimeOn(para);
		List<Week> weekNameList = CalendarUtils.getWeekCountBetween(
				para.getBdate(), para.getEdate());

		List<Object> resultMap = new ArrayList<Object>();
		List<HourData> resultList = null;
		// 时段内汇总平均 - 周几排序、各个小时：汇总平均列表
		for (int i = 0; i < weekNameList.size(); i++) {// 根据周几循环，能将数据按照周几排序
			resultList = new ArrayList<HourData>();
			Week week = weekNameList.get(i);
			for (int j = 0; j < 24; j++) {// 循环24个小时：：数据中也是从0开始的
				String hour = j + "";
				HourData hourData = getWeekHourData(weekHourList, week, hour);// 循环获取，每个小时的数据，
				resultList.add(hourData);
			}
			// Map<String, Object> map = new HashMap<String, Object>();
			// map.put(week.getCn_name(), resultList);// 按照周几：返回结果
			resultMap.add(resultList);
		}
		return resultMap;
	}

	/**
	 * 离开宿舍的时间
	 *
	 * 设置开始结束时间：用来记录天数等的时间函数需要用到。
	 *
	 * 在前端没有传入时间参数的时候，设置时间参数
	 *
	 * @param para
	 */
	private void setStartEndTimeOut(Params para) {
		if (StringUtils.isEmpty(para.getBdate())
				|| StringUtils.isEmpty(para.getEdate())) {
			String bdate;
			String edate;
            ResultData times = accessInoutMapper.outWeekHourListTimesNew(para);// 获取最早、最晚：记录的时间
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

	/**
	 * 进入宿舍的时间
	 *
	 * 设置开始结束时间：用来记录天数等的时间函数需要用到。
	 *
	 * 在前端没有传入时间参数的时候，设置时间参数
	 *
	 * @param para
	 */
	private void setStartEndTime(Params para) {
		if (StringUtils.isEmpty(para.getBdate())
				|| StringUtils.isEmpty(para.getEdate())) {
			String bdate;
			String edate;
            ResultData times = accessInoutMapper.inWeekHourListTimesNew(para);// 获取最早、最晚：记录的时间
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

	/**
	 * 在宿舍的人数统计：起始时间
	 *
	 * 设置开始结束时间：用来记录天数等的时间函数需要用到。
	 *
	 * 在前端没有传入时间参数的时候，设置时间参数
	 *
	 * @param para
	 */
	private void setStartEndTimeOn(Params para) {
		if (StringUtils.isEmpty(para.getBdate())
				|| StringUtils.isEmpty(para.getEdate())) {
			String bdate;
			String edate;
            ResultData times = accessInoutMapper.onWeekHourListTimes(para);// 获取最早、最晚：记录的时间
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

	/**
	 * 根据给定的星期几序号；
	 *
	 * 获取查询结果集当中对应的数据项；如果不存在，则新建一个该日期的，数值为0的数据项
	 *
	 * @param list
	 * @param week
	 *            星期几：序号
	 * @param hour
	 *            %k 小时(0……23)
	 * @return
	 */
	private HourData getWeekHourData(List<ResultData> list, Week week,
			String hour) {
		HourData hourData = null;// 指定日期的data;如果结果集中没有，则构造一个
		for (ResultData data : list) {
			if (data.getWeekindex() == week.getEn_index()
					&& hour.equals(data.getHour())) {
				data.setWeek(week.getCn_name());
				if (week.getCount() > 1) {// 周几出现的次数大于1，则需要将该数值除以出现的次数：算平均
					data.setSum(data.getSum().divide(
							new BigDecimal(week.getCount()), 0,
							BigDecimal.ROUND_UP));// 人数全部向上进位保留整数
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

	@Override
    @Cacheable(value = CacheConstant.DORM_CACHE, key = "'dorm:dormitory:' +#root.methodName +'.' + #para.hashCode()")
	public List outWeekHourList(Params para) throws Exception {

        List<ResultData> weekHourList = accessInoutMapper.outWeekHourListNew(para);
		if (null == weekHourList || weekHourList.size() == 0) {
			return null;
		}
		// 获取查询时间段内的所有日期：防止查询结果当中，有很多天数据是空的，无法显示
		setStartEndTimeOut(para);
		List<Week> weekNameList = CalendarUtils.getWeekCountBetween(
				para.getBdate(), para.getEdate());

		List<Object> resultMap = new ArrayList<Object>();
		List<HourData> resultList = null;
		// 时段内汇总平均 - 周几排序、各个小时：汇总平均列表
		for (int i = 0; i < weekNameList.size(); i++) {// 根据周几循环，能将数据按照周几排序
			resultList = new ArrayList<HourData>();
			Week week = weekNameList.get(i);
			for (int j = 0; j < 24; j++) {// 循环24个小时：：数据中也是从0开始的
				String hour = j + "";
				HourData hourData = getWeekHourData(weekHourList, week, hour);// 循环获取，每个小时的数据，
				resultList.add(hourData);
			}
			// Map<String, Object> map = new HashMap<String, Object>();
			// map.put(week.getCn_name(), resultList);// 按照周几：返回结果
			resultMap.add(resultList);
		}
		return resultMap;
	}

	@Override
    @Cacheable(value = CacheConstant.DORM_CACHE, key = "'dorm:dormitory:' +#root.methodName +'.' + #para.hashCode()")
	public Map<String, Object> dayHour(Params para) throws Exception {

		Params parain = new Params();
		BeanUtils.copyProperties(para, parain);

		Params paraout = new Params();
		BeanUtils.copyProperties(para, paraout);

		Params paraOn = new Params();
		BeanUtils.copyProperties(para, paraOn);

		Map<String, Object> resut=new HashMap<String, Object>();
		// ==========进入宿舍的人数----
        List<ResultData> listIn = accessInoutMapper.dayHourIn(parain);
		// if (null == listIn || listIn.size() == 0) {
		// return null;
		// }
		// 获取查询时间段内的所有日期：防止查询结果当中，有很多天数据是空的，无法显示
		setStartEndTime(parain);
		List<Object> resultMapIn=getDayHourList(parain, listIn);
		// ==========进入宿舍的人数----

		// ==========离开宿舍的人数----
        List<ResultData> listOut = accessInoutMapper.dayHourOut(paraout);
		// if (null == listOut || listOut.size() == 0) {
		// return null;
		// }
		// 获取查询时间段内的所有日期：防止查询结果当中，有很多天数据是空的，无法显示
		setStartEndTimeOut(paraout);
		List<Object> resultMapOut=getDayHourList(paraout, listOut);
		// ==========离开宿舍的人数----

		// ==========在宿舍的人数----
        List<ResultData> listOn = accessInoutMapper.dayHourOn(paraOn);
		// if (null == listOn || listOn.size() == 0) {
		// return null;
		// }
		// 获取查询时间段内的所有日期：防止查询结果当中，有很多天数据是空的，无法显示
		setStartEndTimeOn(paraOn);
		List<Object> resultMapOn=getDayHourList(paraOn, listOn);
		// ==========在宿舍的人数----
		resut.put("into", resultMapIn);
		resut.put("out", resultMapOut);
		resut.put("on", resultMapOn);
		return resut;
	}

	@Override
	public Map getLateToBedroom(Params para) throws Exception {
		//判断是否有数据。没有数据的话就不执行下一步查询
        int num = accessInoutMapper.getLateToBedroomNum(para);
		if (num == 0)
			return null;
        Map lateMap = accessInoutMapper.getLateToBedroom(para);
		if (lateMap == null||lateMap.size() == 0)
			return null;
		lateMap.put("time",para.getBdate());
		return lateMap;
	}



	@Override
	public List<Map<String, Object>> getStudentInOutDormTimes(Params params) throws Exception {


        return accessInoutMapper.getStudentInOutDormTimes(params);
	}

	@Override
	public List<String> getStudentDormYears(Params params) {

        return accessInoutMapper.getStudentDormYears(params);
	}

	@Override
	public List<Map<String, Object>> listDormInOutFlows(Params params) {

        List<Map<String, Object>> result = accessInoutKylinMapper.listDormInOutFlows(params);
		convertToLocalDate(result);

		return result;
	}

	/**
	 * 获取按照日期、小时格式：排序汇总的数据
	 * @param paraout
	 * @param listOut
	 * @return
	 * @throws ParseException
	 */
	private List<Object> getDayHourList(Params paraout, List<ResultData> listOut)
			throws ParseException {
		List<Object> resultMapOut = new ArrayList<Object>();
		List<String> allDaysOut = CalendarUtils.getAllDatesBetween(
				paraout.getBdate(), paraout.getEdate());
		List<ResultData> resultListOut = null;
		for (String dayOut : allDaysOut) {
			resultListOut = new ArrayList<ResultData>();
			for (int j = 0; j < 24; j++) {// 循环24个小时：：数据中也是从0开始的
				String hour = j + "";
				ResultData dayDataOn = getDayHourData(listOut, dayOut, hour);// 循环获取，每个小时的数据，
				resultListOut.add(dayDataOn);
			}
			resultMapOut.add(BeanUtil.objectsToMapsOffNull(resultListOut));
		}
		return resultMapOut;
	}

	/**
	 * 根据给定的日期、小时；获取查询结果集当中对应的数据项；如果不存在，则新建一个该日期的，数值为0的数据项
     *
	 * @param list
	 * @param day
	 * @return
	 */
	private ResultData getDayHourData(List<ResultData> list, String day,
			String hour) {
		ResultData dayData = null;// 指定日期的data;如果结果集中没有，则构造一个
		for (ResultData data : list) {
			if (data.getDatetimeStr().equals(day)
					&& hour.equals(data.getHour())) {
				data.setHour(CalendarUtils.hourToAPm(hour));
				return data;
			}
		}
		dayData = new ResultData();
		dayData.setDatetimeStr(day);
		dayData.setHour(CalendarUtils.hourToAPm(hour));
		dayData.setSum(zero_0);
		return dayData;
	}

    @Cacheable(value = CacheConstant.DORM_CACHE, key = "'dorm:dormitory:' +#root.methodName +'.' + #params.hashCode()")
	public List<Map<String,Object>> getStudnetLateRatio(ParamsStatus params){
        return accessInoutKylinMapper.getLateStudnet(params);
	}

	@Override
	public List<Map> getLateRatio(Params params) {
        List<Map<String, Object>> list = accessInoutKylinMapper.lateRatioKylin(params);
		if (null == list||list.size() == 0)
			return null;
		Map<String,Map<String, Object>> toolMap = new LinkedHashMap<>();
		List<Map> resultList = new ArrayList<>();
 		list.forEach(r ->{
			String number = String.valueOf(r.get("NUM"));
			if(Integer.valueOf(number) > 7)
				number = "7次以上";
			//如果不存在直接创建一个并赋值男女的初始值0
			if(toolMap.get(number) == null){
				Map map = new HashMap();
				map.put("name", number);
				map.put("value",0);
				toolMap.put(number, map);
			}
			Map thisNumberMap = toolMap.get(number);
			//将times和number去除
			Object times = r.get("TIMES");
			Object number1 = r.get("NUMBER");
			//对7次以下的数据做处理
			if (Integer.valueOf(String.valueOf(r.get("NUM"))) <= 7){
				thisNumberMap.put("value",r.get("PNUM"));
				//求平均迟到时段,该迟到次数总的时间/迟到的人数/迟到的次数 number是迟到人数和迟到次数的积.因为迟到是以23：30:00界定的
				int avgMin = (Integer.valueOf(String.valueOf(times))) / (Integer.valueOf(String.valueOf(number1)));
				String startTimeSlot = CalendarUtils.addMinute("23:30:00",avgMin);
				String endTimeSlot = CalendarUtils.addMinute("23:30:00",avgMin+1);
				thisNumberMap.put("times",startTimeSlot+"-"+endTimeSlot);
			}else{
				//>7次的数据累加
				int sumTime = addObjectParam(thisNumberMap.get("time")  == null?0:thisNumberMap.get("time"),times);
				int sumNumber = addObjectParam(thisNumberMap.get("number")== null?0:thisNumberMap.get("number"),number1);
				//存入map用于下一次累加
				thisNumberMap.put("value",addObjectParam(thisNumberMap.get("value"),r.get("PNUM")));
				thisNumberMap.put("time",sumTime);
				thisNumberMap.put("number",sumNumber);
				//求平均迟到时段,该迟到次数总的时间/迟到的人数/迟到的次数 number是迟到人数和迟到次数的积.因为迟到是以23：30:00界定的
				int avgMin = sumTime / sumNumber;
				String startTimeSlot = CalendarUtils.addMinute("23:30:00",avgMin);
				String endTimeSlot = CalendarUtils.addMinute("23:30:00",avgMin+1);
				thisNumberMap.put("times",startTimeSlot+"-"+endTimeSlot);
			}

		});
		//对map数据做处理变成list<map>让前端好遍历
		Set<String> keySet = toolMap.keySet();
		keySet.forEach(k ->{
			Map<String, Object> lastMap = toolMap.get(k);
			if (StringUtils.equals("7次以上",k)){
				lastMap.put("name",">7");
			}
			resultList.add(lastMap);
		});
		return resultList;
	}

	@Override
	public List<Map<String, Object>> listStudentInOutDormWeekView(Params params) {

        return accessInoutMapper.listStudentInOutDormWeekView(params);
	}

	@Override
	public Map<String, Object> listDormInOutFlowsAll(Params params) {
		//获取进出宿舍的总人数
        List<Map<String, Object>> inOutTimes = accessInoutKylinMapper.listDormInOutFlowsAll(params);
		if (null == inOutTimes || inOutTimes.size() == 0) {
			return null;
		}

		Map<String, Object> result = new HashMap<>();
		for (Map<String, Object> map : inOutTimes) {
			if ((int)map.get("IOFLAG") == 0){
				//ioflag=0 代表进入宿舍
				result.put("inNum",map.get("NUM"));
			} else {
				//ioflag=1 代表离开宿舍
				result.put("outNum",map.get("NUM"));
			}
		}

		return result;
	}

	@Override
	public Integer[] listStudentInOutDormHours(Params params) {

        List<Map<String, Object>> list = accessInoutMapper.listStudentInOutDormHours(params);

		if (null == list || list.size() == 0) {
			return null;
		}
		//将每个小时的数据汇总到一个map里
		Map<String, Integer> resultMap = new HashMap<String, Integer>();
		for (Map<String, Object> map : list) {
			String key = (String)map.get("hours");
			Long value = (Long) map.get("num");
			resultMap.put(key,value.intValue());
		}

		//页面要求的每小时时间格式
		String[] hours = new String[]{"06","07","08","09","10","11","12","13","14","15","16","17",
				"18","19","20","21","22","23","23:30","00","01","02","03","04","05"};

		List<String> hoursList = Arrays.asList(hours);

		Integer[] result = new Integer[hours.length];

		for (int i=0 ; i < hours.length; i++) {
			Integer times = resultMap.get(hours[i]);
			if (null != times) {
				result[i] = times;
			} else {
				result[i] = 0;
			}
		}

		//对23:00-24:00这个小时，分成前半小时和后半小时来处理
		//判断23点这个小时内是否有数据
		if(null != resultMap.get("23") && resultMap.get("23") != 0){
            Map<String, Long> halfHourTimesMap = accessInoutMapper.getInOutDormTimesHalfHour(params);
			result[hoursList.indexOf("23")] = halfHourTimesMap.get("firstHalfHour").intValue();
			result[hoursList.indexOf("23:30")] = halfHourTimesMap.get("secondHalfHour").intValue();
		}

		return result;
	}

	@Override
	public Map<String, Object> listStuDormFeature(Params params) {
		Map<String, Object>  result = new HashMap<String, Object>();
        Integer laterTimes = accessInoutMapper.lateTimesBackToDorm(params);

		result.put("laterTimes",laterTimes);
        List<Map<String, Object>> inOutHours = accessInoutMapper.listStudentInOutDormHours(params);
		if (null != inOutHours && inOutHours.size() > 0) {
			//出入次数最高时段的开始时间
			String hoursStart = (String) inOutHours.get(0).get("hours");
			//结束时间是开始时间 + 1
			Integer hoursEndI = Integer.parseInt(hoursStart) + 1;

			String hoursEnd = "";
			//如果小于10,前面补0
			if (hoursEndI < 10) {
				hoursEnd = '0' + String.valueOf(hoursEndI);
			} else {
				hoursEnd = String.valueOf(hoursEndI);
			}

			result.put("hours",hoursStart+"~"+hoursEnd);
		}
		return  result;
	}

	@Override
	public List<Map<String, Object>> getMybeLate(Params params) {
        List<Map<String, Object>> list = accessInoutKylinMapper.mybeLate(params);
		if (null == list || list.size() == 0)
			return null;
		return list;
	}

	@Override
	public int getMybeLatesize(Params params) {
        return accessInoutKylinMapper.mybeLatesize(params);
	}

	@Override
	public Map<String, Object> dormSummarize(Params params) {
        String dormStr = accessInoutKylinMapper.getDormPeak(params);
        int lateNumber = accessInoutKylinMapper.getDormTotalLate(params);
        List<Map<String, Object>> inOutCount = accessInoutKylinMapper.getInOutCount(params);
		String peak;
		int peakNumber;
		if (StringUtils.isBlank(dormStr))
			return null;
		if (Integer.valueOf(dormStr.substring(0,1)) >0){
			peakNumber = Integer.valueOf(dormStr);
		}else{
			peakNumber = Integer.valueOf(dormStr.substring(1,2));
		}
		peak = peakNumber+"点-";
		peakNumber = peakNumber +1;
		peak = peak + peakNumber+"点";
		Map resultMap = new HashedMap();
		resultMap.put("lateNumber",lateNumber);
		resultMap.put("peak",peak);

		// 进出次数比
		if (inOutCount != null && inOutCount.size() > 0) {
			int inTimes = 0;
			int outTimes = 0;
			for (Map<String, Object> map : inOutCount) {
				if ("1".equals(map.get("IOFLAG").toString())) {
					outTimes = Integer.parseInt(map.get("NUM").toString());
				} else {
					inTimes = Integer.parseInt(map.get("NUM").toString());
				}
			}
			if(inTimes != 0 && outTimes != 0){
				if(inTimes >= outTimes)
					resultMap.put("inOutTimes",new BigDecimal(inTimes).divide(new BigDecimal(outTimes), 2, BigDecimal.ROUND_HALF_UP) + ":1.00");
				else
					resultMap.put("inOutTimes", "1.00:" + new BigDecimal(outTimes).divide(new BigDecimal(inTimes), 2, BigDecimal.ROUND_HALF_UP));
			}else{
				resultMap.put("inOutTimes", inTimes + ":" + outTimes);
			}
		}

		return resultMap;
	}

	@Override
	public List<Map> getClockRecord(ParamsStatus params) {
		String hour = params.getHour();
		if ("23".equals(hour)) {
			params.setSql(" AND MINUTE(OpDT) <= 30 ");
		} else if ("23:30".equals(hour)) {
			params.setSql(" AND MINUTE(OpDT) > 30 ");
			params.setHour("23");
		}
		List<Map> clockRecordList = accessInoutMapper.getClockRecord(params);
		if (null == clockRecordList || clockRecordList.size() == 0)
			return  null;
		else
			return clockRecordList;
	}

	@Override
	public int getClockRecordLength(ParamsStatus params) {
		String hour = params.getHour();
		if ("23".equals(hour)) {
			params.setSql(" AND MINUTE(OpDT) <= 30 ");
		} else if ("23:30".equals(hour)) {
			params.setSql(" AND MINUTE(OpDT) > 30 ");
			params.setHour("23");
		}
		return accessInoutMapper.getClockRecordLength(params);
	}

	@Override
	public List<Map> getRestRegular(ParamsStatus params) {
        List<Map> restList = accessInoutMapper.getRestRegular(params);
		if (null == restList || restList.size() == 0)
			return null;
		else
			return  restList;
	}

	@Override
	public List<Map> getLateDetail(Params params) {
		//判断是否有数据。没有数据的话就不执行下一步查询
        int num = accessInoutMapper.getLateToBedroomNum(params);
		if (num == 0)
			return null;
        List<Map> restList = accessInoutMapper.getLateDetail(params);
		return restList;
	}

	@Override
	public Map getLateDetailCount(Params params) {
		//判断是否有数据。没有数据的话就不执行下一步查询
        int num = accessInoutMapper.getLateToBedroomNum(params);
		if (num == 0)
			return null;
        Map sum = accessInoutMapper.getLateDetailCount(params);
		return sum;
	}

	private int addObjectParam(Object o1,Object o2){
		if (o1 == null|| o2 == null)
			return 0;
		int num1 = Integer.valueOf(String.valueOf(o1));
		int num2 = Integer.valueOf(String.valueOf(o2));
		return num1+num2;
	}


	/**
	 * 将kylin的时间转化为本地时间
	 * @param list
	 */
	private void convertToLocalDate(List<Map<String, Object>> list){
		if (null != list && list.size() > 0){
			for (Map<String, Object> map : list) {
				//需要转换的时间
				Date day_id = (Date) map.get("DAY_ID");
				if(null != day_id){
					map.put("DAY_ID", DateUtils.date2Str4GMT16("yyyy-MM-dd", day_id));
				}
			}
		}
	}

    /**
     * 宿舍出入特征--个人晚归情况
     *
     * @param para
     * @return
     */
    @Override
    public List<Map<String, Object>> personLater(ParamsStatus para) {
        return accessInoutMapper.personLater(para);

    }

    /**
     * 宿舍出入特征--个人晚归情况 (总长度)
     *
     * @param para
     * @return
     */
    @Override
    public Long personLaterSize(ParamsStatus para) {
        return accessInoutMapper.personLaterSize(para);
    }

    /**
     * 宿舍出入特征--个人可能未归情况
     *
     * @param para
     * @return
     */
    @Override
    public List<Map<String, Object>> personNoComeBack(ParamsStatus para) {
        List<Map<String, Object>> list = null;
        try {
            list = accessInoutKylinMapper.personNoComeBack(para);
            if (null != list && list.size() > 0) {
                for (Map<String, Object> map : list) {
                    //需要转换的时间
                    Date OpDT = (Date) map.get("OPDT");
                    if (null != OpDT) {
                        map.put("OPDT", DateUtils.date2Str4GMT16("yyyy-MM-dd HH:mm:ss", OpDT));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 宿舍出入特征--个人可能未归情况 (总长度)
     *
     * @param para
     * @return
     */
    @Override
    public Long personNoComeBackSize(ParamsStatus para) {
        return accessInoutKylinMapper.personNoComeBackSize(para);
    }

	/**
	 * 根据日期查询可能未归学生
	 *
	 * @param para
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryNoComeBackByDay(ParamsStatus para) {
        List<Map<String, Object>> list = accessInoutKylinMapper.queryNoComeBackByDay(para);
        if (list == null || list.size() == 0)
            return null;
        list.forEach(map -> {
            Date OpDT = (Date) map.get("OPDT");
            if (null != OpDT) {
                map.put("OPDT", DateUtils.date2Str4GMT16("yyyy-MM-dd HH:mm:ss", OpDT));
				/*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String d = sdf.format(OpDT);
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				Long startTimeStamp = LocalDateTime.parse(d, formatter).toEpochSecond(ZoneOffset.of("+0"));
				map.put("OPDT",sdf.format(new Date(startTimeStamp * 1000)));*/
            }
        });

        return list;
	}
	@Override
	public Long queryNoComeBackByDaySize(ParamsStatus para) {
		return accessInoutKylinMapper.queryNoComeBackByDaySize(para);
	}
	/**
	 * 根据日期导出可能未归学生到excel
	 *
	 * @param para
	 * @return
	 */
	@Override
	public List<Map<String, Object>> excelNoComeBackByDay(ParamsStatus para) {
		return accessInoutKylinMapper.excelNoComeBackByDay(para);
	}
}
