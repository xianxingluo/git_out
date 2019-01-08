package com.ziyun.academic.server;


import com.ziyun.academic.vo.AcademicParams;
import com.ziyun.academic.vo.Params;

import java.util.List;
import java.util.Map;

public interface IAcademicServer {

    /**
     * 首页 学业特征
     *
     * @return CommResponse
     */
    Map<String, Object> getAcademicFeatures(AcademicParams params) throws Exception;

    /**
     * 按课程属性分组(必修，选修，必修&选修),统计各属性课程数量
     *
     * @param params params
     * @return CommResponse
     */
    List<Map<String, Object>> getCourseProperties(AcademicParams params) throws Exception;

    /**
     * 按课程号去重，列出所有课程的信息（课程号\课程名称\【课程属性】\选课人数\通过率\）
     * 通过率计算的是正常考试的通过率
     *
     * @param params params
     * @return CommResponse
     */
    List<Map<String, Object>> listCourseProperties(AcademicParams params) throws Exception;

    /**
     * 查询 课程属性分页总记录数
     *
     * @param params params
     * @return CommResponse
     */
    Integer getCoursePropertiesCount(AcademicParams params) throws Exception;

    /**
     * 按课程性质分组，统计各性质课程数量
     *
     * @param params params
     * @return CommResponse
     */
    List<Map<String, Object>> courseNatures(AcademicParams params) throws Exception;

    /**
     * 按课程号去重，列出所有课程的信息（课程号\课程名称\【课程性质】\选课人数\通过率\）
     * 通过率计算的是正常考试的通过率
     *
     * @param params params
     * @return List
     */
    List<Map<String, Object>> listCourseNatures(AcademicParams params) throws Exception;

    /**
     * 查询 课程性质分页总记录数
     *
     * @param params params
     * @return Integer
     */
    Integer getCourseNaturesCount(AcademicParams params) throws Exception;

    /**
     * 按课程分类分组（理论课、实验课等），统计各分类课程数量
     *
     * @param params params
     * @return CommResponse
     */
    List<Map<String, Object>> getCourseCategories(AcademicParams params) throws Exception;

    /**
     * 按课程号去重，列出所有课程的信息（课程号\课程名称\【课程分类】\选课人数\通过率\）
     * 通过率计算的是正常考试的通过率
     *
     * @param params params
     * @return List
     */
    List<Map<String, Object>> listCourseCategory(AcademicParams params) throws Exception;

    /**
     * 查询 课程分类分页总记录数
     *
     * @param params params
     * @return List
     */
    Integer getCourseCategoryCount(AcademicParams params) throws Exception;

    //学分绩点统计
    List<Map<String, Object>> listScorePoint(AcademicParams params) throws Exception;

    //成绩合格率
    Map<String, Object> getPassRatios(AcademicParams params) throws Exception;

    /**
     * 挂科统计  统计没有挂科的人数，挂1门人数...挂N门人数，
     *
     * @param params params
     * @return List
     */
    List<Map<String, Object>> listExamFailCount(AcademicParams params) throws Exception;

    /**
     * 考试通过情况，按学期统计总课程数，通过课程数，有挂科课程数（按课程号去重）
     *
     * @param params params
     * @return List
     */
    List<Map<String, Object>> listExamPassCourse(AcademicParams params) throws Exception;

    /**
     * 修学分情况 2017-11-08 ysx 新版
     *
     * @param params params
     * @return List
     */
    List<Map<String, Object>> listCreditSituation(AcademicParams params);

    /**
     * 毕业学分完成率
     * 毕业所需学分  * 人数 = 毕业总学分
     * 通过的总学分 / 毕业总学分 = 毕业学分完成率
     *
     * @param params params
     * @return Map
     */
    Map<String, Object> getGraduationCredit(AcademicParams params);

    //重修课程数量
    Map<String, Object> retakeCoursesTop(AcademicParams params);

    //重修的学生信息
    List<Map<String, Object>> retakeStudentMessage(AcademicParams params);

    /**
     * 列出每个学期重修课程前10的课程
     *
     * @param params params
     * @return List
     */
    List<Map<String, Object>> listRetakeCourses(AcademicParams params);

    //个人画像 - 学业特征
    Map<String, Object> getAcademicLabel(Params params);

    Map getTopCategory(AcademicParams params);

    //成绩提高分析  1、成绩
    //List<Map<String, Object>> getScore4Analysis(AcademicParams params);

    //成绩提高分析  2、选课
    // Map<String, Object> getElective4Analysis(AcademicParams params);

    //成绩提高分析  3、上网
    //  List<Map<String, Object>> getNetwork4Analysis(AcademicParams params);

    //成绩提高分析  4、借阅
//    List<Map<String, Object>> getBorrow4Analysis(AcademicParams params);
    // 成绩提高分析 4、借阅 人均借阅时长
//    List<Map<String, Object>> getBorrowTime4Analysis(AcademicParams params);

    //成绩提高分析  5、消费
    //  Map<String, Object> getConsume4Analysis(AcademicParams params);

    // 成绩提高分析 5、消费  按学期统计
    //  Map getConsume4AnalysisBySemester(AcademicParams params);

    /******************顶部条件查询*************************/
    List<String> getParentcode(Params params);

    List<Map> getChildrenList(Params params);

    List<Map> getTopCategoryHaveehcache(Params params);

    void paramTransformCodeOnlyClass(Params params);

    List<Map<String, Object>> failCourseStudentList(AcademicParams params);

    List<Map<String, Object>> courseCategoryStudentList(AcademicParams para);

    List<Map<String, Object>> scorePointList(AcademicParams para);

    List<Map<String, Object>> passFailCourseList(AcademicParams para);

    List<Map<String, Object>> pointTop(AcademicParams para);

    List<Map<String, Object>> electiveTop(AcademicParams para);


    List<Map<String, Object>> electiveTopStudentList(AcademicParams para);

    Map<String, Object> getStudentRanking(AcademicParams para);

    List<Map<String, Object>> completionCourseList(AcademicParams para);

    public Map<String, Object> getScoreCredit(AcademicParams params);

    List<Map<String, Object>> countryGrade(AcademicParams para);

    List<Map<String, Object>> countryGradeStudentList(AcademicParams para);

    List<Map<Object, Object>> organ(AcademicParams para);

    List<Map<Object, Object>> semesterTime(AcademicParams para);

    List<Map<String, Object>> attendanceCard(AcademicParams para);

    int attendanceCardSize(AcademicParams para);

    List<Map<String, Object>> classNums(AcademicParams para);

    List<Map<String, Object>> personAttendanceCard(AcademicParams para);

    int personAttendanceCardSize(AcademicParams para);

    List<Map<String, Object>> getCoursePropertiesStudent(AcademicParams para);

    List<Map<String, Object>> electiveTopCoursenoList(AcademicParams para);

    List<Map<String, Object>> electiveTopCoursenoSemester(AcademicParams para);

    List<Map<String, Object>> compulsoryCoursesList(AcademicParams para);

    int compulsoryCoursesCount(AcademicParams para);

    void getStudentClassRankin(AcademicParams para);

    List<Map<String, Object>> getStudentClassRankingSlideList(AcademicParams para);

    int getStudentClassRankingSlideSize(AcademicParams para);

    List<Map<String, Object>> getStudentYearCreditList(AcademicParams para);

    int getStudentYearCreditSize(AcademicParams para);

    List<Map<String, Object>> getStudentPulishList(AcademicParams para);

    int getStudentPulishSize(AcademicParams para);

    List<Map<String, Object>> getStudentEnglishScoreList(AcademicParams para);

    int getStudentEnglishScoreSize(AcademicParams para);
}
