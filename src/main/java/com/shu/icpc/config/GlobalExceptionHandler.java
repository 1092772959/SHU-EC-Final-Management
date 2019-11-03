package com.shu.icpc.config;

import com.shu.icpc.utils.Constants;
import com.shu.icpc.utils.Result;
import com.shu.icpc.utils.ResultTool;
import org.apache.ibatis.binding.BindingException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value={BindingException.class})
    public String MyBatisException(Exception e){
        e.printStackTrace();
        return "database error, please connect admin";
    }

    @ResponseBody
    @ExceptionHandler(value={DataIntegrityViolationException.class})
    public Result DataIntegrityViolationException(Exception e){
        e.printStackTrace();
        return ResultTool.error(Constants.DATA_INTEGRITY_VIOLATION_CODE,Constants.DATA_INTEGRITY_VIOLATION);
    }

    @ResponseBody
    @ExceptionHandler(value = {IncorrectCredentialsException.class})
    public Result loginExceptionHandling(Exception e) {
        System.out.println("密码错误");
        return ResultTool.error(Constants.LOGIN_WRONG_PASSWORD_CODE, Constants.LOGIN_WRONG_PASSWORD);
    }

    @ResponseBody
    @ExceptionHandler(value = {Exception.class})
    public Result baseException(Exception e){
        System.out.println("----------------未知错误，开始打印---------------------");
        e.printStackTrace();
        String s = new String();
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(s);
        } catch (FileNotFoundException e1) {
//            e1.printStackTrace();
        }
        e.printStackTrace(pw);
        return ResultTool.error(Constants.UNKNOWN_ERROR_CODE,  s);
    }

    @ResponseBody
    @ExceptionHandler(value = {ConstraintViolationException.class, BindException.class, ValidationException.class})
    public Result ValidationException(Exception e){
//        e.printStackTrace();
        return ResultTool.error(Constants.VALIDATION_EMPTY_CODE,Constants.VALIDATION_EMPTY);
    }

    //shiro
    @ResponseBody
    @ExceptionHandler(value = LockedAccountException.class)
    public Result LockedAccountException(Exception e){
        return ResultTool.error(Constants.LOGIN_CHECKED_CODE, Constants.LOGIN_CHECKED);
    }

    @ResponseBody
    @ExceptionHandler(value = DisabledAccountException.class)
    public Result DisabledAccountException(Exception e){
        return ResultTool.error(Constants.LOGIN_REJECTED_CODE, Constants.LOGIN_REJECTED);
    }

    @ResponseBody
    @ExceptionHandler(value = UnauthorizedException.class)
    public Result UnauthorizedException(Exception e){
        return ResultTool.error(Constants.UNAUTHORIZEDEXCEPTION_CODE, Constants.UNAUTHORIZEDEXCEPTION);
    }

    //违反唯一约束
    @ResponseBody
    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
    public Result IntegrityConstraint(Exception e){
        System.out.println("手机号或邮箱重复");
        return ResultTool.error(Constants.FAIL);
    }
}
