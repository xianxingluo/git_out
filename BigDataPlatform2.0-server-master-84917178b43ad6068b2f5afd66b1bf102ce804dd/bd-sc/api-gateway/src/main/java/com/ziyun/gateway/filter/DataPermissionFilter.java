package com.ziyun.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.ziyun.common.constant.AuthConstant;
import com.ziyun.common.constant.Constant;
import com.ziyun.common.entity.User;
import com.ziyun.common.enums.StatusCodeEnum;
import com.ziyun.common.tools.RedisUtils;
import com.ziyun.gateway.response.SimpleResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @description: 数据权限过滤器
 * @author: FubiaoLiu
 * @date: 2018/9/27
 */
public class DataPermissionFilter extends ZuulFilter {
    private static Logger log = LoggerFactory.getLogger(DataPermissionFilter.class);

    @Autowired
    private RedisUtils redisUtils;

    @Value("${request.url.selectAllClassCode}")
    private String requestURL;

    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        if (!ctx.sendZuulResponse()) {
            return false;
        }
        HttpServletRequest request = ctx.getRequest();
        String url = request.getRequestURL().toString();
        if (url.endsWith(AuthConstant.SWAGGER2_API)
                || url.endsWith(AuthConstant.URL_LOGIN)
                || url.endsWith(AuthConstant.URL_LOGOUT)
                || url.endsWith(AuthConstant.URL_GET_ROLE_BY_USER)) {
            return false;
        }
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        log.info("--->>> DataPermissionFilter: send {} request to {}", request.getMethod(), request.getRequestURL().toString());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 校验令牌
        if (authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            String[] split = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername().split(":");
            String username = split[0];
            Long roleId = Long.valueOf(split[1]);
            User user = redisUtils.getUserCache(username, roleId);
            // 如果user为空，表示未登录或登录过期
            if (null != user) {
                Set<String> classSet = user.getClassPermissionSet();
                // classSet为空，则没有权限
                if (CollectionUtils.isNotEmpty(classSet)) {
                    List<String> authParam = filterParam(request);
                    // 参数不为空，用户数据权限和输入参数 取交集
                    if (CollectionUtils.isNotEmpty(authParam)) {
                        /*if (dataPermissionList.containsAll(authParam)) {}*/
                        // 交集为空则没有权限
                        classSet.retainAll(authParam);
                        if (!classSet.isEmpty()) {
                            // 数据权限classSet，用户名，角色ID 作为参数放到请求体，转发到具体微服务
                            return setRequest(ctx, request, classSet, user);
                        } else {
                            ctx.addZuulResponseHeader("Content-Type", "text/html;charset=utf-8");
                            ctx.setSendZuulResponse(false);
                            ctx.setResponseStatusCode(StatusCodeEnum.UNSUPPORTED.getKey());
                            ctx.setResponseBody(StatusCodeEnum.UNSUPPORTED.getValue());
                            return null;
                        }
                    } else {
                        // 数据权限classSet，用户名，角色ID 作为参数放到请求体，转发到具体微服务
                        return setRequest(ctx, request, classSet, user);
                    }
                }
            }
        }

        ctx.addZuulResponseHeader("Content-Type", "text/html;charset=utf-8");
        ctx.setSendZuulResponse(false);
        ctx.setResponseStatusCode(HttpServletResponse.SC_OK);
        SimpleResponse simpleResponse = new SimpleResponse();
        simpleResponse.setMessage(StatusCodeEnum.NO_LOGGED.getValue());
        simpleResponse.setStatusCode(StatusCodeEnum.NO_LOGGED.getKey());
        try {
            ctx.setResponseBody(objectMapper.writeValueAsString(simpleResponse));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 过滤请求参数，只保留跟{数据}权限相关的参数：
     * 1、校区、院系、专业
     * 2、班级多选参数处理
     *
     * @param request
     * @return
     */
    private List<String> filterParam(HttpServletRequest request) {
        String schoolCode = request.getParameter(AuthConstant.SCHOOL_CODE);
        String facultyCode = request.getParameter(AuthConstant.FACULTY_CODE);
        String majorCode = request.getParameter(AuthConstant.MAJOR_CODE);
        String classSelect = request.getParameter(AuthConstant.CLASS_SELECT);
        if (StringUtils.isNotBlank(schoolCode) || StringUtils.isNotBlank(facultyCode) || StringUtils.isNotBlank(majorCode) || StringUtils.isNotBlank(classSelect)) {

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", "Basic YXBwOmFwcA==");
            //设置类型
            httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            //设置请求体参数
            MultiValueMap map = new LinkedMultiValueMap();
            if (StringUtils.isNotBlank(schoolCode)) {
                map.add(AuthConstant.SCHOOL_CODE, schoolCode);
            }
            if (StringUtils.isNotBlank(facultyCode)) {
                map.add(AuthConstant.FACULTY_CODE, facultyCode);
            }
            if (StringUtils.isNotBlank(majorCode)) {
                map.add(AuthConstant.MAJOR_CODE, majorCode);
            }
            if (StringUtils.isNotBlank(classSelect)) {
                map.add(AuthConstant.CLASS_CODE, classSelect);
            }
            HttpEntity entity = new HttpEntity(map, httpHeaders);
            //获取所有的班级
            Set set = restTemplate.postForObject(requestURL, entity, Set.class);
            if (CollectionUtils.isNotEmpty(set)) {
                return new ArrayList<>(set);
            }
            log.info("班级集合：{}", set);
        }
        return new ArrayList<>();
    }

    /**
     * 将班级多选查询条件 转换为数组
     *
     * @param classSelect
     * @return
     */
    private String[] getClassCode(String classSelect) {
        if (StringUtils.isBlank(classSelect)) {
            return null;
        }
        String str = classSelect;
        // 班级编码：去掉头、尾的逗号
        if (str.startsWith(Constant.COMMA_CHART)) {
            str = str.substring(1, str.length());
        }
        if (str.endsWith(Constant.COMMA_CHART)) {
            str = str.substring(0, str.length() - 1);
        }
        return str.split(Constant.COMMA_CHART, -1);
    }

    private List<String> getShortYearMap() {
        List<String> list = new ArrayList<>();
        // 获取当前年份，取后两位，并转换成Integer类型
        Integer shortYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()).substring(2, 4));
        while (shortYear >= Constant.INT_THIRTEEN) {
            list.add(shortYear + Constant.PERCENT_SIGN);
            shortYear--;
        }
        return list;
    }

    /**
     * 设置request参数列表
     *
     * @param ctx
     * @param request
     * @param classSet classCode数据权限集合
     * @param user     当前登录用户
     * @return
     */
    private Object setRequest(RequestContext ctx, HttpServletRequest request, Set<String> classSet, User user) {
        // 关键步骤，一定要get一下，下面这行代码才能取到值
        request.getParameterMap();
        Map<String, List<String>> requestQueryParams = ctx.getRequestQueryParams();

        if (null == requestQueryParams) {
            requestQueryParams = new HashMap<>(Constant.INT_EIGHT);
        }
        //去掉classSelect, 不然选择 某一级查询有影响
        if (requestQueryParams.containsKey(AuthConstant.CLASS_SELECT)) {
            requestQueryParams.remove(AuthConstant.CLASS_SELECT);
        }


        //学历的处理： 目前数据只有本科，如果选择其他，默认设置为2，今后根据具体的数据，在做修改
        if (requestQueryParams.containsKey(AuthConstant.EDUCATION) && StringUtils.isNotBlank(requestQueryParams.get(AuthConstant.EDUCATION).get(0)) && !requestQueryParams.get(AuthConstant.EDUCATION).get(0).equals(Constant.STRING_ONE)) {
            requestQueryParams.put(AuthConstant.EDUCATION, new ArrayList<>(Arrays.asList("2")));
        }
        List<String> roleIdList = new ArrayList<>();
        roleIdList.add(user.getRoleId().toString());
        requestQueryParams.put(AuthConstant.PARAM_CLASS_CODE, new ArrayList<>(classSet));
        requestQueryParams.put(AuthConstant.LOGON_PARAM_USERNAME, new ArrayList<>(Arrays.asList(user.getUsername())));
        requestQueryParams.put(AuthConstant.LOGON_PARAM_USER_ID, new ArrayList<>(Arrays.asList(user.getId().toString())));
        requestQueryParams.put(AuthConstant.LOGON_PARAM_ROLE_ID, roleIdList);
        Integer isSuperAdmin = user.getIsSuperAdmin();
        if (null != isSuperAdmin && isSuperAdmin == 1) {
            List<String> superAdminList = new ArrayList<>();
            superAdminList.add(user.getIsSuperAdmin().toString());
            requestQueryParams.put(AuthConstant.LOGON_PARAM_IS_SUPER_ADMIN, superAdminList);
        }
        ctx.setRequestQueryParams(requestQueryParams);

        ctx.setSendZuulResponse(true);
        ctx.setResponseStatusCode(HttpServletResponse.SC_OK);
        ctx.set("isSuccess", true);
        return null;
    }


}
