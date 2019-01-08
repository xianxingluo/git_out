package com.ziyun.auth.oauth2.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ziyun.common.constant.AuthConstant;
import com.ziyun.common.constant.Constant;
import com.ziyun.common.entity.User;
import com.ziyun.common.enums.StatusCodeEnum;
import com.ziyun.common.response.CommonResponse;
import com.ziyun.common.tools.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author leyangjie
 * @date 2018/11/19 15:03
 */
@Component
@Slf4j
public class MyAuthenticationSuccessHandle implements AuthenticationSuccessHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private AuthorizationServerTokenServices authorizationServerTokenServices;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 登录成功返回用户access_token
     *
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 生成OAuth2AccessToken对象
        OAuth2AccessToken accessToken = createAccessToken(request, authentication);
        String accessTokenValue = accessToken.getValue();
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        //用 : 将用户名和角色id 分割开
        String[] split = user.getUsername().split(":");
        String username = split[0];
        Long roleId = Long.valueOf(split[1]);
        //将用户名放到reids里 目的： 刷新token
        redisUtils.setUsernameCache(username, roleId, null);
        //从redis里面获取用户信息
        User resultUser = (User) redisUtils.getUserCache(username, roleId);
        //将access_token方法放到redis中
        redisUtils.setAccessTokenCache(username, roleId, accessTokenValue, null);
        //将refresh_token放到redis中
        String refreshToken = accessToken.getRefreshToken().getValue();
        redisUtils.setRefreshTokenCache(username, roleId, refreshToken, null);
        Map<String, Object> resultMap = new HashMap<>(Constant.INT_FOUR);
        resultMap.put("accessToken", accessToken);
        resultMap.put("user", resultUser);
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setStatusCode(StatusCodeEnum.OK.getKey());
        commonResponse.setData(resultMap);
        commonResponse.setMessage("登录成功！");
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(commonResponse));

    }

    private String[] extractAndDecodeHeader(String header, HttpServletRequest request) throws IOException {

        byte[] base64Token = header.substring(6).getBytes("UTF-8");
        byte[] decoded;
        try {
            decoded = Base64.getDecoder().decode(base64Token);
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException(
                    "Failed to decode basic authentication token");
        }

        String token = new String(decoded, "UTF-8");

        int delim = token.indexOf(":");

        if (delim == -1) {
            throw new BadCredentialsException("Invalid basic authentication token");
        }
        return new String[]{token.substring(0, delim), token.substring(delim + 1)};
    }

    private OAuth2AccessToken createAccessToken(HttpServletRequest request, Authentication authentication) throws IOException {
        /**
         * @see BasicAuthenticationFilter#doFilterInternal(HttpServletRequest, HttpServletResponse, javax.servlet.FilterChain)
         *  */
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith(AuthConstant.AUTH_HTTP_REQUEST_BASIC)) {
            // 不被认可的客户端异常
            throw new UnapprovedClientAuthenticationException("没有Authorization请求头！");
        }

        // 解析请Authorization 获取client信息
        // client-id: app
        // client-secret: app
        String[] tokens = extractAndDecodeHeader(header, request);
        assert tokens.length == 2;
        String clientId = tokens[0];
        String clientSecret = tokens[1];
        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
        // 判定提交的是否与查询的匹配

        if (clientDetails == null) {
            throw new UnapprovedClientAuthenticationException("clientId对应的配置信息不存在:" + clientId);
        } else if (!StringUtils.equals(clientDetails.getClientSecret(), clientSecret)) {
            throw new UnapprovedClientAuthenticationException("clientSecret不匹配:" + clientId);
        }

        /**  @see DefaultOAuth2RequestFactory#createTokenRequest(java.util.Map, ClientDetails)
         * requestParameters,不同的授权模式有不同的参数，这里自定义的模式，没有参数
         * String clientId,
         * Collection<String> scope, 给自己的前段使用，默认用所有的即可
         * String grantType 自定义
         *
         * */

        TokenRequest tokenRequest = new TokenRequest(new HashMap<>(Constant.INT_SIXTEEN), clientId, clientDetails.getScope(), "costom");
        OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);

        /**
         * @see org.springframework.security.oauth2.provider.token.AbstractTokenGranter#getOAuth2Authentication(ClientDetails, TokenRequest)
         * */
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);

        // 在后面测试的时候居然抛出了一个 事物异常 Could not open JDBC Connection for transaction; nested exception is ja
        // 我的数据库密码写错了，这个方法上加了一个@Transactional注解
        OAuth2AccessToken accessToken = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);
        return accessToken;
    }
}


