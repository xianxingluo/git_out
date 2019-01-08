package com.ziyun.basic.aop;

import com.ziyun.basic.server.OwnSchoolOrgServer;
import com.ziyun.basic.tools.ParamUtils;
import com.ziyun.basic.vo.Params;
import org.apache.commons.collections.CollectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author leyangjie
 * @date 2018/10/29 14:02
 * 参数转换aop
 */
@Component
@Aspect
public class ParamAop {

    @Autowired
    private OwnSchoolOrgServer ownSchoolOrgServer;

    @Pointcut("execution(* com.ziyun.basic.controller.*.*AOP(..))")
    public void pointCut() {

    }

    @Around("pointCut()")
    public Object aroundMethod(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Params params = null;
        if (args[0] instanceof Params) {
            params = (Params) args[0];
        }
        if (params.getEducation() != null && params.getEducation() != 1) {
            params.setEducation(2);
        }
        Set<String> classcodeList = ownSchoolOrgServer.selectOwnClasscode(params);
        if (CollectionUtils.isNotEmpty(classcodeList)) {
            ParamUtils.emptyParamsCode(params);
            params.setClassCode(classcodeList.toArray(new String[]{}));
        }
        args[0] = params;
        Object proceed = null;
        try {
            proceed = joinPoint.proceed(args);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return proceed;
    }
}
