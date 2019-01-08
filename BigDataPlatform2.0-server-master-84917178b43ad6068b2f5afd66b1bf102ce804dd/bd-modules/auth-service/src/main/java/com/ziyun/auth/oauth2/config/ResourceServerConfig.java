package com.ziyun.auth.oauth2.config;


import com.ziyun.auth.oauth2.authentication.MyAuthenticationFailureHandle;
import com.ziyun.auth.oauth2.authentication.MyAuthenticationSuccessHandle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;


/**
 * 资源服务器配置类
 *
 * @author leyangjie
 * @date 2018/11/19 9:27
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    /**
     * 登录成功处理Handle
     */
    @Autowired
    private MyAuthenticationSuccessHandle successHandle;

    /**
     * 登录失败处理Handle
     */
    @Autowired
    private MyAuthenticationFailureHandle failureHandle;

    /**
     * 直接放行路径
     */
    private final String[] AUTH_WHITELIST = {
            "/authentication/form",
            "/login.html",
            "/oauth/token",
            "/v2/auth/role/getRolesByUser",
            "/login"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/login1")
                .loginProcessingUrl("/authentication/form")
                .successHandler(successHandle)
                .failureHandler(failureHandle)
                .and()
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .csrf().disable();
    }

}
