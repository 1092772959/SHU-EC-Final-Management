package com.shu.icpc.utils;

/**
 * @program: demo
 * @description: 返回数据格式
 * @author: ydc
 * @create: 2019-04-25 13:58
 **/
public class Result<Type> {
    private Integer status;
    private String message;
    private Type data;

    public Result() {
    }

    public Result(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Type getData() {
        return data;
    }

    public void setData(Type data) {
        this.data = data;
    }
}
