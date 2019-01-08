package com.ziyun.report.service;

import com.ziyun.report.ReportApplication;
import com.ziyun.report.mapper.ReportMapper;
import com.ziyun.report.model.GraduateEndings;
import com.ziyun.report.model.GraduateLabel;
import com.ziyun.report.model.Param;
import com.ziyun.report.model.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试毕业报告service
 */
@SpringBootTest(classes = ReportApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ReportServiceTest {
    @Autowired
    private IReportService reportService;
    @Autowired
    private ReportMapper reportMapper;

    /**
     * 学业特征 --个人学分绩点是否排在专业前二十名
     */
    @Test
    public void testavgCreditPointRanking() {
        String outid = "134555216";
        BigDecimal bigDecimal = reportService.avgCreditPointRanking(outid);
        System.out.println(bigDecimal);

    }

    /**
     * 毕业生列表
     */
    @Test
    public void testGetGraduate() {
        List<Student> studentList = reportMapper.getGraduate();
        System.out.println("该届毕业生共有："+studentList.size()+"人！");
    }

    /**
     * 查询pu积分
     */
    @Test
    public void testQueryPuIntegral(){
        Map<String,Object> puMap = reportService.queryPuIntegral("162219947");
        if(puMap!=null){
            System.out.println("PU积分:"+puMap.get("integration"));
            System.out.println("PU学分:"+puMap.get("credit"));
        }else {
            System.out.println("没有查到PU积分！");
        }
    }

    /**
     * 查询获奖次数
     */
    @Test
    public void testGetScholarshipNums(){
        int scholarshipNums = reportMapper.getScholarshipNums("134575888");
        System.out.println("获奖："+scholarshipNums+"次！");
    }

    /**
     * 查询借阅书数量
     */
    @Test
    public void testGetBorrowNums(){
        int scholarshipNums = reportMapper.getBorrowNums("134575888");
        System.out.println("借书："+scholarshipNums+"本！");
    }

    /**
     * 查询担任职位
     */
    @Test
    public void testQueryPosition(){
        List<String> positions = reportMapper.queryPosition("144577293");
        if(positions != null && positions.size() > 0) {
            StringBuffer stringBuffer = new StringBuffer("担任");
            positions.stream().forEach(p -> {
                stringBuffer.append(p).append("，");
            });
            System.out.println("担任职位：" + stringBuffer.toString() + "！");
        }else{
            System.out.println("没有担任任何职位！");
        }
    }


    /**
     * 查询上网时长排名百分比
     */
    @Test
    public void testOnlineDurationTop(){
        //Map<String,Object> params = new HashMap<>();
        //params.put("outid","1042801134");
        //params.put("grade","14");
        //params.put("majorCode","45734");
        /*Set<String> set = new HashSet<>();
        set.add("14457611");
        set.add("14455221");*/
        String[] set = new String[]{"1522198012","13455221","1522198011","09455221","14455222","11455222","06455011","08455221","1622198011","10455221","06455012","14455221","10455222","07455222","05455011","07455221","12455222","12455221","05455012","1622198012","11455221"};
        //params.put("classCodes",set);
        Param param = new Param();
        param.setOutid("1045522126");
        param.setGrade("14");
        param.setClassCodes(set);
        Integer ratio = reportService.onlineDurationTop(param);
        if(ratio != null)
            System.out.println("上网时长击败了:"+ratio+"%的人");
        else
            System.out.println("未查到上网排名！");
    }

    /**
     * 查询上网时长
     */
    @Test
    public void testOnlineDuration(){
        Long onlineDuration = reportMapper.onlineDuration("168111525233");
        if(onlineDuration != null) {
            System.out.println("onlineDuration:"+onlineDuration);
            System.out.println("上网总时长"+
                    onlineDuration==null?"0":new BigDecimal(onlineDuration).divide(new BigDecimal(3600),BigDecimal.ROUND_HALF_UP) +"小时");
        }else{
            System.out.println("没有上网时长！");
        }
    }

    /**
     * 查询宿舍内时间长排名百分比
     */
    @Test
    public void testDormDurationTop(){
        //TODO 数据问题，待完成校验（学生表outid和宿舍表outid不一致）
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("outid","1045522126");
        params.put("grade","14");
        params.put("majorCode","2219801");
        Integer dormRatio = reportMapper.dormDurationTop(params);
        if(dormRatio != null)
            System.out.println("宿舍时长超过"+ dormRatio +"%的同学");
        else
            System.out.println("未查到排名信息！");
    }

    /**
     * 查询借阅数量排名百分比
     */
    @Test
    public void testQueryBorrowTop(){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("outid","144556256");
        params.put("grade","14");
        params.put("majorCode","2219602");
        Integer borrowRatio = reportMapper.queryBorrowTop(params);
        if(borrowRatio != null)
            System.out.println("借阅数量超过"+ borrowRatio +"%的同学");
        else
            System.out.println("未查到借阅排名！");
    }

    /**
     * 查询处分次数
     */
    @Test
    public void testQueryPunishNums(){
        int punishNums = reportMapper.queryPunishNums("084551373");
        if(punishNums > 0)
            System.out.println("受过"+ punishNums +"次处分");
        else
            System.out.println("未查到处分记录!");
    }

    /**
     * 查询个人上网信息
     */
    @Test
    public void testQueryOnlineTime(){
        Map<String,Object> map = reportService.queryOnlineTime("1045522126");
        if(map != null && !"0".equals(map.get("day_num"))) {
            //总天数
            BigDecimal totalNum = new BigDecimal((Long) map.get("day_num"));
            //总上网时长（单位是 秒）转换成小时
            BigDecimal totalDuration = new BigDecimal((Long) map.get("total_duration")).divide(new BigDecimal(3600), 0, BigDecimal.ROUND_HALF_UP);
            BigDecimal avgDuration = totalDuration.divide(totalNum, 0, BigDecimal.ROUND_HALF_UP);
            System.out.println("日均上网："+avgDuration+"小时");
        }else {
            System.out.println("未查到处分记录!");
        }
    }

    /**
     * 根据积分查询积分排名
     */
    @Test
    public void testQueryPuIntegralTop(){
        Map<String,Object> map = reportService.queryPuIntegral("144556087");
        if (map != null && map.get("integration") != null && map.get("integration") != "") {
            BigDecimal puIntegral = new BigDecimal(Float.valueOf(map.get("integration").toString()));
            //根据积分查询积分排名
            Integer integrationRation = reportService.queryPuIntegralTop(Float.valueOf(map.get("integration").toString()));
            if(integrationRation != null){
                map.put("integrationRation",integrationRation);
            }
            map.forEach((k, v) -> System.out.println("key:value = " + k + ":" + v));
        }
    }

    /**
     * 详情展示 - 获奖情况
     */
    @Test
    public void testQueryPrizeInfo(){
        List<Map<String, Object>> mapList = reportService.queryPrizeInfo("134575888");
        mapList.stream().forEach(m -> {
            m.forEach((k,v) -> {
                System.out.println("key:value = " + k + ":" + v);
            });
        });
    }

    /**
     * 详情展示 - 学生会情况
     */
    @Test
    public void testGetAssociationOfStudent(){
        Map<String, Object> mapList = reportService.getAssociationOfStudent("144577486");
        mapList.forEach((k,v) -> {
            System.out.println("key:value = " + k + ":" + v);
        });
    }

    /**
     * 查询个人上网信息
     */
    @Test
    public void testGameDurationTop(){
        Map<String,Object> params = new HashMap<>();
        params.put("outid","1045522126");
        params.put("grade","14");
        String[] set = new String[]{"1522198012","13455221","1522198011","09455221","14455222","11455222","06455011","08455221","1622198011","10455221","06455012","14455221","10455222","07455222","05455011","07455221","12455222","12455221","05455012","1622198012","11455221"};
        params.put("classCodes",set);
        Param param = new Param();
        param.setOutid("1045522126");
        param.setGrade("14");
        param.setClassCodes(set);
        Integer gameRation = reportMapper.gameDurationTop(param);
        System.out.println("游戏击败了"+gameRation+"%的人！");
    }

    /**
     * 测试生成标签数据
     */
    @Test
    public void testGraduateLabelStat(){
        reportService.graduateLabelStat();
    }

    /**
     * 测试生成寄语数据
     */
    @Test
    public void testGraduateEndingsStat(){
        reportService.graduateEndingsStat();
    }

    /**
     * 测试添加标签数据
     */
    @Test
    public void testAddGraduateLabel(){
        GraduateLabel label = new GraduateLabel();
        label.setOutid("104282691");
        label.setLabel("与世无争");
        label.setDescribe("云淡风轻");
        label.setDetail("比起外面喧嚣的世界，你更愿意与自己共处");
        reportMapper.addGraduateLabel(label);
    }


    /**
     * 平均学分绩点排在前百分之二十
     */
    @Test
    public void testAvgCreditPointRanking() {
        String outid = "144556087";
        BigDecimal bigDecimal = reportMapper.avgCreditPointRanking(outid);
        System.out.println(bigDecimal);
    }

    /**
     * 测试生成标签数据
     */
    /*@Test
    public void testOnlineSaveRedisStat(){
        reportService.onlineSaveRedis();
    }*/

    /**
     * 平均学分绩点排在前百分之二十
     */
    @Test
    public void testHomePageView() {
        String outid = "144556087";
        reportService.visitsIncrease(outid);
        System.out.println("成功");
    }

    /**
     * 平均学分绩点排在前百分之二十
     */
    @Test
    public void testGetLabel() {
        String outid = "144556087";
        GraduateLabel label = reportService.queryGraduateLabel(outid);
        System.out.println(label.getDescribe());
    }

    @Test
    public void testGetEndings() {
        String outid = "144577486";
        GraduateEndings endings = reportService.queryGraduateEndings(outid);
        System.out.println(endings.getEndings());
    }
}
