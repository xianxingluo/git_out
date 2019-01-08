package com.ziyun.academic.dao;


import com.ziyun.academic.vo.AcademicParams;
import com.ziyun.academic.vo.Params;

import java.util.List;
import java.util.Map;

public interface EduGradeDao {

    /**
     * 首页 -总的平均学分绩点
     */
    Map<String, Object> getAverageScorePoint(Params params);

    /**
     * 首页 - 学业特征 - 重修人数
     */
    Map<String, Object> getRetakeCourseNum(Params params);

    /**
     * 按课程属性分组(必修，选修，必修&选修),统计各属性课程数量
     */
    List<Map<String, Object>> getCourseProperties(Params params);

    /**
     * 按课程号去重，列出所有课程的信息（课程号\课程名称\【课程属性】\选课人数\通过率\）
     * 通过率计算的是正常考试的通过率
     */
    List<Map<String, Object>> listCourseProperties(Params params);

    /**
     * 查询 课程属性分页总记录数
     */
    Integer getCoursePropertiesCount(Params params);

    /**
     * 按课程分类分组（理论课、实验课等），统计各分类课程数量
     */
    List<Map<String, Object>> getCourseCategory(Params params);

    /**
     * 按课程号去重，列出所有课程的信息（课程号\课程名称\【课程分类】\选课人数\通过率\）
     * 通过率计算的是正常考试的通过率
     */
    List<Map<String, Object>> listCourseCategory(Params params);

    Integer getCourseCategoryCount(Params params);

    /**
     * 按课程性质分组，统计各性质课程数量
     */
    List<Map<String, Object>> getCourseNatures(Params params);

    /**
     * 按课程号去重，列出所有课程的信息（课程号\课程名称\【课程性质】\选课人数\通过率\）
     * 通过率计算的是正常考试的通过率
     */
    List<Map<String, Object>> listCourseNatures(Params params);

    /**
     * 查询 课程性质分页总记录数
     */
    Integer getCourseNaturesCount(Params params);

    //学分绩点统计
    List<Map<String, Object>> listScorePoint(Params params);

    /**
     * 成绩合格率
     */
    Map<String, Object> getPassRatios(Params params);

    /**
     * 挂科统计  统计没有挂科的人数，挂1门人数...挂N门人数，
     */
    List<Map<String, Object>> listExamFailCount(Params params);

    /**
     * 考试通过情况，按学期统计总课程数，通过课程数，有挂科课程数（按课程号去重）
     */
    List<Map<String, Object>> listExamPassCourse(Params params);

    //修学分情况 2017-11-08 ysx 新版
    List<Map<String, Object>> listCreditSituation(Params params);

    List<Map<String, Object>> getRank(Params params);

    /**
     * 毕业学分完成率
     * 毕业所需学分  * 人数 = 毕业总学分
     * 通过的总学分 / 毕业总学分 = 毕业学分完成率
     */
    Map<String, Object> getGraduationCredit(Params params);

    /**
     * 个人画像-学业特征-课程数量
     */
    Map<String, Object> getCourseNum(Params params);

    /**
     * 个人画像-学业特征-重修课程数量
     */
    Map<String, Object> getRetakeCourse(Params params);

    List<Map<String, Object>> failCourseStudentList(Params params);

    List<Map<String, Object>> courseCategoryStudentList(AcademicParams para);

    List<Map<String, Object>> scorePointList(AcademicParams para);

    // List<Map<String, Object>> passFailCourseList(AcademicParams para);

    List<Map<String, Object>> pointTop(AcademicParams para);

    List<Map<String, Object>> electiveTop(AcademicParams para);

    List<Map<String, Object>> gradeExam(AcademicParams para);

    List<Map<String, Object>> electiveTopStudentList(AcademicParams para);

    List<Map<String, Object>> completionCourseList(AcademicParams para);

    List<Map> getStudentRanking(Params params);

    List<Map> getStudentAvgCreditPoint(Params params);

    //成绩学分情况
    List<Map<String, Object>> getScoreCredit(AcademicParams params);

    List<Map<String, Object>> failCourseList(AcademicParams para);

    List<Map<String, Object>> passCourseList(AcademicParams para);

    List<Map<String, Object>> countryGrade(AcademicParams para);

    List<Map<String, Object>> countryGradeStudentList(AcademicParams para);

    List<Map<Object, Object>> organOne(Params params);

    List<Map<Object, Object>> organTwo(Params params);

    List<Map<Object, Object>> semesterTime(AcademicParams para);

    List<Map<String, Object>> getCoursePropertiesStudent(AcademicParams para);

    List<Map<String, Object>> electiveTopCoursenoList(AcademicParams para);

    List<Map<String, Object>> electiveTopCoursenoSemester(AcademicParams para);

    List<Map<String, Object>> compulsoryCoursesList(AcademicParams para);

    int compulsoryCoursesCount(AcademicParams para);

    List<Map<String, Object>> getClassRanking(String s);

    void updateStudentClassRaningChange(Map paramMap);

    List<Map<String, Object>> getStudentClassRankingSlideList(AcademicParams para);

    int getStudentClassRankingSlideSize(AcademicParams para);

    List<Map<String, Object>> getStudentYearCreditList(AcademicParams para);

    int getStudentYearCreditSize(AcademicParams para);

    List<Map<String, Object>> getStudentPulishList(AcademicParams para);

    int getStudentPulishSize(AcademicParams para);

    List<Map<String, Object>> getStudentEnglishScoreList(AcademicParams para);

    int getStudentEnglishScoreSize(AcademicParams para);


    List<String> getAllSemester();

    List<Map<String, Object>> noFailCourseStudentList(Params params);


    Map<String, Object> noExamFailCount(AcademicParams params);
}
