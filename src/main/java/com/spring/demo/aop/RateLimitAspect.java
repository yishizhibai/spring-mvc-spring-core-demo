package com.spring.demo.aop;

import com.spring.demo.aop.annotation.RateLimit;
import com.spring.demo.aop.util.RateLimitUtils;
import com.spring.demo.entity.ResultVO;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 限流切面（实验四核心：AOP面向切面编程）
 */
@Aspect
@Component
public class RateLimitAspect {
    private static final Logger log = LoggerFactory.getLogger(RateLimitAspect.class);

    // 切入点：标记@RateLimit注解的方法
    @Pointcut("@annotation(com.spring.demo.aop.annotation.RateLimit)")
    public void rateLimitPointcut() {}

    /**
     * 环绕通知：拦截限流注解方法，实现限流逻辑
     */
    @Around("rateLimitPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 1. 获取注解信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RateLimit rateLimit = method.getAnnotation(RateLimit.class);
        String methodKey = method.getDeclaringClass().getName() + "." + method.getName();

        // 2. 尝试获取令牌
        boolean acquire = RateLimitUtils.tryAcquire(methodKey, rateLimit.qps());
        if (!acquire) {
            log.warn("接口限流：{}，qps={}", methodKey, rateLimit.qps());
            // 调整为只传提示信息，错误码包含在文本中
            return ResultVO.error("429-" + rateLimit.message());
        }

        // 3. 允许调用，执行原方法
        return joinPoint.proceed();
    }
}