package com.ziyun.academic.server;


import com.ziyun.academic.vo.AcademicParams;
import com.ziyun.academic.vo.Params;
import com.ziyun.utils.requests.CommResponse;

import java.util.List;
import java.util.Map;

/**
 * @author ysx
 * @author sxj
 * @history
 * @since 2017/12/07
 */
public interface IEarlyWarningServer {

    /**
     * @return 学业预警选择的条件类目
     */
    List<Map> getEarlyWarnParamShow(String token);

    List<Map> getAcademicEarlyWarnRules(Map<String, Object> map);

    List<Map> getEarlyWarnRules(Map<String, Object> map);

    CommResponse addAcademicWarnRule(Map map);

    CommResponse addWarnRule(Map map);

    CommResponse delAcademicWarnRules(Map<String, Object> map);

    CommResponse delWarnRules(Map<String, Object> map);

    void updateAcademicWarnRules(Map<String, Object> map);

    void updateWarnRules(Map<String, Object> map);


    /**
     * 查询符合预警规则的学生
     */
    List<Map<String, Object>> listAcademicWarningStudents(Params params);

    List<Map<String, Object>> listWarningStudents(Params params);

    /**
     * 查询符合预警规则的学生的总数
     */
    Integer countAcademicWarningStudents(Params params);


    Integer countWarningStudents(Params params);

    String earlyNameIsExist(Map map);

    List<Map<String, Object>> getTermTime(Map map);

    List<Map<String, Object>> doubtfulLevelSchoolList(AcademicParams params);

    int doubtfulLevelSchoolSize(AcademicParams params);

    List<Map<String, Object>> getConsumeIncrease(AcademicParams params);

    int getConsumeIncreaseSize(AcademicParams params);

    List<Map<String, Object>> getRadacctIncrease(AcademicParams params);

    int getRadacctIncreaseSize(AcademicParams params);

    List<Map<String, String>> getEarlyList(AcademicParams params);

    List<Map<String, Object>> toEditPage(AcademicParams params);

    int saveCustomEarly(AcademicParams params);

    int editEnableStatus(AcademicParams params);

    List<Map<String, Object>> getTargetStudent(AcademicParams params);

    long targetStudentCount(AcademicParams params);

    int deleteEarlyWarn(AcademicParams params);

    List<Map<String, Object>> getEarlyLevelNum(AcademicParams params);

    int getEarlyListCount(AcademicParams params);
}
