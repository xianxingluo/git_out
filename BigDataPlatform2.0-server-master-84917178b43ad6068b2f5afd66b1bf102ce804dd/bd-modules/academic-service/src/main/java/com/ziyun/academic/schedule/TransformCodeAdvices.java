package com.ziyun.academic.schedule;


import com.ziyun.academic.server.OwnSchoolOrgServer;
import com.ziyun.academic.tools.CalendarUtils;
import com.ziyun.academic.tools.ParamUtils;
import com.ziyun.academic.vo.Params;
import com.ziyun.utils.common.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Aspect
//@Component("transformAdvice")
public class TransformCodeAdvices {


    @Autowired
    private OwnSchoolOrgServer ownSchoolOrgServer;

    //设置切点匹配controller包下以AOP结尾的所有方法
    @Pointcut("execution(* com.ziyun.academic.controller.*.*AOP(..))")
    public void aspectjMethod() {
    }


    public void filterYearData(Params param) {
        //过滤年份，只显示2013年及之后的数据
        String[] yearArr = param.getYearArr();
        List<String> yearFilterList = new ArrayList<>();
        //1. 选择了年份，过滤掉2013之前的年份
        if (yearArr != null && yearArr.length != 0) {
            for (String elementStr : yearArr) {
                if (Integer.parseInt(elementStr) >= 2013) {
                    yearFilterList.add(elementStr);
                }
            }
            //传过来的年份没有13年之后的，返回13到当前所有年份
            if (yearFilterList.size() != 0) {
                String[] array = new String[yearFilterList.size()];
                param.setYearArr(yearFilterList.toArray(array));
            } else {
                param.setYearArr(CalendarUtils.getFilterYear(2013));
            }
        } else {//2. 未选择年份，默认添加2013到现在的年份
            param.setYearArr(CalendarUtils.getFilterYear(2013));
        }
    }

    //由于要改变方法的参数内容。before只有获得参数无法改变参数因此采用around
//    @Around(value = "aspectjMethod()")
//    public Object aroundMethod(ProceedingJoinPoint jp) {
//        Object obj = null;
//        try {
//            //获取参数。由于拦截的都是param的参数因此args都是有参数的。如果controller没有参数则args长度会变成0
//            Object[] args = jp.getArgs();
//            //如果参数名 classSelect 中包含 ‘级’字，就替换成""
//            if (args != null && args.length != 0 && args[0] instanceof Params) {
//                Params params = ((Params) args[0]);
//                if (StringUtils.isNotBlank(params.getClassSelect()) && params.getClassSelect().indexOf("级") != -1) {
//                    params.setClassSelect(params.getClassSelect().replace("级", ""));
//                }
//            }
//            // 生成HttpServletRequest对象
//            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//            //HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
//            // 获取权限
//            String token = String.valueOf(request.getParameter(CacheConstant.TOKEN_CACHE));
//            Set<String> permissions = TokenCasheManager.getPermissionsCache(token);
//            if (permissions != null) {
//                Params param = (Params) args[0];
//                //年份过滤
//                //  filterYearData(param);
//
//                //所有请求接口都需要进行权限检查
//                if (param.getOutid() == null) {
//                    //查询当前选项所包含的所有班级
//                    Set<String> classcodeList = ownSchoolOrgServer.selectOwnClasscode(param);
//                    // 取交集
//                    classcodeList.retainAll(permissions);
//                    //如果长度不一致则没有全部权限因此需要转换成code
//                    if (410 != classcodeList.size()) {
//                        String[] strArr = classcodeList.toArray(new String[]{});
//                        Arrays.sort(strArr);
//                        // 只清空组织机构
//                        ParamUtils.emptyParamsCode(param);
//                        param.setClassCode(strArr);
//                    }
//                } else {
//                    paramResetNull(param);
//
//                }
//                args[0] = param;
//                //将转换后的参数放入方法
//                obj = jp.proceed(args);
//            } else {
//                LoginManager.logout();
//            }
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//        return obj;
//    }


    @Around(value = "aspectjMethod()")
    public Object aroundMethod(ProceedingJoinPoint jp) {
        Object obj = null;
        try {
            //获取参数。由于拦截的都是param的参数因此args都是有参数的。如果controller没有参数则args长度会变成0
            Object[] args = jp.getArgs();
            //如果参数名 classSelect 中包含 ‘级’字，就替换成""
            if (args != null && args.length != 0 && args[0] instanceof Params) {
                Params params = ((Params) args[0]);
                if (StringUtils.isNotBlank(params.getClassSelect()) && params.getClassSelect().indexOf("级") != -1) {
                    params.setClassSelect(params.getClassSelect().replace("级", ""));
                }
            }
            Params param = (Params) args[0];
            //年份过滤
            //  filterYearData(param);
            //所有请求接口都需要进行权限检查
            if (param.getOutid() == null) {
                //查询当前选项所包含的所有班级
                Set<String> classcodeList = ownSchoolOrgServer.selectOwnClasscode(param);
                // 取交集
                //如果长度不一致则没有全部权限因此需要转换成code
                if (410 != classcodeList.size()) {
                    String[] strArr = classcodeList.toArray(new String[]{});
                    Arrays.sort(strArr);
                    // 只清空组织机构
                    ParamUtils.emptyParamsCode(param);
                    param.setClassCode(strArr);
                }
            } else {
                paramResetNull(param);

            }
            args[0] = param;
            //将转换后的参数放入方法
            obj = jp.proceed(args);

        } catch (Throwable e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 将param对象的schoolCode,facultyCode,majorCode,classSelect 重新设置为null
     *
     * @param param
     */
    private void paramResetNull(Params param) {
        if (StringUtils.isNotBlank(param.getSchoolCode())) {
            param.setSchoolCode(null);
        }
        if (StringUtils.isNotBlank(param.getFacultyCode())) {
            param.setFacultyCode(null);
        }
        if (StringUtils.isNotBlank(param.getMajorCode())) {
            param.setMajorCode(null);
        }
        if (StringUtils.isNotBlank(param.getClassSelect())) {
            param.setClassSelect(null);
        }
    }

}
