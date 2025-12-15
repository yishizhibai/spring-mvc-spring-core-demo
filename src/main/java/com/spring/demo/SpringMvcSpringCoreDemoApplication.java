package com.spring.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 项目启动类
 * 新增@MapperScan：扫描MyBatis的Mapper接口，让Spring容器管理
 */
@SpringBootApplication
@MapperScan("com.spring.demo.mapper") // 关键：扫描mapper包下的所有接口
public class SpringMvcSpringCoreDemoApplication {
    private static final Logger log = LoggerFactory.getLogger(SpringMvcSpringCoreDemoApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringMvcSpringCoreDemoApplication.class, args);
        log.info("=============================================");
        log.info("项目启动成功！访问地址：http://localhost:8080/file/index");
        log.info("=============================================");
    }
}