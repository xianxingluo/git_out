package com.ziyun.auth.controller;

import com.ziyun.auth.constant.AuthConstant;
import com.ziyun.auth.constant.CacheConstant;
import com.ziyun.auth.constant.Constant;
import com.ziyun.auth.service.IUserService;
import com.ziyun.common.entity.User;
import com.ziyun.common.response.CommonResponse;
import com.ziyun.common.tools.RedisUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @description: 登录认证的控制器
 * @author: FubiaoLiu
 * @date: 2018/9/18
 */
@Api(tags = "系统登录", description = "系统登录api")
@Controller
@RequestMapping("/v2/login")
@Slf4j
public class LoginController {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IUserService userService;

    @Autowired
    private ConsumerTokenServices consumerTokenServices;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${auth.accessToken.url}")
    private String accessTokenUrl;

    @ApiOperation(value = "用户登录(作废)", notes = "用户登录", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "username", dataType = "String", value = "用户名"),
            @ApiImplicitParam(paramType = "query", name = "password", dataType = "String", value = "密码")
    })
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Map login(User user, HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = new HashMap<>(Constant.INT_EIGHT);

        try {
            User queryUser = userService.getUser(user.getUsername());
            // 帐号不存在、密码错误
            if (null == queryUser || !user.getPassword().equals(queryUser.getPassword())) {
                resultMap.put("statusCode", 500);
                resultMap.put("message", "帐号或密码错误！");
                return resultMap;
            }
            //账号是否禁用，0：禁用，1：启用
            if (queryUser.getStatus() == 0) {
                resultMap.put("statusCode", 500);
                resultMap.put("message", "帐号已经禁用。");
                return resultMap;
            }
            resultMap.put("statusCode", 200);
            resultMap.put("message", "登录成功");
            //将用户的账号base64转码后存入redis，并返回给前端
            String tokenKey = generateToken(user.getUsername(), user.getPassword());
            this.addUserCache(tokenKey, queryUser);
            //将这个base返回给前端
            resultMap.put(CacheConstant.TOKEN_CACHE, tokenKey);

            String url = null;
            // 如果登录之前没有地址，那么就跳转到首页。
            if (StringUtils.isBlank(url) || ("/".equals(url))) {
                url = request.getContextPath() + "/index";
            }
            // 跳转地址
            resultMap.put("back_url", url);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("statusCode", 500);
            resultMap.put("message", "帐号或密码错误！");
        }
        return resultMap;
    }

    /**
     * 退出系统
     *
     * @param
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "退出系统(作废)", notes = "退出系统", httpMethod = "POST")
    @ApiImplicitParam(paramType = "query", name = "token", dataType = "String", value = "token")
    @RequestMapping(value = "/logout")
    @ResponseBody
    public CommonResponse logout(User user, HttpServletRequest request) throws Exception {
        try {
            String accessToken = user.getAccess_token();
            request.setAttribute("access_token", accessToken);
            destroyAccessToken(request);
            redisUtils.destroyUserInfoCache(user.getUsername(), user.getRoleId());
            return CommonResponse.success("退出成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResponse.failure("退出失败!");
        }
    }

    /**
     * 退出系统
     *
     * @param token
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "退出系统(作废)", notes = "退出系统", httpMethod = "POST")
    @ApiImplicitParam(paramType = "query", name = "token", dataType = "String", value = "token")
    @RequestMapping(value = "/logoutAjax")
    @ResponseBody
    public Map logoutAjax(String token) throws Exception {
        deleteUserCache(token);
        Map<String, Object> resultMap = new HashMap<>(Constant.INT_TWO);
        resultMap.put("success", true);
        return resultMap;
    }

    /**
     * 生成token：根据用户名密码(作废)
     *
     * @param username 用户名
     * @param password 密码
     * @return
     */
    public static String generateToken(String username, String password) {
        //将用户的账号base64转码后存入redis，并返回给前端
        String nameBase = Base64.getEncoder().encodeToString((username + CacheConstant.DELIMITER_CACHE + password + AuthConstant.SIGNING_KEY).getBytes(Charset.forName("UTF-8")));
        return nameBase;
    }

    /**
     * （缓存）添加：当前登录的用户（SysUser）(作废)
     *
     * @return
     */
    public void addUserCache(String token, User user) {
        String key = CacheConstant.USER_CACHE + CacheConstant.DELIMITER_CACHE + token;
        // 修改RedisTemplate的序列化策略，解决乱码问题
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.opsForValue().set(key, user, CacheConstant.LOGIN_INFO_TIMEOUT, TimeUnit.SECONDS);
    }

    /**
     * （缓存）删除：当前登录的用户（SysUser）(作废)
     *
     * @return
     */
    public void deleteUserCache(String token) {
        redisTemplate.delete(CacheConstant.USER_CACHE + CacheConstant.DELIMITER_CACHE + token);
    }

    /**
     * 注销access_token接口
     *
     * @param request
     * @return
     */
    @RequestMapping("/destroy/accessToken")
    @ApiOperation(value = "退出系统", notes = "退出系统", httpMethod = "POST")
    @ApiImplicitParam(paramType = "query", name = "access_token", dataType = "String", value = "access_token")
    @ResponseBody
    public CommonResponse destroyAccessToken(HttpServletRequest request) {
        String accessToken = request.getParameter("access_token");
        if (consumerTokenServices.revokeToken(accessToken)) {
            return CommonResponse.success("注销成功！");
        } else {
            return CommonResponse.failure("注销失败！");
        }
    }

    @RequestMapping("/changeRole")
    @ApiOperation(value = "切换角色", notes = "切换角色", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "access_token", dataType = "String", value = "access_token"),
            @ApiImplicitParam(paramType = "query", name = "userId", dataType = "String", value = "用户id"),
            @ApiImplicitParam(paramType = "query", name = "roleId", dataType = "String", value = "角色id")
    })
    @ResponseBody
    public CommonResponse changeRole(User user, HttpServletRequest request) {
        try {
            //获取token，并销毁token
            String accessToken = user.getAccess_token();
            request.setAttribute("access_token", accessToken);
            destroyAccessToken(request);
            //通过username，password，roleId获取新的access_token
            User user1 = userService.selectByKey(user.getUserId());
            if (user.getRoleId() != null) {
                user1.setRoleId(user.getRoleId());
            }
            //发送rest请求获取access_token
            //设置请求头
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", "Basic YXBwOmFwcA==");
            //设置类型
            httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            //设置请求体参数
            MultiValueMap map = new LinkedMultiValueMap();
            map.add("grant_type", "password");
            map.add("username", user1.getUsername());
            map.add("password", user1.getPassword());
            //这里角色id使用的String类型，如果用原始的Long类型，会出现类型转换异常
            map.add("roleId", user1.getRoleId().toString());
            HttpEntity entity = new HttpEntity(map, httpHeaders);
            //获取新的 OAuth2AccessToken对象
            OAuth2AccessToken auth2AccessToken = restTemplate.postForObject(accessTokenUrl, entity, OAuth2AccessToken.class);
            User resultUser = redisUtils.getUserCache(user1.getUsername(), user1.getRoleId());
            redisUtils.setAccessTokenCache(user1.getUsername(), user1.getRoleId(), auth2AccessToken.getValue(), null);
            redisUtils.setUsernameCache(user1.getUsername(), user1.getRoleId(), null);
            Map<String, Object> resultMap = new HashMap<>(Constant.INT_FOUR);
            resultMap.put("accessToken", auth2AccessToken.getValue());
            resultMap.put("user", resultUser);
            return CommonResponse.success(resultMap);
        } catch (HttpClientErrorException e) {
            log.error(e.getMessage());
            return CommonResponse.unauthorized();
        } catch (RestClientException e) {
            log.error(e.getMessage());
            return CommonResponse.failure("角色切换失败！");
        }
    }

}
