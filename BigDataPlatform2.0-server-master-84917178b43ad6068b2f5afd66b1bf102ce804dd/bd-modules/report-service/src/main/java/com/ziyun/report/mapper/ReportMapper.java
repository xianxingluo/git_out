package com.ziyun.report.mapper;

import com.ziyun.report.annotation.TargetDataSource;
import com.ziyun.report.model.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 毕业报告Mapper,查muysql
 */
public interface ReportMapper {
    /**
     * 根据学号，查询专业排名是否排在前百分只二十
     *
     * @param outid
     * @return
     */
    @TargetDataSource("mysql")
    BigDecimal avgCreditPointRanking(String outid);

    /**
     * 根据学号，查询获奖次数
     * @param outid
     * @return
     */
    @TargetDataSource("mysql")
    int getScholarshipNums(String outid);

    /**
     * 根据学号，查询借阅书的数量
     *
     * @param outid 学号
     * @return
     */
    @TargetDataSource("mysql")
    int getBorrowNums(String outid);

    @TargetDataSource("mysql")
    List<Map<String, Object>> getAcademicCourse(String outid);

    @TargetDataSource("mysql")
    String getPassRations(String outid);

    /**
     * 查询毕业生，在校消费的总金额
     */
    @TargetDataSource("kylin")
    BigDecimal getTotalConsumes(String outid);

    /**
     * 查询个人经常去那个窗口消费，消费了多少次
     *
     * @param outid
     * @return
     */
    @TargetDataSource("kylin")
    List<Map<String, Object>> consumeHobby(String outid);

    @TargetDataSource("mysql")
    List<Map<String, Object>> getBorrowDaysAndBookNums(String outid);

    @TargetDataSource("mysql")
    Map getBestLikeBook(String outid);

    @TargetDataSource("mysql")
    String bookLikeType(String outid);

    @TargetDataSource("kylin")
    List<Map<String, Object>> preferenceListNot(String outid);

    @TargetDataSource("mysql")
    String getConsumeTypeByAcccode(String acccode);

    @TargetDataSource("mysql")
    List<Map<String, Object>> getHighScore(String outid);



    @TargetDataSource("kylin")
    String getBestTimesClass(String outid);

    /**
     * 获取应届毕业生列表
     * @return
     */
    @TargetDataSource("mysql")
    List<Student> getGraduate();

    /**
     * 根据学号查询学生信息
     * @return
     */
    @TargetDataSource("mysql")
    Student queryStudentByoutid(String outid);

    /**
     * 查询PU积分
     * @param outid
     * @return
     */
    @TargetDataSource("mysql")
    Map<String,Object> queryPuIntegral(String outid);

    /**
     * 查询上网时长
     * @param outid
     * @return
     */
    @TargetDataSource("kylin")
    Long onlineDuration(String outid);

    /**
     * 查询上网时长排名
     * @param params
     * @return
     */
    @TargetDataSource("kylin")
    Integer onlineDurationTop(Param params);

    /**
     * 根据学号查询学生标签
     * @param outid
     * @return
     */
    @TargetDataSource("mysql")
    GraduateLabel queryGraduateLabel(String outid);

    /**
     * 根据学号查询上网时长和上网天数
     * @param outid
     * @return
     */
    @TargetDataSource("kylin")
    Map<String,Object> queryOnlineTime(String outid);

    /**
     * 根据学号查询获奖情况
     * @param outid
     * @return
     */
    @TargetDataSource("mysql")
    List<Map<String,Object>> queryPrizeInfo(String outid);

    /**
     * 增加毕业生标签
     * @param graduateLabel
     */
    void addGraduateLabel(GraduateLabel graduateLabel);

    /**
     * 根据学号查询学生寄语
     * @param outid
     * @return
     */
    @TargetDataSource("mysql")
    GraduateEndings queryGraduateEndings(String outid);

    /**
     * 根据学号查询借阅百分比排名
     * @param params
     * @return
     */
    @TargetDataSource("mysql")
    Integer queryBorrowTop(Map<String, Object> params);

    /**
     * 根据学号查询处分次数
     * @param outid
     * @return
     */
    @TargetDataSource("mysql")
    int queryPunishNums(String outid);

    /**
     * 增加结尾寄语
     * @param endings
     */
    void addGraduateEndings(GraduateEndings endings);

    /**
     * 根据学号查询担任职位
     * @param outid
     * @return
     */
    List<String> queryPosition(String outid);

    /**
     * 根据学号查询在宿舍内时长百分比排名
     * @param params
     * @return
     */
    @TargetDataSource("mysql")
    Integer dormDurationTop(Map<String, Object> params);

    /**
     * 根据积分查询积分排名
     * @param integration
     * @return
     */
    @TargetDataSource("mysql")
    Integer queryPuIntegralTop(Float integration);

    /**
     * 查询上网时长
     * @param outid
     * @return
     */
    @TargetDataSource("kylin")
    Long gameDuration(String outid);
    /**
     * 查询游戏时长排名
     * @param params
     * @return
     */
    @TargetDataSource("kylin")
    Integer gameDurationTop(Param params);

    /**
     * 根据专业code查询班级code
     * @param majorCode
     * @return
     */
    @TargetDataSource("mysql")
    Set<String> queryClassByMajor(@org.apache.ibatis.annotations.Param(value="majorCode") String majorCode);

    /**
     * 获取学业信息
     *
     * @param outid
     * @return
     */
    @TargetDataSource("mysql")
    List<Map<String, Object>> getAcademic(String outid);

    List<Map<String, Object>> timeChangeListOne(String outid);

    @TargetDataSource("kylin")
    Map<String, Object> getLikeRestaurant(String outid);

    @TargetDataSource("mysql")
    Object getStudentSchoolType(String outid);

    String isGraduationStudent(String outid);

    /**
     * 新增毕业报告访问次数记录
     * @param graduateVisits
     */
    void addGraduateVisits(GraduateVisits graduateVisits);

    /**
     * 访问量增加
     * @param graduateVisits
     */
    void visitsIncrease(GraduateVisits graduateVisits);

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
