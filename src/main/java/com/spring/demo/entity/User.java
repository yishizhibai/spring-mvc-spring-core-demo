package com.spring.demo.entity;

import lombok.Data;
import java.util.Date;

@Data // Lombok注解自动生成getter/setter，若未用Lombok则手动添加
public class User {
    private String id; // 注意：id类型是String（与数据库一致）
    private String username;
    private Integer age;
    private String phone;
    private Date createTime; // 对应数据库create_time

    // 若未用Lombok，手动添加setCreateTime
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}