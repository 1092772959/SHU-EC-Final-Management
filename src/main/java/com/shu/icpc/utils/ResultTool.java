package com.shu.icpc.utils;

/**
 * @program: result util
 * @description: 返回数据类型
 **/
public class ResultTool {

    public static Result<Object> successGet(Object object){
        Result<Object> result = new Result<>();
        result.setStatus(Constants.SUCCESS_GET);
        String message = Constants.message.get(Constants.SUCCESS_GET);
        result.setMessage(message);
        result.setData(object);
        return result;
    }

    public static Result get(int code, Object data){
        Result result = new Result();
        result.setStatus(code);
        String message = Constants.message.get(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static Result success(){
        Result result = new Result();
        result.setStatus(Constants.SUCCESS);
        String message = Constants.message.get(Constants.SUCCESS);
        result.setMessage(message);
        result.setData(null);
        return result;
    }

    public static Result error(){
        Result result = new Result();
        result.setStatus(Constants.FAIL);
        String message = Constants.message.get(Constants.FAIL);
        result.setMessage(message);
        result.setData(null);
        return result;
    }

    public static Result resp(int code){
        Result result = new Result();
        result.setStatus(code);
        String message = Constants.message.get(code);
        result.setMessage(message);
        return result;
    }

    public static Result resp(int code, Object data){
        Result result = new Result();
        result.setStatus(code);
        String message = Constants.message.get(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }
}
