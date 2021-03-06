package com.ziyun.net.mapper;

import com.ziyun.net.annotation.TargetDataSource;
import com.ziyun.net.vo.AcademicParams;
import com.ziyun.net.vo.Params;
import com.ziyun.net.vo.ParamsStatus;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 学生社群相关查询专用DAO
 *
 * @author 何亚鹏
 * @Date 2017年5月24日
 */
public interface StudentMapper {

    List<LinkedHashMap<String, Object>> getSourceLocation(Params para);

    List<LinkedHashMap<String, Object>> getBorrowhabits(Params para);

    List<LinkedHashMap<String, Object>> getConsume(Params para);

    List<LinkedHashMap<String, Object>> getMajor();

    /**
     * 个人查询条件页面：显示学生信息总条数
     *
     * @param para
     * @return
     */
    Map<String, Object> countStudent(Params para);

    /**
     * 根据参数：分页查询：学生信息：
     *
     * @return
     */
    @TargetDataSource("mysql")
    List<LinkedHashMap<String, Object>> getPageStudent(Params para);

    List<LinkedHashMap<String, Object>> getArchive(Params para);

    List<LinkedHashMap<String, Object>> getOrgTree();

    /**
     * 学生归类信息
     *
     * @param para
     * @return
     */
    @TargetDataSource("mysql")
    List<LinkedHashMap<String, Object>> getStatus(ParamsStatus para);

    /**
     * 借阅归类信息
     *
     * @param para
     * @return
     */
    List<LinkedHashMap<String, Object>> getBorrow(ParamsStatus para);

    /**
     * 惩罚信息
     *
     * @param para
     * @return
     */
    List<LinkedHashMap<String, Object>> getPunish(ParamsStatus para);

    /**
     * 奖励
     *
     * @param para
     * @return
     */
    List<LinkedHashMap<String, Object>> getScholarship(ParamsStatus para);

    /**
     * 出生年月占比
     *
     * @param para
     * @return
     */
    List<LinkedHashMap<String, Object>> countByBrithdry(ParamsStatus para);

    /**
     * 优等生列表
     *
     * @param params
     * @return
     */
    List<LinkedHashMap<String, Object>> scholarshipPageList(ParamsStatus params);

    //获奖次数，根据获奖次数来分别统计男女人数。比如获奖一次的男多少女多少
    List<Map<String, Object>> getScholarshipCountBysex(ParamsStatus params);

    //获奖类型，根据获奖类型来统计次数
    List<Map<String, Object>> getScholarshipTypeCount(ParamsStatus params);

    /**
     * 惩罚列表
     *
     * @param params
     * @return
     */
    List<LinkedHashMap<String, Object>> punishPageList(ParamsStatus params);

    Long scholarshipPageListLength(ParamsStatus para);

    Long punishPageListLength(ParamsStatus para);

    List<Map<String, Object>> getPunishCount(ParamsStatus para);

    /**
     * 根据sql查询 上网时长
     *
     * @param params
     * @return
     */
    List<LinkedHashMap<String, Object>> radacctTime(ParamsStatus params);

    /**
     * 上网高峰时段
     *
     * @param params
     * @return
     */
    List<LinkedHashMap<String, Object>> hotTime(ParamsStatus params);

    /**
     * 来源省份根据班级汇总导出
     *
     * @param params
     * @return
     */
    List<Map<String, Object>> proviceFrom(Params params);

    /**
     * 性别统计
     *
     * @param para
     * @return
     */
    List<Map<String, Object>> genderStatistics(Params para);

    /**
     * 男女比例按学期统计
     *
     * @param para
     * @return
     */
    List<Map<String, Object>> getCountBySex(Params para);

    /**
     * 年龄分布
     *
     * @param para
     * @return
     */
    List<Map<String, Object>> birthdayDistribution(Params para);

    /**
     * 获取当前学期，上学期，上学年的开始时间，结束时间
     *
     * @return
     */

    List<LinkedHashMap> getTermDate();

    List<Map<String, Object>> getgetScholarshipStudentList(ParamsStatus paramsStatus);

    List<Map<String, Object>> getPunishStudentList(ParamsStatus param);

    List<Map<String, Object>> getScholarshipTypeStudentList(ParamsStatus param);

    String getEnrollmentYearById(AcademicParams para);

    List<Map<String, Object>> getStudentDetails(Params param);
}
