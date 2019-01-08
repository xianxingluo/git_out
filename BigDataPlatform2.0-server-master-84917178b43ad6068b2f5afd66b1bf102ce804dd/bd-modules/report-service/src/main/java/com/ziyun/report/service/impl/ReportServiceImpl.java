package com.ziyun.report.service.impl;

import com.ziyun.common.enums.SexEnum;
import com.ziyun.report.enums.EndingsEnum;
import com.ziyun.report.enums.LabelEnum;
import com.ziyun.report.mapper.ReportMapper;
import com.ziyun.report.model.*;
import com.ziyun.report.service.IReportService;
import com.ziyun.utils.cache.CacheConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.apache.commons.collections.CollectionUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 毕业报告service
 */
@Service
public class ReportServiceImpl implements IReportService {

    @Autowired
    private ReportMapper reportMapper;


    /**
     * 获取学业信息
     *
     * @param outid
     * @return
     */
    @Override
    public List<Map<String, Object>> getAcademic(String outid) {
        return reportMapper.getAcademic(outid);
    }

    @Override
    public List<Map<String, Object>> getConsume(String outid) {
        return reportMapper.timeChangeListOne(outid);
    }
    /**
     * 统计生成标签数据
     */
    @Override
    public void graduateLabelStat(){
        //查询学生信息列表
        List<Student> studentList = reportMapper.getGraduate();
        List<GraduateLabel> labels = new ArrayList<GraduateLabel>();
        studentList.stream().forEach(s -> {
            GraduateLabel label = new GraduateLabel();
            label.setOutid(s.getOutid());
            //1、查询平均学分绩点在该专业该届排名
            BigDecimal gradeRatio = reportMapper.avgCreditPointRanking(s.getOutid());
            BigDecimal puIntegral = null;
            Boolean flag = false;
            if(gradeRatio != null && gradeRatio.compareTo(new BigDecimal("80")) >= 0){
                //2、查询PU积分
                Map<String,Object> puMap = reportMapper.queryPuIntegral(s.getOutid());
                flag = true;
                if(puMap != null){
                    puIntegral = new BigDecimal(Float.valueOf(puMap.get("integration").toString()));
                }
                if(puIntegral != null && puIntegral.compareTo(new BigDecimal("75")) >= 0){
                    if("女".equals(s.getSex())){
                        label.setLabel(String.valueOf(LabelEnum.GODDESS.getKey()));
                        label.setDescribe(LabelEnum.GODDESS.getDescribe());
                    }else{
                        label.setLabel(String.valueOf(LabelEnum.MALE_GOD.getKey()));
                        label.setDescribe(LabelEnum.MALE_GOD.getDescribe());
                    }
                    label.setDetail(gradeRatio + "%," + puIntegral);
                }else{
                    label.setLabel(String.valueOf(LabelEnum.SUPER_SCHOLAR.getKey()));
                    label.setDescribe(LabelEnum.SUPER_SCHOLAR.getDescribe());
                    label.setDetail(gradeRatio + "%");
                }
                labels.add(label);
                return;
            }

            //3、查询获奖次数
            int scholarshipNums = reportMapper.getScholarshipNums(s.getOutid());
            if(scholarshipNums>=3){
                label.setLabel(String.valueOf(LabelEnum.WINNER_OF_LIFE.getKey()));
                label.setDescribe(LabelEnum.WINNER_OF_LIFE.getDescribe());
                label.setDetail("" + scholarshipNums);
                labels.add(label);
                return;
            }

            //4、查询图书借阅数量
            int borrowNums = reportMapper.getBorrowNums(s.getOutid());
            if(borrowNums >= 20){
                label.setLabel(String.valueOf(LabelEnum.WELL_READ.getKey()));
                label.setDescribe(LabelEnum.WELL_READ.getDescribe());
                label.setDetail("" + borrowNums);
                labels.add(label);
                return;
            }

            //5、查询担任的学生干部
            List<String> positions = reportMapper.queryPosition(s.getOutid());
            if(positions != null && positions.size() > 0){
                label.setLabel(String.valueOf(LabelEnum.STUDENT_LEADER.getKey()));
                label.setDescribe(LabelEnum.STUDENT_LEADER.getDescribe());
                StringBuffer stringBuffer = new StringBuffer();
                positions.stream().forEach(p -> {
                    if("班长".equals(p) || "团支书".equals(p)){
                        stringBuffer.append(s.getClassCode()).append("班").append(p).append("、");
                    }else{
                        stringBuffer.append(p).append("、");
                    }
                });

                if (!"".equals(stringBuffer.toString())) {
                    label.setDetail(stringBuffer.deleteCharAt(stringBuffer.length() - 1).toString());
                }

                labels.add(label);
                return;
            }

            //6、校园活动达人
            if(!flag){
                Map<String,Object> puMap = reportMapper.queryPuIntegral(s.getOutid());
                if(puMap != null){
                    puIntegral = new BigDecimal(Float.valueOf(puMap.get("integration").toString()));
                }
            }
            if(puIntegral != null && gradeRatio.compareTo(new BigDecimal("80")) >= 0){
                label.setLabel(String.valueOf(LabelEnum.SCHOOL_ACTIVE_MAN.getKey()));
                label.setDescribe(LabelEnum.SCHOOL_ACTIVE_MAN.getDescribe());
                label.setDetail("" + puIntegral);
                labels.add(label);
                return;
            }

            //7、查询上网总时长排名百分比

            //params.put("classCodes",getClassByMajor(s.getMajorCode()));
            Param param = new Param();
            param.setOutid(s.getOutid());
            param.setGrade(s.getClassCode().substring(0,2));
            param.setClassCodes(getClassByMajor(s.getMajorCode()));
            Long onlineDuration = reportMapper.onlineDuration(s.getOutid());
            if(onlineDuration != null && onlineDuration > 0){
                param.setDuration(onlineDuration);
                Integer onlineRatio = reportMapper.onlineDurationTop(param);
                if(onlineRatio != null && onlineRatio >= 80){
                    label.setLabel(String.valueOf(LabelEnum.INTERNET_ENTHUSIASTS.getKey()));
                    label.setDescribe(LabelEnum.INTERNET_ENTHUSIASTS.getDescribe());
                    label.setDetail(onlineRatio + "%,"
                            + new BigDecimal(onlineDuration).divide(new BigDecimal(3600), BigDecimal.ROUND_HALF_UP).toString());
                    labels.add(label);
                    return;
                }
            }

            //8、查询宿舍内时间长排名百分比
            Map<String,Object> params = new HashMap<>();
            params.put("outid",s.getOutid());
            params.put("grade",s.getClassCode().substring(0,2));
            params.put("majorCode",s.getMajorCode());
            Integer dormRatio = reportMapper.dormDurationTop(params);
            if(dormRatio != null && dormRatio >= 80){
                if("女".equals(s.getSex())){
                    label.setLabel(String.valueOf(LabelEnum.FEMALE_OTAKU.getKey()));
                    label.setDescribe(LabelEnum.FEMALE_OTAKU.getDescribe());
                }else{
                    label.setLabel(String.valueOf(LabelEnum.INDOORSMAN.getKey()));
                    label.setDescribe(LabelEnum.INDOORSMAN.getDescribe());
                }
                label.setDetail(dormRatio + "%");
                labels.add(label);
                return;
            }

            //9、与世无争
            label.setLabel(String.valueOf(LabelEnum.NONE_OF_MY_BUSINESS.getKey()));
            label.setDescribe(LabelEnum.NONE_OF_MY_BUSINESS.getDescribe());
            label.setDetail("比起外面喧嚣的世界，你更愿意与自己共处");
            labels.add(label);
        });

        labels.stream().forEach( l -> {
            reportMapper.addGraduateLabel(l);
        });
    }

    /**
     * 统计生成结尾寄语
     */
    @Override
    public void graduateEndingsStat() {
        //查询学生信息列表
        List<Student> studentList = reportMapper.getGraduate();
        //List<GraduateEndings> endings = new ArrayList<>();
        studentList.stream().forEach(s -> {
            GraduateEndings ending = new GraduateEndings();
            StringBuffer stringBuffer = new StringBuffer("");
            ending.setOutid(s.getOutid());

            //1、查询借阅百分比排名
            Map<String,Object> params = new HashMap<>();
            params.put("outid",s.getOutid());
            params.put("grade",s.getClassCode().substring(0,2));
            params.put("majorCode",s.getMajorCode());
            Integer borrowRatio = reportMapper.queryBorrowTop(params);
            if(borrowRatio != null && borrowRatio >= 80){
                stringBuffer.append(EndingsEnum.FIRST.getKey()).append(",");
            }

            //2、查询处分次数
            int punishNums = reportMapper.queryPunishNums(s.getOutid());
            if(punishNums > 0){
                stringBuffer.append(EndingsEnum.SECOND.getKey()).append(",");
            }

            //3、查询获奖次数
            int scholarshipNums = reportMapper.getScholarshipNums(s.getOutid());
            if(scholarshipNums>=3){
                stringBuffer.append(EndingsEnum.THIRD.getKey()).append(",");
            }

            //4、查询平均绩点排名百分比
            BigDecimal gradeRatio = reportMapper.avgCreditPointRanking(s.getOutid());
            if(gradeRatio != null && gradeRatio.compareTo(new BigDecimal("80")) >= 0){
                stringBuffer.append(EndingsEnum.FOURTH.getKey()).append(",");
            }

            //5、查询上网时长排名百分比
            Param param = new Param();
            param.setOutid(s.getOutid());
            param.setGrade(s.getClassCode().substring(0,2));
            param.setClassCodes(getClassByMajor(s.getMajorCode()));

            Long onlineDuration = reportMapper.onlineDuration(s.getOutid());
            if(onlineDuration != null && onlineDuration > 0){
                param.setDuration(onlineDuration);
                Integer onlineRatio = reportMapper.onlineDurationTop(param);
                if(onlineRatio != null && onlineRatio >= 80){
                    stringBuffer.append(EndingsEnum.FIFTH.getKey()).append(",");
                }
            }

            //6、查询游戏时长排名百分比
            Long gameDuration = reportMapper.gameDuration(s.getOutid());
            if(gameDuration != null && gameDuration > 0){
                param.setDuration(gameDuration);
                Integer gameRation = reportMapper.gameDurationTop(param);
                if(gameRation != null && gameRation >= 80){
                    stringBuffer.append(EndingsEnum.SIXTH.getKey()).append(",");
                }
            }

            if(!"".equals(stringBuffer.toString())){
                ending.setEndings(stringBuffer.deleteCharAt(stringBuffer.length() - 1).toString());
            }

            //endings.add(ending);
            reportMapper.addGraduateEndings(ending);
        });

        /*endings.stream().forEach(e -> {
            reportMapper.addGraduateEndings(e);
        });*/
    }

    public String[] getClassByMajor(String majorCode){
        if (majorCode == null || "".equals(majorCode)) {
            return null;
        }
        Set<String> classCodeList = reportMapper.queryClassByMajor(majorCode);
        return classCodeList.toArray(new String[]{});
    }

    /**
     * 学生概述 --学业特征--个人平均学分绩点是否排在专业前二十名
     *
     * @param outid 学号
     * @return
     */
    @Override
    public BigDecimal avgCreditPointRanking(String outid) {
        return reportMapper.avgCreditPointRanking(outid);
    }

    /**
     * 毕业报告--获取毕业生，必修，选修各多少门
     *
     * @param outid
     * @return
     */
    @Override
    public List<Map<String, Object>> getAcademicCourse(String outid) {
        List<Map<String, Object>> academicCourseList = reportMapper.getAcademicCourse(outid);
        //若学生只有必修课，没有选修课，则将选修课设置为0门
        if (academicCourseList != null && academicCourseList.size() == 1) {
            Map<String, Object> map = new HashMap<>();
            if ("必修".equals(academicCourseList.get(0).get("course_properties_person"))) {
                map.put("num", 0);
                map.put("course_properties_person", "选修");
            } else {
                map.put("num", 0);
                map.put("course_properties_person", "必修");
            }
            academicCourseList.add(map);
        }
        return academicCourseList;
    }

    /**
     * 获取个人学业的成绩合格率
     */
    @Override
    public String getPassRations(String outid) {
        return reportMapper.getPassRations(outid);
    }

    /**
     * 获取个人消费的基本详情：
     * 例如：四年一共消费多少钱，经常去那个窗口
     *
     * @param outid
     * @return
     */
    @Override
    public Map<String, Object> getConsumeDeatails(String outid) {
        Map<String, Object> resultMap = new HashMap<>();
        //查询在大学这几年，一共消费的金额
        BigDecimal totalConsumes = reportMapper.getTotalConsumes(outid);
        //从kylin中查出的消费单位是分，转换成元
        if (totalConsumes != null) {
            totalConsumes = totalConsumes.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
            resultMap.put("totalConsumes", totalConsumes);
        }
        //查询该学生经常去那个窗口消费，消费多少次
        List<Map<String, Object>> list = reportMapper.consumeHobby(outid);
        if (list != null && list.size() > 0) {
            list.forEach(r -> {
                resultMap.put("name", r.get("NAME"));
                resultMap.put("nums", r.get("NUMS"));
            });
        }
        //查询该学生经常去那个食堂就餐
        Map<String, Object> restaurantMap = reportMapper.getLikeRestaurant(outid);
        if (restaurantMap != null) {
            resultMap.put("topname", restaurantMap.get("topname"));
        }
        return resultMap;
    }

    /**
     * 毕业报告--获取借阅的基本详情
     *
     * @param outid 学号
     * @return
     */
    @Override
    @Cacheable(value = CacheConstant.GRADUATION_CACHE, key = "'borrow:report:' + #outid")
    public Map<String, Object> getBorrowDetails(String outid) {
        Map<String, Object> resultMap = new HashMap<>();
        //获取该学生借阅的总天数
        List<Map<String, Object>> list = reportMapper.getBorrowDaysAndBookNums(outid);
        if (list != null && list.size() > 0) {
            list.forEach(r -> {
                //一共借阅的天数
                resultMap.put("days", r.get("days"));
                //一共借阅了多少本书
                resultMap.put("bookNums", r.get("bookNums"));
            });
        }
        //获取该学生借用哪一本书，时间最长
        Map bookLastTime = reportMapper.getBestLikeBook(outid);
        if (bookLastTime != null) {
            resultMap.putAll(bookLastTime);
        }
        //获取哪一种类的书是你的最爱
        String bookType = reportMapper.bookLikeType(outid);
        resultMap.put("bookType", bookType);
        return resultMap;
    }

    @Override
    public List<Map<String, Object>> preferenceListNot(String outid) {
        //返回结果对象
        List<Map<String, Object>> resultList = new ArrayList<>();
        List<Map<String, Object>> list = reportMapper.preferenceListNot(outid);
        //总的消费金额
        BigDecimal totalConsume = new BigDecimal(0);
        if (list == null || list.size() == 0) {
            return null;
        }
        for (Map map : list) {
            totalConsume = totalConsume.add(new BigDecimal(map.get("OPFARE").toString()));
        }
        //获取的总消费金额单位时分，转换成元
        totalConsume = totalConsume.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
        //其他消费总金额
        BigDecimal otherConsume = new BigDecimal(0);
        //其他消费
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                //返回结果Map
                Map<String, Object> resultMap = new HashMap<>();
                //从第六条开始就是 其他
                if (i > 4) {
                    otherConsume = otherConsume.add(new BigDecimal(list.get(i).get("OPFARE").toString()));

                    if (i == list.size() - 1) {
                        //将分转换成元
                        otherConsume = otherConsume.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
                        resultMap.put("opfare", otherConsume);
                        resultMap.put("consumeType", "其他");
                        String proportion = otherConsume.divide(totalConsume, 4, BigDecimal.ROUND_HALF_UP).doubleValue() * 100 + "%";
                        resultMap.put("proportion", proportion);
                        resultList.add(resultMap);
                    }
                    continue;
                }
                //获取消费金额，由于单位是分，转换成元
                BigDecimal consumeMonry = new BigDecimal(list.get(i).get("OPFARE").toString()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
                resultMap.put("opfare", consumeMonry);
                //根据消费编码，来获取消费类型
                String consumeType = reportMapper.getConsumeTypeByAcccode(list.get(i).get("ACCCODE").toString());
                resultMap.put("consumeType", consumeType);
                //消费类目百分比
//              String proportion=consumeMonry.divide(totalConsume,2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100))+"%";
                String proportion = consumeMonry.divide(totalConsume, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP) + "%";
                resultMap.put("proportion", proportion);
                resultList.add(resultMap);
            }
        }
        return resultList;
    }

    /**
     * 获取专业课成绩最高分的课程，如果最高分有多门，则全部显示
     *
     * @param outid
     * @return
     */
    @Override
    public List<Map<String, Object>> getHighScore(String outid) {
        //返回结果对象
        List<Map<String, Object>> resultMap = new ArrayList<>();
        //获取该学生必修课最高分
        List<Map<String, Object>> list = reportMapper.getHighScore(outid);
        if (list != null && list.size() > 0) {
            resultMap.addAll(list);
        }
        return resultMap;
    }

    @Override
    public String getBestTimesClass(String outid) {
        return reportMapper.getBestTimesClass(outid);
    }
    /**
     * 获取应届毕业生列表
     * @return
     */
    @Override
    public List<Student> getGraduate() {
        return reportMapper.getGraduate();
    }

    /**
     * 根据学号查询学生信息
     * @param outid
     * @return
     */
    @Override
    public Student queryStudentByoutid(String outid) {
        return reportMapper.queryStudentByoutid(outid);
    }

    /**
     * 查询PU积分
     *
     * @param outid
     * @return
     */
    @Override
    public Map<String,Object> queryPuIntegral(String outid) {
        return reportMapper.queryPuIntegral(outid);
    }

    /**
     * 查询上网时长排名
     *
     * @param params
     * @return
     */
    @Override
    public Integer onlineDurationTop(Param params) {
        return reportMapper.onlineDurationTop(params);
    }

    /**
     * 根据学号查询学生标签
     * @param outid
     * @return
     */
    @Override
    @Cacheable(value = CacheConstant.GRADUATION_CACHE, key = "'label:report:' + #outid")
    public GraduateLabel queryGraduateLabel(String outid) {
        GraduateLabel label = reportMapper.queryGraduateLabel(outid);
        if (label == null)
            return new GraduateLabel();

        Student student = this.queryStudentByoutid(label.getOutid());
        if (student != null && "女".equals(student.getSex())) {
            label.setSex(SexEnum.FEMALE.getCode());
        } else {
            label.setSex(SexEnum.MALE.getCode());
        }

        return label;
    }

    /**
     * 根据学号查询上网时长和上网天数
     *
     * @param outid
     * @return
     */
    @Override
    public Map<String, Object> queryOnlineTime(String outid) {
        return reportMapper.queryOnlineTime(outid);
    }

    /**
     * 根据学号查询获奖情况
     *
     * @param outid
     * @return
     */
    @Override
    public List<Map<String, Object>> queryPrizeInfo(String outid) {
        return reportMapper.queryPrizeInfo(outid);
    }

    /**
     * 根据学号查询学生寄语
     * @param outid
     * @return
     */
    @Override
    @Cacheable(value = CacheConstant.GRADUATION_CACHE, key = "'endings:report:' + #outid")
    public GraduateEndings queryGraduateEndings(String outid){
        return reportMapper.queryGraduateEndings(outid);
    }

    /**
     * 根据积分查询积分排名
     *
     * @param integration
     * @return
     */
    @Override
    public Integer queryPuIntegralTop(Float integration) {
        return reportMapper.queryPuIntegralTop(integration);
    }

    /**
     * 根据专业code查询班级code
     *
     * @param majorCode
     * @return
     */
    @Override
    public Set<String> queryClassByMajor(String majorCode) {
        return reportMapper.queryClassByMajor(majorCode);
    }

    /**
     * 查询学生上网信息
     *
     * @param student
     * @return
     */
    @Override
    @Cacheable(value = CacheConstant.GRADUATION_CACHE, key = "'online:report:' + #student.outid")
    public Map<String, Object> onlineOfStudent(Student student) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            Map<String, Object> map = queryOnlineTime(student.getOutid());
            if (map == null || (Long) map.get("day_num") == 0) {
                return resultMap;
            }
            //总天数
            BigDecimal totalNum = new BigDecimal((Long) map.get("day_num"));
            //总上网时长（单位是 秒）转换成小时
            BigDecimal totalDuration = new BigDecimal((Long) map.get("total_duration")).divide(new BigDecimal(3600), 0, BigDecimal.ROUND_HALF_UP);
            BigDecimal avgDuration = totalDuration.divide(totalNum, 0, BigDecimal.ROUND_HALF_UP);
            resultMap.put("totalDuration", totalDuration);
            resultMap.put("avgDuration", avgDuration);

            Param params = new Param();
            params.setOutid(student.getOutid());
            params.setDuration((Long) map.get("total_duration"));
            params.setGrade(student.getClassCode().substring(0, 2));
            params.setClassCodes(getClassByMajor(student.getMajorCode()));
            Integer onlineDurationTop = onlineDurationTop(params);
            resultMap.put("onlineDurationTop", (onlineDurationTop != null ? onlineDurationTop : 0) + "%");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return resultMap;
    }

    /**
     * 获取学生上网数据
     *
     * @param outid
     * @return
     */
    @Override
    @Cacheable(value = CacheConstant.GRADUATION_CACHE, key = "'online:report:' + #outid")
    public Map<String, Object> getOnlineOfStudent(String outid) {
        Student student = reportMapper.queryStudentByoutid(outid);
        if (student == null)
            return new HashMap();
        return this.onlineOfStudent(student);
    }

    /**
     * 获取学生活动数据
     *
     * @param outid
     * @return
     */
    @Override
    @Cacheable(value = CacheConstant.GRADUATION_CACHE, key = "'activity:report:' + #outid")
    public Map<String, Object> getActivityOfStudent(String outid) {
        Map<String, Object> map = this.queryPuIntegral(outid);
        if (map == null || map.get("integration") == null || "".equals(map.get("integration"))) {
            return new HashMap();
        }
        //根据积分查询积分排名
        Integer integrationRation = this.queryPuIntegralTop(Float.valueOf(map.get("integration").toString()));
        if (integrationRation != null) {
            map.put("integrationRation", integrationRation + "%");
        }
        return map;
    }

    /**
     * 获取学生获奖数据
     *
     * @param outid
     * @return
     */
    @Override
    @Cacheable(value = CacheConstant.GRADUATION_CACHE, key = "'scholarship:report:' + #outid")
    public Map<String, Object> getScholarshipOfStudent(String outid) {
        Map<String, Object> resultMap = new HashMap<>();
        List<Map<String, Object>> mapList = this.queryPrizeInfo(outid);
        if (mapList == null) {
            return null;
        }
        BigDecimal bonus = new BigDecimal("0");
        for (Map map : mapList) {
            bonus = bonus.add(new BigDecimal(map.get("amount").toString()));
        }
        resultMap.put("dataList", mapList);
        resultMap.put("bonus", bonus);
        return resultMap;
    }

    /**
     * 获取学生学业数据
     *
     * @param outid
     * @return
     */
    @Override
    @Cacheable(value = CacheConstant.GRADUATION_CACHE, key = "'academic:report:' + #outid")
    public Map<String, Object> getAcademicOfStudent(String outid) {
        Map<String, Object> resultMap = new HashMap<>();
        //获取个人必修课，选修课各多少门
        List<Map<String, Object>> list = this.getAcademicCourse(outid);
        if (!CollectionUtils.isEmpty(list)) {
            //获取个人学业成绩合格率
            String passRations = this.getPassRations(outid);
            //获取专业课成绩最高分的课程
            List<Map<String, Object>> resultList = this.getHighScore(outid);
            //获取该学生，打卡次数最多的教室
            //String beatTimesClass = this.getBestTimesClass(outid);
            resultMap.put("academicCourse", list);
            resultMap.put("passRations", passRations);
            resultMap.put("highScore", resultList);
            //resultMap.put("beatTimesClass", beatTimesClass);
        }
        return resultMap;
    }

    /**
     * 获取学生消费数据
     *
     * @param outid
     * @return
     */
    @Override
    @Cacheable(value = CacheConstant.GRADUATION_CACHE, key = "'consume:report:' + #outid")
    public Map<String, Object> getConsumeOfStudent(String outid) {
        Map<String, Object> resultMap = new HashMap<>();

        //获取个人消费基本详情
        Map<String, Object> map = this.getConsumeDeatails(outid);
        if (map != null && map.size() > 0) {
            //个人消费类目百分比
            List<Map<String, Object>> resultList = this.preferenceListNot(outid);

            resultMap.put("consumeDetails", map);
            resultMap.put("ratioList", resultList);
        }
        return resultMap;
    }

    @Override
    public Object getStudentSchoolType(Param param) {
        return reportMapper.getStudentSchoolType(param.getOutid());
    }

    @Override
    public Map isGraduationStudent(String outid) {
        Map<String, Object> map = new HashMap<>();
        //查询的class_code已经过处理，取前两位
        String classCode = reportMapper.isGraduationStudent(outid);
        //获取当前时间年份后两位,然后减4，默认是毕业生的班级号前两位
        String year = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()).toString().substring(2)) - 4 + "";
        if (year.equals(classCode)) {
            map.put("code", 1);
        } else {
            map.put("code", 0);
        }
        GraduateVisits graduateVisits = reportMapper.queryVisitsByOutid(outid);
        if (graduateVisits != null) {
            map.put("visits", graduateVisits.getVisits());
        } else {
            map.put("visits", 0);
        }
        return map;
    }

    /**
     * 访问量增加
     *
     * @param outid
     */
    @Override
    @CachePut(value = CacheConstant.GRADUATION_CACHE, key = "'visits:report:' + #outid")
    public GraduateVisits visitsIncrease(String outid) {
        GraduateVisits graduateVisits = reportMapper.queryVisitsByOutid(outid);
        if (graduateVisits == null) {
            graduateVisits = new GraduateVisits();
            graduateVisits.setOutid(outid);
            graduateVisits.setVisits(1);
            graduateVisits.setCreateTime(new Date());
            reportMapper.addGraduateVisits(graduateVisits);
        } else {
            graduateVisits.setVisits(graduateVisits.getVisits() + 1);
            graduateVisits.setUpdateTime(new Date());
            reportMapper.visitsIncrease(graduateVisits);
        }
        return graduateVisits;
    }

    /**
     * 获取学生会会长信息
     *
     * @param outid
     * @return
     */
    @Override
    @Cacheable(value = CacheConstant.GRADUATION_CACHE, key = "'association:report:' + #outid")
    public Map<String, Object> getAssociationOfStudent(String outid) {
        Map<String, Object> map = reportMapper.getAssociationOfStudent(outid);
        if(map == null || map.size() == 0)
            return new HashMap<>();
        return map;
    }

    /**
     * 根据学号查询毕业报告访问次数
     *
     * @param outid
     */
    @Override
    @Cacheable(value = CacheConstant.GRADUATION_CACHE, key = "'visits:report:' + #outid")
    public GraduateVisits queryVisitsByOutid(String outid) {
        return reportMapper.queryVisitsByOutid(outid);
    }
}
