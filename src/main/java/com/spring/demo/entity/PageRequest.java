package com.spring.demo.entity;

import lombok.Data;

/**
 * 分页请求参数实体类
 */
@Data
public class PageRequest {
    // 当前页码，默认第1页
    private int pageNum = 1;
    // 每页显示数量，默认10条
    private int pageSize = 10;
}