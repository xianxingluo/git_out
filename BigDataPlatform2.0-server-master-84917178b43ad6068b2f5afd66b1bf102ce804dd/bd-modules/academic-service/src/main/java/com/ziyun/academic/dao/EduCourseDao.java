package com.ziyun.academic.dao;


import com.ziyun.academic.vo.AcademicParams;
import com.ziyun.academic.vo.Params;

import java.util.List;
import java.util.Map;

public interface EduCourseDao {

    /**
     * 首页 学业特征-选课人次
     */
    Map<String, Object> getCoursePeopleTimes(AcademicParams params);

    //重修课程数量
    List<Map<String, Object>> retakeCoursesTop(AcademicParams params);

    /**
     * 重修课程导出
     */
    List<Map<String, Object>> listRetakeCourses(AcademicParams params);

    List<Map<String, Object>> getRetakeStudentList(AcademicParams params);

    /**
     * 查询有重修课程的学期
     *
     * @param params
     * @return
     */
    List<Integer> listTerms(AcademicParams params);


    //成绩提高分析  2、选课
    List<Map<String, Object>> getElective4Analysis(Params params);

    List<Map<String, Object>> getElectiveCategory4Analysis(Params params);

}