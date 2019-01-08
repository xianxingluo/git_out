package com.ziyun.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.ziyun.common.constant.AuthConstant;
import com.ziyun.common.constant.Constant;
import com.ziyun.common.enums.StatusCodeEnum;
import com.ziyun.common.tools.RedisUtils;
import com.ziyun.gateway.response.SimpleResponse;
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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 权限过滤器
 * @author: FubiaoLiu
 * @date: 2018/9/27
 */
public class AuthFilter extends ZuulFilter {
    private static Logger log = LoggerFactory.getLogger(AuthFilter.class);

    @Value("${auth.accessToken.url}")
    public String accessTokenUrl;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = null;
        if (authentication.getPrincipal() instanceof User) {
            user = (User) authentication.getPrincipal();
            String[] split = user.getUsername().split(":");
            String username = split[0];
            Long roleId = Long.valueOf(split[1]);
            //获取access_token
            String accessToken = redisUtils.getAccessTokenCache(username, roleId);
            if (StringUtils.isBlank(accessToken)) {
                //access_token已经过期，从redis中获取用户名
                String redisUsername = redisUtils.getUsernameCache(username, roleId);
                if (StringUtils.isBlank(redisUsername)) {
                    try {
                        //用户名也过期，跳转到登录页面
                        log.warn("用户未登录！");
                        ctx.addZuulResponseHeader("Content-Type", "text/html;charset=utf-8");
                        ctx.setSendZuulResponse(false);
                        ctx.setResponseStatusCode(HttpServletResponse.SC_OK);
                        SimpleResponse simpleResponse = new SimpleResponse();
                        simpleResponse.setStatusCode(StatusCodeEnum.NO_LOGGED.getKey());
                        simpleResponse.setMessage(StatusCodeEnum.NO_LOGGED.getValue());
                        ctx.setResponseBody(objectMapper.writeValueAsString(simpleResponse));
                        return null;
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        //从redis里面获取refresh_token，通过refresh_token重新获取access_token
                        String refreshToken = redisUtils.getRefreshTokenCache(username, roleId);
                        // 获取access_token代码
                        //设置请求头
                        HttpHeaders httpHeaders = new HttpHeaders();
                        httpHeaders.add("Authorization", "Basic YXBwOmFwcA==");
                        //设置类型
                        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

                        //设置请求体参数
                        MultiValueMap map = new LinkedMultiValueMap();
                        map.add("grant_type", "refresh_token");
                        map.add("refresh_token", refreshToken);
                        map.add("roleId", roleId + "");
                        HttpEntity entity = new HttpEntity(map, httpHeaders);
                        //获取新的 OAuth2AccessToken对象
                        OAuth2AccessToken auth2AccessToken = restTemplate.postForObject(accessTokenUrl, entity, OAuth2AccessToken.class);
                        log.info("{}方法换取新token:{} 时间:{}", request.getRequestURL(), auth2AccessToken.getValue(), LocalTime.now());
                        redisUtils.setAccessTokenCache(username, roleId, auth2AccessToken.getValue(), null);
                        log.info("auth2AccessToken:{}", auth2AccessToken);
                        //重置用户名在redis的过期时间
                        redisUtils.expireUsernameCache(username, roleId);
                        redisUtils.setAccessTokenCache(username, roleId, auth2AccessToken.getValue(), null);
                        //获取到新的access_token返回给前端，并将URL和URL参数一起发给前端，前端拿着这些重新刷新页面
                        log.info("返回新的access_token给前端！");
                        ctx.addZuulResponseHeader("Content-Type", "text/html;charset=utf-8");
                        ctx.setSendZuulResponse(false);
                        ctx.setResponseStatusCode(StatusCodeEnum.OK.getKey());
                        Map<String, Object> resultMap = new HashMap<>(Constant.INT_FOUR);
                        resultMap.put("url", request.getRequestURI());
                        Map<String, String[]> parameterMap = request.getParameterMap();
                        resultMap.put("parameterMap", parameterMap);
                        resultMap.put("method", request.getMethod().toLowerCase());
                        ctx.setResponseBody(objectMapper.writeValueAsString(new SimpleResponse(resultMap, auth2AccessToken.getValue())));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } else {
                //access_token未过期，重置用户名在redis的过期时间
                redisUtils.expireUsernameCache(username, roleId);
                redisUtils.expireUserCache(username, roleId);
                // 进行路由，调用api服务提供者
                ctx.setSendZuulResponse(true);
                return null;
            }

        }
        return null;
    }

}
