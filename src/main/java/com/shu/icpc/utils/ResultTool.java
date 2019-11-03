package com.shu.icpc.utils;

import java.util.List;

/**
 * @program: demo
 * @description: 返回数据类型
 * @author: ydc
 * @create: 2019-04-25 13:59
 **/
public class ResultTool {

    public static Result<Object> success(int code){
        Result<Object> result =  new Result<>();
        result.setStatus(code);
        return result;
    }

    public static Result<List<Object>> success(int code, List<Object> object){
        Result<List<Object>> result = new Result<>();
        result.setStatus(code);
        result.setData(object);
        return result;
    }

    public static Result<Object> success(int code, Object object){
        Result<Object> result = new Result<>();
        result.setStatus(code);
        result.setData(object);
        return result;
    }

    public static Result<List<Object>> success(List<Object> object){
        Result<List<Object>> result = new Result<>();
        result.setStatus(Constants.SUCCESS);
        result.setData(object);
        return result;
    }

    public static Result<Object> success(Object object){
        Result<Object> result = new Result<>();
        result.setStatus(Constants.SUCCESS);
        result.setData(object);
        return result;
    }

    public static Result<Object> successGet(Object object){
        Result<Object> result = new Result<>();
        result.setStatus(Constants.SUCCESS_GET);
        result.setData(object);
        return result;
    }

    public static Result<List<Object>> successGet(List<Object> object){
        Result<List<Object>> result = new Result<>();
        result.setStatus(Constants.SUCCESS_GET);
        result.setData(object);
        return result;
    }

    public static Result success(){
        Result result = new Result();
        result.setStatus(Constants.SUCCESS);
        result.setData(null);
        return result;
    }

    public static Result error(int code){
        Result result = new Result();
        result.setStatus(code);
        result.setData(null);
        return result;
    }

    public static Result error(int code, String message){
        Result result = new Result(code, message);
        result.setData(null);
        return result;
    }
}
