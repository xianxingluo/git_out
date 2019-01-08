package com.ziyun.gateway.config;

import com.ziyun.gateway.config.custom.CustomAuth2EntryPoint;
import com.ziyun.gateway.config.custom.CustomAuth2ResponseTranslate;
import com.ziyun.gateway.service.MyAccessDecisionManager;
import com.ziyun.gateway.service.MyFilterSecurityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * @author leyangjie
 * @date 2018/11/21 19:51
 */
@Configuration
@EnableResourceServer
@EnableWebSecurity
public class WebSecurityConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private OAuth2WebSecurityExpressionHandler webSecurityExpressionHandler;

    @Autowired
    private MyAccessDecisionManager myAccessDecisionManager;
    /**
     * 直接放行路径
     */
    private final String[] AUTH_WHITELIST = {
            /**
             * swagger
            */
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/**/v2/api-docs",
            "/webjars/**",
            /**
             * 权限
            */
            "/auth/authentication/form"
    };

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .and()
                .csrf().disable().exceptionHandling()
                // 定义的不存在access_token时候响应
                .authenticationEntryPoint(new CustomAuth2EntryPoint());
        http.addFilterBefore(myFilterSecurityInterceptor(), FilterSecurityInterceptor.class);
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.expressionHandler(webSecurityExpressionHandler);
        AuthenticationEntryPoint entryPoint = new OAuth2AuthenticationEntryPoint();
        ((OAuth2AuthenticationEntryPoint) entryPoint).setExceptionTranslator(new CustomAuth2ResponseTranslate());
        resources.authenticationEntryPoint(entryPoint);
    }

    @Bean
    public TokenStore tokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

    @Bean
    public OAuth2WebSecurityExpressionHandler oAuth2WebSecurityExpressionHandler(ApplicationContext applicationContext) {
        OAuth2WebSecurityExpressionHandler expressionHandler = new OAuth2WebSecurityExpressionHandler();
        expressionHandler.setApplicationContext(applicationContext);
        return expressionHandler;
    }

    @Bean
    public MyFilterSecurityInterceptor myFilterSecurityInterceptor() {
        MyFilterSecurityInterceptor myFilterSecurityInterceptor = new MyFilterSecurityInterceptor();
        myFilterSecurityInterceptor.setMyAccessDecisionManager(myAccessDecisionManager);
        return myFilterSecurityInterceptor;
    }

}
