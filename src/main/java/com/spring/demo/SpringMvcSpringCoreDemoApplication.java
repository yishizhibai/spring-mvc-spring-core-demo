package com.spring.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringMvcSpringCoreDemoApplication {
    private static final Logger log = LoggerFactory.getLogger(SpringMvcSpringCoreDemoApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringMvcSpringCoreDemoApplication.class, args);
        log.info("=============================================");
        log.info("项目启动成功！访问地址：http://localhost:8080/file/index");
        log.info("=============================================");
    }
}