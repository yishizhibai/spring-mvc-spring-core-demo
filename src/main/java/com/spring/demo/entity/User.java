package com.spring.demo.entity;

public class User {
    // 用户ID
    private Long id;
    // 用户名
    private String username;
    // 年龄
    private Integer age;
    // 手机号
    private String phone;

    // 手动编写getter/setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}