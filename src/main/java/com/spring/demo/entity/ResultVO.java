package com.spring.demo.entity;

public class ResultVO {
    // 响应码：200成功，500失败
    private Integer code;
    // 响应消息
    private String msg;
    // 响应数据
    private Object data;

    // 静态构造方法
    public static ResultVO success() {
        return new ResultVO(200, "操作成功", null);
    }

    public static ResultVO success(Object data) {
        return new ResultVO(200, "操作成功", data);
    }

    public static ResultVO error(String msg) {
        return new ResultVO(500, msg, null);
    }

    // 私有构造器
    private ResultVO(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // 手动编写getter/setter
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}