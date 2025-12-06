package com.spring.demo.aop;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExceptionAspect {
    private static final Logger log = LoggerFactory.getLogger(ExceptionAspect.class);

    // 切入点：匹配所有业务层实现类的方法
    @Pointcut("execution(* com.spring.demo.service.*.impl.*.*(..))")
    public void serviceMethodPointcut() {}

    // 异常通知：捕获业务层方法抛出的异常
    @AfterThrowing(pointcut = "serviceMethodPointcut()", throwing = "ex")
    public void handleServiceException(Throwable ex) {
        log.error("业务层方法执行异常，类型：{}，信息：{}",
                ex.getClass().getSimpleName(),
                ex.getMessage(),
                ex);
    }
}