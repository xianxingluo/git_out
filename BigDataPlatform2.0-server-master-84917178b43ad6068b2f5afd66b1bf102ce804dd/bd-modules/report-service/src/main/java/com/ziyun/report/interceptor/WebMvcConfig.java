package com.ziyun.report.interceptor;

import com.ziyun.utils.date.DateConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.ziyun.report.constant.Constants.URL_EXCLUDE_INTERCEPT;
import static com.ziyun.report.constant.Constants.URL_INTERCEPT;

/**
 * 注册拦截器
 * Created by  Zeng on 2018/4/23.
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new DateConverter());
    }

    @Bean
    public ExecutorService executorService() {
        return Executors.newCachedThreadPool();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册自定义拦截器，添加拦截路径和排除拦截路径
        registry.addInterceptor(new AuthInterceptor())
                .addPathPatterns(URL_INTERCEPT)
                .excludePathPatterns(URL_EXCLUDE_INTERCEPT);
    }
}
