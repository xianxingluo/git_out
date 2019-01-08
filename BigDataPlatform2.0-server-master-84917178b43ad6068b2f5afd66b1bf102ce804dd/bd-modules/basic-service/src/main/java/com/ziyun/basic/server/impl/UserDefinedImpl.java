package com.ziyun.basic.server.impl;

import com.ziyun.basic.mapper.UserDefinedMapper;
import com.ziyun.basic.entity.SysorgTree;
import com.ziyun.basic.enums.StudentStatusEnum;
import com.ziyun.basic.server.UserDefinedServer;
import com.ziyun.basic.tools.LoginManager;
import com.ziyun.basic.tools.UserDefinedUtils;
import com.ziyun.basic.vo.Params;
import com.ziyun.common.constant.Constant;
import com.ziyun.utils.cache.TokenCasheManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by admin on 2018/1/23.
 */
@Service
public class UserDefinedImpl implements UserDefinedServer {
    @Autowired
    private UserDefinedMapper userDefinedMapper;

    @Override
    public List<SysorgTree> getOrgans(Params params) {
        List<SysorgTree> orgtree = userDefinedMapper.getOrgtree(params.getOrgCode());
        return orgtree;
    }

    @Override
    public List<SysorgTree> getDataAuthTree(Params params) {
        Long id = null;
        if (Constant.STRING_ZERO.equals(params.getOrgCode())) {
            id = Constant.LONG_TWO;
        }
        List<SysorgTree> orgTree = userDefinedMapper.getDataAuthTree(id, params.getLogonRoleId(), params.getLogonIsSuperAdmin());
        return orgTree;
    }

    @Override
    public Map getClasscodeByDefined(Params params) {
        //根据权限生成初始自定义
        Map<String, List<String>> definedMap = getInitialDefined();

        //选择了入学年份
        if (StringUtils.isNotBlank(params.getEnrollmentYear())) {
            //对学期做处理
            definedMap.put("semester", getMaxSemester(params.getEnrollmentYear()));
            //对学籍状态进行处理
            List<String> statusList = userDefinedMapper.getStudentStatus(params);
            Set<String> statusSet = getStatus(statusList);
            putValueIntoMap(definedMap, "graduate", statusSet);
            putValueIntoMap(definedMap, "scholastic", statusSet);
            //对classcode做处理
            List<String> classCode = getClassCodeByParams(params);
            putValueIntoMap(definedMap, "classCode", classCode);
        }

        //选择了学期
        if (params.getTermNum() != null) {
            //获取符合条件的入学年份
            List<String> retainYear = chooseSemester(params);
            List<String> year = definedMap.get("year");
            //将入学年份合并后后塞入year中
            putValueIntoMap(definedMap, "year", retainYear);
            //获取当前新生的最大学期数,如果小于当前学期则移除在籍中的新生
            Integer maxSemester = getMaxSemesterOfNewStudent();
            if (maxSemester < params.getTermNum()) {
                List<String> graduate = (List<String>) definedMap.get("scholastic");
                if (null != graduate && graduate.size() > 0) {
                    //防止出现在籍中不存在新生的情况直接遍历，如有有新生则移除
                    for (int i = 0; i < graduate.size(); i++) {
                        if (StringUtils.equals(graduate.get(i), "新生")) {
                            graduate.remove(i);
                            break;
                        }
                    }
                }
            }
            //移除放假期间
            List<String> timeList = (List<String>) definedMap.get("time");
            if (null != timeList && timeList.size() > 0) {
                for (int i = 0; i < timeList.size(); i++) {
                    if (StringUtils.equals(timeList.get(i), "放假期间")) {
                        timeList.remove(i);
                        break;
                    }
                }
            }
            //对班级做处理，
            List<String> classCodeSet = getClasscodeBySemester(params.getTermNum());
            putValueIntoMap(definedMap, "classCode", classCodeSet);
        }
        //在选择时间期间的，判断是否选了放假期间，选了就将学期返回结果为null
        if (null != params.getTermtype() && params.getTermtype() == 2) {
            definedMap.put("semester", null);
        }

        //选择了学籍状态，学籍对时间期间无影响不做处理
        if (StringUtils.isNotBlank(params.getEduStatus())) {
            //处理入学年份
            //与definedMap中的入学年份合并，做交集
            List<String> classcodeList = getClassCodeByStatus(params);
            putValueIntoMap(definedMap, "year", getYear(classcodeList));
            List<String> targetYear = definedMap.get("year");
            //处理学期
            //根据年份list来返回对应的学期,将学期与definedMap中的学期合并
            putValueIntoMap(definedMap, "semester", getSemesterByYear(targetYear));
            //处理班级
            // 通过当前状态的入学年份来获取对应的班级然后将班级和definedMap中的入学年份合并
//            Set<String> classcodeSet = getClassCodeByYear(targetYear, params);
            putValueIntoMap(definedMap, "classCode", classcodeList);
        }

        //选了班级，班级对时间期间无影响
        if (StringUtils.isNotBlank(params.getClassSelect()) || StringUtils.isNotBlank(params.getMajorCode())) {
            //对年份做处理。
            Set<String> yearSet = splitYear(params);
            putValueIntoMap(definedMap, "year", yearSet);
            //由于有留级生这种状态因此根据班级号搜索数据库来获取最大学期
            List<String> semesterList = getSemesterByClasscode(params);
            putValueIntoMap(definedMap, "semester", semesterList);
            //处理学籍
            Set<String> statusSet = getStatus(userDefinedMapper.getStatusByClasscode(params));
            putValueIntoMap(definedMap, "graduate", statusSet);
            putValueIntoMap(definedMap, "scholastic", statusSet);
            Set<String> classSet = getClasscodeByMajor(params);
            putValueIntoMap(definedMap, "classCode", classSet);
        }
        return definedMap;
    }


    //初始根据权限获得自定义搜索
    private Map getInitialDefined() {
        Map<String, List<String>> definedMap = UserDefinedUtils.getUserDefined();
        List<String> classList = getClassCodeByParams(null);
        Set<String> permissions = TokenCasheManager.getPermissionsCache(LoginManager.getToken());
        if (null == classList) {
            return null;
        }
        classList.retainAll(permissions);
        if (classList.size() > 0) {
            //将权限处理后的班级code去数据库查询学籍状态。
            String[] classCode = classList.toArray(new String[classList.size()]);
            Params toolParam = new Params();
            //将参数复制一份。如果将classcode加入会因为引用传递将原来的classcode覆盖
//            BeanUtils.copyProperties(params,toolParam);
            toolParam.setClassCode(classCode);
            List<String> statusList = userDefinedMapper.getStudentStatus(toolParam);
            //将学籍状态放入map
            Set<String> statusSet = getStatus(statusList);
            putValueIntoMap(definedMap, "scholastic", statusSet);
            putValueIntoMap(definedMap, "graduate", statusSet);
            //将班级转换成年份放入map
            definedMap.put("year", getYear(classList));
            //将学期放入map
            definedMap.put("semester", getSemesterByYear(definedMap.get("year")));
            //将班级放入map
            definedMap.put("classCode", classList);
            return definedMap;
        } else
            return null;

    }

    private void putValueIntoMap(Map<String, List<String>> map, String key, Object value) {
        List<String> nowList = map.get(key);
        if (value instanceof List) {
            if (nowList != null) {
                nowList.retainAll((ArrayList<String>) value);
                map.put(key, nowList);
            } else {
                map.put(key, null);
            }
        } else if (value instanceof Set) {
            if (nowList != null) {
                nowList.retainAll((Set<String>) value);
                map.put(key, nowList);
            } else {
                map.put(key, null);
            }
        }
    }

    /**
     * 由于缓存不能加载dao上因此写个方法来获取
     *
     * @param params
     * @return
     */
    private List<String> getClassCodeByParams(Params params) {
        return userDefinedMapper.getClasscodeByDefined(params);
    }

    /**
     * 根据学籍状态返回班级code
     *
     * @param params
     * @return
     */
    private List<String> getClassCodeByStatus(Params params) {
        return userDefinedMapper.getClasscodeByStatus(params);
    }

    /**
     * 根据入学年份来获得对应的班级列表
     *
     * @param yearList
     * @param params
     * @return
     */
    private Set<String> getClassCodeByYear(List<String> yearList, Params params) {
        Set<String> classCodeSet = new HashSet();
        Params param = new Params();
        BeanUtils.copyProperties(params, param);
        //循环符合条件的年份获取对应的班级，将班级放入set中去重。这么做的原因是因为有留级生这种情况有多个班级。因此对获取classcode的方法加了缓存
        for (String year : yearList) {
            param.setEnrollmentYear(year);
            classCodeSet.addAll(getClassCodeByParams(param));
        }
        return classCodeSet;
    }

    private List<String> getClasscodeBySemester(Integer semester) {
        return userDefinedMapper.getClasscodeBySemester(semester);
    }

    //将班级转换成入学年份
    private List<String> getYear(List<String> list) {
        Set<String> classSet = new LinkedHashSet<>();
        list.forEach(c -> {
            classSet.add("20" + c.substring(0, 2));
        });
        return new ArrayList(classSet);
    }

    /**
     * 根据班级查询入学年份
     */
    private Set<String> splitYear(Params params) {
        List<String> list = userDefinedMapper.getOwnYearByClasscode(params);
        Set<String> set = new LinkedHashSet<>();
        list.forEach(l -> {
            String[] year = l.split(",");
            for (String str : year) {
                //判断是否为空字符串,将非空年份放入set
                if (str.length() >= 2) {
                    set.add(str);
                }
            }
        });
        return set;
    }


    //将数据库的学籍状态转换成前端需要的字段;废弃
    private void getStatusFun(Map map, List<String> list) {
        Set<String> statusSet = new HashSet<>();
        Set<String> scholastic = new HashSet<>();
        statusSet.add("全部");
        list.forEach(s -> {
            String[] status = s.split(",");
            for (String str : status) {
                //判断是否为空字符串,将学籍状态和是否在籍放入对应的set
                if (str.length() >= 2) {
                    statusSet.add(StudentStatusEnum.getValue(str.substring(2, 3)));
                    scholastic.add(str.substring(0, 1));
                }
            }
        });
        //判断在籍的学生
        if (scholastic.contains("1")) {
            List<String> graduate = (List<String>) map.get("graduate");
            map.put("graduate", graduate.retainAll(statusSet));
        } else
            map.put("graduate", null);
        //判断不在籍的学生
        if (scholastic.contains("2")) {
            List<String> notInSchool = (List<String>) map.get("scholastic");
            map.put("scholastic", notInSchool.retainAll(statusSet));
        } else
            map.put("scholastic", null);

    }

    private Set<String> getClasscodeByMajor(Params params) {
        return userDefinedMapper.getClasscodeByMajor(params);
    }

    //将数字库学籍字段生成前端需要的格式
    private Set<String> getStatus(List<String> list) {
        Set<String> statusSet = new HashSet<>();
        list.forEach(s -> {
            String[] status = s.split(",");
            for (String str : status) {
                //判断是否为空字符串,将学籍状态和是否在籍放入对应的set
                if (str.length() >= 2) {
                    statusSet.add(StudentStatusEnum.getValue(str.substring(2, 3)));
                }
            }
        });
        if (statusSet.size() > 0)
            statusSet.add("全部");
        return statusSet;
    }

    //将入学年份最大的年份转换成学期；暂弃
    private void getSemesterByYear(Map<String, Object> map, List<String> list) {
        if (null != list && list.size() > 0) {
            Collections.sort(list, (Object o1, Object o2) -> {
                return Integer.valueOf(String.valueOf(o1)) - Integer.valueOf(String.valueOf(o2));
            });
            String maxYear = list.get(0);
            map.put("semester", getMaxSemester(maxYear));
        } else {
            map.put("semester", null);
        }
    }

    //放入年份list转换成对应的学期学期
    private List<String> getSemesterByYear(List<String> list) {
        if (null != list && list.size() > 0) {
            Collections.sort(list, (Object o1, Object o2) -> {
                return Integer.valueOf(String.valueOf(o1)) - Integer.valueOf(String.valueOf(o2));
            });
            String maxYear = list.get(0);
            return getMaxSemester(maxYear);
        } else
            return null;
    }

    //根据classcode获取该班级的最大学期list
    private List<String> getSemesterByClasscode(Params params) {
        int maxSemester = userDefinedMapper.getMaxSemesterByClasscode(params);
        return getSemesterList(maxSemester);
    }

    //获取当前年份所对应的学期数据
    private List<String> getMaxSemester(String year) {
        int semester = userDefinedMapper.getMaybeSemesterByYear(year);
        semester = semester > 8 ? 8 : semester;
        return getSemesterList(semester);
    }

    //将数字转换成对应的学期
    private List<String> getSemesterList(int max) {
        List<String> semesterList = new ArrayList();
        String[] strArr = {"一", "二", "三", "四", "五", "六", "七", "八"};
        for (int i = 0; i < max; i++) {
            semesterList.add("第" + strArr[i] + "学期");
        }
        return semesterList;
    }

    /**
     * 选择了学期，对入学年份的处理。返回符合的入学年份
     */
    private List<String> chooseSemester(Params params) {
        List<String> list = new ArrayList<>();
        List<String> yearList = userDefinedMapper.getYearByMaxSemester(params.getTermNum() - 1);
        Set<String> yearSet = new HashSet<>();
        yearList.forEach(y -> {
            String[] strArr = y.split(",");
            for (int i = 0; i < strArr.length; i++) {
                yearSet.add(strArr[i]);
            }
        });
        String maxYear = yearSet.stream().max((Object o1, Object o2) -> {
            return Integer.valueOf(String.valueOf(o1)) - Integer.valueOf(String.valueOf(o2));
        }).get();
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        //拼接剔除的年份
        for (int i = Integer.valueOf(maxYear); i >= 2005; i--) {
            list.add("" + i);
        }
        return list;
    }

    /**
     * 获取当前新生最大的学期数
     *
     * @return
     */
    private Integer getMaxSemesterOfNewStudent() {
        int newNumber = userDefinedMapper.getYearBySemester(0);
        return userDefinedMapper.getTotalSemester(String.valueOf(newNumber));
    }

    /**
     * 判断是否是新生；判断依据是当前月是否大于7.如果大于7判断year是否一致，如果小于7则比较year+1
     *
     * @param str
     * @return
     */
    private boolean isFreshman(String str) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        if (month >= 7)
            return StringUtils.equals(str, String.valueOf(year));
        else
            return StringUtils.equals(str, String.valueOf(year + 1));
    }

    /**
     * 如果选择的是入学年份选项，择对学期和学籍状态进行处理
     *
     * @param year
     * @param map
     */
    private void chooseYear(String year, Map map) {
        List<String> statusList = new ArrayList();
        //对学籍状态进行处理，如果是新生则不包含老生和留级生。老生则不包含新生
        if (isFreshman(year)) {
            statusList.add("留级");
            statusList.add("老生");
        } else {
            statusList.add("新生");
        }
    }

    @Override
    public List<Map<String, Object>> getEnrollmentYear() {
        return userDefinedMapper.getEnrollmentYear();
    }
}
