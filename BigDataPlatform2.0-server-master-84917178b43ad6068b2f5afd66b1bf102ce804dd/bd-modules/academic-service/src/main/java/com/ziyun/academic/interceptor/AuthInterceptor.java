package com.ziyun.academic.interceptor;


import com.ziyun.academic.filter.ShiroFilterUtils;
import com.ziyun.academic.server.OwnSchoolOrgServer;
import com.ziyun.academic.tools.LoggerUtils;
import com.ziyun.academic.vo.Params;
import com.ziyun.common.constant.AuthConstant;
import com.ziyun.utils.cache.CacheConstant;
import com.ziyun.utils.cache.TokenCasheManager;
import com.ziyun.utils.common.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 请求拦截器：对请求提取有数据权限的参数，进行数据权限判断。
 * 只能在此处做判断，等jdbc层的数据权限判断完成后，会影响缓存。
 * tyk
 */
public class AuthInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);

    //@Resource
    //RedisTemplate<String, List<String>> redisTemplate;

    @Autowired
    private OwnSchoolOrgServer ownSchoolOrgServer;

    /**
     * Handler执行完成之后调用这个方法
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exc)
            throws Exception {

    }

    /**
     * Handler执行之后，ModelAndView返回之前调用这个方法
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    /**
     * Handler执行之前调用这个方法
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 获取请求的URL
        String url = request.getRequestURI();
        // 不需要处理的url
        if (StringUtils.isBlank(url) || "/".equals(url)) {
            return true;
        }
        // 获取Session
        //HttpSession session = request.getSession();
        //获取权限
        String token = String.valueOf(request.getParameter(CacheConstant.TOKEN_CACHE));
        boolean isLogined = false;
        if (com.ziyun.utils.common.StringUtils.isNotEmpty(token)) {
            isLogined = TokenCasheManager.isLoginedByCache(token);
            if (!isLogined) {//没有登录给出提示
                LoggerUtils.debug(getClass(), "当前用户没有登录，并且是Ajax请求！");
                String message = "当前用户没有登录!";
                ShiroFilterUtils.returnJsonResult(response, message, String.valueOf(HttpServletResponse.SC_MULTIPLE_CHOICES));
                //IntegratedProcessUtil.dealNotLogin(request, response);
            }
        }
        Set<String> permission = TokenCasheManager.getPermissionsCache(token);
        if (permission != null) {
            //对请求提取有数据权限的参数，与数据权限进行比较，判断该请求是否有权限。//Map<String, String[]> getParameterMap()
            // String[]主要是为了适应页面表单提交为checkbox的情况。
            Map param = request.getParameterMap();
            //判断请求参数是否是permission是否子集？
            // 特殊情况：1、什么参数都不传等？2、分页参数排除3、：url/academic/student/getTopCategory 用exclude-mapping path处理4、班级多选参数

            List<String> authParam = filterParam(param);
            //如果经过参数过滤后，什么参数都没有
            if (CollectionUtils.isNotEmpty(authParam)) {
                //对参数去除空格；带有空格的时候权限比对不通过
                int size = authParam.size();
                for (int i = 0; i < size; i++) {
                    authParam.set(i, authParam.get(i).trim());
                }
                if (permission.containsAll(authParam)) {
                    logger.info("url：" + url + "  has permissions.");
                    return true;
                }
            } else {
                //获取当前用户账号,登录的时候 存放在session中
                //取出存放在redis中的该账号的组织机构，
                Set<String> organ = TokenCasheManager.getDataPermissionsCache(token);
                //判断是否存入redis，如果没有取出当前学校所有的班级。与该账号所拥有的班级做比对(因为读取的权限包含菜单和数据权限，无法单独区分)，获取该用户所含的班级
                if (organ == null) {
                    organ = new HashSet<>();
                    //获取所有的班级。
                    Set<String> classSet = ownSchoolOrgServer.selectOwnClasscode(new Params());
                    //比对权限将权限中有的classcode放进organ中
                    classSet.retainAll(permission);
                    organ.addAll(classSet);
                    //将本次操作放入redis，存放的key是用户账号
                    TokenCasheManager.setDataPermissionsCache(token, organ);
                }

                //判断organ的size如果size > 0则代表有数据权限。否则认定为没有数据权限
                if (organ.size() > 0) {
                    return true;
                } else {
                    return false;
                }

            }
        }


        //IntegratedProcessUtil.dealNoPerssion(request, response);
        LoggerUtils.debug(getClass(), "当前用户没有权限，并且是Ajax请求！");
        String message = "当前用户没有权限!";
        ShiroFilterUtils.returnJsonResult(response, message, String.valueOf(HttpServletResponse.SC_UNAUTHORIZED));
        logger.info("url：" + url + "  do not has permissions.");
        return false;
    }

    /* *//**
     * 如果什么都不传，怎么判断权限？至少要有一个数据权限。数据权限，目前是可以转换为整形；菜单权限，目前传的是字符串。这种方法难以维护，故不采用。
     * @param permission
     * @return
     *//*
    private boolean hasDataPermission(Set<String> permission) {
        int i = 0;
        for (String s : permission) {
            try {
                long dataPermission = Long.parseLong(s);
                i++;
                break;
            } catch (NumberFormatException numberFormatException) {
                //do nothing
                //意义：过滤菜单权限。
            }
        }
        if (i > 0) {
            return true;
        }
        return false;
    }*/

    /**
     * liquan 建议：如果什么都不传，默认为查所有。如果查所有，可以通过permission包含0来判断。
     * TODO:这段代码这么写，则查询条件得做同步修改，除非此人有所有权限，不然在查全部时，需要将对应权限字段传过来。
     *
     * @param permission
     * @return
     */
    private boolean hasDataAllPermission(Set<String> permission) {
        boolean flag = false;
        for (String s : permission) {
            if ("0".equals(s)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * 过滤请求参数，只保留跟{数据}权限相关的参数：
     * 1、校区、院系、专业
     * 2、班级多选参数处理
     *
     * @param param
     * @return
     */
    private List<String> filterParam(Map<String, String[]> param) {
        if (param == null) {
            return null;
        }
        List<String> authParam = new ArrayList<>();
        for (Map.Entry<String, String[]> entry : param.entrySet()) {
            String key = entry.getKey();
            String[] value = entry.getValue();
            // 对code的内容也做了判断，为空不做处理
            if (Arrays.asList(AuthConstant.PARAM_DATA_PERMISSION).contains(key) && StringUtils.isNotBlank(value[0])) {
                authParam.add(value[0]);
            }
            // 班级做特殊处理
            if (AuthConstant.PARAM_CLASS_CODE.equals(key)) {
                String[] classCode = getClassCode(value[0]);
                if (classCode != null) {
                    authParam.addAll(Arrays.asList(classCode));
                }
            }
        }
        return authParam;
    }

    /**
     * 将班级多选查询条件 转换为数组
     *
     * @param classSelect
     * @return
     */
    private String[] getClassCode(String classSelect) {
        if (StringUtils.isNotBlank(classSelect)) {
            String str = classSelect;
            if (str.startsWith(",")) {// 班级编码：去掉头、尾的逗号
                str = str.substring(1, str.length());
            }
            if (str.endsWith(",")) {// 班级编码：去掉头、尾的逗号
                str = str.substring(0, str.length() - 1);
            }
            String[] array = str.split(",", -1);
            return array;
        } else {
            return null;
        }
    }

    public static void main(String[] args) {
        Map<Object, Object> authParam = new HashMap<>();

        if (authParam.isEmpty()) {
            System.out.println("empty");
        }
    }

    ////redis存入组织机构对应的classcode
    //private void setOrgan(List<String> organ,String key){
    //	redisTemplate.opsForValue().set(key, organ);
    //}
    ////redis取出组织机构对应的classcode
    //private List<String> getOrgan(String key){
    //	return redisTemplate.opsForValue().get(key);
    //}
}
