package com.ziyun.academic.server.impl;


import com.ziyun.academic.dao.EarlyWarningRDao;
import com.ziyun.academic.entity.EarlyParam;
import com.ziyun.academic.enums.AcademicEarlyEnum;
import com.ziyun.academic.server.IEarlyWarningServer;
import com.ziyun.academic.tools.ParamUtils;
import com.ziyun.academic.vo.AcademicParams;
import com.ziyun.academic.vo.Params;
import com.ziyun.academic.vo.ParamsStatus;
import com.ziyun.utils.cache.TokenCasheManager;
import com.ziyun.utils.requests.CommResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ysx
 * @author sxj
 * @history
 * @since 2017/12/07
 */
@Service
public class EarlyWarningServerImpl implements IEarlyWarningServer {

    @Autowired
    private EarlyWarningRDao earlyWarningRDao;


    @Override
    public List<Map> getEarlyWarnParamShow(String token) {
        List<Map> list = earlyWarningRDao.getEarlyWarnParamShow();
        Set<String> menus = TokenCasheManager.getMenusCache(token);
        for (int i = list.size() - 1; i >= 0; i--) {
            Map map = list.get(i);
            String menu = "warning_" + String.valueOf(map.get("id"));
            if (!menus.contains(menu)) {
                list.remove(i);
            }
        }
        return list;
    }

    @Override
    public List<Map> getAcademicEarlyWarnRules(Map<String, Object> map) {
        map = ParamUtils.getUserByTokenId(map);
        if (map == null) {
            return null;
        }
        List<Map> ruleList = earlyWarningRDao.getAcademicEarlyWarnRules(map);
        return ruleList;
    }

    @Override
    public List<Map> getEarlyWarnRules(Map<String, Object> map) {
        map = ParamUtils.getUserByTokenName(map);
        if (map == null) {
            return null;
        }
        List<Map> ruleList = earlyWarningRDao.getEarlyWarnRules(map);
        return ruleList;
    }

    @Override
    public CommResponse addAcademicWarnRule(Map map) {
        map = ParamUtils.getUserByTokenId(map);
        if (map == null) {
            return CommResponse.failure();
        }
        int flag = earlyWarningRDao.addAcademicWarnRule(map);
        return flag == 1 ? CommResponse.success() : CommResponse.failure();
    }

    @Override
    public CommResponse addWarnRule(Map map) {
        map = ParamUtils.getUserByTokenName(map);
        if (map == null) {
            return CommResponse.failure();
        }
        int flag = earlyWarningRDao.addWarnRule(map);
        return flag == 1 ? CommResponse.success() : CommResponse.failure();
    }

    @Override
    public CommResponse delAcademicWarnRules(Map<String, Object> map) {
        //由于前段只做单次删除操作。将id转换成数组进行批量删除
        Integer id = Integer.valueOf(String.valueOf(map.get("id")));
        if (id == null) {
            return CommResponse.failure();
        }
        ParamsStatus param = new ParamsStatus();
        param.setId(new int[]{id});
        map = ParamUtils.getUserByTokenId(map);
        if (map == null) {
            return CommResponse.failure();
        }
        param.setBase(String.valueOf(map.get("userId")));
        int flag = earlyWarningRDao.delAcademicWarnRules(param);
        return flag == 1 ? CommResponse.success() : CommResponse.failure();
    }

    @Override
    public CommResponse delWarnRules(Map<String, Object> map) {
        //由于前段只做单次删除操作。将id转换成数组进行批量删除
        Integer id = Integer.valueOf(String.valueOf(map.get("id")));
        if (id == null) {
            return CommResponse.failure();
        }
        ParamsStatus param = new ParamsStatus();
        param.setId(new int[]{id});
        map = ParamUtils.getUserByTokenName(map);
        if (map == null) {
            return CommResponse.failure();
        }
        param.setBase(String.valueOf(map.get("username")));
        int flag = earlyWarningRDao.delWarnRules(param);
        return flag == 1 ? CommResponse.success() : CommResponse.failure();
    }

    @Override
    public void updateAcademicWarnRules(Map<String, Object> map) {
        earlyWarningRDao.updateWarnRules(map);
    }

    @Override
    public void updateWarnRules(Map<String, Object> map) {
        delWarnRules(map);
        addWarnRule(map);
    }

    @Override
    public List<Map<String, Object>> listAcademicWarningStudents(Params params) {

        params.setWarnRule(getAcademicWarnRule(params.getWarnRuleId()));

        return earlyWarningRDao.listAcademicWarningStudents(params);
    }

    @Override
    public List<Map<String, Object>> listWarningStudents(Params params) {

        params.setWarnRule(getWarnRule(params.getWarnRuleId()));

        return earlyWarningRDao.listWarningStudents(params);
    }

    @Override
    public Integer countAcademicWarningStudents(Params params) {

        params.setWarnRule(getAcademicWarnRule(params.getWarnRuleId()));

        return earlyWarningRDao.countAcademicWarningStudents(params);
    }

    @Override
    public Integer countWarningStudents(Params params) {

        params.setWarnRule(getWarnRule(params.getWarnRuleId()));

        return earlyWarningRDao.countWarningStudents(params);
    }

    /**
     * 根据预警规则id查询预警规则，并拼装成查询条件字符串
     */
    public String getAcademicWarnRule(String warnRuleId) {

        Map<String, Object> map = earlyWarningRDao.getAcademicWarnRule(warnRuleId);
        if (map == null || map.isEmpty() || map.size() == 1) {
            return null;
        }
        String warnDetail = (String) map.get("warn_rule");
        //由规则拼成的查询条件字符串
        String separator = (Integer) map.get("screen_rule") == 1 ? " and " : " or ";
        StringBuilder rules = new StringBuilder();
        if (StringUtils.isNotBlank(warnDetail)) {
            String[] split = warnDetail.split(",");
            List<String> list = Arrays.asList(split);
            for (int i = 0; i < list.size(); i++) {
                String name = "";
                switch (i) {
                    case 0:
                    case 1:
                        name = AcademicEarlyEnum.TERM_CREDIT.getEarlyType();
                        break;
                    case 2:
                    case 3:
                        name = AcademicEarlyEnum.AVG_CREDIT_POINT.getEarlyType();
                        break;
                    case 4:
                    case 5:
                        name = AcademicEarlyEnum.CLASS_RANKING_CHANGE.getEarlyType();
                        break;
                    case 6:
                    case 7:
                        name = AcademicEarlyEnum.FAIL_COURSE_NUM.getEarlyType();
                        break;
                    case 8:
                    case 9:
                        name = AcademicEarlyEnum.FAIL_REQUIREDCOURSE_SUM.getEarlyType();
                        break;
                    case 10:
                    case 11:
                        name = AcademicEarlyEnum.FAIL_ELECTIVECOURSE_SUM.getEarlyType();
                        break;
                    case 12:
                    case 13:
                        name = AcademicEarlyEnum.REBUILD_COURSE_SUM.getEarlyType();
                        break;
                    case 14:
                    case 15:
                        name = AcademicEarlyEnum.CET4SCORE.getEarlyType();
                        break;
                    case 16:
                    case 17:
                        name = AcademicEarlyEnum.GRADE_EXAMINATION_TIME.getEarlyType();
                        break;
                    case 18:
                    case 19:
                        name = AcademicEarlyEnum.PASS_GRADEEXAMINATION_SUM.getEarlyType();
                        break;
                    default:
                        break;
                }
                if ("".equals(name)) {
                    continue;
                }
                if (i % 2 == 0 && StringUtils.isNotBlank(list.get(i))) {
                    rules.append(separator);
                    if (i != (list.size() - 1) && StringUtils.isNotBlank(list.get(i + 1))) {
                        rules.append(" ( ");
                    }
                    rules.append(name + " >= " + list.get(i));
                }
                if (i % 2 != 0 && StringUtils.isNotBlank(list.get(i))) {
                    String tmp = separator;
                    String end = "";
                    if (StringUtils.isNotBlank(list.get(i - 1))) {
                        tmp = " and ";
                        end = " ) ";
                    }
                    rules.append(tmp + " " + name + " <= " + list.get(i) + end);
                }

            }
        }
        return "( " + rules.toString().replaceFirst(separator, "") + " ) and";

    }

    /**
     * 校验当前用户所创建预警是否已经存在
     *
     * @param map
     * @return
     */
    @Override
    public String earlyNameIsExist(Map map) {
        map = ParamUtils.getUserByTokenId(map);
        return earlyWarningRDao.earlyNameIsExist(map);
    }


    /**
     * 根据预警规则id查询预警规则，并拼装成查询条件字符串
     */
    public String getWarnRule(String warnRuleId) {

        String warnRule = earlyWarningRDao.getWarnRule(warnRuleId);

        if (StringUtils.isNotBlank(warnRule)) {
            //把规则中的中文替换成数据库中的字段
            warnRule = warnRule.replace("挂科数目", "fail_course_sum")
                    .replace("选课数目", "course_sum")
                    .replace("平均学分绩点", "avg_credit_point")
                    .replace("消费金额", "spend_sum")
                    .replace("上网时长", "net_time_sum")
                    .replace("宿舍时长", "dorm_time_sum")
                    .replace("≥", ">=")
                    .replace("≤", "<=");
            //规则数组
            String[] ruleArray = warnRule.split(",");
            //由规则拼成的查询条件字符串
            String queryStr = " and " + StringUtils.join(ruleArray, " and ");

            return queryStr;
        } else {
            return "";
        }
    }

    /**
     * 获取学期的开始，结束时间
     *
     * @param map
     * @return
     */
    @Override
    public List<Map<String, Object>> getTermTime(Map map) {
        return earlyWarningRDao.getTermTime(map);
    }

    /**
     * 疑是离校学生列表
     *
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> doubtfulLevelSchoolList(AcademicParams params) {
        return earlyWarningRDao.doubtfulLevelSchoolList(params);
    }

    /**
     * 疑是离校学生列表长度
     *
     * @param params
     * @return
     */
    @Override
    public int doubtfulLevelSchoolSize(AcademicParams params) {
        return earlyWarningRDao.doubtfulLevelSchoolSize(params);
    }

    /**
     * 常规预警:消费激增
     *
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> getConsumeIncrease(AcademicParams params) {
        return earlyWarningRDao.getConsumeIncrease(params);
    }

    /**
     * 常规预警：消费激增总人数
     *
     * @param params
     * @return
     */
    @Override
    public int getConsumeIncreaseSize(AcademicParams params) {
        return earlyWarningRDao.getConsumeIncreaseSize(params);
    }

    /**
     * 常规预警：上网激增
     *
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> getRadacctIncrease(AcademicParams params) {
        return earlyWarningRDao.getRadacctIncrease(params);
    }

    /**
     * 常规预警：上网激增总人数
     *
     * @param params
     * @return
     */
    @Override
    public int getRadacctIncreaseSize(AcademicParams params) {
        return earlyWarningRDao.getRadacctIncreaseSize(params);
    }

    /**
     * 获取预警列表，有earlyType 决定,若earlyType为1：预设预警，2--》智能预警，3--》我的预警
     *
     * @param params
     * @return
     */
    @Override
    public List<Map<String, String>> getEarlyList(AcademicParams params) {
        List<Map<String, String>> list = earlyWarningRDao.getEarlyList(params);
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(r -> {
                String warnId = String.valueOf(r.get("id"));
                String warnParam = earlyWarningRDao.getWarnParamByWarnID(warnId);
                r.put("warnParam", warnParam);
                if (StringUtils.isBlank(r.get("early_desc"))) {
                    r.put("early_desc", "暂无");
                }
            });
        }
        return list;
    }

    /**
     * 根据预警id，获取预警参数
     *
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> toEditPage(AcademicParams params) {
        return earlyWarningRDao.toEditPage(params);
    }

    /**
     * 保存自定义预警
     *
     * @param params
     * @return
     */
    @Override
    public int saveCustomEarly(AcademicParams params) {
        try {
            //保存自定义预警
            earlyWarningRDao.saveEarlyRule(params);
            ArrayList<EarlyParam> paramList = params.getParamList();
            if (CollectionUtils.isNotEmpty(paramList)) {
                paramList.forEach(r -> {
                    r.setWarnId(params.getId());
                    earlyWarningRDao.saveEarlyParams(r);
                });
            }
        } catch (Exception e) {
            return 0;
        }
        return 1;
    }

    /**
     * 编辑是否启用状态
     *
     * @param params
     * @return
     */
    @Override
    public int editEnableStatus(AcademicParams params) {
        try {
            earlyWarningRDao.editEnableStatus(params);
        } catch (Exception e) {
            return 0;
        }
        return 1;
    }

    /**
     * 获取满足要求的学生
     *
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> getTargetStudent(AcademicParams params) {
        List<Map<String, String>> list = earlyWarningRDao.findWarnRule(params.getId());
        StringBuilder sb = new StringBuilder();
        setRuleParam(list, sb);
        params.setSql(sb.toString());
        return earlyWarningRDao.getTargetStudent(params);
    }

    /**
     * 获取满足要求的学生的总长度
     *
     * @param params
     * @return
     */
    @Override
    public long targetStudentCount(AcademicParams params) {
        List<Map<String, String>> list = earlyWarningRDao.findWarnRule(params.getId());
        StringBuilder sb = new StringBuilder();
        setRuleParam(list, sb);
        params.setSql(sb.toString());
        return earlyWarningRDao.targetStudentCount(params);
    }

    /**
     * 删除我的预警
     *
     * @param params
     * @return
     */
    @Override
    @Transactional
    public int deleteEarlyWarn(AcademicParams params) {
        //第一步删除early_warn_param表中的数据
        earlyWarningRDao.deleteEarlyParam(params);
        return earlyWarningRDao.deleteEarlyWarn(params);
    }

    /**
     * 设置规则参数方法
     *
     * @param list 参数实体集合
     * @param sb   StringBuilder对象
     */
    private void setRuleParam(List<Map<String, String>> list, StringBuilder sb) {
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(r -> {
                String warnParam = r.get("warn_param");
                String minNum = r.get("min_num");
                String maxNum = r.get("max_num");

                //对punish_type特殊处理
                if ("punish_type".equals(warnParam)) {
                    if (StringUtils.isNotBlank(minNum)) {
                        String[] split = minNum.split("、");
                        sb.append(" and ");
                        sb.append(warnParam).append(" in ");
                        for (int i = 0; i < split.length; i++) {
                            if (i == 0) {
                                sb.append("(");
                            }
                            if (i != split.length - 1) {
                                sb.append("'" + split[i] + "',");
                            } else {
                                sb.append("'" + split[i] + "')");
                            }

                        }
                    } else if (StringUtils.isNotBlank(maxNum)) {
                        sb.append(" and ");
                        sb.append(warnParam).append(" = ").append("'" + maxNum + "'");
                    } else {
                        sb.append(" and ");
                        sb.append(warnParam).append(" != ").append(" '' ");
                    }

                } else {
                    //warnParam 非punish_type
                    if (StringUtils.isNotBlank(minNum)) {
                        sb.append(" and ");
                        sb.append(warnParam).append(" >= ").append(minNum + "  ");
                    }
                    if (StringUtils.isNotBlank(maxNum)) {
                        sb.append(" and ");
                        sb.append(warnParam).append(" <= ").append(maxNum + "  ");
                    }
                }


            });
        }
    }

    /**
     * 获取预警等级条数分布
     *
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> getEarlyLevelNum(AcademicParams params) {
        List<Map<String, Object>> list = earlyWarningRDao.getEarlyLevelNum(params);
        if (CollectionUtils.isNotEmpty(list) && list.size() != 4) {
            //如果某一预警级别为0，也为前段显示
            List<String> earlyLevelList = list.stream().map(r -> r.get("early_level").toString()).collect(Collectors.toList());
            List<String> requireEarlyLevel = Arrays.asList("一般", "较严重", "严重", "特别严重");
            for (String earlyLevel : requireEarlyLevel) {
                if (!earlyLevelList.contains(earlyLevel)) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("early_level", earlyLevel);
                    map.put("num", 0);
                    list.add(map);
                }
            }
        }
        return list;
    }

    @Override
    public int getEarlyListCount(AcademicParams params) {
        return earlyWarningRDao.getEarlyListCount(params);
    }
}
