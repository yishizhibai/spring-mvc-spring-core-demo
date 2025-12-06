package com.spring.demo.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {
    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    /**
     * 切入点：所有业务层实现类的方法
     */
    @Pointcut("execution(* com.spring.demo.service.*.impl.*.*(..))")
    public void servicePointcut() {}

    /**
     * 环绕通知：记录方法入参、出参、执行时间
     */
    @Around("servicePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取方法信息
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        // 记录入参
        Object[] args = joinPoint.getArgs();
        log.info("【业务层调用】类：{}，方法：{}，入参：{}", className, methodName, args);

        // 执行方法并计时
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();

        // 记录出参和执行时间
        log.info("【业务层返回】类：{}，方法：{}，出参：{}，执行时间：{}ms",
                className, methodName, result, (endTime - startTime));

        return result;
    }
}