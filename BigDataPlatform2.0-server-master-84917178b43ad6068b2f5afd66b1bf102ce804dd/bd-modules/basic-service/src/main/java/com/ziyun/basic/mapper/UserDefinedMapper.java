package com.ziyun.basic.mapper;

import com.ziyun.basic.entity.SysorgTree;
import com.ziyun.basic.vo.Params;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by admin on 2018/1/23.
 */
public interface UserDefinedMapper {
    List<SysorgTree> getOrgtree(String id);

    List<SysorgTree> getDataAuthTree(@Param("id") Long id, @Param("logonRoleId") Long logonRoleId, @Param("logonIsSuperAdmin") Integer logonIsSuperAdmin);

    List<String> getClasscodeByDefined(Params params);

    List<String> getClasscodeByStatus(Params params);

    List<String> getStudentStatus(Params params);

    Set<String> getClasscodeByMajor(Params params);

    List<String> getClasscodeBySemester(Integer semester);

    int getTotalSemester(String year);

    int getMaybeSemesterByYear(String enrollmentYear);

    int getYearBySemester(Integer termNum);

    List<String> getYearByMaxSemester(Integer termNum);

    int getMaxSemesterByClasscode(Params params);

    List<String> getOwnYearByClasscode(Params params);

    List<String> getStatusByClasscode(Params params);

    List<Map<String, Object>> getEnrollmentYear();
}
