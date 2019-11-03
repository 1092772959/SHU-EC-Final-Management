package com.shu.icpc.utils;

public class Constants {
    public static final Integer hashTime = 1024;

    //-------------通用
    public static final Integer SUCCESS = 600;      //修改操作成功

    public static final Integer SUCCESS_GET = 605;  //get操作成功

    public static final Integer NAME_DUPLICATED = 730;      //名称重复

    public static final Integer FAIL = 800;
    public static final Integer UNAUTHENTICATE = 804;

    public static final Integer DATA_INTEGRITY_VIOLATION_CODE = 801;
    public static final String DATA_INTEGRITY_VIOLATION = "数据不合法";

    public static final Integer VALIDATION_EMPTY_CODE = 802;
    public static final String VALIDATION_EMPTY = "请求字段不能为空";

    public static final Integer UNKNOWN_ERROR_CODE = 803;
    public static final String UNKNOWN_ERROR = "未知错误";

    public static final Integer UNAUTHORIZEDEXCEPTION_CODE = 805;
    public static final String UNAUTHORIZEDEXCEPTION = "用户身份等级不足";

//-----------status code

    public static final Integer CHECK_STATUS_PASS = 1;

    public static final Integer CHECK_STATUS_CHECKED = 2;

    public static final Integer CHECK_STATUS_REJECTED = 3;



    public static final Long COOKIE_TIMEOUT = 1000000000L;

    public static final String SESSION_USER = "user";

//----------------login

    public static final Integer LOGIN_SUCCESS_CODE = 600;           //学生子
    public static final Integer LOGIN_SUCCESS_COACH_CODE = 601;
    public static final Integer LOGIN_SUCCESS_CHIEF_CODE = 602;
    public static final Integer LOGIN_SUCCESS_ADMIN = 604;
    public static final String LOGIN_SUCCESS = "登陆成功";

    public static final Integer LOGOUT_SUCCESS_CODE = 600;
    public static final String LOGOUT_SUCCESS = "登出成功";

    public static final Integer LOGIN_CHECKED_CODE = 700;
    public static final String LOGIN_CHECKED = "账号正在审核";

    public static final Integer LOGIN_REJECTED_CODE = 701;
    public static final String LOGIN_REJECTED = "注册请求被驳回";

    public static final Integer LOGIN_NO_ACCOUNT_CODE = 702;
    public static final String LOGIN_NO_ACCOUNT = "账号不存在";

    public static final Integer LOGIN_WRONG_PASSWORD_CODE = 703;
    public static final String LOGIN_WRONG_PASSWORD = "密码错误";



    //-----------------sign up
    public static final Integer SIGN_UP_SUCCESS_CODE = 600;

    public static final Integer SIGN_UP_GET_SCHOOL_CODE = 614;

    public static final Integer SIGN_UP_ACCOUNT_EXISTS = 711;

    public static final Integer SIGN_UP_SCHOOL_EXISTS = 712;

    public static final Integer SIGN_UP_NO_SCHOOL = 713;

    public static final Integer UPDATE_QUOTA_ERROR = 723;   //该学校报名数已超过此限额

    public static final String ADMIN = "admin";

    public static final String CHIEF = "chief";

    public static final String COACH = "coach";

    public static final String STU = "stu";
}