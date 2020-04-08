package com.shu.icpc.utils;

import java.util.List;

/**
 * @program: result util
 * @description: 返回数据类型
 * @author: ydc
 * @create: 2019-04-25 13:59
 **/
public class ResultTool {

    public static Result<Object> successGet(Object object){
        Result<Object> result = new Result<>();
        result.setStatus(Constants.SUCCESS_GET);
        result.setData(object);
        return result;
    }

    public static Result get(int code, Object data){
        Result result = new Result();
        result.setStatus(code);
        result.setData(data);
        return result;
    }

    public static Result success(){
        Result result = new Result();
        result.setStatus(Constants.SUCCESS);
        result.setData(null);
        return result;
    }

    public static Result error(){
        Result result = new Result();
        result.setStatus(Constants.FAIL);
        result.setData(null);
        return result;
    }

    public static Result resp(int code){
        Result result = new Result();
        result.setStatus(code);
        return result;
    }

    public static Result resp(int code, Object data){
        Result result = new Result();
        result.setStatus(code);
        result.setData(data);
        return result;
    }
}
