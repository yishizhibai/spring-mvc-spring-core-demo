package com.spring.demo.aop.annotation;

import java.lang.annotation.*;

/**
 * 自定义限流注解（实验四核心扩展：AOP+自定义注解）
 */
@Target(ElementType.METHOD) // 仅作用于方法
@Retention(RetentionPolicy.RUNTIME) // 运行时生效
@Documented
public @interface RateLimit {
    /**
     * 限流阈值（每秒允许调用次数）
     */
    int qps() default 5;

    /**
     * 提示信息
     */
    String message() default "接口调用过于频繁，请稍后再试";
}