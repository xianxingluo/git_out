package com.ziyun.report.service;

import com.ziyun.report.model.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IReportService {
    List<Map<String, Object>> getAcademic(String outid);

    List<Map<String, Object>> getConsume(String outid);
    /**
     * 统计生成标签数据
     */
    void graduateLabelStat();

    /**
     * 统计生成结尾寄语
     */
    void graduateEndingsStat();

    BigDecimal avgCreditPointRanking(String outid);

    List<Map<String, Object>> getAcademicCourse(String outid);

    String getPassRations(String outid);

    Map<String, Object> getConsumeDeatails(String outid);

    Map<String, Object> getBorrowDetails(String outid);

    List<Map<String, Object>> preferenceListNot(String outid);

    List<Map<String, Object>> getHighScore(String outid);

    String getBestTimesClass(String outid);

    /**
     * 获取应届毕业生列表
     * @return
     */
    List<Student> getGraduate();

    /**
     * 根据学号查询学生信息
     * @param outid
     * @return
     */
    Student queryStudentByoutid(String outid);

    /**
     * 查询PU积分
     * @param outid
     * @return
     */
    Map<String,Object> queryPuIntegral(String outid);

    /**
     * 查询上网时长排名百分比
     * @param params
     * @return
     */
    Integer onlineDurationTop(Param params);

    /**
     * 根据学号查询学生标签
     * @param outid
     * @return
     */
    GraduateLabel queryGraduateLabel(String outid);

    /**
     * 根据学号查询上网时长和上网天数
     * @param outid
     * @return
     */
    Map<String,Object> queryOnlineTime(String outid);

    /**
     * 根据学号查询获奖情况
     * @param outid
     * @return
     */
    List<Map<String,Object>> queryPrizeInfo(String outid);

    /**
     * 根据学号查询学生寄语
     * @param outid
     * @return
     */
    GraduateEndings queryGraduateEndings(String outid);

    /**
     * 根据积分查询积分排名
     * @param integration
     * @return
     */
    Integer queryPuIntegralTop(Float integration);

    /**
     * 根据专业code查询班级code
     * @param majorCode
     * @return
     */
    Set<String> queryClassByMajor(String majorCode);

    Map<String, Object> onlineOfStudent(Student student);

    /**
     * 获取学生上网数据
     *
     * @param outid
     * @return
     */
    Map<String, Object> getOnlineOfStudent(String outid);

    /**
     * 获取学生活动数据
     *
     * @param outid
     * @return
     */
    Map<String, Object> getActivityOfStudent(String outid);

    /**
     * 获取学生获奖数据
     *
     * @param outid
     * @return
     */
    Map<String, Object> getScholarshipOfStudent(String outid);

    /**
     * 获取学生学业数据
     *
     * @param outid
     * @return
     */
    Map<String, Object> getAcademicOfStudent(String outid);

    /**
     * 获取学生消费数据
     *
     * @param outid
     * @return
     */
    Map<String, Object> getConsumeOfStudent(String outid);

    Object getStudentSchoolType(Param param);

    Map<String, Integer> isGraduationStudent(String outid);
    /**
     * 访问量增加
     * @param outid
     */
    GraduateVisits visitsIncrease(String outid);

    /**
     * 根据学号查询毕业报告访问次数
     * @param outid
     */
    GraduateVisits queryVisitsByOutid(String outid);

    /**
     * 获取学生会会长信息
     *
     * @param outid
     * @return
     */
    Map<String, Object> getAssociationOfStudent(String outid);
}
