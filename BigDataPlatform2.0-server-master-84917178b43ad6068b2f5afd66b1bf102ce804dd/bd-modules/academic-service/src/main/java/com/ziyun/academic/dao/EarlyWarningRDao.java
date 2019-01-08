package com.ziyun.academic.dao;


import com.ziyun.academic.entity.EarlyParam;
import com.ziyun.academic.vo.AcademicParams;
import com.ziyun.academic.vo.NetParams;
import com.ziyun.academic.vo.Params;
import com.ziyun.academic.vo.ParamsStatus;

import java.util.List;
import java.util.Map;

public interface EarlyWarningRDao {

    /**
     * 查询符合预警规则的学生
     * @param params 预警规则
     * @return List
     */
    List<Map<String, Object>> listAcademicWarningStudents(Params params);

    List<Map<String, Object>> listWarningStudents(Params params);

    Integer countAcademicWarningStudents(Params params);


    Integer countWarningStudents(Params params);

    /**
     * 根据id查询预警规则
     * @param id 预警规则id
     * @return String
     */
    Map<String, Object> getAcademicWarnRule(String id);

    String getWarnRule(String id);

    /*与前台交互部分*/
    List<Map> getEarlyWarnParamShow();

    List<Map> getAcademicEarlyWarnRules(Map map);

    List<Map> getEarlyWarnRules(Map map);

    int addAcademicWarnRule(Map map);
    int addWarnRule(Map map);

    int delAcademicWarnRules(ParamsStatus param);


    int delWarnRules(ParamsStatus param);


    /**
     * 用于学业预警
     * 1. 消费金额
     * 2. 上网时长
     * @param para
     * @return
     */
    Double selectTotalResult(NetParams para);

    void updateWarnRules(Map<String, Object> map);

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

    void saveEarlyRule(AcademicParams params);

    void saveEarlyParams(EarlyParam r);

    void editEnableStatus(AcademicParams params);

    List<Map<String, String>> findWarnRule(Long id);

    List<Map<String, Object>> getTargetStudent(AcademicParams params);

    long targetStudentCount(AcademicParams params);

    int deleteEarlyWarn(AcademicParams params);

    String getWarnParamByWarnID(String warnId);

    void deleteEarlyParam(AcademicParams params);

    List<Map<String, Object>> getEarlyLevelNum(AcademicParams params);

    int getEarlyListCount(AcademicParams params);
}
