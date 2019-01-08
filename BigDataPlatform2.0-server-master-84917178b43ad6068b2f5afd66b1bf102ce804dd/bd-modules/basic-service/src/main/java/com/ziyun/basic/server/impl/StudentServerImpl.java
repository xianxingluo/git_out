package com.ziyun.basic.server.impl;

import com.ziyun.basic.mapper.OwnSchoolOrgMapper;
import com.ziyun.basic.mapper.StudentMapper;
import com.ziyun.basic.entity.OwnOrgStudent;
import com.ziyun.basic.entity.OwnOrgStudentType;
import com.ziyun.basic.server.StudentServer;
import com.ziyun.basic.tools.GCD;
import com.ziyun.basic.vo.Params;
import com.ziyun.basic.vo.ParamsStatus;
import com.ziyun.utils.cache.CacheConstant;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class StudentServerImpl implements StudentServer {

	private static Logger logger = Logger.getLogger(StudentServerImpl.class);

	@Autowired
	public StudentMapper studentMapper;

	@Autowired
	public OwnSchoolOrgMapper ownSchoolOrgMapper;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ziyun.basic.server.StudentServer#getSource()
	 */
	@Override
	@Cacheable(value = CacheConstant.BASIC_CACHE, key = "'base:student:' + #root.methodName + '.' + #params.hashCode()")
	public List<LinkedHashMap<String, Object>> getSource(Params params) {
		List<LinkedHashMap<String, Object>> list = studentMapper.getSourceLocation(params);
		if (null ==list||list.size()==0)
			return null;
		return list;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ziyun.basic.server.StudentServer#getBorrowhabits()
	 */
	@Override
	@Cacheable(value = CacheConstant.BASIC_CACHE, key = "'base:student:' + #root.methodName + '.' + #params.hashCode()")
	public List<LinkedHashMap<String, Object>> getBorrowhabits(Params params) {
		return studentMapper.getBorrowhabits(params);
	}

	// *********平台未调用，先隐藏**********
	/*@Override
	@Cacheable(value = CacheConstant.BASIC_CACHE, key = "'basic:student:' + #root.methodName + '.' + #params.hashCode()")
	public Map<String, List<HotSpotVo>> internet(Params params, String type) {
		List<A3Hotspot> spots = hotspotDao.listHotspot(params);
		// week sum 类型 Map<week, Map<类型,sum>> 数据类型 MAP<week List<>>
		Map<String, Map<String, Integer>> hotspotMap = new HashMap<>();
		SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
		for (A3Hotspot a3Hotspot : spots) {// 遍历记录 将记录按照星期相加
			String week = dateFm.format(a3Hotspot.getDate());
			Map<String, Integer> map;
			if ("Single".equals(type)) {// 当单独时以流量为准 排序
				map = cleanHotspotByFlow(hotspotMap.get(week), a3Hotspot);// 将记录中的详细信息，填写到map<week,Integer>
			} else {// 查询集体时以 人次为准查询热门访问
				map = cleanHotspotByTimes(hotspotMap.get(week), a3Hotspot);// 将记录中的详细信息，填写到map<week,Integer>
			}
			hotspotMap.put(week, map);// 结果相加
		}

		// 将map进行top排序 top5
		Map<String, List<HotSpotVo>> result = new HashMap<>();
		for (String week : hotspotMap.keySet()) {
			Map<String, Integer> weekResult = hotspotMap.get(week);
			List<HotSpotVo> volist = new ArrayList<>();
			for (String Hotspot : weekResult.keySet()) {// 将对象排序 获取top5
				HotSpotVo vo = new HotSpotVo();
				vo.setWeek(week);
				vo.setType(Hotspot);
				vo.setNum(weekResult.get(Hotspot));
				volist.add(vo);
			}
			Collections.sort(volist);
			if (volist.size() > 5) {
				result.put(week, volist.subList(0, 5));
			} else {
				result.put(week, volist);
			}
		}
		return result;
	}*/

	/**
	 * 深信服数据 根据人次累加
	 * // *********未被调用，先隐藏**********
	 * @param map
	 * @param a3Hotspot
	 * @return
	 */
	/*private Map<String, Integer> cleanHotspotByTimes(Map<String, Integer> map,
			A3Hotspot a3Hotspot) {
		if (map == null) {
			map = new HashMap<String, Integer>();
		}
		Detail temp = a3Hotspot.getDetail();
		if (temp != null) {
			List<Spot> sport = temp.getData();
			for (Spot spot : sport) {
				String appName = spot.getApp();
				appName = appName.split(":")[0];
				Integer i = map.get(appName);
				if (i == null) {
					i = new Integer(1);
				} else {
					i++;
				}
				map.put(appName, i);
			}
		}
		return map;
	}*/

	/**
	 * 深信服数据 根据流量累加
	 * // *********未被调用，先隐藏**********
     * @param
     * @param
	 * @return
	 */
	/*private Map<String, Integer> cleanHotspotByFlow(Map<String, Integer> map,
			A3Hotspot a3Hotspot) {
		if (map == null) {
			map = new HashMap<String, Integer>();
		}
		Detail temp = a3Hotspot.getDetail();
		if (temp != null) {
			List<Spot> sport = temp.getData();
			for (Spot spot : sport) {
				String appName = spot.getApp();
				appName = appName.split(":")[0];
				Integer i = map.get(appName);
				if (i == null) {
					i = new Integer(0);
				}
				i += new Integer((int) spot.getTotal());
				map.put(appName, i);
			}
		}
		return map;
	}*/

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ziyun.basic.server.StudentServer#getConsume()
	 */
	@Override
	@Cacheable(value = CacheConstant.BASIC_CACHE, key = "'base:student:' + #root.methodName + '.' + #params.hashCode()")
	public List<LinkedHashMap<String, Object>> getConsume(Params params) {
		return studentMapper.getConsume(params);
	}

	@Override
	@Cacheable(value = CacheConstant.BASIC_CACHE, key = "'base:student:' + #root.methodName + '.' + #params.hashCode()")
	public List<LinkedHashMap<String, Object>> getMajor(Params params) {
		return studentMapper.getMajor();

	}

	@Override
	@Cacheable(value = CacheConstant.BASIC_CACHE, key = "'base:student:' + #root.methodName + '.' + #params.hashCode()")
	public Integer countStudent(Params params) {
		Map<String, Object> map = studentMapper.countStudent(params);
		return Integer.valueOf(map.get("sum") + "");
	}

	@Override
	@Cacheable(value = CacheConstant.BASIC_CACHE, key = "'base:student:' + #root.methodName + '.' + #params.hashCode()")
	public List<LinkedHashMap<String, Object>> getPageStudent(Params params) {
		return studentMapper.getPageStudent(params);
	}

	/*
	 *
	 * 学院关系
	 *
	 * @Override
	 */
	@Cacheable(value = CacheConstant.BASIC_CACHE, key = "'base:student:' + #root.methodName")
	public List<LinkedHashMap<String, Object>> getOrgTree() {
		return studentMapper.getOrgTree();
	}

	// ========================学生画像=====================================
	// 用户特征

	// 用户档案
	@Override
	@Cacheable(value = CacheConstant.BASIC_CACHE, key = "'base:student:' + #root.methodName + '.' + #params.hashCode()")
	public List<LinkedHashMap<String, Object>> getArchive(Params params) {
		return studentMapper.getArchive(params);
	}

	/** 归类 学籍信息
	 * @see com.ziyun.basic.server.StudentServer getStatus(com.ziyun.basic.vo.Params)
	 */
	@Override
	// @Cacheable(value = CacheConstant.BASIC_CACHE, key = "'basic:student:' + #root.methodName + '.' + #params.hashCode()")
    public Map<String, Object> getStatus(ParamsStatus params) {
		Map<String,Object> statusMap= new HashMap<>();
		//本省人为准
		params.setSql("source_location");
		List<LinkedHashMap<String, Object>> source_location=studentMapper.getStatus(params);
		if(source_location.size()>0){
			statusMap.put("source_location", source_location.get(0).get("queryType")+"省人为主");
		}
		//90后为主 此条sql语句支撑到
		String Sql = "( "
				+ "CASE "
				+ "WHEN CONVERT(birthdate,SIGNED) < 19900000 THEN	'80'"
				+ "WHEN (CONVERT(birthdate,SIGNED) < 19950000	AND CONVERT(birthdate,SIGNED) >= 19900000 ) THEN '90' "
				+ "WHEN (CONVERT(birthdate,SIGNED) < 20000000	AND CONVERT(birthdate,SIGNED) >= 19950000 ) THEN '95' "
				+ "WHEN (CONVERT(birthdate,SIGNED) < 20050000	AND CONVERT(birthdate,SIGNED) >= 20000000 ) THEN '00' "
				+ "WHEN (CONVERT(birthdate,SIGNED) < 20100000	AND CONVERT(birthdate,SIGNED) >= 20050000 ) THEN '05' "
				+ "WHEN (CONVERT(birthdate,SIGNED) < 20150000	AND CONVERT(birthdate,SIGNED) >= 20100000 ) THEN '10' "
				+ "WHEN (CONVERT(birthdate,SIGNED) < 20200000	AND CONVERT(birthdate,SIGNED) >= 20150000 ) THEN '15' "
				+ "WHEN (CONVERT(birthdate,SIGNED) >=20200000 ) THEN '20' "
				+ "ELSE"
				+ "	'其他' "
				+ "END "
				+ ")";

		params.setSql(Sql);
		List<LinkedHashMap<String, Object>> birthday=studentMapper.getStatus(params);
		if(birthday.size()>0){
			statusMap.put("birthday", birthday.get(0).get("queryType")+"后");
		}
		//男女比例
		List<LinkedHashMap<String, Object>> sex=countBySex(params);
		if(sex.size()>1){
			int num0 = ((Long)sex.get(0).get("num")).intValue();
			int num1 = ((Long)sex.get(1).get("num")).intValue();
			//获取约分比率
			int gcd = GCD.gcd(num0,num1);
			if (gcd > 1) {
				//最大公约数大于1，num0,num1都进行约分
				num0 = num0 / gcd;
				num1 = num1 / gcd;
			}
			Integer percentageRatio = num0 > num1 ? num1 : num0;
			if ("男".equals(sex.get(0).get("queryType"))) {
                //如果能够整除，那就是= ，否则≈
                if (num0 % num1 == 0 || num1 % num0 == 0) {
                    statusMap.put("sex", (sex.get(0).get("queryType") + ":" + sex.get(1).get("queryType")) + "=" + new BigDecimal((num0)).divide(new BigDecimal(percentageRatio), 2, BigDecimal.ROUND_HALF_UP) + ":" + new BigDecimal((num1)).divide(new BigDecimal(percentageRatio), 2, BigDecimal.ROUND_HALF_UP));
                } else {
                    statusMap.put("sex", (sex.get(0).get("queryType") + ":" + sex.get(1).get("queryType")) + "≈" + new BigDecimal((num0)).divide(new BigDecimal(percentageRatio), 2, BigDecimal.ROUND_HALF_UP) + ":" + new BigDecimal((num1)).divide(new BigDecimal(percentageRatio), 2, BigDecimal.ROUND_HALF_UP));
                }
			} else {
                //如果能够整除，那就是= ，否则≈
                if (num0 % num1 == 0 || num1 % num0 == 0) {
                    statusMap.put("sex", (sex.get(1).get("queryType") + ":" + sex.get(0).get("queryType")) + "=" + new BigDecimal((num1)).divide(new BigDecimal(percentageRatio), 2, BigDecimal.ROUND_HALF_UP) + ":" + new BigDecimal((num0)).divide(new BigDecimal(percentageRatio), 2, BigDecimal.ROUND_HALF_UP));
                } else {
                    statusMap.put("sex", (sex.get(1).get("queryType") + ":" + sex.get(0).get("queryType")) + "≈" + new BigDecimal((num1)).divide(new BigDecimal(percentageRatio), 2, BigDecimal.ROUND_HALF_UP) + ":" + new BigDecimal((num0)).divide(new BigDecimal(percentageRatio), 2, BigDecimal.ROUND_HALF_UP));
                }
			}
			//学生总人数
			statusMap.put("totalNum", ((Long) sex.get(0).get("num")).intValue() + ((Long) sex.get(1).get("num")).intValue());
		} else if (sex.size() > 0) {
			int num0 = ((Long) sex.get(0).get("num")).intValue();
			if ("女".equals(sex.get(0).get("queryType"))) {
				statusMap.put("sex", "男:女=" + "0:" + num0);
			} else {
				statusMap.put("sex", "男:女=" + num0 + ":0");
				//statusMap.put("sex", (sex.get(0).get("queryType") + ":" + (!"男".equals(sex.get(0).get("queryType")) ? "男" : "女") + "=" + num0 + ":0"));
			}
			//学生总人数
			statusMap.put("totalNum", num0);
		}
		return statusMap;
	}


	@Cacheable(value = CacheConstant.BASIC_CACHE, key = "'base:student:' + #root.methodName + '.' + #startDate + #endDate")
	public Map<String,Object> avgValues(String startDate,String endDate){
		Map<String, Object> result = new HashMap<>();
		ParamsStatus params =new ParamsStatus();
		params.setStartDate(startDate);
		params.setEndDate(endDate);
		//平均借书时长
		params.setSql("	FORMAT(sum(datediff(IFNULL(RETURN_DATE,now()),BORROW_DATE))/count(*),4) ");
		List<LinkedHashMap<String, Object>> avgBorrowDays = studentMapper.getBorrow(params);
		if(avgBorrowDays.size()>0){
			result.put("avgBorrowDays",avgBorrowDays.get(0).get("queryType"));
		}
		//平均借阅数量
		params.setSql("	FORMAT(count(*)/COUNT(DISTINCT e.OUTID),4) ");
		List<LinkedHashMap<String, Object>> avgBorrowNum = studentMapper.getBorrow(params);
		if(avgBorrowNum.size()>0){
			result.put("avgBorrowNum",avgBorrowNum.get(0).get("queryType"));
		}
		//平均借阅次数
		params.setSql(" FORMAT(count(DISTINCT CONCAT(e.outid,DATE_FORMAT(e.borrow_date, '%Y-%c-%d')))/count(*),4) ");
		List<LinkedHashMap<String, Object>> avgBorrowTimes = studentMapper.getBorrow(params);
		if(avgBorrowTimes.size()>0){
			result.put("avgBorrowTimes",avgBorrowTimes.get(0).get("queryType"));
		}

		return result;
	}

	/**
	 * 奖励和惩罚
	 * @see com.ziyun.basic.server.StudentServer#getPunishAndReward(com.ziyun.basic.vo.ParamsStatus)
	 */
	@Override
	@Cacheable(value = CacheConstant.BASIC_CACHE, key = "'base:student:' + #root.methodName + '.' + #params.hashCode()")
	public Map<String, Object> getPunishAndReward(ParamsStatus params) {
		Map<String, Object> result = new HashMap<>();

		//惩罚次数
		params.setSql(" count(*) ");
		List<LinkedHashMap<String, Object>> punish = studentMapper.getPunish(params);
		if (punish == null || punish.size() == 0) {
			return null;
		} else {
			result.put("punishTimes", punish.get(0).get("val")+"次");
		}
		//惩罚人数
		params.setSql(" count(DISTINCT p.outid ) ");
		punish = studentMapper.getPunish(params);
		if(punish.size()>0){
			result.put("punishNumber", punish.get(0).get("val")+"人");
		}
		if(params.getEdate() != null && params.getBdate() != null){
			params.setEdate(params.getEdate().substring(0, 4));
			params.setBdate(params.getBdate().substring(0, 4));
		}
		//获奖次数
		params.setSql(" count(*) ");
		List<LinkedHashMap<String, Object>> scholarship = studentMapper.getScholarship(params);
		if (scholarship.size() > 0) {
			result.put("ScholarshipTimes", scholarship.get(0).get("val") + "次");
		}
		//获奖人数
		params.setSql(" count(DISTINCT p.outid ) ");
		scholarship = studentMapper.getScholarship(params);
		if (scholarship.size() > 0) {
			result.put("ScholarshipNumber", scholarship.get(0).get("val") + "人");
		}

		return result;
	}


	/** 归类 学籍信息
	 * @see com.ziyun.basic.server.StudentServer getStatus(com.ziyun.basic.vo.Params)
	 */
	@Override
	@Cacheable(value = CacheConstant.BASIC_CACHE, key = "'base:student:' + #root.methodName + '.' + #params.hashCode()")
	public List<String> getPersonalStatus(ParamsStatus params) {
		List<String> status = new ArrayList<String>();
		//本省人为准
		params.setSql("source_location");
		List<LinkedHashMap<String, Object>> source_location = studentMapper.getStatus(params);
		if(source_location.size()>0){
			status.add(source_location.get(0).get("queryType")+"省人");
		}
		//90后为主 此条sql语句支撑到
		String Sql = "( "
				+ "CASE "
				+ "WHEN CONVERT(birthdate,SIGNED) < 19900000 THEN	'80'"
				+ "WHEN (CONVERT(birthdate,SIGNED) < 19950000	AND CONVERT(birthdate,SIGNED) >= 19900000 ) THEN '90' "
				+ "WHEN (CONVERT(birthdate,SIGNED) < 20000000	AND CONVERT(birthdate,SIGNED) >= 19950000 ) THEN '95' "
				+ "WHEN (CONVERT(birthdate,SIGNED) < 20050000	AND CONVERT(birthdate,SIGNED) >= 20000000 ) THEN '00' "
				+ "WHEN (CONVERT(birthdate,SIGNED) < 20100000	AND CONVERT(birthdate,SIGNED) >= 20050000 ) THEN '05' "
				+ "WHEN (CONVERT(birthdate,SIGNED) < 20150000	AND CONVERT(birthdate,SIGNED) >= 20100000 ) THEN '10' "
				+ "WHEN (CONVERT(birthdate,SIGNED) < 20200000	AND CONVERT(birthdate,SIGNED) >= 20150000 ) THEN '15' "
				+ "WHEN (CONVERT(birthdate,SIGNED) >=20200000 ) THEN '20' "
				+ "ELSE"
				+ "	'其他' "
				+ "END "
				+ ")";

		params.setSql(Sql);
		List<LinkedHashMap<String, Object>> birthday = studentMapper.getStatus(params);
		if(birthday.size()>0){
			status.add(birthday.get(0).get("queryType")+"后");
		}
		//少数民族
		params.setSql("nation");
		List<LinkedHashMap<String, Object>> nation = studentMapper.getStatus(params);
		if(nation.size()>1){
			status.add(birthday.get(0).get("queryType")+"族");
		}
		//男女比例
		return status;
	}

	/*@Override  // *********平台未调用，先隐藏**********
	@Cacheable(value = CacheConstant.BASIC_CACHE, key = "'basic:student:' + #root.methodName + '.' + #params.hashCode()")
	public Map<String, Object> getHotsport(ParamsStatus params) {
		Map<String,Object> hotsportMap= new HashMap<>();
		//上网总时长 人均上网时长 上网高峰时段 上网内容喜好
		//上网总时长
		String Sql =" sum(DATEDIFF(acctstoptime,acctstarttime)) ";
		params.setSql(Sql);
		List<LinkedHashMap<String, Object>> radacctTime=studentMapper.radacctTime(params);
		if(radacctTime==null||radacctTime.size()==0||radacctTime.get(0)==null){
			hotsportMap.put("time","0天");
		}else{
			hotsportMap.put("time",radacctTime.get(0).get("val")+"天");
		}
		//人均上网时长
		Sql =" FORMAT(sum(DATEDIFF(acctstoptime,acctstarttime))*24/count(e.outid),2) ";
		params.setSql(Sql);
		radacctTime=studentMapper.radacctTime(params);
		if(radacctTime==null||radacctTime.size()==0||radacctTime.get(0)==null){

			hotsportMap.put("avgTime","0小时");
		}else{
			hotsportMap.put("avgTime",radacctTime.get(0).get("val")+"小时");
		}
		//上网高峰时段
		List<ResultData> hourlist = new ArrayList<>();
		if (StringUtils.isNotEmpty(params.getOutid())) {
			hourlist = radacctTimeDao.hourListOne(params);//个体查询
		}
		//else{
		//	hourlist = radacctTimeDao.hourList(params);//群体查询
		//}
		 if(null==hourlist||hourlist.size()==0||hourlist.size()==0){
			 hotsportMap.put("rushTime","无");
		 }else{
			int hour= Integer.parseInt(hourlist.get(0).getHour());
			 hotsportMap.put("rushTime",hour+"~"+(hour+1));
//			 List<ResultData> sortHourlist = new ArrayList<ResultData>();
//			 sortHourlist.addAll(hourlist);
//			 Collections.sort(hourlist);//已经排序的：按照人数排序
//			 int i=0;
//			 for (ResultData data : hourlist) {
//				 i++;
//				 data.setIndex(i);
//			 }
//			 hourlist.g
//			 hourlist.get(0);
		 }
//		radacctTime=studentMapper.hotTime(params);
//		int hour = 0;
//		if(radacctTime==null||radacctTime.size()==0||radacctTime.get(0)==null){
//			hotsportMap.put("rushTime","0");
//		}else{
//			 hour = Integer.parseInt((String)radacctTime.get(0).get("HOUR"));
//			 hotsportMap.put("rushTime",hour+"~"+(hour+1));
//		}

		List<ResultData> list = hotspotDao.preferenceList(params);
		if(list==null||list.size()==0||list.get(0)==null){
			hotsportMap.put("favourite","无");
		}else{
			 hotsportMap.put("favourite",list.get(0).getName());
		}
		return hotsportMap;
	}*/

	@Cacheable(value = CacheConstant.BASIC_CACHE, key = "'base:student:' + #root.methodName + '.' + #params.hashCode()")
	public List<LinkedHashMap<String, Object>> countBySex(ParamsStatus params) {
		params.setSql(" e.sex ");
		return studentMapper.getStatus(params);
	}

	@Cacheable(value = CacheConstant.BASIC_CACHE, key = "'base:student:' + #root.methodName + '.' + #params.hashCode()")
	@Override
	public List<LinkedHashMap<String, Object>> countByBirthday(ParamsStatus params) {
		return studentMapper.countByBirthday(params);
	}

	/**
	 * 奖励列表
	 * @see com.ziyun.basic.server.StudentServer scholarshipList(com.ziyun.basic.vo.ParamsStatus)
	 */
	@Override
	@Cacheable(value = CacheConstant.BASIC_CACHE, key = "'base:student:' + #root.methodName + '.' + #params.hashCode()")
	public List<LinkedHashMap<String, Object>> scholarshipPageList(ParamsStatus params) {
		List<LinkedHashMap<String, Object>> list = studentMapper.scholarshipPageList(params);
		if (null == list||list.size() == 0)
			return null;
		return	list;
	}


	/**
	 * 惩罚列表
	 * @see com.ziyun.basic.server.StudentServer scholarshipList(com.ziyun.basic.vo.ParamsStatus)
	 */
	@Override
	@Cacheable(value = CacheConstant.BASIC_CACHE, key = "'base:student:' + #root.methodName + '.' + #params.hashCode()")
	public List<LinkedHashMap<String, Object>> punishPageList(ParamsStatus params) {
		List<LinkedHashMap<String, Object>> list = studentMapper.punishPageList(params);
		if(null == list||list.size() == 0)
			return null;
		return	list;
	}

	@Override
	@Cacheable(value = CacheConstant.BASIC_CACHE, key = "'base:student:' + #root.methodName + '.' + #params.hashCode()")
	public Long scholarshipPageListLength(ParamsStatus para) {
		return studentMapper.scholarshipPageListLength(para);
	}

	@Override
	@Cacheable(value = CacheConstant.BASIC_CACHE, key = "'base:student:' + #root.methodName + '.' + #params.hashCode()")
	public Long punishPageListLength(ParamsStatus para) {
		return studentMapper.punishPageListLength(para);
	}

	@Override
	@Cacheable(value = CacheConstant.BASIC_CACHE, key = "'base:student:' + #root.methodName + '.' + #params.hashCode()")
	public List<Map<String, Object>> proviceFrom(Params para) {
		return studentMapper.proviceFrom(para);
	}

	@Override
	@Cacheable(value = CacheConstant.BASIC_CACHE, key = "'base:student:' + #root.methodName + '.' + #params.hashCode()")
	public List<Map<String, Object>> genderStatistics(Params para) {
		return studentMapper.genderStatistics(para);
	}

	@Override
	@Cacheable(value = CacheConstant.BASIC_CACHE, key = "'base:student:' + #root.methodName + '.' + #params.hashCode()")
	public List<Map<String, Object>> birthdayDistribution(Params para) {
		return studentMapper.birthdayDistribution(para);
	}

	/*@Override
	//@Cacheable(value = CacheConstant.BASIC_CACHE, key = "'basic:student:' + #root.methodName + '.' + #params.hashCode()")
	public List<Map<String, Object>> getCountBySex(Params para) {
		List<Map<String, Object>> sexList = studentMapper.getCountBySex(para) != null?studentMapper.getCountBySex(para):new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		Map<String,Map> resultMap = new LinkedHashMap<>();
		if (null == sexList||sexList.size() == 0)
			return null;
		//循环结果集，按学期将男女数据统一放在一起
		for(Map sex:sexList){

			String level = String.valueOf(sex.get("grade_level"));
			String gradename = GradeLevel.getGradename(level);
			String num = String.valueOf(levelToNumber(level));
			//判断当前list的size是否和生成的index做比较，因为sql按grade_level从低到高排序，所以如果size=index代表不存在
			if(resultMap.get(num) == null){
				Map<String,Object> map = new HashMap<>();
				map.put("nan",0);
				map.put("nv",0);
				map.put("nv_ratio","0%");
				map.put("nan_ratio","0%");
				resultMap.put(num,map);
			}
			Map<String, Object> map = resultMap.get(num);
			if(StringUtils.equals(String.valueOf(sex.get("sex")), "男")){
				map.put("nan", sex.get("num"));
				map.put("nan_ratio", sex.get("ratio"));
			}else{
				map.put("nv", sex.get("num"));
				map.put("nv_ratio", sex.get("ratio"));
			}
			map.put("sum", sex.get("sum"));
			map.put("grade", gradename);
		}
		resultMap.entrySet().forEach(v ->{
			resultList.add(v.getValue());
		});
		return resultList;
	}*/

	@Override
	public List<Map<String, Object>> getCountBySex(Params para) {
		List<Map<String, Object>> sexList = studentMapper.getCountBySex(para);
		if (null == sexList){
			return new ArrayList<>();
		}
		return sexList;
	}
	private int levelToNumber(String level){
		int count = -1;
		switch(level)
		{
			case "4-2":
			++count;
			case "4-1":
			++count;
			case "3-2":
			++count;
			case "3-1":
			++count;
			case "2-2":
			++count;
			case "2-1":
			++count;
			case "1-2":
			++count;
			case "1-1":
			++count;
			default:
			break;
		}
		return count;
	}

	@Override
	@Cacheable(value = CacheConstant.BASIC_CACHE, key = "'base:student:' + #root.methodName + '.' + #params.hashCode()")
	public List<Map<String, Object>> getPunishCount(ParamsStatus params) {
		List<Map<String, Object>> list = studentMapper.getPunishCount(params);
		if(null == list|| list.size() == 0)
			return null;
		return list;
	}

	@Override
	@Cacheable(value = CacheConstant.BASIC_CACHE, key = "'base:student:' + #root.methodName + '.' + #params.hashCode()")
	public List<Map<String, Object>> getScholarshipCountBysex(ParamsStatus params) {
		List<Map<String, Object>> scholarshipList = studentMapper.getScholarshipCountBysex(params);
		if (null == scholarshipList||scholarshipList.size() == 0)
			return null;
		Map<String,Map<String, Object>> resultMap = new HashMap<String,Map<String, Object>>();
		List<Map<String, Object>> resultList = new ArrayList<>();

		scholarshipList.forEach(r ->{
			//判断当前这个次数是否存在结果集中，不存在则新建一个map.大于7次以上统一为7次以上
			String number = String.valueOf(r.get("num"));
			if(Integer.valueOf(number) > 7)
				number = "7次以上";
			//如果不存在直接创建一个并赋值男女的初始值0
			if(resultMap.get(number) == null){
				Map map = new HashMap();
				map.put("nan",0);
				map.put("nv",0);
				map.put("name", number);
				resultMap.put(number, map);
			}

			//取出当前这个数字的value。将当前循环的结果塞入结果中
			Map<String, Object> map = resultMap.get(number);
			if(StringUtils.equals("男", String.valueOf(r.get("sex")))){
				//如果是7次以上则在原来基础上累加值。1-7次的不存在重复的key因此直接put值
				int sum = Integer.valueOf(String.valueOf(map.get("nan")));
				sum = sum + Integer.valueOf(String.valueOf(r.get("sum")));
				map.put("nan", sum);
			}else{
				int sum = Integer.valueOf(String.valueOf(map.get("nv")));
				sum = sum + Integer.valueOf(String.valueOf(r.get("sum")));
				map.put("nv", sum);
			}
		});
		resultMap.values();
		Set<String> keys = resultMap.keySet();
		keys.forEach(k ->{
			Map<String, Object> oneMap = resultMap.get(k);

			int man = Integer.valueOf(String.valueOf(oneMap.get("nan") ));
			int woman = Integer.valueOf(String.valueOf(oneMap.get("nv") ));
			int sum = man + woman;
			int nanRatio = Math.round(man*100/sum);
			int nvRatio = 100 - nanRatio;
			oneMap.put("total", sum);
			oneMap.put("nan_ratio", nanRatio+"%");
			oneMap.put("nv_ratio", nvRatio+"%");
			//根据测试要求改成7<原来是7次以上
			if(StringUtils.equals("7次以上",k))
				oneMap.put("name",">7");
			resultList.add(oneMap);
		});
		return resultList;
	}

	@Override
	@Cacheable(value = CacheConstant.BASIC_CACHE, key = "'base:student:' + #root.methodName + '.' + #params.hashCode()")
	public List<Map<String, Object>> getScholarshipTypeCount(ParamsStatus params) {
		List<Map<String, Object>> typeList = studentMapper.getScholarshipTypeCount(params);
		List<Map<String, Object>> resultList = new ArrayList<>();
		if (null == typeList || typeList.size() == 0)
			return null;
		for(int i=0;i < typeList.size();i++){
			if(i < 4){
				Map<String, Object> typeMap = typeList.get(i);
				Map<String, Object> hm = new HashMap<>();
				//前三加其他从第四位开始都叫其他
				if(i == 3){
					hm.put("name", "其他");
				}else{
					hm.put("scholarshipType", 0);
					hm.put("name", typeMap.get("type"));
				}
				hm.put("value", typeMap.get("num"));
				hm.put("type", typeMap.get("type"));
				resultList.add(hm);
 			}else{
				Map<String, Object> map = resultList.get(3);
				Map<String, Object> typeMap = typeList.get(i);
				BigDecimal rd = new BigDecimal(String.valueOf(map.get("value")));
				BigDecimal td = new BigDecimal(String.valueOf(typeMap.get("num")));
				BigDecimal sum = rd.add(td);
				map.put("value", sum);

				StringBuilder sb = new StringBuilder((String)map.get("type"));
				sb.append("," + typeMap.get("type"));
				map.put("scholarshipType", 1);
				map.put("type", sb.toString());
			}
		}
		return resultList;
	}

	@Override
	@Cacheable(value = CacheConstant.BASIC_CACHE, key = "'base:student:' + #root.methodName ")
	public List<LinkedHashMap> getTermDate() {

		List<LinkedHashMap> data = studentMapper.getTermDate();
		return data;
	}

	@Override
	public List<Map<String, Object>> getgetScholarshipStudentList(ParamsStatus paramsStatus) {
		return studentMapper.getgetScholarshipStudentList(paramsStatus);
	}

	@Override
	public List<Map<String, Object>> getPunishStudentList(ParamsStatus param) {
		return studentMapper.getPunishStudentList(param);
	}

    /**
     * 奖惩特征 - 获奖类型 - 获奖类型学生列表
     *
     * @param param
     * @return
     */
    @Override
    public List<Map<String, Object>> getScholarshipTypeStudentList(ParamsStatus param) {
		return studentMapper.getScholarshipTypeStudentList(param);
    }

    @Override
    public List<Map<String, Object>> getStudents(Params params) {
		return studentMapper.getStudents(params);
    }

    @Override
    public Long getStudentSize(Params params) {
		return studentMapper.getStudentSize(params);
    }

    /**
     * 提供给消费微服务  获取所有的学生基本详情
     *
     * @param params
     * @return
     */
    @Override
    public List<OwnOrgStudentType> selectAllDetail(Params params) {
		return studentMapper.selectAllDetail(params);
    }

    /**
     * 根据学号id，查询学生基本信息
     *
     * @param outid 学号
     * @return
     */
    @Override
    public OwnOrgStudent selectByPrimaryKey(String outid) {
		return studentMapper.selectByPrimaryKey(outid);
    }

    /**
     * 生源地分布--根据省份查询学生列表
     *
     * @param para
     * @return
     */
    @Override
    public List<Map<String, Object>> sourceStudentList(Params para) {
		List<Map<String, Object>> maps = studentMapper.sourceStudentList(para);
		//入学年份格式转换
		dateFormatTransport(maps);
		return maps;
    }

	//时间格式转换
	List<Map<String, Object>> dateFormatTransport(List<Map<String, Object>> mapList) {
		mapList.forEach(r -> {
			String birthdate = r.get("birthdate") + "";
			//将出生年份格式"20101024" 改为"2010-10-24"
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date parse = sdf.parse(birthdate);
				String format = sdf2.format(parse);
				r.put("birthdate", format);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		});
		return mapList;
	}

    /**
     * 生源地分布--根据省份查询学生列表总长度
     *
     * @param para
     * @return
     */
    @Override
    public int sourceStudentListCount(Params para) {
		return studentMapper.sourceStudentListCount(para);
    }

	/**
	 * 男女比率--学生列表
	 *
	 * @param param
	 * @return
	 */
	@Override
	public List<Map<String, Object>> sexRatioStudentList(Params param) {
		return studentMapper.sexRatioStudentList(param);
	}

	/**
	 * 男女比率--学生列表总长度
	 *
	 * @param param
	 * @return
	 */
	@Override
	public int sexRatioStudentListCount(Params param) {
		return studentMapper.sexRatioStudentListCount(param);
	}

	/**
	 * 判断是否是毕业生
	 *
	 * @param outid
	 * @return
	 */
	@Override
	public boolean isGraduationStudent(String outid) {
		Integer num = studentMapper.isGraduationStudent(outid);
		if (num != null && num > 0)
			return true;
		return false;
	}

	/**
	 * 年龄分布--学生列表
	 *
	 * @param para
	 * @return
	 */
	@Override
	public List<Map<String, Object>> countByBrithdryStudentList(ParamsStatus para) {
		List<Map<String, Object>> maps = studentMapper.countByBrithdryStudentList(para);
		//生日年份
		dateFormatTransport(maps);
		return maps;
	}

	/**
	 * 年龄分布--学业列表总长度
	 *
	 * @param para
	 * @return
	 */
	@Override
	public int countByBrithdryStudentListCount(ParamsStatus para) {
		return studentMapper.countByBrithdryStudentListCount(para);
	}

    @Override
    public String getEnrollmentYearById(Params params) {
        return studentMapper.getEnrollmentYearById(params);
    }

    /**
     * 奖惩特征--》下面的学生列表模块（新增的方法）
     *
     * @param paramsStatus
     * @return
     */
    @Override
    public List<Map<String, Object>> getScholarshipPunishStudentList(ParamsStatus paramsStatus) {
        return studentMapper.getScholarshipPunishStudentList(paramsStatus);
    }

    /**
     * 奖罚特征--学生列表总长度
     *
     * @param paramsStatus
     * @return
     */
    @Override
    public Long getScholarshipPunishStudentCount(ParamsStatus paramsStatus) {
        return studentMapper.getScholarshipPunishStudentCount(paramsStatus);
    }

    @Override
    public Map selectByOutid(String outid) {
        return studentMapper.selectByOutid(outid);
    }

	/**
	 * 活动积分长度
	 *
	 * @param param
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getActiveRanking(ParamsStatus param) {
		return studentMapper.getActiveRanking(param);
	}

	/**
	 * 奖惩特征--活动积分长度
	 *
	 * @param param
	 * @return
	 */
	@Override
	public int getActiveRankingSize(ParamsStatus param) {
		return studentMapper.getActiveRankingSize(param);
	}

    /**
     * 获取处罚类型
     *
     * @param params
     * @return
     */
    @Override
    public List<String> getPunishType(Params params) {
        return studentMapper.getPunishType(params);
    }
}
