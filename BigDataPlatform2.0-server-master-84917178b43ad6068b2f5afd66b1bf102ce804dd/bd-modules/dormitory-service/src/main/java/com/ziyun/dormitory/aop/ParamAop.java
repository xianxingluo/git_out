package com.ziyun.dormitory.aop;

import com.ziyun.common.enums.SexEnum;
import com.ziyun.common.model.Params;
import com.ziyun.utils.common.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author leyangjie
 * @date 2018/11/5 17:54
 */
@Component
@Aspect
public class ParamAop {

    @Pointcut("execution(* com.ziyun.dormitory.controller.*.*AOP(..))")
    public void pointCut() {

    }


    @Around("pointCut()")
    public Object arountMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Params params = null;
        if (args[0] instanceof Params) {
            params = (Params) args[0];
        }
        if (params != null && StringUtils.isNotBlank(params.getSex())) {
            if (SexEnum.MALE.getCode().equals(params.getSex())) {
                params.setSex(SexEnum.MALE.getValue());
            }
            if (SexEnum.FEMALE.getCode().equals(params.getSex())) {
                params.setSex(SexEnum.FEMALE.getValue());
            }
            args[0] = params;
        }
        Object proceed = joinPoint.proceed(args);
        return proceed;
    }
}
