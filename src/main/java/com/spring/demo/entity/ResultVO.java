package com.spring.demo.entity;
//统一的API响应结果封装类，用于统一前端接口返回格式
public class ResultVO {
    // 状态码，200表示成功，其他值表示错误
    private int code;
    // 状态描述信息
    private String msg;
    // 数据 payload，根据业务需求可以是任意类型
    private Object data;

    // 1. 无数据的成功返回
    public static ResultVO success(String msg) {
        ResultVO result = new ResultVO();
        result.setCode(200);
        result.setMsg(msg);
        return result;
    }

    // 2. 带任意Object数据的成功返回（覆盖ChunkUploadResult等自定义对象）
    public static ResultVO success(String msg, Object data) {
        ResultVO result = new ResultVO();
        result.setCode(200);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    // 3. 带long类型数据的成功返回（覆盖chunkSize等长整型）
    public static ResultVO success(String msg, long data) {
        ResultVO result = new ResultVO();
        result.setCode(200);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    // 错误返回方法（保留原有）
    public static ResultVO error(String msg) {
        ResultVO result = new ResultVO();
        result.setCode(500);
        result.setMsg(msg);
        return result;
    }

    public static ResultVO error(int code, String msg) {
        ResultVO result = new ResultVO();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    // getter/setter（必须保留）
    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }
    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }
}