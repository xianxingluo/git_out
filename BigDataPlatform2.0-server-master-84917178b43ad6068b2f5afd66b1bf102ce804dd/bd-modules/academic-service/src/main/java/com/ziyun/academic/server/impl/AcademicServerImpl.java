package com.ziyun.academic.server.impl;

import com.google.common.collect.Multimap;
import com.ziyun.academic.dao.EduCourseDao;
import com.ziyun.academic.dao.EduGradeDao;
import com.ziyun.academic.dao.EduGradeKylinDao;
import com.ziyun.academic.entity.OwnOrgStree;
import com.ziyun.academic.enums.GradeLevel;
import com.ziyun.academic.server.IAcademicServer;
import com.ziyun.academic.server.IOwnOrgTreeService;
import com.ziyun.academic.server.IStudentServer;
import com.ziyun.academic.server.OwnSchoolOrgServer;
import com.ziyun.academic.tools.LoginManager;
import com.ziyun.academic.tools.ParamUtils;
import com.ziyun.academic.vo.AcademicParams;
import com.ziyun.academic.vo.Params;
import com.ziyun.utils.cache.CacheConstant;
import com.ziyun.utils.cache.TokenCasheManager;
import com.ziyun.utils.date.DateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @description:学业分析
 * @author: yk.tan
 * @since: 2017/5/17
 * @history:
 */
@Service
public class AcademicServerImpl implements IAcademicServer {
    @Autowired
    private EduGradeDao eduGradeDao;

    @Autowired
    private EduCourseDao eduCourseDao;


    @Autowired
    private IOwnOrgTreeService ownOrgTreeService;

    @Autowired
    private OwnSchoolOrgServer ownSchoolOrgServer;

    @Autowired
    private EduGradeKylinDao eduGradeKylinDao;

    @Autowired
    private IStudentServer studentServer;

    @Override
    @Cacheable(value = CacheConstant.ORGANIZATION_KEY_QUERY_CACHE, key = "#root.targetClass.hashCode() + #root.methodName + #params.hashCode()")
    public Map<String, Object> getAcademicFeatures(AcademicParams params) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //选课数量
        Map<String, Object> courseNumMap = eduGradeDao.getCourseNum(params);

        //成绩合格率
        Map<String, Object> passRatio = eduGradeDao.getPassRatios(params);

        //平均学分绩点
        Map<String, Object> avgCreditPoint = eduGradeDao.getAverageScorePoint(params);

        //重修人数
        Map<String, Object> retakeCourse = eduGradeDao.getRetakeCourseNum(params);

        resultMap.put("courseNum", courseNumMap.get("course_num"));

        String ratioString = "";
        if (null != passRatio && null != passRatio.get("ratio")) {
            ratioString = passRatio.get("ratio").toString() + "%";
        } else {
            ratioString = "0";
        }

        resultMap.put("pass_ratio", ratioString);

        if (null != avgCreditPoint) {
            resultMap.put("avg_credit_point", avgCreditPoint.get("avg_credit_point"));
        } else {
            resultMap.put("avg_credit_point", 0);
        }

        resultMap.put("retake_course_num", retakeCourse.get("retake_course_num"));

        return resultMap;
    }

    @Override
    @Cacheable(value = CacheConstant.ORGANIZATION_KEY_QUERY_CACHE, key = "#root.targetClass.hashCode() + #root.methodName + #params.hashCode()")
    public List<Map<String, Object>> listCourseProperties(AcademicParams params) throws Exception {

        return eduGradeDao.listCourseProperties(params);
    }

    @Override
    @Cacheable(value = CacheConstant.ORGANIZATION_KEY_QUERY_CACHE, key = "#root.targetClass.hashCode() + #root.methodName + #params.hashCode()")
    public Integer getCoursePropertiesCount(AcademicParams params) throws Exception {

        return eduGradeDao.getCoursePropertiesCount(params);
    }

    @Override
    @Cacheable(value = CacheConstant.ORGANIZATION_KEY_QUERY_CACHE, key = "#root.targetClass.hashCode() + #root.methodName + #params.hashCode()")
    public List<Map<String, Object>> getCourseProperties(AcademicParams params) throws Exception {
        return removeCourse(eduGradeDao.getCourseProperties(params));
    }

    @Override
    @Cacheable(value = CacheConstant.ORGANIZATION_KEY_QUERY_CACHE, key = "#root.targetClass.hashCode() + #root.methodName + #params.hashCode()")
    public List<Map<String, Object>> getCourseCategories(AcademicParams params) throws Exception {

        return removeCourse(eduGradeDao.getCourseCategory(params));
    }

    @Override
    @Cacheable(value = CacheConstant.ORGANIZATION_KEY_QUERY_CACHE, key = "#root.targetClass.hashCode() + #root.methodName + #params.hashCode()")
    public List<Map<String, Object>> listCourseCategory(AcademicParams params) throws Exception {

        return eduGradeDao.listCourseCategory(params);
    }

    @Override
    @Cacheable(value = CacheConstant.ORGANIZATION_KEY_QUERY_CACHE, key = "#root.targetClass.hashCode() + #root.methodName + #params.hashCode()")
    public Integer getCourseCategoryCount(AcademicParams params) throws Exception {

        return eduGradeDao.getCourseCategoryCount(params);
    }

    @Override
    @Cacheable(value = CacheConstant.ORGANIZATION_KEY_QUERY_CACHE, key = "#root.targetClass.hashCode() + #root.methodName + #params.hashCode()")
    public List<Map<String, Object>> courseNatures(AcademicParams params) throws Exception {

        return removeCourse(eduGradeDao.getCourseNatures(params));
    }

    @Override
    @Cacheable(value = CacheConstant.ORGANIZATION_KEY_QUERY_CACHE, key = "#root.targetClass.hashCode() + #root.methodName + #params.hashCode()")
    public List<Map<String, Object>> listCourseNatures(AcademicParams params) throws Exception {

        return eduGradeDao.listCourseNatures(params);
    }

    @Override
    @Cacheable(value = CacheConstant.ORGANIZATION_KEY_QUERY_CACHE, key = "#root.targetClass.hashCode() + #root.methodName + #params.hashCode()")
    public Integer getCourseNaturesCount(AcademicParams params) throws Exception {

        return eduGradeDao.getCourseNaturesCount(params);
    }

    @Override
//    @Cacheable(value = CacheConstant.ORGANIZATION_KEY_QUERY_CACHE, key = "#root.targetClass.hashCode() + #root.methodName + #params.hashCode()")
    public List<Map<String, Object>> listScorePoint(AcademicParams params) throws Exception {

        List<Map<String, Object>> result = eduGradeDao.listScorePoint(params);
        //convertGrade(result);
        return result;
    }

    @Override
    @Cacheable(value = CacheConstant.ORGANIZATION_KEY_QUERY_CACHE, key = "#root.targetClass.hashCode() + #root.methodName + #params.hashCode()")
    public Map<String, Object> getPassRatios(AcademicParams params) throws Exception {

        return eduGradeDao.getPassRatios(params);
    }

    @Override
    @Cacheable(value = CacheConstant.ORGANIZATION_KEY_QUERY_CACHE, key = "#root.targetClass.hashCode() + #root.methodName + #params.hashCode()")
    public List<Map<String, Object>> listExamFailCount(AcademicParams params) throws Exception {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        //有挂科的统计
        List<Map<String, Object>> failNumList = eduGradeDao.listExamFailCount(params);
        //挂科为0门的统计
        Map<String, Object> noExamFailMap = eduGradeDao.noExamFailCount(params);
        if (noExamFailMap != null && noExamFailMap.size() > 0) {
            noExamFailMap.put("failCourseNum", noExamFailMap.get("fail_course_num"));
            noExamFailMap.put("fail_course_num", "没有挂科");
            noExamFailMap.put("typeNum", 0);
            result.add(noExamFailMap);
        }
        long failNumGreaterThan = 0L;
        if (failNumList.size() > 0) {
            int i = 0;
            for (Map<String, Object> map : failNumList) {
                map.put("failCourseNum", map.get("fail_course_num"));
                if ((long) map.get("fail_course_num") < 11) {
                    map.put("fail_course_num", "挂科" + map.get("fail_course_num") + "门");
                    map.put("typeNum", 0);
                    result.add(map);
                } else {
                    //当挂科门数大于10时，累计数量，只展示一个  挂科10门以上人数
                    failNumGreaterThan += (Long) map.get("num");

                }

            }

            if (failNumGreaterThan > 0) {
                Map<String, Object> greaterThanMap = new HashMap<String, Object>();
                greaterThanMap.put("failCourseNum", 11);
                greaterThanMap.put("num", failNumGreaterThan);
                greaterThanMap.put("fail_course_num", "挂科10门以上");
                greaterThanMap.put("typeNum", 1);
                result.add(greaterThanMap);

            }
        }
        return result;
    }

    @Override
    @Cacheable(value = CacheConstant.ORGANIZATION_KEY_QUERY_CACHE, key = "#root.targetClass.hashCode() + #root.methodName + #params.hashCode()")
    public List<Map<String, Object>> listExamPassCourse(AcademicParams params) throws Exception {

        List<Map<String, Object>> result = eduGradeDao.listExamPassCourse(params);
        //  convertGrade(result);
        return result;
    }

    @Override
    @Cacheable(value = CacheConstant.ORGANIZATION_KEY_QUERY_CACHE, key = "#root.targetClass.hashCode() + #root.methodName + #params.hashCode()")
    public List<Map<String, Object>> listCreditSituation(AcademicParams params) {
        List<Map<String, Object>> result = eduGradeDao.listCreditSituation(params);
        /*if (StringUtils.isNotBlank(params.getOutid())) {
            //班级排名
            List<Map<String, Object>> rank = eduGradeDao.getRank(params);
            if (result.size() > 0 && rank.size() > 0) {
                for (Map<String, Object> credit : result) {
                    for (Map<String, Object> r : rank ) {
                        if(credit.get("term_num").equals(r.get("term_num"))){
                            credit.put("rank", r.get("rank"));
                        }
                    }
                }
            }

        }*/
//        convertGrade(result);
        return result;
    }

    @Override
    @Cacheable(value = CacheConstant.ORGANIZATION_KEY_QUERY_CACHE, key = "#root.targetClass.hashCode() + #root.methodName + #params.hashCode()")
    public Map<String, Object> getGraduationCredit(AcademicParams params) {

        Map<String, Object> result = eduGradeDao.getGraduationCredit(params);
        return result;
    }

    @Override
    @Cacheable(value = CacheConstant.ORGANIZATION_KEY_QUERY_CACHE, key = "#root.targetClass.hashCode() + #root.methodName + #params.hashCode()")
    public Map<String, Object> retakeCoursesTop(AcademicParams params) {
//查询有重修课程的学期
//        List<Integer> terms = eduCourseDao.listTerms(params);

//        if (null == terms || terms.size() == 0) {
//            return null;
//        }
        Map<String, Object> result = new HashMap<String, Object>();
//
//        if (null == params.getTermNum()) {
//            //自定义的学期字段和重修课程的学期字段都为空的时候，默认学期字段为有重修课程的学期第一个学期
//            if (null == params.getTermNumAcademic()) {
//                params.setTermNumAcademic(terms.get(0));
//            }
//        } else {
//            //如果自定义的学期字段不为空，则查询都以自定义的学期为准
//            params.setTermNumAcademic(params.getTermNum());
//        }
//
        List<Map<String, Object>> mapList = eduCourseDao.listRetakeCourses(params);

        if (CollectionUtils.isEmpty(mapList)) {
            return null;
        }

        mapList.forEach(map -> {
            Integer passCourseNum = Integer.parseInt(map.get("passCourse").toString());
            Integer failCourseNum = Integer.parseInt(map.get("failCourse").toString());
            Integer ration = passCourseNum > failCourseNum ? failCourseNum : passCourseNum;
            //passCourseNum,failCourseNum非空判断
            if (passCourseNum != 0 && failCourseNum != 0) {
                if (passCourseNum % failCourseNum == 0 || failCourseNum % passCourseNum == 0) {
                    map.put("passRation", "通过:未通过=" + (passCourseNum / ration) + ":" + (failCourseNum / ration));
                } else {
                    map.put("passRation", "通过:未通过≈" + (new BigDecimal(passCourseNum).divide(new BigDecimal(ration), 2, BigDecimal.ROUND_HALF_UP)) + ":" + new BigDecimal(failCourseNum).divide(new BigDecimal(ration), 2, BigDecimal.ROUND_HALF_UP));
                }
            } else if (passCourseNum == 0) {
                map.put("passRation", "通过:未通过 =" + (0 + ":" + failCourseNum));
            } else if (failCourseNum == 0) {
                map.put("passRation", "通过:未通过  =" + passCourseNum + ":0");
            }

        });
//
//        result.put("terms", terms);
        result.put("retakeCourses", mapList);

        return result;
    }

    @Override
    public List<Map<String, Object>> retakeStudentMessage(AcademicParams params) {
        return eduCourseDao.getRetakeStudentList(params);
    }

    private Map<String, List<Object>> getMapObject4Redis(Multimap<Object, Object> multimap) {
        Map<String, List<Object>> result = new LinkedHashMap();
        multimap.asMap().keySet().forEach(k -> {
            List<Object> resultMap = new LinkedList<>();
            for (Object map : multimap.get(k)) {
                resultMap.add(map);
            }
            result.put(String.valueOf(k), resultMap);
        });
        return result;
    }

    @Override
    @Cacheable(value = CacheConstant.ORGANIZATION_KEY_QUERY_CACHE, key = "#root.targetClass.hashCode() + #root.methodName + #params.hashCode()")
    public List<Map<String, Object>> listRetakeCourses(AcademicParams params) {
        List<Map<String, Object>> result = eduCourseDao.listRetakeCourses(params);
        convertGrade(result);
        return result;
    }


    private void convertGrade(List<Map<String, Object>> listMap) {
        Map map;
        for (int i = 0; i < listMap.size(); i++) {
            map = listMap.get(i);
            //学期
            String termNum = String.valueOf(map.get("term_num"));
            if (null != termNum) {
                String termName = GradeLevel.getName(termNum);
                if (StringUtils.isNotBlank(termName)) {
                    map.put("term_name", termName);
                } else {
                    listMap.remove(i);
                    i--;
                }
            }
        }
    }

    /**
     * 当统计的课程数量为零时，移除数据
     *
     * @return List
     */
    private List<Map<String, Object>> removeCourse(List<Map<String, Object>> list) {
        if (null == list) {
            return null;
        }
        for (Map<String, Object> map : list) {
            if ((long) map.get("num") == 0L) {
                list.remove(map);
            }
        }

        return list;
    }

    @Override
    //  @Cacheable(value = CacheConstant.ORGANIZATION_KEY_QUERY_CACHE, key = "#root.targetClass.hashCode() + #root.methodName + #params.hashCode()")
    public Map<String, Object> getAcademicLabel(Params params) {
        Map<String, Object> result = new HashMap<>();
        //选课数量
        Map<String, Object> courseNumMap = eduGradeDao.getCourseNum(params);

        //如果选课数量为0，直接返回Null，不再查询成绩合格率，平均学分点等其他数据
        if ((long) courseNumMap.get("course_num") == 0L) {
            return null;
        }

        result.put("course_num", courseNumMap.get("course_num"));

        //成绩合格率
        Map<String, Object> passRatio = eduGradeDao.getPassRatios(params);
        result.put("ratio", passRatio.get("ratio"));

        //平均学分绩点
        Map<String, Object> avgCreditPoint = eduGradeDao.getAverageScorePoint(params);
        result.put("avg_credit_point", avgCreditPoint.get("avg_credit_point"));

        //重修课程数量
        Map<String, Object> retakeCourseMap = eduGradeDao.getRetakeCourse(params);
        result.put("retake_course", retakeCourseMap.get("retake_course"));

        return result;

    }


    /**
     * 由于redis序列化是不支持guava的集合类，故采取此种策略。
     * Multimap的entry是ImmutableEntry
     *
     * @param multimap
     * @return
     */
    private Map<String, List<Map>> getMap4Redis(Multimap<Object, Map> multimap) {
        Map<String, List<Map>> result = new LinkedHashMap();
        multimap.asMap().keySet().forEach(k -> {
            List<Map> resultMap = new LinkedList<>();
            for (Map map : multimap.get(k)) {
                resultMap.add(map);
            }
            result.put(String.valueOf(k), resultMap);
        });
        return result;
    }

    //学分绩点不另外算，从已有接口中获取。
    private void mergeList(List<Map<String, Object>> mapList, List<Map<String, Object>> scorePoint) {
        for (Map<String, Object> map : mapList) {
            if (map.get("term_num") != null) {
                for (Map<String, Object> score : scorePoint) {
                    if (score.get("term_num") != null && String.valueOf(map.get("term_num")).equals(score.get("term_num"))) {
                        map.put("avg_credit_point", score.get("avg_credit_point"));
                    }

                }
            }
        }
    }

    @Override
    public Map getTopCategory(AcademicParams params) {
        //判断是否存在于session中不存在则比对数据
        //获得shiro中的权限
        Set<String> permissions = TokenCasheManager.getPermissionsCache(LoginManager.getToken());
        Map map = new HashMap();
        List<Map> mapList = getTopCategoryHaveehcache(params);
        //mapList = orgStreeDao.getTopCategory(params);
        List<Map> levelOne = new ArrayList<>();
        List<Map> levelTwo = new ArrayList<>();
        List<Map> levelThree = new ArrayList<>();
        List<Map> levelFour = new ArrayList<>();
        Set<String> levelTwoParentType = new HashSet<>();
        //遍历权限获取
        for (Map m : mapList) {
            for (String s : permissions) {
                if (s.equals(m.get("org_code"))) {
                    int level = (int) m.get("org_level");
                    switch (level) {
                        case 0:
                            levelOne.add(m);
                            break;
                        case 1:
                            levelTwo.add(m);
                            if (m != null && m.get("parent_code") != null) {
                                levelTwoParentType.add(String.valueOf(m.get("parent_code")));
                            }
                            break;
                        case 2:
                            levelThree.add(m);
                            break;
                        case 3:
                            levelFour.add(m);
                            break;
                        default:
                            break;
                    }
                    break;
                }
            }
        }
        //levelTwo 特殊处理
        if (levelTwoParentType.size() > 1) {
            for (Map map1 : levelTwo) {
                if (map1.get("parent_code") != null) {
                    if ("slg".equals(map1.get("parent_code"))) {
                        map1.put("org_name", "苏理工" + map1.get("org_name"));
                    } else if ("jkd".equals(map1.get("parent_code"))) {
                        map1.put("org_name", "张家港校区" + map1.get("org_name"));
                    }
                }
            }
        }
        Collections.sort(levelFour, (Object o1, Object o2) -> {
            return Integer.valueOf(String.valueOf(((Map) o2).get("org_code"))) - Integer.valueOf(String.valueOf(((Map) o1).get("org_code")));
        });
        map.put("level1", levelOne);
        map.put("level2", levelTwo);
        map.put("level3", levelThree);
        map.put("level4", levelFour);
        return map;

    }


    private BigDecimal doubleToBigDecimal(Object obj) {
        return obj == null ? BigDecimal.ZERO : BigDecimal.valueOf((double) obj);
    }

    private BigDecimal longToBigDecimal(Object obj) {
        return obj == null ? BigDecimal.ZERO : BigDecimal.valueOf((long) obj);
    }

    /**
     * 对数据分组。
     *
     * @param listMap
     * @param keystr
     * @return
     */
    private Map<String, List<Map<String, Object>>> convertMap(List<Map<String, Object>> listMap, String
            keystr) {
        Map<String, List<Map<String, Object>>> result = new LinkedHashMap<>();
        String key;
        for (Map<String, Object> map : listMap) {
            key = String.valueOf(map.get(keystr));
            //类别
            //types.add((String) map.get("exam_natures"));
            if (result.get(key) == null) {
                List<Map<String, Object>> mapList = new ArrayList<>();
                mapList.add(map);
                result.put(key, mapList);
            } else {
                result.get(key).add(map);
            }
        }
        return result;
    }

    /**
     * 数据按keystr分组
     *
     * @param result
     * @param types
     * @param listMap
     * @param keystr
     */
    private void convertMap(Map<String, List<Map<String, Object>>> result, Set<String> types, List<Map<String,
            Object>> listMap, String keystr) {

        String key;
        for (Map<String, Object> map : listMap) {
            key = String.valueOf(map.get(keystr));
            types.add(key);
            //类别
            //types.add((String) map.get("exam_natures"));
            if (result.get(key) == null) {
                List<Map<String, Object>> mapList = new ArrayList<>();
                mapList.add(map);
                result.put(key, mapList);
            } else {
                result.get(key).add(map);
            }
        }
    }

    //转换学期查询条件，如-1转换为 2016-2017-1 etc
    private void convertSemester(Params params) {
        //do nothing
        /*if (StringUtils.isNotBlank(params.getSemester())) {
            if ("0".equals(params.getSemester()) || "-1".equals(params
                    .getSemester())) {
                SemesterUtil.computeSemesterFromDate(params);
            } else
                params.setSemester(null);//异常数据，则默认显示所有数据。
        }*/
    }

    @Override
    public List<String> getParentcode(Params params) {
        OwnOrgStree parent = ownOrgTreeService.getPcodeByCcode(params);
        List<String> al = new ArrayList<>();
        al.add(parent.getOrgCode());
        List<String> parentList = getParent(parent, al);
        Collections.reverse(parentList);
        return parentList;
    }

    public List<String> getParent(OwnOrgStree trees, List<String> flag) {
        if (trees.getOrgStree() != null) {
            flag.add(trees.getParentCode());
            getParent(trees.getOrgStree(), flag);
        }
        return flag;
    }

    public void checkClearNull(List list) {
        int nullNum = 0;
        for (Object o : list) {
            if (o == null) {
                nullNum++;
            }
        }
        if (nullNum == list.size()) {
            list.clear();
        }
    }

    @Override
    public List<Map> getChildrenList(Params params) {
        return ownOrgTreeService.getChildrenList(params);
    }

    /**
     * 拆分getTopCategory因为现在每个账号有不同的数据权限因此这块需要单独处理
     */
    @Override
//	@Cacheable(value = CacheConstant.ORGANIZATION_CACHE, key = "#params.orgCode")
    public List<Map> getTopCategoryHaveehcache(Params params) {
        List<Map> mapList = ownOrgTreeService.getAllTopCategory(params);
        return mapList;
    }

    /**
     * 将前端传过来的组织机构code转换成该角色有的classCode,如果带有outid则不作处理
     *
     * @param param 清空原来的组织机构参数
     */
    @Override
    public void paramTransformCodeOnlyClass(Params param) {
        if (param.getOutid() == null) {

            // 获取权限
            Set<String> permissions = TokenCasheManager.getPermissionsCache(LoginManager.getToken());
            Set<String> classcodeList = ownSchoolOrgServer.selectOwnClasscode(param);

            // 取交集
            classcodeList.retainAll(permissions);

            String[] strArr = classcodeList.toArray(new String[]{});
            Arrays.sort(strArr);

            // 只清空组织机构
            ParamUtils.emptyParamsCode(param);
            param.setClassCode(strArr);
        }
    }

    /**
     * 根据挂科门数，显示学生列表
     *
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> failCourseStudentList(AcademicParams params) {
        List<Map<String, Object>> list = null;
        if (params != null && "0".equals(params.getFailCourseNum())) {
            list = eduGradeDao.noFailCourseStudentList(params);
        } else {
            list = eduGradeDao.failCourseStudentList(params);
        }
        return list;
    }

    /**
     * 学业特征-课程选择-课程分类(课程性质，课程属性 ，三个共用一个方法)-柱形图学生列表
     *
     * @param para
     * @return
     */
    @Override
    public List<Map<String, Object>> courseCategoryStudentList(AcademicParams para) {

        return eduGradeDao.courseCategoryStudentList(para);
    }

    /**
     * 学业特征-学分绩点统计--柱形图对应的平均学分绩点
     *
     * @param para
     * @return
     */
    @Override
    public List<Map<String, Object>> scorePointList(AcademicParams para) {
        return eduGradeDao.scorePointList(para);
    }

    /**
     * 学业特征-考试通过情况-- 柱形图课程有无挂科列表
     *
     * @param para
     * @return
     */
    @Override
    public List<Map<String, Object>> passFailCourseList(AcademicParams para) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        //挂科的科目
        if (StringUtils.isNotBlank(para.getType()) && "0".equals(para.getType())) {
            List<Map<String, Object>> failCourseList = eduGradeDao.failCourseList(para);
            if (failCourseList != null && failCourseList.size() != 0) {
                resultList.addAll(failCourseList);
            }
        }
        //没有挂科
        if (StringUtils.isNotBlank(para.getType()) && "1".equals(para.getType())) {
            List<Map<String, Object>> passCourseList = eduGradeDao.passCourseList(para);
            if (passCourseList != null && passCourseList.size() != 0) {
                resultList.addAll(passCourseList);
            }
        }
        //选择全部的科目
        if (StringUtils.isBlank(para.getType())) {
            List<Map<String, Object>> failCourseList = eduGradeDao.failCourseList(para);
            if (failCourseList != null && failCourseList.size() != 0) {
                resultList.addAll(failCourseList);
            }
            List<Map<String, Object>> passCourseList = eduGradeDao.passCourseList(para);
            if (passCourseList != null && passCourseList.size() != 0) {
                resultList.addAll(passCourseList);
            }
        }
        //挂科统计
        return resultList;
    }

    /**
     * 学业特征-绩点排名
     *
     * @param para
     * @return
     */
    @Override
    public List<Map<String, Object>> pointTop(AcademicParams para) {
        return eduGradeDao.pointTop(para);
    }

    /**
     * 学业特征-选修课排名
     *
     * @param para
     * @return
     */
    @Override
    public List<Map<String, Object>> electiveTop(AcademicParams para) {
        return eduGradeDao.electiveTop(para);
    }


    /**
     * 学业特征-选修课排名--选修课学生列表
     *
     * @param para
     * @return
     */
    @Override
    public List<Map<String, Object>> electiveTopStudentList(AcademicParams para) {
        return eduGradeDao.electiveTopStudentList(para);
    }

    /**
     * 学业特征-修学分情况--修学分情况课程列表
     *
     * @param para
     * @return
     */
    @Override
    public List<Map<String, Object>> completionCourseList(AcademicParams para) {
        return eduGradeDao.completionCourseList(para);
    }

    @Override
    public Map<String, Object> getStudentRanking(AcademicParams para) {
        Map<String, Object> resultMap = new HashedMap();

        String enrollmentYear = studentServer.getEnrollmentYearById(para);
        para.setEnrollmentYear(enrollmentYear);
        List<Map> rankingList = eduGradeDao.getStudentRanking(para);
        List<Map> pointList = eduGradeDao.getStudentAvgCreditPoint(para);
        if (null == pointList || pointList.size() == 0 || rankingList == null || rankingList.size() == 0) {
            return null;
        } else {
            resultMap.put("ranking", rankingList);
            resultMap.put("point", pointList);
            return resultMap;
        }
    }

    @Override
    @Cacheable(value = CacheConstant.COURSE_CACHE, key = "#root.methodName + #params.hashCode()")
    public Map<String, Object> getScoreCredit(AcademicParams params) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> maps = eduGradeDao.getScoreCredit(params);
        if (null == maps || maps.size() == 0) {
            return null;
        }
        Set<String> types = new LinkedHashSet<>();
        Map<String, List> scoreMap = new HashedMap();
        Map<String, List> pointMap = new HashedMap();
        Map<String, List> subjectMap = new HashedMap();
        Map<String, List> creditMap = new HashedMap();
        for (Map<String, Object> map : maps) {
            double convertScore = 0;
            //1.及格情况
            String[] scoreArray = {"中", "良", "优", "通过", "合格", "及格", "不及格", "不通过", "不合格"};
            String scoreStr = String.valueOf(map.get("score"));
            if (Arrays.asList(scoreArray).contains(scoreStr)) {
                convertScore = 60 + (Double.parseDouble((String) map.get("point")) - 1) * 10;
            } else {
                convertScore = Double.parseDouble((String) map.get("score"));
            }
            map.put("convertScore", convertScore);
            if (map.get("semester") != null) {
                String semester = String.valueOf(map.get("semester"));
                types.add(semester);
                if (null != scoreMap.get(semester)) {
                    List scoreList = scoreMap.get(semester);
                    List pointList = pointMap.get(semester);
                    List subjectList = subjectMap.get(semester);
                    List creditList = creditMap.get(semester);
                    scoreList.add(convertScore);
                    pointList.add(map.get("point"));
                    subjectList.add(map.get("course_name"));
                    creditList.add(map.get("credit"));
                } else {
                    List<Double> scoreList = new ArrayList();
                    List pointList = new ArrayList();
                    List subjectList = new ArrayList();
                    List creditList = new ArrayList();
                    scoreList.add(convertScore);
                    pointList.add(map.get("point"));
                    subjectList.add(map.get("course_name"));
                    scoreMap.put(semester, scoreList);
                    Object credit = map.get("credit");
                    creditList.add(credit);
                    creditMap.put(semester, creditList);
                    pointMap.put(semester, pointList);
                    subjectMap.put(semester, subjectList);
                }
            }
        }
        result.put("gradenames", types);
        result.put("score", scoreMap);
        result.put("point", pointMap);
        result.put("subject", subjectMap);
        result.put("credit", creditMap);
        return result;
    }

    /**
     * 学业特征-国家等级考试
     *
     * @param para
     * @return
     */
    @Override
    public List<Map<String, Object>> countryGrade(AcademicParams para) {
        //返回结果集对象
        List<Map<String, Object>> resultList = new ArrayList<>();
        List<Map<String, Object>> list = eduGradeDao.countryGrade(para);
        if (list != null && list.size() > 0 && list.size() == 1) {
            list.get(0).put("ration", 0);
            resultList.add(list.get(0));
            return resultList;
        } else if (list != null && list.size() > 1) {

            for (int i = 1; i < list.size(); i++) {
                if (i == 1) {
                    list.get(0).put("ration", 0);
                    resultList.add(list.get(0));
                }
                Long prePassNum = (Long) list.get(i - 1).get("passNum");
                Long nextPassNum = (Long) list.get(i).get("passNum");
                //增长率>0 用 ration 表示    增长率 < 0 用 noration 表示
                String ration = "";
                if (prePassNum == 0) {
                    ration = "0.00" + "%";
                } else {
                    ration = Math.round(nextPassNum - prePassNum) * 100 / prePassNum + "%";
                }
                list.get(i).put("ration", ration);
                resultList.add(list.get(i));
            }
        }

        return resultList;
    }

    /**
     * 学业特征-国家等级考试--国考学生列表展示
     *
     * @param para
     * @return
     */
    @Override
    public List<Map<String, Object>> countryGradeStudentList(AcademicParams para) {
        return eduGradeDao.countryGradeStudentList(para);
    }

    /**
     * 等级考试--下拉框数据展示
     *
     * @param para
     * @return
     */
    @Override
    public List<Map<Object, Object>> organ(AcademicParams para) {
        //一级菜单
        List<Map<Object, Object>> oneMenuList = eduGradeDao.organOne(para);
        if (oneMenuList != null && oneMenuList.size() > 0) {
            for (int i = 0; i < oneMenuList.size(); i++) {
                String courseNoOne = (String) oneMenuList.get(i).get("courseNoOneZh");
                para.setCourseNo(courseNoOne);
                List<Map<Object, Object>> twoMenuList = eduGradeDao.organTwo(para);
                if (twoMenuList != null && twoMenuList.size() > 0) {
                    oneMenuList.get(i).put("twoMenuList", twoMenuList);
                }
            }
        }
        return oneMenuList;
    }

    @Override
    public List<Map<Object, Object>> semesterTime(AcademicParams para) {
        return eduGradeDao.semesterTime(para);
    }

    /**
     * 上课考勤打卡列表
     *
     * @param params
     * @return
     */
    @Override
    //  @Cacheable(value = CacheConstant.COURSE_CACHE, key = "#root.methodName + #params.hashCode()")
    public List<Map<String, Object>> attendanceCard(AcademicParams params) {
        //查kylin
        List<Map<String, Object>> list = eduGradeKylinDao.attendanceCard(params);
        list.forEach(r -> {
            String outid = r.get("outid").toString();
            r.put("opdt", DateUtils.date2Str4GMT16("yyyy-MM-dd HH:mm:ss", r.get("opdt").toString()));
            Map studentMap = studentServer.selectByOutid(outid);
            r.putAll(studentMap);
        });
        return list;
    }

    /**
     * 上课考勤打卡列表总长度
     *
     * @param
     * @return
     */
    @Override
//    @Cacheable(value = CacheConstant.COURSE_CACHE, key = "#root.methodName + #params.hashCode()")
    public int attendanceCardSize(AcademicParams params) {
        return eduGradeKylinDao.attendanceCardSize(params);
    }

    /**
     * 上课考勤打卡列表 --教室编号
     *
     * @param params
     * @return
     */
    @Override
    @Cacheable(value = CacheConstant.COURSE_CACHE, key = "#root.methodName + #params.hashCode()")
    public List<Map<String, Object>> classNums(AcademicParams params) {
        return eduGradeKylinDao.classNums(params);
    }

    @Override
//    @Cacheable(value = CacheConstant.COURSE_CACHE, key = "#root.methodName + #params.hashCode()")
    public List<Map<String, Object>> personAttendanceCard(AcademicParams params) {
        List<Map<String, Object>> list = eduGradeKylinDao.personAttendanceCard(params);
        list.forEach(r -> {
            String outid = r.get("outid").toString();
            r.put("opdt", DateUtils.date2Str4GMT16("yyyy-MM-dd HH:mm:ss", r.get("opdt").toString()));
            Map studentMap = studentServer.selectByOutid(outid);
            r.putAll(studentMap);
        });
        return list;
    }

    /**
     * 个人--上课考勤打卡总长度
     *
     * @param params
     * @return
     */
    @Override
//    @Cacheable(value = CacheConstant.COURSE_CACHE, key = "#root.methodName + #params.hashCode()")
    public int personAttendanceCardSize(AcademicParams params) {
        return eduGradeKylinDao.personAttendanceCardSize(params);
    }

    /**
     * 获取个人 课程类型
     *
     * @param para
     * @return
     */
    @Override
    public List<Map<String, Object>> getCoursePropertiesStudent(AcademicParams para) {
        return eduGradeDao.getCoursePropertiesStudent(para);
    }

    /**
     * 选修课排名--弹窗一，统计选课人数
     *
     * @param para
     * @return
     */
    @Override
    public List<Map<String, Object>> electiveTopCoursenoList(AcademicParams para) {
        return eduGradeDao.electiveTopCoursenoList(para);
    }

    /**
     * 选修课排名--柱形图弹窗二，统计某门课程，每一学期变化情况
     *
     * @param para
     * @return
     */
    @Override
    public List<Map<String, Object>> electiveTopCoursenoSemester(AcademicParams para) {
        return eduGradeDao.electiveTopCoursenoSemester(para);
    }

    /**
     * 学业预警 -- 查询必修课程 该科2-4门的学生
     *
     * @param para
     * @return
     */
    @Override
    public List<Map<String, Object>> compulsoryCoursesList(AcademicParams para) {
        return eduGradeDao.compulsoryCoursesList(para);
    }

    /**
     * @param para 学业预警  必修课 在 2--4门之间
     * @return
     */
    @Override
    public int compulsoryCoursesCount(AcademicParams para) {
        return eduGradeDao.compulsoryCoursesCount(para);
    }

    /**
     * 获取所有学生的班级排名
     *
     * @param para
     */
    @Override
    public void getStudentClassRankin(AcademicParams para) {
        //1. 获取所有在籍生的学号
        List<String> outids = studentServer.getStudentOutids(para);
        //循环遍历
        if (CollectionUtils.isNotEmpty(outids)) {
            for (int i = 0; i < outids.size(); i++) {
                List<Map<String, Object>> list = eduGradeDao.getClassRanking(outids.get(i));
                if (CollectionUtils.isNotEmpty(list)) {
                    //循环遍历
                    for (int j = 1; j < list.size(); j++) {
                        //preMap 上一学期的map对象
                        Map<String, Object> preMap = list.get(j - 1);
                        //当前学期map对象
                        Map<String, Object> currentMap = list.get(j);
                        Map paramMap = new HashMap();
                        paramMap.put("outid", outids.get(i));
                        paramMap.put("semester", currentMap.get("semester"));
                        //当前学期与上一学期班级排名变化情况（当前学期班级排名 - 上一学期班级排名）
                        paramMap.put("classRnkingChange", (Long) currentMap.get("class_ranking") - (Long) preMap.get("class_ranking"));
                        //上一学期班级排名
                        paramMap.put("lastSemesterClassRanking", preMap.get("semester") + "学期班级名次第" + preMap.get("class_ranking") + "名");
                        paramMap.put("curSemesterClassRanking", currentMap.get("semester") + "学期班级名次第" + currentMap.get("class_ranking") + "名");
                        //将结果更新到early_warning_academic_student 表中
                        eduGradeDao.updateStudentClassRaningChange(paramMap);


                    }
                }
            }
        }
    }

    /**
     * 学业预警 获取班级排名下滑查过十名的学生列表
     *
     * @param para
     * @return
     */
    @Override
    public List<Map<String, Object>> getStudentClassRankingSlideList(AcademicParams para) {
        return eduGradeDao.getStudentClassRankingSlideList(para);
    }

    /**
     * 学业预警 获取班级排名下滑查过十名的学生列表总长度
     *
     * @param para
     * @return
     */
    @Override
    public int getStudentClassRankingSlideSize(AcademicParams para) {
        return eduGradeDao.getStudentClassRankingSlideSize(para);
    }

    /**
     * 学业预警 获取学生一整学年 获得学分小于 18学分的学生列表
     *
     * @param para
     * @return
     */
    @Override
    public List<Map<String, Object>> getStudentYearCreditList(AcademicParams para) {
        return eduGradeDao.getStudentYearCreditList(para);
    }

    /**
     * 学业预警：获取学生一整学年获取学分小于18学分的学生列表总长度
     *
     * @param para
     * @return
     */
    @Override
    public int getStudentYearCreditSize(AcademicParams para) {
        return eduGradeDao.getStudentYearCreditSize(para);
    }

    /**
     * 学业预警 -- 统计受过处分的学生列表（处分类型：严重警告","警告处分"除外）
     *
     * @param para
     * @return
     */
    @Override
    public List<Map<String, Object>> getStudentPulishList(AcademicParams para) {
        return eduGradeDao.getStudentPulishList(para);
    }

    /**
     * 学业预警 -- 统计受过处分的学生列表总长度（处分类型：严重警告","警告处分"除外）
     *
     * @param para
     * @return
     */
    @Override
    public int getStudentPulishSize(AcademicParams para) {
        return eduGradeDao.getStudentPulishSize(para);
    }

    /**
     * 学业预警 -- 获取指定学期英语成绩小于390分的学生列表
     *
     * @param para
     * @return
     */
    @Override
    public List<Map<String, Object>> getStudentEnglishScoreList(AcademicParams para) {
        return eduGradeDao.getStudentEnglishScoreList(para);
    }

    /**
     * 学业预警 -- 获取指定学期英语成绩小于390分的学生列表总长度
     *
     * @param para
     * @return
     */
    @Override
    public int getStudentEnglishScoreSize(AcademicParams para) {
        return eduGradeDao.getStudentEnglishScoreSize(para);
    }
}
