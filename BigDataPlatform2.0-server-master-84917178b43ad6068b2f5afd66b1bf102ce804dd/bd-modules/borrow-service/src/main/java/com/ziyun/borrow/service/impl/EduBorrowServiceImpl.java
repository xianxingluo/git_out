package com.ziyun.borrow.service.impl;

import com.ziyun.borrow.mapper.EduBorrowMapper;
import com.ziyun.borrow.model.EduStatus;
import com.ziyun.borrow.model.OwnOrgStudent;
import com.ziyun.borrow.service.EduStatusService;
import com.ziyun.borrow.service.IEduBorrowService;
import com.ziyun.borrow.service.OwnOrgSchoolService;
import com.ziyun.borrow.service.OwnOrgStudentService;
import com.ziyun.common.enums.WeekdayEnum;
import com.ziyun.common.model.*;
import com.ziyun.utils.cache.CacheConstant;
import com.ziyun.utils.common.BeanUtil;
import com.ziyun.utils.common.CalendarUtils;
import com.ziyun.utils.common.ParamUtils;
import com.ziyun.utils.date.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.ziyun.borrow.vo.ResultData;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

@Service
public class EduBorrowServiceImpl implements IEduBorrowService {

	private static Logger logger = Logger.getLogger(EduBorrowServiceImpl.class);

	private static BigDecimal zero = new BigDecimal(0.00);

	private static BigDecimal zero_0 = new BigDecimal(0);

	@Autowired
	public EduBorrowMapper borrowMapper;

	@Resource
	public EduStatusService eduStatusService;

	@Autowired
	private OwnOrgSchoolService ownOrgSchoolService;

	@Autowired
	private OwnOrgStudentService ownOrgStudentService;

	@Cacheable(value = CacheConstant.BORROW_CACHE, key = "'borrow:borrow:' +#root.methodName +'.' + #para.hashCode()")
	public List<Map> bookTopList(Params para) throws Exception {
		List<Map> list = borrowMapper.bookTopList(para);
		int index = 0;
		for (Map map : list) {
			String outid = String.valueOf(map.get("outid"));// 学号
			EduStatus eduStatus = eduStatusService.selectByOutid(outid);
			if (null != eduStatus
					&& StringUtils.isNotEmpty(eduStatus.getName())) {
				map.put("name", eduStatus.getName());// 学生姓名
			}
			index = index + 1;
			map.put("index", index);// 排序，数据在前台显示第几个
		}
		return list;
	}

	@Cacheable(value = CacheConstant.BORROW_CACHE, key = "'borrow:borrow:' +#root.methodName +'.' + #para.hashCode()")
	public List<Map> peopleTopList(Params para) throws Exception {
		List<Map> list = borrowMapper.peopleTopList(para);
		int index = 0;
		setStartEndTime(para);
		int monthNum = CalendarUtils.getMonthSpace(para.getBdate(),
				para.getEdate());

		for (Map map : list) {
			String sum = String.valueOf(map.get("sum"));
			BigDecimal avgBig;
			if (0 != monthNum) {// 选择的时间不在同一个月，才需要除以月
				avgBig = new BigDecimal(sum).divide(new BigDecimal(monthNum),
						2, BigDecimal.ROUND_HALF_UP);// 原始数据单位是分，这里转换成元
			} else {
				avgBig = new BigDecimal(sum);// 原始数据单位是分，这里转换成元
			}
			map.put("avg", avgBig.toPlainString());// 月平均
			//
			String outid = String.valueOf(map.get("outid"));// 学号
			EduStatus eduStatus = eduStatusService.selectByOutid(outid);
			if (null != eduStatus
					&& StringUtils.isNotEmpty(eduStatus.getName())) {
				map.put("name", eduStatus.getName());// 学生姓名
			}
			index = index + 1;
			map.put("index", index);// 排序，数据在前台显示第几个
		}
		return list;
	}

	@Cacheable(value = CacheConstant.BORROW_CACHE, key = "'borrow:borrow:' +#root.methodName +'.' + #para.hashCode()")
	public List<Map> avgPeopleTopList(Params para) throws Exception {

		List<Map> list = borrowMapper.peopleTopList(para);
		int index = 0;
		setStartEndTime(para);
		int monthNum = CalendarUtils.getMonthSpace(para.getBdate(),
				para.getEdate());

		for (Map map : list) {
			String sum = String.valueOf(map.get("sum"));
			BigDecimal avgBig;
			if (0 != monthNum) {// 选择的时间不在同一个月，才需要除以月
				avgBig = new BigDecimal(sum).divide(new BigDecimal(monthNum),
						2, BigDecimal.ROUND_HALF_UP);// 原始数据单位是分，这里转换成元
			} else {
				avgBig = new BigDecimal(sum);// 原始数据单位是分，这里转换成元
			}
			map.put("sum", avgBig.toPlainString());// 月平均
			//
			String outid = String.valueOf(map.get("outid"));// 学号
			EduStatus eduStatus = eduStatusService.selectByOutid(outid);
			if (null != eduStatus
					&& StringUtils.isNotEmpty(eduStatus.getName())) {
				map.put("name", eduStatus.getName());// 学生姓名
			}
			index = index + 1;
			map.put("index", index);// 排序，数据在前台显示第几个
		}
		return list;
	}

	@Override
	@Cacheable(value = CacheConstant.BORROW_CACHE, key = "'borrow:borrow:' +#root.methodName +'.' + #para.hashCode()")
	public List<Map<String, Object>> preferenceList(Params para)
			throws Exception {
		List<ResultData> list = borrowMapper.preferenceList(para);
		if (null == list||list.size() == 0)
			return null;
		return BeanUtil.objectsToMapsOffNull(list);
	}

	@Cacheable(value = CacheConstant.BORROW_CACHE, key = "'borrow:borrow:' +#root.methodName +'.' + #para.hashCode()")
	@Override
    public List<Map<String, Object>> preferenceListTop(Params para) {
		List<Map<String, Object>> map = borrowMapper.preferenceListTop(para);
		int i=0;
		for (Map value : map) {
			value.put("index",i++ );
		}
		return map;
	}

	@Cacheable(value = CacheConstant.BORROW_CACHE, key = "'borrow:borrow:' +#root.methodName +'.' + #para.hashCode()")
	public List<Map<String, Object>> preferenceListTwo(Params para)
			throws Exception {
		List<ResultData> list = borrowMapper.preferenceListTwo(para);

		return BeanUtil.objectsToMapsOffNull(list);
	}

	@Cacheable(value = CacheConstant.BORROW_CACHE, key = "'borrow:borrow:' +#root.methodName +'.' + #para.hashCode()")
	@Override
	public List<Map> preferenceListTwoTop(Params para) {
		List<Map> map = borrowMapper.preferenceListTwoTop(para);
		int i=0;
		for (Map value : map) {
			value.put("index",i++ );
		}
		return map;
	}

	/**
	 * 根据给定的日期；获取查询结果集当中对应的数据项；如果不存在，则新建一个该日期的，数值为0的数据项
	 *
	 * @param list
	 * @param day
	 * @return
	 */
	private ResultData getDayData(List<ResultData> list, String day) {
		ResultData dayData;// 指定日期的data;如果结果集中没有，则构造一个
		for (ResultData data : list) {
			if (data.getDatetimeStr().equals(day)) {
				return data;
			}
		}
		dayData = new ResultData();
		dayData.setDatetimeStr(day);
		dayData.setSum(zero);
		return dayData;
	}

	@Cacheable(value = CacheConstant.BORROW_CACHE, key = "'borrow:borrow:' +#root.methodName +'.' + #para.hashCode()")
	public Map<String, Object> sexWeekList(Params para) throws Exception {
		List<ResultData> resultSexList = borrowMapper.sexWeekList(para);
		if (resultSexList == null || resultSexList.size()==0)
			return  null;
		// 获取查询时间段内的所有日期：防止查询结果当中，有很多天数据是空的，无法显示
		setStartEndTime(para);
		List<Week> weekNameList = CalendarUtils.getWeekCountBetween(
				para.getBdate(), para.getEdate());
		List<ResultData> resultManList = new ArrayList<>();
		List<ResultData> resultWomenList = new ArrayList<>();
		// 时段内汇总平均 - 周几排序、男女分成2个列表
		for (int i = 0; i < weekNameList.size(); i++) {// 根据周几循环，能将数据按照周几排序
			Week week = weekNameList.get(i);
			ResultData manData = getWeekData(resultSexList, week, "男");// 男
			ResultData womenData = getWeekData(resultSexList, week, "女");// 女
			resultManList.add(manData);
			resultWomenList.add(womenData);
		}
		Map<String, Object> resultMap = new HashMap<>();
		List<Map<String, Object>> manlist = BeanUtil
				.objectsToMapsOffNull(resultManList);
		List<Map<String, Object>> womenlist = BeanUtil
				.objectsToMapsOffNull(resultWomenList);
		resultMap.put("man", manlist);
		resultMap.put("women", womenlist);
		return resultMap;
	}

	@Cacheable(value = CacheConstant.BORROW_CACHE, key = "'borrow:borrow:' +#root.methodName +'.' + #para.hashCode()")
	public List<Map<String, Object>> preferenceTopList(Params para)
			throws Exception {
		List<ResultData> list = borrowMapper.preferenceTopList(para);
		List<Map<String, Object>> resultList = BeanUtil.objectsToMapsOffNull(list);
		return resultList;
	}

	/**
	 * 根据给定的星期几序号；获取查询结果集当中对应的数据项；如果不存在，则新建一个该日期的，数值为0的数据项
	 *
	 * @param list
	 * @param week
	 *            星期几：序号
	 * @param sex
	 *            性别
	 * @return
	 */
	private ResultData getWeekData(List<ResultData> list, Week week, String sex) {
		ResultData dayData;// 指定日期的data;如果结果集中没有，则构造一个
		for (ResultData data : list) {
			if (data.getWeekindex() == week.getEn_index()
					&& sex.equals(data.getSex())) {
				data.setWeek(week.getCn_name());
				// 性别字符转换
				if ("M".equals(sex)) {
					data.setSex("男");
				} else if ("F".equals(sex)) {
					data.setSex("女");
				}
				if (week.getCount() > 1) {// 周几出现的次数大于1，则需要将该数值除以出现的次数：算平均
					data.setSum(data.getSum().divide(
							new BigDecimal(week.getCount()), 0,
							BigDecimal.ROUND_UP)); // 月平均消费
				}
				return data;
			}
		}
		dayData = new ResultData();
		// 性别字符转换
		if ("M".equals(sex)) {
			dayData.setSex("男");
		} else if ("F".equals(sex)) {
			dayData.setSex("女");
		}
		dayData.setWeekindex(week.getEn_index());
		dayData.setSum(zero);
		return dayData;
	}

	@Cacheable(value = CacheConstant.BORROW_CACHE, key = "'borrow:borrow:' +#root.methodName +'.' + #para.hashCode()")
	public Map<Integer, Object> hourList(Params para) throws Exception {
		String outid=para.getOutid();
		// setStartEndTime(para);
		List<ResultData> hourList = borrowMapper.hourList(para);
		if (null == hourList || hourList.size() == 0) {
			return null;
		}
		setStartEndTime(para);
		int dayNum = CalendarUtils.getMonthSpace(para.getBdate(),
				para.getEdate());
		if(dayNum==0){//防止0作为被除数
			dayNum=1;
		}
		// 为了和修改前的返回格式一致，前端就不用再修改了
		Map<Integer, Object> hourmap = new HashMap<>();
		// 时段内汇总平均 - 周几排序、各个小时：汇总平均列表
		for (int j = 0; j < 24; j++) {// 循环24个小时：：数据中也是从0开始的
			String hour = j + "";
			HourData hourData = getHourData(hourList, hour);// 循环获取，每个小时的数据，
			if (null != outid && outid.length() > 0) {
				hourmap.put(j, hourData.getSum());
			}else{//个人的，不用平均
				BigDecimal avg = hourData.getSum().divide(new BigDecimal(dayNum),
						2, BigDecimal.ROUND_HALF_UP);//人数、人次都进位
				hourmap.put(j, avg);
			}

		}
		// 把0点变成24点：供给前端显示
		hourmap.put(24, hourmap.get(0));
		hourmap.remove(0);

		return hourmap;
	}

	/**
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
			ResultData times;
			if (null != para.getTermNum() && null != para.getEnrollmentYear()){
				Map<String, String> dateMap = borrowMapper.getDateByYearTerm(para);
				para.setEdate(dateMap.get("endtime"));
				para.setBdate(dateMap.get("starttime"));
				times = borrowMapper.weekHourListTimes(para);
			}else
				times = borrowMapper.weekHourListTimes(para);// 获取最早、最晚：记录的时间

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
	 * @param hour
	 *            %k 小时(0……23)
	 * @return
	 */
	private HourData getHourData(List<ResultData> list, String hour) {
		HourData hourData;// 指定日期的data;如果结果集中没有，则构造一个
		for (ResultData data : list) {
			if (hour.equals(data.getHour())) {
				hourData = new HourData(hour, data.getSum());
				return hourData;
			}
		}
		hourData = new HourData(hour, zero_0);
		return hourData;
	}

	//@Cacheable(value = CacheConstant.BORROW_CACHE, key = "'borrow:borrow:' +#root.methodName +'.' + #para.hashCode()")
	public List weekHourList(Params para) throws Exception {
		//个人查询：保留小数：方便前台显示出数据的大小
		int round=0;//保留几位小数：默认汇总平均（群体）是0位。个人是2位
		if(StringUtils.isNotEmpty(para.getOutid())){
			round=2;
		}
		List<ResultData> weekHourList = borrowMapper.weekHourList(para);
		if (null == weekHourList || weekHourList.size() == 0) {
			return null;
		}
		// 获取查询时间段内的所有日期：防止查询结果当中，有很多天数据是空的，无法显示
		setStartEndTime(para);
		List<Week> weekNameList = CalendarUtils.getWeekCountBetween(
				para.getBdate(), para.getEdate());

		List<Object> resultMap = new ArrayList<>();
		List<HourData> resultList;
		// 时段内汇总平均 - 周几排序、各个小时：汇总平均列表
		for (int i = 0; i < weekNameList.size(); i++) {// 根据周几循环，能将数据按照周几排序
			resultList = new ArrayList<>();
			Week week = weekNameList.get(i);
			for (int j = 7; j < 22; j++) {// 循环24个小时：：数据中也是从0开始的
				String hour = j + "";
				HourData hourData = getWeekHourData(weekHourList, week, hour,round);// 循环获取，每个小时的数据，
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
	 *
	 * 获取查询结果集当中对应的数据项；如果不存在，则新建一个该日期的，数值为0的数据项
	 *
	 * @param list
	 * @param week
	 *            星期几：序号
	 * @param hour
	 *            %k 小时(0……23)
	 * @param hour
	 *            保留几位小数
	 * @return
	 */
	private HourData getWeekHourData(List<ResultData> list, Week week,
									 String hour, int round) {
		HourData hourData;// 指定日期的data;如果结果集中没有，则构造一个
		for (ResultData data : list) {
			if (data.getWeekindex() == week.getEn_index()
					&& hour.equals(data.getHour())) {
				BigDecimal totalsum = data.getSum();// 后加的：没有除以周几出现次数的总值
				data.setWeek(week.getCn_name());
				if (week.getCount() > 1) {// 周几出现的次数大于1，则需要将该数值除以出现的次数：算平均
					data.setSum(data.getSum().divide(
							new BigDecimal(week.getCount()), round,
							BigDecimal.ROUND_HALF_UP)); // 月平均:除以月以后；
				}
				hour = CalendarUtils.hourToAPm(hour);// 24小时：转换成上午下午12时制：如：6a、12a、6p
				hourData = new HourData(hour, data.getSum(), totalsum);
				return hourData;
			}
		}
		hour = CalendarUtils.hourToAPm(hour);// 24小时：转换成上午下午12时制：如：6a、12a、6p
		hourData = new HourData(hour, zero, zero);
		return hourData;
	}

	@Cacheable(value = CacheConstant.BORROW_CACHE, key = "'borrow:borrow:' +#root.methodName +'.' + #para.hashCode()")
	public List<Map<String, Object>> dayPeopleNum(Params para) throws Exception {
		List<ResultData> list = borrowMapper.dayPeopleNum(para);
		if (null == list || list.size() == 0) {
			return null;
		}
		// 获取查询时间段内的所有日期：防止查询结果当中，有很多天数据是空的，无法显示
		setStartEndTime(para);
//		List<String> allDays = CalendarUtils.getAllDatesBetween(
//				para.getBdate(), para.getEdate());
//		List<ResultData> resultList = new ArrayList<ResultData>();
//		for (String day : allDays) {
//			ResultData dayData = getDayData(list, day);
//			resultList.add(dayData);
//		}
//		for (ResultData result:list){
//			resultList.add(result);
//		}
		return BeanUtil.objectsToMapsOffNull(list);
	}

	@Cacheable(value = CacheConstant.BORROW_CACHE, key = "'borrow:borrow:' +#root.methodName +'.' + #para.hashCode()")
	public List<Map<String, Object>> dayBorrowNum(Params para) throws Exception {
		List<ResultData> list = borrowMapper.dayBorrowNum(para);
		if (null == list || list.size() == 0) {
			return null;
		}
		// 获取查询时间段内的所有日期：防止查询结果当中，有很多天数据是空的，无法显示
		setStartEndTime(para);
//		List<String> allDays = CalendarUtils.getAllDatesBetween(
//				para.getBdate(), para.getEdate());
//		List<ResultData> resultList = new ArrayList<ResultData>();
//		for (String day : allDays) {
//			ResultData dayData = getDayData(list, day);
//			resultList.add(dayData);
//		}
		return BeanUtil.objectsToMapsOffNull(list);
	}

	@Cacheable(value = CacheConstant.BORROW_CACHE, key = "'borrow:borrow:' +#root.methodName +'.' + #para.hashCode()")
	@Override
	public Map analysis(Params para) {
		Map map = borrowMapper.analysis(para);
		return map;
	}

	@Cacheable(value = CacheConstant.BORROW_CACHE, key = "'borrow:borrow:' +#root.methodName +'.' + #para.hashCode()")
	@Override
	public Map analysisShow(Params para) throws ParseException {
		Map map = borrowMapper.analysis(para);
		if(null==map){
			return null;
		}
		Long monthNums;
		//如果是个人的借阅可能会导致monthNums的为null，设置为1可以防止除0异常，
		if (null != para.getOutid()){
			if(para.getSemester() != null) {
				monthNums = DateUtils.timeDifference(para.getBdate(), para.getEdate()) / 30;
			}else{
				monthNums = borrowMapper.getTimesByParams(para) != null ? borrowMapper.getTimesByParams(para) : 1;
				monthNums = monthNums > 46?46:monthNums;
			}
		}else{
			if(para.getSemester() != null) {
				monthNums = borrowMapper.getTimeByParams(para) != null ? borrowMapper.getTimeByParams(para) : 1;
			}else{
				monthNums = borrowMapper.getTimesByParams(para) != null ? borrowMapper.getTimesByParams(para) : 1;
			}
		}

		BigDecimal timeBD = new BigDecimal(monthNums);
		String avgbookNum = String.valueOf(map.get("avgbookNum"));
		if("null".equals(avgbookNum)){
			avgbookNum ="0";
		}
		map.put("avgbookNum", new BigDecimal(avgbookNum).toPlainString());
		String avgtimeSum = String.valueOf(map.get("avgtimeSum"));
		if("null".equals(avgtimeSum)){
			avgtimeSum ="0";
		}
		map.put("avgtimeSum", new BigDecimal(avgtimeSum).toPlainString());
		// times借阅频次，每月的人均借阅数量
		BigDecimal sumBig = new BigDecimal(avgbookNum).divide(timeBD, 2,
				BigDecimal.ROUND_HALF_UP);//
		map.put("times", sumBig.toPlainString());// 保留2位小数
		return map;
	}

	@Cacheable(value = CacheConstant.BORROW_CACHE, key = "'borrow:borrow:' +#root.methodName +'.' + #para.hashCode()")
	@Override
	public Map analysisOn(Params para) {
		Map map = borrowMapper.analysis(para);
		if(null==map){
			return null;
		}
		Params param = getSutentClass(para);
		if (null == param.getClassCode() || param.getClassCode().length == 0 ) {
			return null;
		}
		// 班级的平均
		Map mapClass = borrowMapper.analysis(param);
		// 把班级的平均借阅：时长、本数：赋值到个人相应字段上：给图表提供参考
		String avg = String.valueOf(mapClass.get("avgtimeSum"));
		if("null".equals(avg)){
			avg ="0";
		}
		BigDecimal avgtimeSum = new BigDecimal(avg);
		map.put("avgtimeSum", avgtimeSum);
		//
		String avgbook = String.valueOf(mapClass.get("avgbookNum"));
		if("null".equals(avgbook)){
			avgbook ="0";
		}
		BigDecimal avgbookNum = new BigDecimal(avgbook);
		map.put("avgbookNum", avgbookNum);
		return map;
	}

	/**
	 * 个人页面查询时：为了显示班级的平均作为参考：这里根据学号查询出班级
	 *
	 * 获取个人对应的：班级平均：查询条件
	 *
	 * @param para
	 */
	private Params getSutentClass(Params para) {
		Params param = new Params();
		BeanUtils.copyProperties(para, param);
		// 班级每天的平均消费额：：作为个人金额的参考来显示
		OwnOrgStudent student = ownOrgStudentService.selectByPrimaryKey(param
				.getOutid());
		if (null != student && StringUtils.isNotEmpty(student.getClassCode())) {
			param.setOutid(null);
			param.setClassSelect(student.getClassCode());
		}
		return param;
	}

	@Override
	public List<Map<String, Object>> exclPreferenceList(Params para) throws Exception {
		List<Map<String, Object>> result = new ArrayList<>();
		//取出所有选参数的所有班级信息
		List<OwnSchoolOrg> classList = ownOrgSchoolService.selectBy(para);
		//清空组织机构的参数
		ParamUtils.emptyParamsCode(para);
		for(OwnSchoolOrg clazz:classList){
			para.setClassSelect(clazz.getClassCode());
//        	String str = ParamUtils.getOrganString(clazz);
			//用来存放一个班级的数据
			List<Map<String, Object>> oneresult = new ArrayList<>();
			//循环取出的借阅类型.将借阅类型的dscrp(借阅书籍类型编号)参数复制给params循环取得借阅书籍详情
			List<Map<String, Object>> typeData = preferenceList(para);
			for(Map<String, Object> t : typeData){
				para.setSomeCode(String.valueOf(t.get("dscrp")));
                List<Map<String, Object>> map = preferenceListTop(para);
				for(Map m : map){
					m.put("school", clazz.getSchoolName());
					m.put("faculty", clazz.getFacultyName());
					m.put("major", clazz.getMajorName());
					m.put("class", clazz.getClassCode());
					m.put("type", t.get("name"));
					oneresult.add(m);
				}
			}
			//对存放结果进行排序,按借阅数量进行降序操作
			Collections.sort(oneresult, (o1, o2) -> {
				int num1 = Integer.parseInt(String.valueOf(o1.get("bookNum")));//name1是从你list里面拿出来的一个
				int num2 = Integer.parseInt(String.valueOf(o2.get("bookNum"))); //name1是从你list里面拿出来的第二个name
				return num2 - num1;
			});
			result.addAll(oneresult);
		}
		return result;
	}

	@Override
	public List<Map> exclAnalysis(Params para) {
		List<OwnSchoolOrg> classList = ownOrgSchoolService.selectBy(para);
		List<Map> resultMap = borrowMapper.exclAnalysis(para);
		for(OwnSchoolOrg clazz:classList){
//			String str = ParamUtils.getOrganString(clazz);
			for(Map map : resultMap){
				boolean isClazz = clazz.getClassCode().equals(map.get("organ"));
				if(isClazz){
					map.put("school", clazz.getSchoolName());
					map.put("faculty", clazz.getFacultyName());
					map.put("major", clazz.getMajorName());
					map.put("class", clazz.getClassCode());
					break;
				}
			}
		}
		return resultMap;
	}

	@Override
	public List<Map<String, Object>> exclHourList(Params para) throws Exception {
		String outid=para.getOutid();
		// setStartEndTime(para);
		List<ResultData> dataList = borrowMapper.exclHourList(para);
		List<OwnSchoolOrg> clazzList = ownOrgSchoolService.selectBy(para);
		if (null == dataList || dataList.size() == 0) {
			return null;
		}
		setStartEndTime(para);
		int monthNum = CalendarUtils.getMonthSpace(para.getBdate(),
				para.getEdate());
		if(monthNum==0){//防止0作为被除数
			monthNum=1;
		}
		// 为了和修改前的返回格式一致，前端就不用再修改了
		List<Map<String, Object>> resultList = new ArrayList<>();
		for(ResultData data:dataList){
			// 时段内汇总平均 - 周几排序、各个小时：汇总平均列表。因为借阅这块没有0点借阅数据因此取巧从1开始24就是0
			for (int j = 1; j <= 24; j++) {// 循环24个小时：：数据中也是从0开始的
				String hour = j + "";
				Map<String, Object> map = getNowHour(data, hour,clazzList);
				if (null == outid && map != null) {
					BigDecimal rersultNum = new BigDecimal(String.valueOf(map.get("num")));
					BigDecimal avg = rersultNum.divide(new BigDecimal(monthNum),
							2, BigDecimal.ROUND_HALF_UP);//人数、人次都进位
					map.put("num", avg);
					resultList.add(map);
				}else if(null != outid && map != null ){
					resultList.add(map);
				}
			}
		}

		return resultList;
	}

	private Map<String, Object> getNowHour(ResultData data,String hour,List<OwnSchoolOrg> list){
		Map<String, Object> map = null;
		if (hour.equals(data.getHour())) {
			map = new HashMap<>();
			for(OwnSchoolOrg org : list){
				if(org.getClassCode().equals(data.getClassCode())){
					int hourInteger = Integer.parseInt(hour);
					String time = hourInteger-1 + ":00-" + hourInteger + ":00";
					map.put("time", time);
					map.put("school", org.getSchoolName());
					map.put("faculty", org.getFacultyName());
					map.put("major", org.getMajorName());
					map.put("class", org.getClassCode());
					map.put("num", data.getSum());
				}
			}

		}
		return map;
	}

	@Override
	public Map<String, List> getBorrowType(com.ziyun.borrow.vo.Params params) {
		List<Map<String, Object>> list = borrowMapper.getBorrowType(params);
		if (null == list || list.size() == 0)
			return null;
		Map<String, List> resultMap = new HashMap<>();
		List<Map<String, Object>> valueList = new ArrayList<>();
		List<Object> xaxisList = new ArrayList<>();

		//将结果集变成top7+其他的形式，并将name数据提取出来，组成X轴数据方便前端
		for(int i=0;i<list.size();i++){
			if(i <= 6){
				Map<String, Object> hm = new HashMap<>();
				hm.putAll(list.get(i));
				xaxisList.add(hm.get("name"));
				valueList.add(hm);
			}else{
				if(i == 7){
					xaxisList.add("其他");
					Map<String, Object> hm = new HashMap<>();
					hm.putAll(list.get(i));
					hm.put("name", "其他");
					valueList.add(hm);
				}else{
					Map<String, Object> map = valueList.get(7);
					//处理其他部分。将每个types拼接在一起以，分开。types是图书分类。并将所有的值累加
					StringBuilder sb = new StringBuilder(String.valueOf(map.get("types")));
					map.put("types", sb.append(","+list.get(i).get("types")).toString());
					long sum = Long.parseLong(String.valueOf(map.get("value")))
							+ Long.parseLong(String.valueOf(list.get(i).get("value")));
					map.put("value", sum);
				}
			}
		}
		resultMap.put("value", valueList);
		resultMap.put("xaxis", xaxisList);
		return resultMap;
	}

	@Cacheable(value = CacheConstant.BORROW_CACHE, key = "'borrow:borrow:' +#root.methodName +'.' + #params.hashCode()")
	@Override
	public List<Map<String, Object>> getLevelTwoType(ParamsStatus params) {
		List<Map<String, Object>> typeList = borrowMapper.getBorrowTypeLevelTwo(params);
		if (null == typeList || typeList.size()  == 0)
			return null;
		return typeList;
	}

	@Override
	public List<Map<String,Object>> getBorrowBookList(Params params) {
		return borrowMapper.getBorrowBookList(params);
	}

	@Cacheable(value = CacheConstant.BORROW_CACHE, key = "'borrow:borrow:' +#root.methodName +'.' + #params.hashCode()")
	@Override
	public List<Map<String, Object>> getBorrowBookDetail(Params params) {
		List<Map<String, Object>> list = borrowMapper.getBorrowBookDetail(params);
		if (null == list || list.size() == 0)
			return null;
		return list;
	}

	@Override
	public int getBorrowBookDetailLength(Params params){
		return borrowMapper.getBorrowBookDetailLength(params);
	}

	@Override
	public List<Map<String, Object>> getBorrowPeopleDetail(ParamsStatus params) {
//		List<Map<String, Object>> list = borrowMapper.getBorrowPeopleDetail(params);
		List<Map<String, Object>> list = borrowMapper.getPeopleDetail(params);
//		Long time = null;
		if (null == list || list.size() == 0 )
			return null;
//		if(params.getSemester() != null) {
//			String nowTerm = borrowMapper.getNowTerm();
//			if (StringUtils.equals(params.getSemester(),nowTerm))
//				params.setTimeframe("1");
//			time = borrowMapper.getTime(params);
//			BigDecimal timeBD = new BigDecimal(time);
//			list.forEach(t ->{
//				BigDecimal bd = new BigDecimal(Long.valueOf(String.valueOf(t.get("value"))));
//				t.put("frequency", bd.divide(timeBD,2,BigDecimal.ROUND_HALF_UP).doubleValue());
//				bd = null;
//			});
//		}
		return list;
	}

	@Override
	public int getBorrowPeopleDetailLength(Params params) {
		return borrowMapper.getBorrowPeopleDetailLength(params);
	}

	@Override
	public List<Map<String, Object>> getBorrowVariationTrend(Params params) {
		List<ResultData> peopleList = borrowMapper.dayPeopleNum(params);
		List<ResultData> borrowList = borrowMapper.dayBorrowNum(params);
		List<Map<String, Object>> resultList = new ArrayList<>();
		if (null == peopleList||peopleList.size() == 0)
			return null;
		//遍历people和borrow的结果，由于借阅书籍和借阅人数一一对应。因此这两个长度一样。将这两个数据合并成一个list供前端生成table
		for(int i=0;i<peopleList.size();i++){
			Map<String, Object> hm = new HashMap<>();
			hm.put("date", peopleList.get(i).getDatetimeStr());
			int borrowNum = borrowList.get(i).getSum().intValue();
			int peopleNum = peopleList.get(i).getSum().intValue();
			hm.put("borrowNum", borrowNum);
			hm.put("peopleNum", peopleNum);
			resultList.add(hm);
			//hm = null;
		}
		return resultList;
	}

	@Override
	public int getBorrowtrendLength(Params para) {
		return borrowMapper.borrowtrendLength(para);
	}

	@Override
	public List<Map<String, Object>> getWeekBorrowTotal(Params params) throws Exception {
		List<Map<String, Object>> resultList = new ArrayList<>();
		//取出借阅频次。用来计算平均借阅次数和最高时段
		List<List<HourData>> avgList = getWeekBorrow(params) !=null?getWeekBorrow(params):new ArrayList<>();
		if (null == avgList||avgList.size() == 0)
			return null;
		for (int i = 0; i <avgList.size(); i++) {
			Map<String, Object> map = new HashMap<>();
			int max = 0;
			String maxHour = "0a";
			int avgSum = 0;
			int totalSum =0;
			for (int j = 0; j < avgList.get(i).size(); j++) {
				HourData thisHour = avgList.get(i).get(j);
				avgSum = avgSum + thisHour.getSum().intValue();
				totalSum = totalSum + thisHour.getTotalsum().intValue();
				if(max < thisHour.getSum().intValue()){
					max = thisHour.getSum().intValue();
					maxHour = thisHour.getHour();
				}
			}
			if(maxHour.endsWith("a")){
				int hour = Integer.valueOf(maxHour.substring(0,1));
				maxHour = hour+":00-"+ (hour+1) + ":00";
			}else{
				int hour = Integer.valueOf(maxHour.substring(0,1)) + 12;
				maxHour = hour+":00-"+ (hour+1) + ":00";
			}
			map.put("maxTime",maxHour);
			map.put("avg",avgSum);
			map.put("sum",totalSum);
			map.put("week", WeekdayEnum.getValue(String.valueOf(i+1)));
			resultList.add(map);
		}
		return resultList;
	}

	@Override
	public List<Map<String, Object>> getBorrowPeople(Params params) throws Exception {
		List<Map<String, Object>> list = borrowMapper.getWeekBorrowDetails(params);
		if (null == list || list.size() == 0)
			return null;
		//调用借阅人群的男女平均借阅方法
		Map<String, Object> sexWeekMap = sexWeekList(params);
		//由于数据库中0是星期天的数据因此将数据首尾调换
		List<Map<String, Object>> resultList =new ArrayList<>();
		if(list.size() > 0){
			resultList = list.subList(1,7);
			resultList.add(list.get(0));
		}
		//取出男女数据
		List womenList = (List)sexWeekMap.get("women");
		List manList = (List)sexWeekMap.get("man");
		for (int i = 0; i <resultList.size() ; i++) {
			Map manMap = (Map) manList.get(i);
			int man = Integer.valueOf(String.valueOf(manMap.get("sum")));

			int woman = Integer.valueOf(String.valueOf(((Map)womenList.get(i)).get("sum")));
			int total = man + woman;
			Map<String, Object> map = resultList.get(i);
			map.put("nan",man);
			map.put("nv",woman);
			map.put("avgsum",total);
			map.put("week",manMap.get("week"));
		}
		return resultList;
	}

	@Override
	public Map<String, Object> getBorrowFeature(Params params) throws Exception {
		Map<String, Object> featureList = borrowMapper.borrowfeature(params);
		if (null == featureList||featureList.size() == 0)
			return null;
		return featureList;
	}

	@Cacheable(value = CacheConstant.BORROW_CACHE, key = "'borrow:borrow:' +#root.methodName +'.' + #para.hashCode()")
	public List<List<HourData>> getWeekBorrow(Params para) throws Exception {
		//个人查询：保留小数：方便前台显示出数据的大小
		int round=0;//保留几位小数：默认汇总平均（群体）是0位。个人是2位
		if(StringUtils.isNotEmpty(para.getOutid())){
			round=2;
		}
		List<ResultData> weekHourList = borrowMapper.weekHourList(para);
		if (null == weekHourList || weekHourList.size() == 0) {
			return null;
		}
		// 获取查询时间段内的所有日期：防止查询结果当中，有很多天数据是空的，无法显示
		setStartEndTime(para);
		List<Week> weekNameList = CalendarUtils.getWeekCountBetween(
				para.getBdate(), para.getEdate());

		List<List<HourData>> resultMap = new ArrayList<List<HourData>>();
		List<HourData> resultList;
		// 时段内汇总平均 - 周几排序、各个小时：汇总平均列表
		for (int i = 0; i < weekNameList.size(); i++) {// 根据周几循环，能将数据按照周几排序
			resultList = new ArrayList<>();
			Week week = weekNameList.get(i);
			for (int j = 0; j < 24; j++) {// 循环24个小时：：数据中也是从0开始的
				String hour = j + "";
				HourData hourData = getWeekHourData(weekHourList, week, hour,round);// 循环获取，每个小时的数据，
				resultList.add(hourData);
			}
			// Map<String, Object> map = new HashMap<String, Object>();
			// map.put(week.getCn_name(), resultList);// 按照周几：返回结果
			resultMap.add(resultList);
		}
		return resultMap;
	}

	@Override
	public List<Map<String, Object>> getBorrowStudentList(Params params) {
		return borrowMapper.getBorrowStudentList(params);
	}

	@Override
	public Long getBorrowStudentCount(Params params) {
		return borrowMapper.getBorrowStudentCount(params);
	}
}
