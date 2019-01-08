package com.ziyun.consume.server.impl;


import com.ziyun.common.model.HourData;
import com.ziyun.consume.dao.EcardConsumeTypeDao;
import com.ziyun.consume.dao.EcardRecConsumeCopyDao;
import com.ziyun.consume.entity.EcardConsumeType;
import com.ziyun.consume.entity.OwnOrgStudent;
import com.ziyun.consume.entity.OwnOrgStudentType;
import com.ziyun.consume.entity.OwnSchoolOrg;
import com.ziyun.consume.server.IEcardRecConsumeServer;
import com.ziyun.consume.server.StudentServer;
import com.ziyun.consume.tools.CalendarUtils;
import com.ziyun.consume.tools.Week;
import com.ziyun.consume.vo.ConsumeParams;
import com.ziyun.consume.vo.HourDataExport;
import com.ziyun.consume.vo.Params;
import com.ziyun.consume.vo.ResultData;
import com.ziyun.utils.cache.CacheConstant;
import com.ziyun.utils.common.BeanUtil;
import com.ziyun.utils.date.DateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Service
public class EcardRecConsumeImpl implements IEcardRecConsumeServer {

    private static Logger logger = Logger.getLogger(EcardRecConsumeImpl.class);

    private static BigDecimal zero = new BigDecimal(0.00);
    private static BigDecimal zero_0 = new BigDecimal(0);

    private static BigDecimal hundred = new BigDecimal(100);
    private static BigDecimal one = new BigDecimal(1);

    @Resource
    public EcardRecConsumeCopyDao ecardRecConsumeCopyDao;

    @Resource
    public EcardConsumeTypeDao consumeTypeDao;

    @Autowired
    private StudentServer studentServer;





    /**
     * 设置开始结束时间：用来记录天数等的时间函数需要用到。
     * <p>
     * 在前端没有传入时间参数的时候，设置时间参数
     *
     * @param para
     */
    private void setStartEndTime(Params para) {

        if (StringUtils.isNotEmpty(para.getBdate())
                && StringUtils.isNotEmpty(para.getEdate())) {
        } else {
            String bdate = "";
            String edate = "";

            ResultData times = ecardRecConsumeCopyDao.startEndTimes(para);// 获取最早、最晚：记录的时间

            // 如果没有从查询结果中取到开始、结束时间；则设置为当天
            if (null == times || null == times.getBdate()) {
                bdate = CalendarUtils.toYyyy2MM2dd(new Date());
            } else {
                bdate = DateUtils.date2Str4GMT16("yyyy-MM-dd", times.getBdate());//kylin的jdbc接口时间格式化有问题。这里手动处理
            }
            if (null == times || null == times.getEdate()) {
                edate = CalendarUtils.toYyyy2MM2dd(CalendarUtils.getDateBy(1));
            } else {
                edate = DateUtils.date2Str4GMT16("yyyy-MM-dd", times.getEdate());// 结束时间：往后推一天，应为查询条件是小于结束时间
            }
            para.setBdate(bdate);
            para.setEdate(edate);
        }
    }


    /**
     * 新版{群体查询：为提高查询效率：按照天、班级汇总}16
     * <p>
     * 设置开始结束时间：用来记录天数等的时间函数需要用到。
     * <p>
     * 在前端没有传入时间参数的时候，设置时间参数
     *
     * @param para
     */
    private void setStartEndTimeNew(Params para) {

        if (StringUtils.isNotEmpty(para.getBdate())
                && StringUtils.isNotEmpty(para.getEdate())) {
        } else {
            String bdate = "";
            String edate = "";

            ResultData times = ecardRecConsumeCopyDao.startEndTimesNew(para);// 获取最早、最晚：记录的时间

            // 如果没有从查询结果中取到开始、结束时间；则设置为当天
            if (null == times || null == times.getBdate()) {
                bdate = CalendarUtils.toYyyy2MM2dd(new Date());
            } else {
                bdate = DateUtils.date2Str4GMT16("yyyy-MM-dd", times.getBdate());//kylin的jdbc接口时间格式化有问题。这里手动处理
            }
            if (null == times || null == times.getEdate()) {
                edate = CalendarUtils.toYyyy2MM2dd(CalendarUtils.getDateBy(1));
            } else {
                edate = DateUtils.date2Str4GMT16("yyyy-MM-dd", times.getEdate());// 结束时间：往后推一天，应为查询条件是小于结束时间
            }
            para.setBdate(bdate);
            para.setEdate(edate);
        }
    }

    @Cacheable(value = CacheConstant.CONSUME_CACHE, key = "'consume:'+#root.methodName+':'+#para.hashCode()")
    public List sumTopList(Params para) throws Exception {
        List<ResultData> list = ecardRecConsumeCopyDao.sumTopList(para);
        if (null == list || list.size() == 0) {
            return null;
        }
        int index = 0;
        setStartEndTime(para);
        int dayNum = CalendarUtils.getDateSpace(para.getBdate(),
                para.getEdate());//按照时间
        //     Map<String, OwnOrgStudentType> StudentMap = orgStudentServe.selectAllDetail(new Params());
        List<OwnOrgStudentType> stuList = studentServer.selectAllDetail(new Params());
        Map<String, OwnOrgStudentType> StudentMap = new HashMap<String, OwnOrgStudentType>();
        for (OwnOrgStudentType studentType : stuList) {
            StudentMap.put(studentType.getOutid(), studentType);
        }
        for (ResultData data : list) {
            BigDecimal sumBig = data.getSum().divide(hundred, 2,
                    BigDecimal.ROUND_HALF_UP);// 原始数据单位是分，这里转换成元
            data.setSum(sumBig);// 保留2位小数
            // 计算平均消费
            BigDecimal avgBig = sumBig;
            if (0 < dayNum) {// 选择的时间不在同一个月，才需要除以月
                BigDecimal monthNum = new BigDecimal(dayNum).divide(new BigDecimal(30), 4,
                        BigDecimal.ROUND_HALF_UP);
                avgBig = sumBig.divide(monthNum, 2,
                        BigDecimal.ROUND_HALF_UP);// 原始数据单位是分，这里转换成元
            }
            data.setAllAvg(avgBig);// 所有天数/30：月平均消费
            //
            BigDecimal validAvgBig = sumBig;
            BigDecimal validMonthNum = new BigDecimal(data.getNum()).divide(new BigDecimal(30), 4,
                    BigDecimal.ROUND_HALF_UP);//有效的月数（实际消费天数/30）
            if (validMonthNum.compareTo(zero_0) > 0) {// 选择的时间不在同一个月，才需要除以月
                validAvgBig = sumBig.divide(validMonthNum, 2,
                        BigDecimal.ROUND_HALF_UP);// 原始数据单位是分，这里转换成元
            }
            data.setAvg(validAvgBig);// 有消费的所有天数/30：月平均消费
            //
            String outid = String.valueOf(data.getOutid());// 学号
            if (StudentMap.containsKey(outid)) {
                OwnOrgStudentType orgStudentType = StudentMap.get(outid);
                if (StringUtils.isNotEmpty(orgStudentType.getName())) {
                    data.setName(orgStudentType.getName());// 学生姓名
                }
            }
            index = index + 1;
            data.setIndex(index);// 排序，数据在前台显示第几个
        }
        return BeanUtil.objectsToMapsOffNull(list);
    }

    @Override
    @Cacheable(value = CacheConstant.CONSUME_CACHE, key = "'consume:'+#root.methodName+':'+#para.hashCode()")
    public Map sumCollect(Params para) throws Exception {
        ResultData map = ecardRecConsumeCopyDao.sumCollect(para);
        if (null == map) {
            return new HashMap();
        }
        //获取总人数
        long totalNum = studentServer.getStudentSize(para);
        setStartEndTime(para);
        int dayNum = CalendarUtils.getDateSpace(para.getBdate(),
                para.getEdate());// 日期相差的天数
        BigDecimal dayNums = new BigDecimal(1);
        if (dayNum == 0) {
            dayNums = new BigDecimal(1);
        } else {
            dayNums = new BigDecimal(dayNum);
        }
        //除以有效人数的平均消费次数
        BigDecimal sumBig = map.getAvgTimes().divide(dayNums, 0,
                BigDecimal.ROUND_HALF_UP);//
        map.setAvgTimes(sumBig);
        //除以总人数的平均消费次数
        BigDecimal allAvgTimes = map.getTimes().divide(dayNums.multiply(new BigDecimal(totalNum)), 0,
                BigDecimal.ROUND_HALF_UP);//
        map.setAllAvgTimes(allAvgTimes);
        //除以总人数的平均消费
        BigDecimal allAvg = map.getSum().divide(new BigDecimal(totalNum), 0,
                BigDecimal.ROUND_HALF_UP);//
        map.setAllAvg(allAvg);
        //消费总天数
        BigDecimal totalDays = ecardRecConsumeCopyDao.totalConsumeDays(para);
        BigDecimal avgConsumeDays = totalDays.divide(new BigDecimal(totalNum), 2, BigDecimal.ROUND_HALF_UP);
        map.setAvgConsumeDays(avgConsumeDays);
        return BeanUtil.beanToMapOffNull(map);
    }


    @Override
    @Cacheable(value = CacheConstant.CONSUME_CACHE, key = "'consume:'+#root.methodName+':'+#para.hashCode()")
    public Map avgConsumeByDay(Params para) throws Exception {
        ResultData map = ecardRecConsumeCopyDao.avgConsumeByDay(para);
        if (null == map) {
            return new HashMap();
        }
        return BeanUtil.beanToMapOffNull(map);
    }

    @Override
    @Cacheable(value = CacheConstant.CONSUME_CACHE, key = "'consume:'+#root.methodName+':'+#para.hashCode()")
    public List avgBottomListDetail(Params para) throws Exception {
        List<ResultData> list = ecardRecConsumeCopyDao.sumBottomList(para);
        int index = 0;
        setStartEndTime(para);
        int dayNum = CalendarUtils.getDateSpace(para.getBdate(),
                para.getEdate());
        List<OwnOrgStudentType> ownOrgStudentTypes = studentServer.selectAllDetail(new Params());
        Map<String, OwnOrgStudentType> studentMap = new HashMap<>();
        //将ownOrgStudentTypes 转换成每一个outid作为key，OwnOrgStudentType做为value
        if (ownOrgStudentTypes != null && ownOrgStudentTypes.size() > 0) {
            ownOrgStudentTypes.forEach(r -> studentMap.put(r.getOutid(), r));
        }
        //   Map<String, OwnOrgStudentType> StudentMap = orgStudentServe.selectAllDetail(new Params());
        for (ResultData data : list) {
            BigDecimal sumBig = data.getSum().divide(hundred, 2,
                    BigDecimal.ROUND_HALF_UP);// 原始数据单位是分，这里转换成元
            data.setSum(sumBig);// 保留2位小数
            // 计算平均消费
            BigDecimal avgBig = sumBig;
            if (0 != dayNum) {// 选择的时间不在同一个月，才需要除以月
                BigDecimal monthNum = new BigDecimal(dayNum).divide(new BigDecimal(30), 4,
                        BigDecimal.ROUND_HALF_UP);
                avgBig = sumBig.divide(monthNum, 2,
                        BigDecimal.ROUND_HALF_UP);// 原始数据单位是分，这里转换成元
            }
            data.setAllAvg(avgBig);// 所有天数/30：月平均消费
            //
            BigDecimal validAvgBig = sumBig;
            BigDecimal validMonthNum = new BigDecimal(data.getNum()).divide(new BigDecimal(30), 4,
                    BigDecimal.ROUND_HALF_UP);//有效的月数（实际消费天数/30）
            if (validMonthNum.compareTo(zero_0) > 0) {// 选择的时间不在同一个月，才需要除以月
                validAvgBig = sumBig.divide(validMonthNum, 2,
                        BigDecimal.ROUND_HALF_UP);// 原始数据单位是分，这里转换成元
            }
            data.setAvg(validAvgBig);// 有消费的所有天数/30：月平均消费
            //
            String outid = String.valueOf(data.getOutid());// 学号
            if (studentMap.containsKey(outid)) {
                OwnOrgStudentType orgStudentType = studentMap.get(outid);
                if (StringUtils.isNotEmpty(orgStudentType.getName())) {
                    data.setName(orgStudentType.getName());// 学生姓名
                }
//				if (StringUtils.isNotEmpty(orgStudentType.getSchoolName())) {
//					map.put("schoolName", orgStudentType.getSchoolName());// 学生姓名
//				}
                if (StringUtils.isNotEmpty(orgStudentType.getClassCode())) {
                    data.setClassCode(orgStudentType.getClassCode());// 班级
                }
//				if (StringUtils.isNotEmpty(orgStudentType.getFacultyName())) {
//					map.put("facultyName", orgStudentType.getFacultyName());// 学生姓名
//				}
                if (StringUtils.isNotEmpty(orgStudentType.getMajorName())) {
                    data.setMajorName(orgStudentType.getMajorName());// 学生姓名
                }
            }
            index = index + 1;
            data.setIndex(index);// 排序，数据在前台显示第几个
        }
        return BeanUtil.objectsToMapsOffNull(list);
    }

    @Override
    @Cacheable(value = CacheConstant.CONSUME_CACHE, key = "'consume:'+#root.methodName+':'+#para.hashCode()")
    public ResultData sumCount(Params para) throws Exception {
        ResultData result = ecardRecConsumeCopyDao.sumCount(para);

        return result;
    }

    //@Cacheable(value = CacheConstant.BEHAVIOR_CACHE, key = "#root.targetClass.hashCode() +'.' +#root.methodName +'.' + #para.hashCode()")
    public List avgBottomList(Params para) throws Exception {
        List<ResultData> list = ecardRecConsumeCopyDao.sumBottomList(para);

        int index = 0;
        setStartEndTime(para);
        int dayNum = CalendarUtils.getDateSpace(para.getBdate(),
                para.getEdate());
        // Map<String, OwnOrgStudentType> StudentMap = orgStudentServe.selectAllDetail(new Params());
        List<OwnOrgStudentType> stuList = studentServer.selectAllDetail(new Params());
        Map<String, OwnOrgStudentType> StudentMap = new HashMap<String, OwnOrgStudentType>();
        for (OwnOrgStudentType studentType : stuList) {
            StudentMap.put(studentType.getOutid(), studentType);
        }
        for (ResultData data : list) {
            BigDecimal sumBig = data.getSum().divide(hundred, 2,
                    BigDecimal.ROUND_HALF_UP);// 原始数据单位是分，这里转换成元
            data.setSum(sumBig);// 保留2位小数
            // 计算平均消费
            BigDecimal avgBig = sumBig;
            if (0 != dayNum) {// 选择的时间不在同一个月，才需要除以月
                BigDecimal monthNum = new BigDecimal(dayNum).divide(new BigDecimal(30), 4,
                        BigDecimal.ROUND_HALF_UP);
                avgBig = sumBig.divide(monthNum, 2,
                        BigDecimal.ROUND_HALF_UP);// 原始数据单位是分，这里转换成元
            }
            data.setAllAvg(avgBig);// 所有天数/30：月平均消费
            //
            BigDecimal validAvgBig = sumBig;
            BigDecimal validMonthNum = new BigDecimal(data.getNum()).divide(new BigDecimal(30), 4,
                    BigDecimal.ROUND_HALF_UP);//有效的月数（实际消费天数/30）
            if (validMonthNum.compareTo(zero_0) > 0) {// 选择的时间不在同一个月，才需要除以月
                validAvgBig = sumBig.divide(validMonthNum, 2,
                        BigDecimal.ROUND_HALF_UP);// 原始数据单位是分，这里转换成元
            }
            data.setAvg(validAvgBig);// 有消费的所有天数/30：月平均消费
            //
            String outid = String.valueOf(data.getOutid());// 学号
            if (StudentMap.containsKey(outid)) {
                OwnOrgStudentType orgStudentType = StudentMap.get(outid);
                if (StringUtils.isNotEmpty(orgStudentType.getName())) {
                    data.setName(orgStudentType.getName());// 学生姓名
                }
            }
            index = index + 1;
            data.setIndex(index);// 排序，数据在前台显示第几个
        }
        return BeanUtil.objectsToMapsOffNull(list);
    }

    @Override
    @Cacheable(value = CacheConstant.CONSUME_CACHE, key = "'consume:'+#root.methodName+':'+#para.hashCode()")
    public List<Map<String, Object>> preferenceList(Params para)
            throws Exception {
        List<ResultData> list = ecardRecConsumeCopyDao.preferenceList(para);

        // 各个分类的占比，前几、其他
        List<ResultData> resultlist = new ArrayList<ResultData>();// 返回给前台的
        if (list == null || list.size() == 0) {
            return null;
        }
        for (int i = 0; i < list.size(); i++) {
            if (i > 4) {//
                break;
            } else {
                // 填充各个消费分类的名称
                // EcardConsumeType consumeType = consumeTypeDao
                // .selectByPrimaryKey(list.get(i).getAcccode());
                // if (null != consumeType
                // && StringUtils.isNotEmpty(consumeType.getDscrp())) {
                // list.get(i).setDscrp(consumeType.getDscrp());
                // }
                BigDecimal avgBig = list.get(i).getSum()
                        .divide(hundred, 2, BigDecimal.ROUND_HALF_UP);// 原始数据单位是分，这里转换成元
                list.get(i).setSum(avgBig);
                resultlist.add(list.get(i));
            }
        }
        return BeanUtil.objectsToMapsOffNull(resultlist);
    }


    @Cacheable(value = CacheConstant.CONSUME_CACHE, key = "'consume:'+#root.methodName+':'+#para.hashCode()")
    public List<Map<String, Object>> preferenceListTop(Params para)
            throws Exception {

        List<ResultData> list = ecardRecConsumeCopyDao.preferenceListTop(para);

        // 各个分类的占比，前几、其他
        List<ResultData> resultlist = new ArrayList<ResultData>();// 返回给前台的
        for (int i = 0; i < list.size(); i++) {
            // 填充各个消费分类的名称
            // EcardConsumeType consumeType = consumeTypeDao
            // .selectByPrimaryKey(list.get(i).getAcccode());
            // if (null != consumeType
            // && StringUtils.isNotEmpty(consumeType.getDscrp())) {
            // list.get(i).setDscrp(consumeType.getDscrp());
            // }

            //数据库字段：放到前端显示的字段中
            list.get(i).setAcccode(Long.parseLong(list.get(i).getDscrp()));
            list.get(i).setDscrp(null);
            //
            BigDecimal avgBig = list.get(i).getSum()
                    .divide(hundred, 2, BigDecimal.ROUND_HALF_UP);// 原始数据单位是分，这里转换成元
            list.get(i).setSum(avgBig);
            resultlist.add(list.get(i));
        }
        List<Map<String, Object>> resutltList = BeanUtil
                .objectsToMapsOffNull(resultlist);
        // 增加排序号
        int i = 0;
        for (Map<String, Object> map : resutltList) {
            map.put("index", i++);
        }
        return resutltList;
    }

    @Cacheable(value = CacheConstant.CONSUME_CACHE, key = "'consume:'+#root.methodName+':'+#para.hashCode()")
    @Override
    public List<Map<String, Object>> preferenceListTest(Params para)
            throws Exception {

        List<ResultData> list = ecardRecConsumeCopyDao.preferenceListTest(para);

        // 各个分类的占比，前几、其他
        List<ResultData> resultlist = new ArrayList<ResultData>();// 返回给前台的
        for (int i = 0; i < list.size(); i++) {
            if (i > 4) {//
                break;
            } else {
                // 填充各个消费分类的名称
                // EcardConsumeType consumeType = consumeTypeDao
                // .selectByPrimaryKey(list.get(i).getAcccode());
                // if (null != consumeType
                // && StringUtils.isNotEmpty(consumeType.getDscrp())) {
                // list.get(i).setDscrp(consumeType.getDscrp());
                // }
                BigDecimal avgBig = list.get(i).getSum()
                        .divide(hundred, 2, BigDecimal.ROUND_HALF_UP);// 原始数据单位是分，这里转换成元
                list.get(i).setSum(avgBig);
                resultlist.add(list.get(i));
            }
        }
        return BeanUtil.objectsToMapsOffNull(resultlist);
    }

    @Cacheable(value = CacheConstant.CONSUME_CACHE, key = "'consume:'+#root.methodName+':'+#para.hashCode()")
    @Override
    public List<Map<String, Object>> preferenceSex(Params para)
            throws Exception {


        List<ResultData> list = ecardRecConsumeCopyDao.preferenceSex(para);

        // 各个分类的占比，前几、其他
        ResultData data210 = new ResultData();// 餐费
        ResultData data215 = new ResultData();// 商场购物
        ResultData data800 = new ResultData();// 考试费
        ResultData dataOther = new ResultData();// 其他

        data210.setLabel("餐费");
        data215.setLabel("商场购物");
        data800.setLabel("考试费");
        dataOther.setLabel("其它");

        if (null == list || list.size() == 0) {
            return null;
        }
        for (int i = 0; i < list.size(); i++) {

            if (list.get(i).getAcccode().longValue() == 210) {
                if ("男".equals(list.get(i).getSex())) {
                    data210.setMansum(data210.getMansum().add(list.get(i).getSum()));
                }
                if ("女".equals(list.get(i).getSex())) {
                    data210.setWomansum(data210.getWomansum().add(list.get(i).getSum()));
                }
                data210.setAcccode(210L);
            }
            //
            else if (list.get(i).getAcccode().longValue() == 215) {
                if ("男".equals(list.get(i).getSex())) {
                    data215.setMansum(data215.getMansum().add(list.get(i).getSum()));
                }
                if ("女".equals(list.get(i).getSex())) {
                    data215.setWomansum(data215.getWomansum().add(list.get(i).getSum()));
                }
                data215.setAcccode(215L);
            }
            //
            else if (list.get(i).getAcccode().longValue() == 800) {
                if ("男".equals(list.get(i).getSex())) {
                    data800.setMansum(data800.getMansum().add(list.get(i).getSum()));
                }
                if ("女".equals(list.get(i).getSex())) {
                    data800.setWomansum(data800.getWomansum().add(list.get(i).getSum()));
                }
                data800.setAcccode(800L);
            }
            //
            else {
                if ("男".equals(list.get(i).getSex())) {
                    dataOther.setMansum(dataOther.getMansum().add(list.get(i).getSum()));
                }
                if ("女".equals(list.get(i).getSex())) {
                    dataOther.setWomansum(dataOther.getWomansum().add(list.get(i).getSum()));
                }
                dataOther.setAcccode(-100L);
            }
        }

        data210.setSum(data210.getMansum().add(data210.getWomansum()));
        data210.setSum(data210.getSum()
                .divide(hundred, 2, BigDecimal.ROUND_HALF_UP));// 原始数据单位是分，这里转换成元
        data215.setSum(data215.getMansum().add(data215.getWomansum()));
        data215.setSum(data215.getSum()
                .divide(hundred, 2, BigDecimal.ROUND_HALF_UP));// 原始数据单位是分，这里转换成元
        data800.setSum(data800.getMansum().add(data800.getWomansum()));
        data800.setSum(data800.getSum()
                .divide(hundred, 2, BigDecimal.ROUND_HALF_UP));// 原始数据单位是分，这里转换成元
        dataOther.setSum(dataOther.getMansum().add(dataOther.getWomansum()));
        dataOther.setSum(dataOther.getSum()
                .divide(hundred, 2, BigDecimal.ROUND_HALF_UP));// 原始数据单位是分，这里转换成元

        List<Map<String, Object>> resutlt1 = new ArrayList<Map<String, Object>>();
        resutlt1.add(BeanUtil.beanToMapOffNull(data210));
        resutlt1.add(BeanUtil.beanToMapOffNull(data215));
        resutlt1.add(BeanUtil.beanToMapOffNull(data800));
        resutlt1.add(BeanUtil.beanToMapOffNull(dataOther));
        return resutlt1;
    }

    @Cacheable(value = CacheConstant.CONSUME_CACHE, key = "'consume:'+#root.methodName+':'+#para.hashCode()")
    @Override
    public Map<String, List<Map<String, Object>>> detailHour(Params para)
            throws Exception {
        Params paraClass = new Params();
        BeanUtils.copyProperties(para, paraClass);//用于后面分页

        List<ResultData> list = ecardRecConsumeCopyDao.detailHourDeptnameNoTest(para);
        List<ResultData> listTest = ecardRecConsumeCopyDao.detailHourTypeTest(para);
        list.addAll(listTest);
        if (null == list || list.size() == 0) {
            return null;
        }
        Map<String, List<ResultData>> typeMap = new HashMap<String, List<ResultData>>();
        for (int i = 0; i < list.size(); i++) {
            BigDecimal avgBig = list.get(i).getSum()
                    .divide(hundred, 2, BigDecimal.ROUND_HALF_UP);// 原始数据单位是分，这里转换成元
            list.get(i).setSum(avgBig);
            //
            if (typeMap.containsKey(list.get(i).getDscrp())) {
                List<ResultData> typeList = typeMap.get(list.get(i).getDscrp());//按照消费类型分组
                typeList.add(list.get(i));
            } else {
                List<ResultData> typeList = new ArrayList<>();
                typeList.add(list.get(i));
                typeMap.put(list.get(i).getDscrp(), typeList);
            }
        }
        Map<String, List<Map<String, Object>>> resultMap = new HashMap<String, List<Map<String, Object>>>();

        for (Entry<String, List<ResultData>> tlist : typeMap.entrySet()) {
            // 时段内汇总平均 -各个小时：汇总平均列表
            List<Map<String, Object>> resultList = new ArrayList<>();
            for (int j = 0; j < 24; j++) {// 循环24个小时：：数据中也是从0开始的
                String hour = j + "";
                ResultData hourdata = new ResultData();
                for (ResultData data : tlist.getValue()) {
                    if (hour.equals(data.getHour())) {
                        if (com.ziyun.utils.common.StringUtils.isEmpty(hourdata.getHour())) {
                            hourdata = data;
                            hourdata.setTimes(new BigDecimal(1));
                        } else {
                            hourdata.setSum(hourdata.getSum().add(data.getSum()));
                            hourdata.setTimes(hourdata.getTimes().add(new BigDecimal(1)));
                            if (hourdata.getDatetime().before(data.getDatetime())) {//需求是按照类型合并然后：以该类型的最后时间作为时间
                                hourdata.setDatetime(data.getDatetime());
                            }
                        }
                    }
                }//data.setDatetimeStr(DateUtils.date2Str4GMT16("yyyy-MM-dd", data.getDatetime()));
                if (null != hourdata.getDatetime()) {
                    hourdata.setDatetimeStr(DateUtils.date2Str4GMT16(DateUtils.DATE_YY_MM_DD_MM_SS_ME, hourdata.getDatetime()));//kylin的jdbc接口时区bug.
                }
                resultList.add(BeanUtil.beanToMapOffNull(hourdata));
            }
            resultMap.put(tlist.getKey(), resultList);
        }
        return resultMap;
    }

    @Cacheable(value = CacheConstant.CONSUME_CACHE, key = "'consume:'+#root.methodName+':'+#para.hashCode()")
    @Override
    public List<Map<String, Object>> preferenceDeptnameSex(Params para)
            throws Exception {
        Params paraClass = new Params();
        BeanUtils.copyProperties(para, paraClass);//用于后面分页

        List<ResultData> list = new ArrayList<ResultData>();

        List<ResultData> returnlist = getDeptSexResultData(para);
        int i = 0;

        //前面只是查询数据：汇总男女对应的金额；这里按照汇总后的总金额进行排序。并且进行分页，返回分页的结果（如果没有传入分页条件，则返回全部结果集）
        Comparator<ResultData> comparator = (h1, h2) -> h1.getSum().compareTo(h2.getSum());
        returnlist.sort(comparator.reversed());// 按照总金额：降序
        List<ResultData> pagelist = new ArrayList<ResultData>();
        if (null != paraClass.getStart()) {//需要分页
            int start = paraClass.getStart();
            int limit = paraClass.getLimit();
            int last = start + limit;
            for (int k = 0; k < returnlist.size(); k++) {
                if (k >= start && k < last) {// 在分页间隔之间的记录：就是分页要提取的记录
                    pagelist.add(returnlist.get(k));
                } else if (k >= last) {//超过分页取值范围的就不用循环了
                    break;
                }
            }
        } else {//不需要分页：直接返回list
            for (ResultData data : returnlist) {
                i = i + 1;
                data.setIndex(i);
            }
            return BeanUtil.objectsToMapsOffNull(returnlist);
        }
        for (ResultData data : pagelist) {
            data.setIndex(i++);
        }
        return BeanUtil.objectsToMapsOffNull(pagelist);
    }

    /**
     * 获取：商家（考试分类）：计算男女的总金额、男百分比、女百分比
     *
     * @param para
     * @return
     */
    private List<ResultData> getDeptSexResultData(Params para) {
        List<ResultData> list;
        if (StringUtils.isNotBlank(para.getSomeCode()) && "800".equals(para.getSomeCode())) {//考试类：只查询考试的三级分类作为商家显示
            list = ecardRecConsumeCopyDao.preferenceClass3SexTest(para);
        } else if (StringUtils.isNotBlank(para.getSomeCode()) && "215".equals(para.getSomeCode())) {//商场购物：商家显示
            list = ecardRecConsumeCopyDao.preferenceDeptnameSexNoTest(para);
        } else if (StringUtils.isNotBlank(para.getSomeCode()) && "210".equals(para.getSomeCode())) {//餐费：商家显示
            list = ecardRecConsumeCopyDao.preferenceDeptnameSexNoTest(para);
        } else if (StringUtils.isNotBlank(para.getSomeCode()) && "-100".equals(para.getSomeCode())) {//其他：商家显示
            list = ecardRecConsumeCopyDao.preferenceDeptnameSexNoTest(para);
        } else {//查询全部分类：包含（餐费、商场购物、考试类、其他类）
            list = ecardRecConsumeCopyDao.preferenceDeptnameSexNoTest(para);
            List<ResultData> listTest = ecardRecConsumeCopyDao.preferenceClass3SexTest(para);
            list.addAll(listTest);
        }


        // 各个分类的占比，前几、其他
        List<ResultData> resultlist = new ArrayList<ResultData>();// 返回给前台的
        for (int i = 0; i < list.size(); i++) {
            BigDecimal avgBig = list.get(i).getSum()
                    .divide(hundred, 2, BigDecimal.ROUND_HALF_UP);// 原始数据单位是分，这里转换成元
            list.get(i).setSum(avgBig);
            resultlist.add(list.get(i));
        }
        Map<String, Object> deptMap = new HashMap<String, Object>();
        //用map把同一个商家（考试分类）的：男女数据：合并成一条记录
        for (ResultData manData : resultlist) {
            if (deptMap.containsKey(manData.getDscrp())) {
                ResultData mData = (ResultData) deptMap.get(manData.getDscrp());
                if ("男".equals(manData.getSex())) {// 外层只循环：男的：消费商家
                    mData.setMansum(manData.getSum());
                } else if ("女".equals(manData.getSex())) {// 外层只循环：男的：消费商家
                    mData.setWomansum(manData.getSum());
                }
            } else {
                if ("男".equals(manData.getSex())) {// 外层只循环：男的：消费商家
                    manData.setMansum(manData.getSum());
                } else if ("女".equals(manData.getSex())) {// 外层只循环：男的：消费商家
                    manData.setWomansum(manData.getSum());
                }
                deptMap.put(manData.getDscrp(), manData);
            }
        }

        List<ResultData> returnlist = new ArrayList<ResultData>();
        //循环每个商家（考试分类）：计算男女的总金额、男百分比、女百分比
        for (Entry<String, Object> entry : deptMap.entrySet()) {
            ResultData enData = (ResultData) entry.getValue();
            if (null == enData.getMansum()) {
                enData.setMansum(new BigDecimal(0));
            }
            if (null == enData.getWomansum()) {
                enData.setWomansum(new BigDecimal(0));
            }
            enData.setSum(enData.getMansum().add(enData.getWomansum()));//男女的总金额
            if (enData.getSum().compareTo(new BigDecimal(0)) == 0) {//总金额为0：则百分比也都是0
                enData.setManPercent("0%");
                enData.setWomanPercent("0%");
            } else {
                enData.setManPercent(enData.getMansum().divide(enData.getSum(), 2, BigDecimal.ROUND_HALF_UP).multiply(hundred) + "%");
                enData.setWomanPercent(enData.getWomansum().divide(enData.getSum(), 2, BigDecimal.ROUND_HALF_UP).multiply(hundred) + "%");
            }
            returnlist.add(enData);
        }
        return returnlist;
    }

    @Cacheable(value = CacheConstant.CONSUME_CACHE, key = "'consume:'+#root.methodName+':'+#para.hashCode()")
    @Override
    public int preferenceDeptnameSexTotal(Params para)
            throws Exception {
        Params paraClass = new Params();
        BeanUtils.copyProperties(para, paraClass);//用于后面分页
        //
        List<ResultData> returnlist = getDeptSexResultData(para);
        return returnlist.size();
    }



    @Override
    @Cacheable(value = CacheConstant.CONSUME_CACHE, key = "'consume:'+#root.methodName+':'+#para.hashCode()")
    public List<Map<String, Object>> preferenceListNot(Params para)
            throws Exception {
        if (StringUtils.isNotEmpty(para.getOutid())) para.setYearArr(null);

        List<ResultData> list = ecardRecConsumeCopyDao.preferenceListNot(para);

        // 各个分类的占比，前几、其他
        List<ResultData> resultlist = new ArrayList<ResultData>();// 返回给前台的
        //其他消费
        BigDecimal otherConsume = new BigDecimal(0);
        for (int i = 0; i < list.size(); i++) {
            if (i > 4) {//
                //break;
                //大于4就为其他消费,等于list.size()-1 就是其他的最后一条数据
                if (i == list.size() - 1) {
                    otherConsume = otherConsume.add(list.get(i).getSum());
                    otherConsume = otherConsume.divide(hundred, 2, BigDecimal.ROUND_HALF_UP);// 原始数据单位是分，这里转换成元
                    list.get(i).setSum(otherConsume);
                    list.get(i).setDscrp("其他");
                    resultlist.add(list.get(i));
                }
                otherConsume = otherConsume.add(list.get(i).getSum());
            } else {
                // 填充各个消费分类的名称
                EcardConsumeType consumeType = consumeTypeDao
                        .selectByPrimaryKey(list.get(i).getAcccode());
                if (null != consumeType
                        && StringUtils.isNotEmpty(consumeType.getDscrp())) {
                    list.get(i).setDscrp(consumeType.getDscrp());
                }
                BigDecimal avgBig = list.get(i).getSum()
                        .divide(hundred, 2, BigDecimal.ROUND_HALF_UP);// 原始数据单位是分，这里转换成元
                list.get(i).setSum(avgBig);
                resultlist.add(list.get(i));
            }
        }
        return BeanUtil.objectsToMapsOffNull(resultlist);
    }

    @Cacheable(value = CacheConstant.CONSUME_CACHE, key = "'consume:'+#root.methodName+':'+#para.hashCode()")
    public List<Map<String, Object>> timeChangeList(Params para)
            throws Exception {

        List<ResultData> list = ecardRecConsumeCopyDao.timeChangeList(para);

        List<ResultData> monthList = ecardRecConsumeCopyDao.monthAvgList(para);

        if (null == list || list.size() == 0) {
            return null;
        }
        //获取总人数
        Long totalNum = studentServer.getStudentSize(para);

        for (ResultData monthData : monthList) {
            if (null == monthData.getNum()) {
                monthData.setNum(0L);
            }
            //有效人数的月人均
            if (monthData.getNum().compareTo(Long.valueOf(0L)) > 0) {
                monthData.setMonthAvg(monthData.getSum().divide(new BigDecimal(monthData.getNum()),
                        2, BigDecimal.ROUND_HALF_UP));//有消费记录的人数的月人均
                monthData.setAllMonthAvg(monthData.getSum().divide(new BigDecimal(totalNum),
                        2, BigDecimal.ROUND_HALF_UP));//全部人数的月人均
            } else {
                monthData.setMonthAvg(zero_0);
                monthData.setAllMonthAvg(zero_0);
            }

        }

        int i = 0;//序号
        for (ResultData data : list) {
            data.setDatetimeStr(DateUtils.date2Str4GMT16("yyyy-MM-dd", data.getDatetime()));
            BigDecimal avgBig = data.getSum().divide(hundred, 2,
                    BigDecimal.ROUND_HALF_UP);// 原始数据单位是分，这里转换成元
            data.setSum(avgBig); // 平均消费

            if (null == data.getNum()) {
                data.setNum(0L);
            }
            if (data.getNum().compareTo(Long.valueOf(0L)) > 0) {
                data.setAvg(data.getSum().divide(new BigDecimal(data.getNum()),
                        2, BigDecimal.ROUND_HALF_UP));//有消费记录的人数的日人均
                data.setAllAvg(data.getSum().divide(new BigDecimal(totalNum),
                        2, BigDecimal.ROUND_HALF_UP));//全部人数的日人均
            } else {
                data.setAvg(zero_0);
                data.setAllAvg(zero_0);
            }
            //把月人均加入到日的记录
            for (ResultData monthData : monthList) {
                if (data.getDatetimeStr().substring(0, 7).equals(monthData.getDatetimeStr())) {//日期截串到月和年月比较
                    data.setMonthAvg(monthData.getMonthAvg());
                    data.setAllMonthAvg(monthData.getAllMonthAvg());
                }
            }
            //
            i++;
            data.setIndex(i);
        }
        // 获取查询时间段内的所有日期：防止查询结果当中，有很多天数据是空的，无法显示
//		setStartEndTime(para);
//		List<String> allDays = CalendarUtils.getAllDatesBetween(
//				para.getBdate(), para.getEdate());
//		List<ResultData> resultList = new ArrayList<ResultData>();
//		int i=0;//序号
//		for (String day : allDays) {
//			ResultData dayData = getDayDatacharge(list, day);
//			i++;
//			dayData.setIndex(i);
//			resultList.add(dayData);
//		}
        return BeanUtil.objectsToMapsOffNull(list);
    }

    @Override
    @Cacheable(value = CacheConstant.CONSUME_CACHE, key = "'consume:'+#root.methodName+':'+#para.hashCode()")
    public ResultData timeChangeCount(Params para)
            throws Exception {

        ResultData result = ecardRecConsumeCopyDao.timeChangeCount(para);


        return result;
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

    @Cacheable(value = CacheConstant.CONSUME_CACHE, key = "'consume:'+#root.methodName+':'+#para.hashCode()")
    public List<Map<String, Object>> timeChangeListOne(Params para)
            throws Exception {

        List<ResultData> list = ecardRecConsumeCopyDao.timeChangeListOne(para);

        if (null == list || list.size() == 0) {// 如果个人没有数据；则班级的平均也不用显示了；没有参考意义了
            return null;
        }
        for (ResultData data : list) {
            data.setDatetimeStr(DateUtils.date2Str4GMT16("yyyy-MM-dd", data.getDatetime()));
            BigDecimal avgBig = data.getSum().divide(hundred, 2,
                    BigDecimal.ROUND_HALF_UP);// 原始数据单位是分，这里转换成元
            data.setSum(avgBig); // 平均消费
            data.setNum(null);
        }

        // 获取查询时间段内的所有日期：防止查询结果当中，有很多天数据是空的，无法显示
        setStartEndTime(para);
        List<String> allDays = CalendarUtils.getAllDatesBetween(
                para.getBdate(), para.getEdate());
        // 获取个人对应的：班级平均：查询条件
        Params param = getSutentClass(para);
        if (null == param.getClassCode() || param.getClassCode().length == 0) {
            return null;
        }

        List<ResultData> listClass = ecardRecConsumeCopyDao
                .timeChangeList(param);

        for (ResultData data : listClass) {
            data.setDatetimeStr(DateUtils.date2Str4GMT16("yyyy-MM-dd", data.getDatetime()));
            BigDecimal avgBig = data.getSum().divide(hundred, 2,
                    BigDecimal.ROUND_HALF_UP);// 原始数据单位是分，这里转换成元
            data.setSum(avgBig); // 平均消费
        }
        // 个人每天总金额
        List<ResultData> resultList = new ArrayList<ResultData>();
        for (String day : allDays) {
            ResultData dayData = getDayDataOne(list, day);
            resultList.add(dayData);
        }
        // 班级每天平均金额
        List<ResultData> resultListClass = new ArrayList<ResultData>();
        for (String day : allDays) {
            ResultData dayData = getDayDatachargeOne(listClass, day);
            resultListClass.add(dayData);
        }
        for (ResultData oneResult : resultList) {
            for (ResultData classResult : resultListClass) {
                if (oneResult.getDatetimeStr().equals(
                        classResult.getDatetimeStr())) {
                    oneResult.setAvg(classResult.getAvg());// 把班级每天的平均消费额：赋值到个人相应的字段上；一起返回前台
                }
            }
        }
        return BeanUtil.objectsToMapsOffNull(resultList);
    }

    /**
     * 个人页面查询时：为了显示班级的平均作为参考：这里根据学号查询出班级
     * <p>
     * 获取个人对应的：班级平均：查询条件
     *
     * @param para
     */
    private Params getSutentClass(Params para) {
        Params param = new Params();
        BeanUtils.copyProperties(para, param);
        // 班级每天的平均消费额：：作为个人金额的参考来显示

        OwnOrgStudent student = studentServer.selectByPrimaryKey(param
                .getOutid());
        if (null != student && StringUtils.isNotEmpty(student.getClassCode())) {
            param.setOutid(null);
            param.setClassSelect(student.getClassCode());
        }
        return param;
    }

//	/**
//	 * 根据给定的日期；获取查询结果集当中对应的数据项；如果不存在，则新建一个该日期的，数值为0的数据项
//	 *
//	 * @param list
//	 * @param day
//	 * @return
//	 */
//	private ResultData getDayDatacharge(List<ResultData> list, String day) {
//		ResultData dayData = null;// 指定日期的data;如果结果集中没有，则构造一个
//		for (ResultData data : list) {
//			// String dataDay = CalendarUtils.toYyyy2MM2dd(data.getDatetime());
//			if (data.getDatetimeStr().equals(day)) {
//				// data.setDatetimeStr(day);
//				if (data.getNum().longValue() == 0L) {
//					data.setNum(1L);//人数不能设置成1
//				}
//				data.setAvg(data.getSum().divide(new BigDecimal(data.getNum()),
//						2, BigDecimal.ROUND_HALF_UP));
//				return data;
//			}
//		}
//		dayData = new ResultData();
//		dayData.setDatetimeStr(day);
//		dayData.setSum(zero);
//		dayData.setNum(0L);// 每天消费的总人数
//		dayData.setAvg(zero_0);// 每天消费的平均金额
//		return dayData;
//	}

    /**
     * 根据给定的日期；获取查询结果集当中对应的数据项；如果不存在，则新建一个该日期的，数值为0的数据项
     *
     * @param list
     * @param day
     * @return
     */
    private ResultData getDayDatachargeOne(List<ResultData> list, String day) {
        ResultData dayData = null;// 指定日期的data;如果结果集中没有，则构造一个
        for (ResultData data : list) {
            // String dataDay = CalendarUtils.toYyyy2MM2dd(data.getDatetime());
            if (data.getDatetimeStr().equals(day)) {
                // data.setDatetimeStr(day);
                if (null == data.getNum()) {
                    data.setNum(0L);
                }
                if (data.getNum().longValue() > 0L) {
                    data.setAvg(data.getSum().divide(new BigDecimal(data.getNum()),
                            2, BigDecimal.ROUND_HALF_UP));
                } else {
                    data.setAvg(zero);
                }
                return data;
            }
        }
        dayData = new ResultData();
        dayData.setDatetimeStr(day);
        // dayData.setSum(zero);
        // dayData.setNum(0L);// 每天消费的总人数
        dayData.setAvg(zero);// 每天消费的平均金额
        return dayData;
    }

    /**
     * 根据给定的日期；获取查询结果集当中对应的数据项；如果不存在，则新建一个该日期的，数值为0的数据项
     *
     * @param list
     * @param day
     * @return
     */
    private ResultData getDayDataOne(List<ResultData> list, String day) {
        ResultData dayData = null;// 指定日期的data;如果结果集中没有，则构造一个
        for (ResultData data : list) {
            // String dataDay = CalendarUtils.toYyyy2MM2dd(data.getDatetime());
            if (data.getDatetimeStr().equals(day)) {
                // data.setDatetimeStr(day);
                return data;
            }
        }
        dayData = new ResultData();
        dayData.setDatetimeStr(day);
        dayData.setSum(zero);
        // dayData.setNum(0L);// 每天消费的总人数
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
        ResultData dayData = null;// 指定日期的data;如果结果集中没有，则构造一个
        for (ResultData data : list) {
            // String dataDay = CalendarUtils.toYyyy2MM2dd(data.getDatetime());
            if (data.getDatetimeStr().equals(day)) {
                // data.setDatetimeStr(day);
                return data;
            }
        }
        dayData = new ResultData();
        dayData.setDatetimeStr(day);
        dayData.setSum(zero);
        dayData.setNum(0L);// 每天消费的总人数
        return dayData;
    }

    @Cacheable(value = CacheConstant.CONSUME_CACHE, key = "'consume:'+#root.methodName+':'+#para.hashCode()")
    public Map<String, Object> sexWeekList(Params para) throws Exception {
//分别统计，男生，女生，周一到周天消费总金额
        List<ResultData> resultSexList = ecardRecConsumeCopyDao
                .sexWeekList(para);
        //统计周几：人均消费
        List<ResultData> weekSumList = ecardRecConsumeCopyDao
                .sexWeekSum(para);
        if (resultSexList == null || resultSexList.size() == 0 || weekSumList == null || weekSumList.size() == 0) {
            return null;
        }
        //获取总人数
        Long totalNum = studentServer.getStudentSize(para);

        // if (null == weekHourList || weekHourList.size() == 0) {
        // return null;
        // }
        // 获取查询时间段内的所有日期：防止查询结果当中，有很多天数据是空的，无法显示
        setStartEndTime(para);
        List<Week> weekNameList = CalendarUtils.getWeekCountBetween(
                para.getBdate(), para.getEdate());
        List<ResultData> resultManList = new ArrayList<ResultData>();
        List<ResultData> resultWomenList = new ArrayList<ResultData>();
        List<ResultData> weekindexSum = new ArrayList<ResultData>();

        // 时段内汇总平均 - 周几排序、男女分成2个列表
        for (int i = 0; i < weekNameList.size(); i++) {// 根据周几循环，能将数据按照周几排序
            Week week = weekNameList.get(i);
            ResultData manData = getWeekDataSex(resultSexList, week, "男");
            ResultData womenData = getWeekDataSex(resultSexList, week, "女");
            resultManList.add(manData);
            resultWomenList.add(womenData);
            //统计周几：人均消费
            ResultData avgData = getWeekPersonAvg(weekSumList, week, totalNum);
            weekindexSum.add(avgData);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> manlist = BeanUtil
                .objectsToMapsOffNull(resultManList);
        List<Map<String, Object>> womenlist = BeanUtil
                .objectsToMapsOffNull(resultWomenList);
        resultMap.put("man", manlist);
        resultMap.put("women", womenlist);
        //统计周几：人均消费
        List<Map<String, Object>> personAvg = BeanUtil
                .objectsToMapsOffNull(weekindexSum);
        resultMap.put("personAvg", personAvg);
        return resultMap;
    }

    @Override
    @Cacheable(value = CacheConstant.CONSUME_CACHE, key = "'consume:'+#root.methodName+':'+#para.hashCode()")
    public List<Map<String, Object>> WeekTypePersonAvgList(Params para) throws Exception {


        List<ResultData> resultSexList = ecardRecConsumeCopyDao
                .sexWeekList(para);
        //统计周几：人均消费
        List<ResultData> weekSumList = ecardRecConsumeCopyDao
                .sexWeekSum(para);
        //统计周几：各个消费类型的金额
        List<ResultData> weekTypeList = ecardRecConsumeCopyDao
                .WeekConsumeType(para);
        //获取总人数
        long totalNum = studentServer.getStudentSize(para);
        // if (null == weekHourList || weekHourList.size() == 0) {
        // return null;
        // }
        // 获取查询时间段内的所有日期：防止查询结果当中，有很多天数据是空的，无法显示
        setStartEndTime(para);
        List<Week> weekNameList = CalendarUtils.getWeekCountBetween(
                para.getBdate(), para.getEdate());
        List<ResultData> weekindexSum = new ArrayList<ResultData>();

        // 时段内汇总平均 - 周几排序、男女分成2个列表
        for (int i = 0; i < weekNameList.size(); i++) {// 根据周几循环，能将数据按照周几排序
            Week week = weekNameList.get(i);
            ResultData manData = getWeekDataSex(resultSexList, week, "男");
            ResultData womenData = getWeekDataSex(resultSexList, week, "女");
            //统计周几：人均消费
            ResultData avgData = getWeekPersonAvg(weekSumList, week, totalNum);
            avgData.setAvg(avgData.getSum());//人均消费
            //1、把各个字段都汇总到一个week对象上。
            avgData.setMansum(manData.getSum());
            avgData.setWomansum(womenData.getSum());
            avgData.setSum(avgData.getMansum().add(avgData.getWomansum()));//男女的总金额
            //2、计算百分比
            if (avgData.getSum().compareTo(new BigDecimal(0)) == 0) {//总金额为0：则百分比也都是0
                avgData.setManPercent("0%");
                avgData.setWomanPercent("0%");
            } else {
                avgData.setManPercent(avgData.getMansum().divide(avgData.getSum(), 2, BigDecimal.ROUND_HALF_UP).multiply(hundred) + "%");
                avgData.setWomanPercent(avgData.getWomansum().divide(avgData.getSum(), 2, BigDecimal.ROUND_HALF_UP).multiply(hundred) + "%");
            }
            //
            for (ResultData resultData : weekTypeList) {
                if (resultData.getWeekindex() == week.getEn_index()) {
                    if (resultData.getAcccode().longValue() == 220) {//用水
                        avgData.setSum1(resultData.getSum().divide(new BigDecimal(week.getCount() * 100), 2,
                                BigDecimal.ROUND_HALF_UP));
                    } else if (resultData.getAcccode().longValue() == 216) {//用电
                        avgData.setSum2(resultData.getSum().divide(new BigDecimal(week.getCount() * 100), 2,
                                BigDecimal.ROUND_HALF_UP));
                    } else if (resultData.getAcccode().longValue() == 211) {//淋浴
                        avgData.setSum3(resultData.getSum().divide(new BigDecimal(week.getCount() * 100), 2,
                                BigDecimal.ROUND_HALF_UP));
                    } else if (resultData.getAcccode().longValue() == 800) {//考试
                        avgData.setSum4(resultData.getSum().divide(new BigDecimal(week.getCount() * 100), 2,
                                BigDecimal.ROUND_HALF_UP));
                    } else if (resultData.getAcccode().longValue() == 210) {//餐费
                        avgData.setSum5(resultData.getSum().divide(new BigDecimal(week.getCount() * 100), 2,
                                BigDecimal.ROUND_HALF_UP));
                    } else if (resultData.getAcccode().longValue() == 215) {//商场购物
                        avgData.setSum6(resultData.getSum().divide(new BigDecimal(week.getCount() * 100), 2,
                                BigDecimal.ROUND_HALF_UP));
                    }
                }
            }
            //
            //为空的消费类型：金额设置为0
            if (null == avgData.getSum1()) {
                avgData.setSum1(zero_0);
            }
            if (null == avgData.getSum2()) {
                avgData.setSum2(zero_0);
            }
            if (null == avgData.getSum3()) {
                avgData.setSum3(zero_0);
            }
            if (null == avgData.getSum4()) {
                avgData.setSum4(zero_0);
            }
            if (null == avgData.getSum5()) {
                avgData.setSum5(zero_0);
            }
            if (null == avgData.getSum6()) {
                avgData.setSum6(zero_0);
            }
            //
            weekindexSum.add(avgData);
        }
        //判断是否为空，为空返回空数组，方便前端判断。不能返回null。否则excel导出会报错
        boolean isNull = false;
        for (ResultData oneResult : weekindexSum) {
            if (oneResult.getSum().compareTo(new BigDecimal(0)) > 0) {
                isNull = true;
                break;
            }
        }
        if (!isNull) {
            return new ArrayList<Map<String, Object>>();
        }
        //各个字段合并的总明细
        List<Map<String, Object>> personAvg = BeanUtil
                .objectsToMapsOffNull(weekindexSum);
        return personAvg;
    }

    /**
     * 根据给定的星期几序号、性别；获取查询结果集当中对应的数据项；如果不存在，则新建一个该日期的，数值为0的数据项
     *
     * @param list
     * @param week 星期几：序号
     * @param sex  性别
     * @return
     */
    private ResultData getWeekDataSex(List<ResultData> list, Week week, String sex) {
        ResultData dayData = null;// 指定日期的data;如果结果集中没有，则构造一个
        for (ResultData data : list) {
            if (data.getWeekindex() == week.getEn_index()
                    && sex.equals(data.getSex())) {
                data.setWeek(week.getCn_name());
                if (week.getCount() > 1) {// 周几出现的次数大于1，则需要将该数值除以出现的次数：算平均
                    data.setSum(data.getSum().divide(
                            new BigDecimal(week.getCount()), 2,
                            BigDecimal.ROUND_HALF_UP));
                }
                // data.getSum().divide(week.
                BigDecimal avgBig = data.getSum().divide(hundred, 2,
                        BigDecimal.ROUND_HALF_UP);// 原始数据单位是分，这里转换成元
                data.setSum(avgBig); // 月平均消费
                return data;
            }
        }
        dayData = new ResultData();
        dayData.setSex(sex);
        dayData.setWeek(week.getCn_name());
        dayData.setWeekindex(week.getEn_index());
        dayData.setSum(zero);
        return dayData;
    }

    /**
     * 根据给定的星期几序号；获取查询结果集当中对应的数据项；如果不存在，则新建一个该日期的，数值为0的数据项
     * <p>
     * {周几的人均消费}是计算好的，这里不用再除以周几出现的次数
     *
     * @param list
     * @param week
     * @param totalNum 总人数
     *                 星期几：序号
     * @return
     */
    private ResultData getWeekPersonAvg(List<ResultData> list, Week week, Long totalNum) {
        ResultData dayData = null;// 指定日期的data;如果结果集中没有，则构造一个
        for (ResultData data : list) {
            if (data.getWeekindex() == week.getEn_index()) {
                data.setWeek(week.getCn_name());
                BigDecimal avgBig = data.getSum().divide(hundred, 2,
                        BigDecimal.ROUND_HALF_UP);// 原始数据单位是分，这里转换成元
                data.setSum(avgBig); //
                BigDecimal allAvgBig = data.getAllAvg().divide(hundred.multiply(new BigDecimal(totalNum * week.getCount())), 2,
                        BigDecimal.ROUND_HALF_UP);// 原始数据单位是分，这里转换成元
                data.setAllAvg(allAvgBig); //
                return data;
            }
        }
        dayData = new ResultData();
        dayData.setWeek(week.getCn_name());
        dayData.setWeekindex(week.getEn_index());
        dayData.setSum(zero);
        dayData.setAllAvg(zero); //
        return dayData;
    }

    @Override
    @Cacheable(value = CacheConstant.CONSUME_CACHE, key = "'consume:'+#root.methodName+':'+#para.hashCode()")
    public List weekHourList(Params para) throws Exception {
        // 个人查询：保留小数：方便前台显示出数据的大小
        int round = 0;// 保留几位小数：默认汇总平均（群体）是0位。个人是2位
        if (StringUtils.isNotEmpty(para.getOutid())) {
            round = 2;
        }

        List<ResultData> weekHourList = ecardRecConsumeCopyDao
                .weekHourList(para);

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
                HourData hourData = getWeekHourData4Person(weekHourList, week, hour,
                        round);// 循环获取，每个小时的数据，
                resultList.add(hourData);
            }
            // Map<String, Object> map = new HashMap<String, Object>();
            // map.put(week.getCn_name(), resultList);// 按照周几：返回结果
            resultMap.add(resultList);
        }
        return resultMap;
    }

    @Override
    @Cacheable(value = CacheConstant.CONSUME_CACHE, key = "'consume:'+#root.methodName+':'+#para.hashCode()")
    public List hourList(Params para) throws Exception {
        // 个人查询：保留小数：方便前台显示出数据的大小

        List<ResultData> hourList = ecardRecConsumeCopyDao
                .hourList(para);
        if (null == hourList || hourList.size() == 0) {
            return null;
        }
        List<ResultData> resultList = new ArrayList<>();
        // 时段内汇总平均 - 周几排序、各个小时：汇总平均列表
        for (int j = 0; j < 24; j++) {// 循环24个小时：：数据中也是从0开始的
            String hour = j + "";
            ResultData hourData = getHourData(hourList, hour);// 循环获取，每个小时的数据，
            resultList.add(hourData);
        }
        return BeanUtil.objectsToMapsOffNull(resultList);
    }

    @Override
    // @Cacheable(value = CacheConstant.CONSUME_CACHE, key = "'consume:'+#root.methodName+':'+#para.hashCode()")
    public List weekHourListNew(Params para) throws Exception {
        // 个人查询：保留小数：方便前台显示出数据的大小
        int round = 0;// 保留几位小数：默认汇总平均（群体）是0位。个人是2位
        if (StringUtils.isNotEmpty(para.getOutid())) {
            round = 2;
        }

        List<ResultData> weekHourList = ecardRecConsumeCopyDao
                .weekHourListNew(para);

        if (null == weekHourList || weekHourList.size() == 0) {
            return null;
        }
        // 获取查询时间段内的所有日期：防止查询结果当中，有很多天数据是空的，无法显示
        setStartEndTimeNew(para);
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


    @Override
    @Cacheable(value = CacheConstant.CONSUME_CACHE, key = "'consume:'+#root.methodName+':'+#para.hashCode()")
    public List<Map<String, Object>> weekHourListNewDetail(Params para) throws Exception {
        // 个人查询：保留小数：方便前台显示出数据的大小
        int round = 0;// 保留几位小数：默认汇总平均（群体）是0位。个人是2位
        if (StringUtils.isNotEmpty(para.getOutid())) {
            round = 2;
        }

        List<ResultData> weekHourList = ecardRecConsumeCopyDao
                .weekHourListNew(para);

        if (null == weekHourList || weekHourList.size() == 0) {
            return null;
        }
        // 获取查询时间段内的所有日期：防止查询结果当中，有很多天数据是空的，无法显示
        setStartEndTimeNew(para);
        List<Week> weekNameList = CalendarUtils.getWeekCountBetween(
                para.getBdate(), para.getEdate());

        List<Map<String, Object>> resultMap = new ArrayList<Map<String, Object>>();
        List<HourData> resultList = null;
        // 时段内汇总平均 - 周几排序、各个小时：汇总平均列表
        for (int i = 0; i < weekNameList.size(); i++) {// 根据周几循环，能将数据按照周几排序
            resultList = new ArrayList<HourData>();
            Week week = weekNameList.get(i);
            for (int j = 0; j < 24; j++) {// 循环24个小时：：数据中也是从0开始的
                String hour = j + "";
                HourData hourData = getWeekHourData(weekHourList, week, hour,
                        round);// 循环获取，每个小时的数据，
                resultList.add(hourData);
            }
            //+++ 按2个小时（或是几个小时）再此汇总
            Map<String, Object> hoursList = new HashMap<String, Object>();// 按2个小时（或是几个小时）再此汇总
            BigDecimal a0T5 = resultList.get(0).getSum()
                    .add(resultList.get(1).getSum())
                    .add(resultList.get(2).getSum())
                    .add(resultList.get(3).getSum())
                    .add(resultList.get(4).getSum())
                    .add(resultList.get(5).getSum());// 0-5点的金额统计：包含整个5点范围内的
            BigDecimal a6T7 = resultList.get(6).getSum()
                    .add(resultList.get(7).getSum());// 6-7点的金额统计
            BigDecimal a8T9 = resultList.get(8).getSum()
                    .add(resultList.get(9).getSum());// 6-7点的金额统计
            BigDecimal a10T11 = resultList.get(10).getSum()
                    .add(resultList.get(11).getSum());// 6-7点的金额统计
            BigDecimal a12Tp1 = resultList.get(12).getSum()
                    .add(resultList.get(13).getSum());// 6-7点的金额统计
            BigDecimal p2Tp3 = resultList.get(14).getSum()
                    .add(resultList.get(15).getSum());// 6-7点的金额统计
            BigDecimal p4Tp5 = resultList.get(16).getSum()
                    .add(resultList.get(17).getSum());// 6-7点的金额统计
            BigDecimal p6Tp7 = resultList.get(18).getSum()
                    .add(resultList.get(19).getSum());// 6-7点的金额统计
            BigDecimal p8Tp9 = resultList.get(20).getSum()
                    .add(resultList.get(21).getSum());// 6-7点的金额统计
            BigDecimal p10Tp11 = resultList.get(22).getSum()
                    .add(resultList.get(23).getSum());// 6-7点的金额统计
            //
            BigDecimal totalsum = a0T5.add(a6T7).add(a8T9).add(a10T11).add(a12Tp1).add(p2Tp3).add(p4Tp5).add(p6Tp7).add(p8Tp9)
                    .add(p10Tp11);
            hoursList.put("week", week.getCn_name());
            hoursList.put("sum", totalsum);
            //
            hoursList.put("0a", a0T5);//0a-6a金额
            hoursList.put("6a", a6T7);//6a-8a金额
            hoursList.put("8a", a8T9);//8a-10a金额
            hoursList.put("10a", a10T11);//10a-12a金额
            hoursList.put("12a", a12Tp1);//12a-2p金额
            hoursList.put("2p", p2Tp3);//2p-4p金额
            hoursList.put("4p", p4Tp5);//4p-6p金额
            hoursList.put("6p", p6Tp7);//6p-8p金额
            hoursList.put("8p", p8Tp9);//8p-10p金额
            hoursList.put("10p", p10Tp11);//10p-12p金额
            //+++ 按2个小时（或是几个小时）再此汇总
            resultMap.add(hoursList);
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
    private HourDataExport getWeekHourDataExport(List<ResultData> list, Week week,
                                                 String hour, int round) {
        HourDataExport hourData = null;// 指定日期的data;如果结果集中没有，则构造一个
        //时间段：显示格式
        String huorstr = hourToPeriod(hour);
        //
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
                hourData = new HourDataExport(hour, data.getSum());
                hourData.setWeekstr(week.getCn_name());
                hourData.setHourstr(huorstr);
                return hourData;
            }
        }
        hour = CalendarUtils.hourToAPm(hour);// 24小时：转换成上午下午12时制：如：6a、12a、6p
        hourData = new HourDataExport(hour, zero);
        hourData.setWeekstr(week.getCn_name());
        hourData.setHourstr(huorstr);
        return hourData;
    }

    /**
     * 时间点：变成时间段格式：｛｝
     *
     * @param hour
     * @return
     */
    private String hourToPeriod(String hour) {
        int startHuor = Integer.parseInt(hour);
//		startHuor=startHuor+1;
        int endHuor = startHuor + 1;
        String huorstr = "";
        String endhuorstr = "";
        if (startHuor < 10) {
            huorstr = "0" + startHuor;
        } else {
            huorstr = startHuor + "";
        }//
        if (endHuor < 10) {
            endhuorstr = "0" + endHuor;
        } else {
            endhuorstr = endHuor + "";
        }
        huorstr = huorstr + ":00" + "-" + endhuorstr + ":00";
        return huorstr;
    }

    /**
     * 获取每个小时对应的值
     *
     * @param list
     * @param hour
     * @return
     */
    private ResultData getHourData(List<ResultData> list,
                                   String hour) {
        for (ResultData data : list) {
            if (hour.equals(data.getHour())) {
                return data;
            }
        }
        ResultData result = new ResultData();
        result.setSum(zero);
        result.setHour(hour);
        return result;
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
                    //从数据库查询的单位是分，转化成元
                    data.setSum(data.getSum().divide(hundred, 2, BigDecimal.ROUND_HALF_UP));
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
     * 此方法个人消费时段统计，此处是统计次数，不是统计金额。
     * <p>
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
    private HourData getWeekHourData4Person(List<ResultData> list, Week week,
                                            String hour, int round) {
        HourData hourData = null;// 指定日期的data;如果结果集中没有，则构造一个
        for (ResultData data : list) {
            if (data.getWeekindex() == week.getEn_index()
                    && hour.equals(data.getHour())) {
                data.setWeek(week.getCn_name());
                if (week.getCount() > 1) {// 周几出现的次数大于1，则需要将该数值除以出现的次数：算平均
                    //从数据库查询的单位是分，转化成元
                    data.setSum(data.getSum().divide(one, 2, BigDecimal.ROUND_HALF_UP));
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

    @Cacheable(value = CacheConstant.CONSUME_CACHE, key = "'consume:'+#root.methodName+':'+#para.hashCode()")
    public List<Map<String, Object>> consumeDayList(Params para)
            throws Exception {


        List<ResultData> list = ecardRecConsumeCopyDao.consumeDayList(para);

        if (null == list || list.size() == 0) {
            return null;
        }
        for (ResultData data : list) {
            data.setDatetimeStr(DateUtils.date2Str4GMT16("yyyy-MM-dd", data.getDatetime()));
            BigDecimal avgBig = data.getSum().divide(hundred, 2,
                    BigDecimal.ROUND_HALF_UP);// 原始数据单位是分，这里转换成元
            data.setSum(avgBig); // 累计消费
        }
        // 获取查询时间段内的所有日期：防止查询结果当中，有很多天数据是空的，无法显示
        setStartEndTime(para);
        List<String> allDays = CalendarUtils.getAllDatesBetween(
                para.getBdate(), para.getEdate());
        List<ResultData> resultList = new ArrayList<ResultData>();
        for (String day : allDays) {
            ResultData dayData = getDayData(list, day);
            resultList.add(dayData);
        }
        return BeanUtil.objectsToMapsOffNull(resultList);
    }

    @Cacheable(value = CacheConstant.CONSUME_CACHE, key = "'consume:'+#root.methodName+':'+#para.hashCode()")
    public List<Map<String, Object>> rechargeDayList(Params para)
            throws Exception {

        List<ResultData> list = ecardRecConsumeCopyDao.rechargeDayList(para);
        if (null == list || list.size() == 0) {
            return null;
        }
        for (ResultData data : list) {
            BigDecimal avgBig = data.getSum().divide(hundred, 2,
                    BigDecimal.ROUND_HALF_UP);// 原始数据单位是分，这里转换成元
            data.setSum(avgBig); // 累计消费
        }
        // 获取查询时间段内的所有日期：防止查询结果当中，有很多天数据是空的，无法显示
        List<String> allDays = CalendarUtils.getAllDatesBetween(
                para.getBdate(), para.getEdate());
        List<ResultData> resultList = new ArrayList<ResultData>();
        for (String day : allDays) {
            ResultData dayData = getDayData(list, day);
            resultList.add(dayData);
        }
        return BeanUtil.objectsToMapsOffNull(resultList);
    }



    /**
     * 消费学生列表
     *
     * @param para
     * @return
     */
    @Cacheable(value = CacheConstant.CONSUME_CACHE, key = "'consume:'+#root.methodName+':'+#para.hashCode()")
    @Override
    public List<Map<String, Object>> getConsumeStudentList(Params para) {
        setStartEndTime(para);
        //优化：由于直接查kylin速度会很慢，所以先去mysql，查十名学生的学号，然后根据学号，去kylin查消费金额
        List<Map<String, Object>> studentList = studentServer.getStudents(para);
        //构造一个参数map
        Map<String, Object> paramsMap = new HashMap<>();
        List<String> outids = studentList.stream().map(r -> (String) r.get("outid")).collect(Collectors.toList());
        if (outids != null && outids.size() > 0) {
            //将学号id集合添加到map
            paramsMap.put("outids", outids);
        }
        if (StringUtils.isNotBlank(para.getBdate())) {
            //添加开始时间，结束时间
            paramsMap.put("bdate", para.getBdate());
            paramsMap.put("edate", para.getEdate());
        }

        List<Map<String, Object>> consumeStudentList = ecardRecConsumeCopyDao.getConsumeStudentList(paramsMap);
        //将consumeStudentList 转换成map，把学号作为map的key
        Map<String, Map> consumeStudentMap = new HashMap<>();
        if (consumeStudentList != null && consumeStudentList.size() > 0) {
            consumeStudentList.forEach(r -> consumeStudentMap.put(r.get("outid") + "", r));
        }
        for (Map<String, Object> map : studentList) {
            if (consumeStudentMap.containsKey(map.get("outid"))) {
                map.put("num", consumeStudentMap.get(map.get("outid")).get("num"));
                map.put("avgConsume", new BigDecimal(consumeStudentMap.get(map.get("outid")).get("avgConsume") + "").divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
            } else {
                map.put("num", 0);
                map.put("avgConsume", 0);
            }
        }
        return studentList;
    }

    /**
     * 消费学生列表总长度
     *
     * @param para
     * @return
     */
    //  @Cacheable(value = CacheConstant.CONSUME_CACHE, key = "'consume:'+#root.methodName+':'+#para.hashCode()")
    @Override
    public Long getConsumeStudentCount(Params para) {
        return studentServer.getStudentSize(para);
    }


    @Override
    public List<Map<String, Object>> getConsumeCategoryDetail(Params params) {
        //返回结果对象
        List<Map<String, Object>> resultList = new ArrayList<>();
        List<Map<String, Object>> list = ecardRecConsumeCopyDao.getConsumeCategoryDetail(params);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        //根据消费acccode，添加消费类型
        Map<String, String> consumeTypeMap = new HashMap<>();
        consumeTypeMap.put("210", "餐费");
        consumeTypeMap.put("215", "购物");
        consumeTypeMap.put("216", "用电");
        consumeTypeMap.put("222", "用水");
        consumeTypeMap.put("800", "考试费");
        list.forEach(r -> {
            if (consumeTypeMap.containsKey(r.get("acccode").toString())) {
                r.put("consumeType", consumeTypeMap.get(r.get("acccode").toString()));
                resultList.add(r);
            }
        });
        //求 "其它"
        List<Map<String, Object>> otherList = ecardRecConsumeCopyDao.getConsumeCategoryDetailOther(params);
        if (CollectionUtils.isNotEmpty(otherList)) {
            //求消费总金额
            BigDecimal totalConsumeMoney = otherList.stream().map(r -> new BigDecimal(r.get("sums").toString())).reduce((x1, x2) -> x1.add(x2)).get();
            //消费总次数
            Long totalConsumeNums = otherList.stream().map(r -> Long.parseLong(r.get("num").toString())).reduce((x1, x2) -> x1 + x2).get();
            //消费总人数
            long totalPersonNums = otherList.stream().map(r -> r.get("outid").toString()).distinct().count();
            Map<String, Object> othrtMap = new HashMap<>();
            othrtMap.put("acccode", 123);
            othrtMap.put("sums", totalConsumeMoney);
            othrtMap.put("consumeType", "其他");
            othrtMap.put("total_consume_num", totalConsumeNums);
            othrtMap.put("total_person_num", totalPersonNums);
            othrtMap.put("avg_consume_money", totalConsumeMoney.divide(new BigDecimal(totalConsumeNums), 2, BigDecimal.ROUND_HALF_UP));
            othrtMap.put("avg_person_money", totalConsumeMoney.divide(new BigDecimal(totalPersonNums), 2, BigDecimal.ROUND_HALF_UP));
            resultList.add(othrtMap);
        }
        if (CollectionUtils.isNotEmpty(resultList)) {
            return consumeTypeSort(resultList, params);
        }
        return resultList;
    }

    /**
     * 根据消费类型排序
     *
     * @param resultList
     * @param params
     */
    private List<Map<String, Object>> consumeTypeSort(List<Map<String, Object>> resultList, Params params) {
        //如果 order为空就默认为倒叙
        if (StringUtils.isBlank(params.getOrder()) || "descending".equals(params.getOrder())) {
            //倒序
            resultList = resultList.stream().sorted((m1, m2) -> {
                if (StringUtils.isBlank(params.getSort())) {
                    //如果 sort字段为空，就默认以 sums 来排序
                    return new BigDecimal(m2.get("sums").toString()).compareTo(new BigDecimal(m1.get("sums").toString()));
                }
                return new BigDecimal(m2.get(params.getSort()).toString()).compareTo(new BigDecimal(m1.get(params.getSort()).toString()));
            }).collect(Collectors.toList());
        } else {
            //正序
            resultList = resultList.stream().sorted((m1, m2) -> {
                if (StringUtils.isBlank(params.getSort())) {
                    return new BigDecimal(m1.get("sums").toString()).compareTo(new BigDecimal(m2.get("sums").toString()));
                }
                return new BigDecimal(m1.get(params.getSort()).toString()).compareTo(new BigDecimal(m2.get(params.getSort()).toString()));
            }).collect(Collectors.toList());
        }
        return resultList;
    }

    /**
     * 消费类目明细-- 餐厅消费详情
     *
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> getCanteenConsumeDetail(ConsumeParams params) {
        return ecardRecConsumeCopyDao.getCanteenConsumeDetail(params);
    }

    /**
     * 消费类目明细--餐费总长度
     *
     * @param params
     * @return
     */
    @Override
    public Integer getCanteenConsumeDetailCount(ConsumeParams params) {
        return ecardRecConsumeCopyDao.getCanteenConsumeDetailCount(params);
    }

    /**
     * 消费类目明细--购物，用水
     *
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> getShopConsumeDetail(ConsumeParams params) {
        return ecardRecConsumeCopyDao.getShopConsumeDetail(params);
    }

    /**
     * 消费类目明细-- 购物，用水
     *
     * @param params
     * @return
     */
    @Override
    public Integer getShopConsumeDetailCount(ConsumeParams params) {
        return ecardRecConsumeCopyDao.getShopConsumeDetailCount(params);
    }

    /**
     * 考试费列表
     *
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> getExamConsumeDetail(ConsumeParams params) {
        return ecardRecConsumeCopyDao.getExamConsumeDetail(params);
    }

    /**
     * 考试费列表长度
     *
     * @param params
     * @return
     */
    @Override
    public Integer getExamConsumeDetailCount(ConsumeParams params) {
        return ecardRecConsumeCopyDao.getExamConsumeDetailCount(params);
    }

    /**
     * 消费类目明细 ，查询餐费的所有餐饮
     *
     * @param params
     * @return
     */
    @Override
    public List<String> getConsumeDptNames(ConsumeParams params) {
        List<Map<String, Object>> list = ecardRecConsumeCopyDao.getConsumeDptNames(params);
        List<String> dptNames = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(r -> {
                if (Integer.parseInt(r.get("num").toString()) > 1) {
                    dptNames.add(r.get("dptName").toString());
                }
            });
            dptNames.add("其他");
        }
        return dptNames;
    }
}
