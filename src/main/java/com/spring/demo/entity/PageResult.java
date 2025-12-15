package com.spring.demo.entity;

import lombok.Data;
import java.util.List;

/**
 * 分页返回结果实体类
 */
@Data
public class PageResult<T> {
    // 当前页码
    private int pageNum;
    // 每页显示数量
    private int pageSize;
    // 总记录数
    private long total;
    // 分页数据列表
    private List<T> list;

    // 构造方法
    public PageResult(int pageNum, int pageSize, long total, List<T> list) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.list = list;
    }
}