package com.ziyun.academic.dao;


import com.ziyun.academic.annotation.TargetDataSource;
import com.ziyun.academic.vo.AcademicParams;

import java.util.List;
import java.util.Map;

/**
 * 课程dao，查询kylin
 */
public interface EduGradeKylinDao {
    @TargetDataSource("kylin")
    List<Map<String, Object>> attendanceCard(AcademicParams para);

    @TargetDataSource("kylin")
    int attendanceCardSize(AcademicParams para);

    @TargetDataSource("kylin")
    List<Map<String, Object>> classNums(AcademicParams para);

    @TargetDataSource("kylin")
    List<Map<String, Object>> personAttendanceCard(AcademicParams para);

    @TargetDataSource("kylin")
    int personAttendanceCardSize(AcademicParams para);
}
