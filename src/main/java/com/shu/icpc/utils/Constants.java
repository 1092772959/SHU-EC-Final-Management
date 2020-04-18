package com.shu.icpc.utils;

import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

public class Constants {
    public static final Integer hashTime = 1024;

    public static final Integer RETRY_TIME = 3;

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

    public static final String SESSION_USER = "user";       //key of storing user info in session memory

//----------------login

    public static final Integer LOGIN_SUCCESS_CODE = 600;           //student
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

    //-----------------solo contest related
    public static final Integer STUDENT_NOT_EXISTS = 720;   //no such student

    public static final Integer SOLO_CONTEST_NOT_EXISTS = 721; // no such solo contest

    public static final Integer NUM_LIMIT = 722;

    public static final Integer REGISTER_TIME_ERROR = 723;

    public static final Integer SOLO_NO_RECORD = 724;  //no sign in record to delete

    public static final Integer DUPLICATION_SIGN_IN = 725;

    public static final Integer NUM_ERROR = 726;

    public static final Integer TIME_ERROR = 727;

    //-----------------team contest related
    public static final Integer SIGN_IN_NO_TEAM = 730;  // no such team

    public static final Integer CONTEST_NOT_EXISTS = 731;  // no such contest

    public static final Integer QUOTA_LIMIT =  732;

    //-----------------article related
    public static final Integer ARTICLE_NO_EXISTS = 740;

    public static final Integer ARTICLE_NO_ACCESS = 741;

    public static final Integer CHECK_ILLEGAL_CODE = 704;

    //-----------------credentials related
    public static final Integer ZIP_ERROR = 750;

    public static final Integer CREDENTIAL_NOT_EXISTS = 751;


    //-----------------upload credential related
    public static final String MSG_ZIP_FILE_ILLEGAL = "Unzip file error, check this file before";

    public static final String MSG_FILE_NAME_NO_MAPPING = "The file name has no id mapping";

    public static final String MSG_OSS_FAILED = "Push to qiniu oss failed";

    public static final String MSG_NAME_NOT_NUMBER = "File name is not a team id number";

    public static final String MSG_CREDENTIAL_EXISTS = "This credential file has already existed in the system";

    @Value("${oss.bucket.private}")
    public static String BUCKET_PRIVATE;

    @Value("${oss.bucket}")
    public static String BUCKET_PUBLIC;


    public static final String ADMIN = "admin";

    public static final String CHIEF = "chief";

    public static final String COACH = "coach";

    public static final String STU = "stu";

    /**
     *  status code -> message mapping
     */

    public static Map<Integer, String> message = new HashMap<>();

    static{
        message.put(SUCCESS, "success");
        message.put(SUCCESS_GET, "success");
        message.put(NAME_DUPLICATED, "name duplicated");
        message.put(FAIL, "failed");
        message.put(UNAUTHENTICATE, "unauthenticated");
        message.put(DATA_INTEGRITY_VIOLATION_CODE, "data failed to store in mysql, check validation");
        message.put(VALIDATION_EMPTY_CODE, "miss certain fields in request");
        message.put(UNKNOWN_ERROR_CODE, "unknown error");
        message.put(UNAUTHORIZEDEXCEPTION_CODE, "have no role permission");
        message.put(LOGIN_SUCCESS_COACH_CODE, "normal coach login success");
        message.put(LOGIN_SUCCESS_ADMIN, "admin login success");
        message.put(LOGIN_SUCCESS_CHIEF_CODE, "chief coach login success");
        message.put(LOGIN_CHECKED_CODE, "account checked, please contact your coach");
        message.put(LOGIN_REJECTED_CODE, "account sign up rejected, please contact your coach");
        message.put(LOGIN_NO_ACCOUNT_CODE, "account not exists");
        message.put(LOGIN_WRONG_PASSWORD_CODE, "wrong account id or password");
        message.put(SIGN_UP_ACCOUNT_EXISTS, "this account has already been used");
        message.put(SIGN_UP_SCHOOL_EXISTS, "this school has already in the system");
        message.put(SIGN_UP_NO_SCHOOL, "no such school, sign up failed");
        message.put(UPDATE_QUOTA_ERROR, "quota for your school has reached the limit, sign in failed");
        message.put(STUDENT_NOT_EXISTS, "student id not exists");
        message.put(SOLO_CONTEST_NOT_EXISTS, "solo contest id not exists");
        message.put(NUM_LIMIT, "quota is limited for the contest, sign in failed");
        message.put(REGISTER_TIME_ERROR, "It is not opened for sign in or sign off");
        message.put(SOLO_NO_RECORD, "user has not signed in, failed to sign off");
        message.put(DUPLICATION_SIGN_IN, "You has already signed in for this contest!");
        message.put(NUM_ERROR, "quota for this contest is illegal, please use correct number");
        message.put(TIME_ERROR, "There may be illegal time limitations, please check again");
        message.put(SIGN_IN_NO_TEAM, "Team id not exists");
        message.put(CONTEST_NOT_EXISTS, "Contest id not exists");
        message.put(QUOTA_LIMIT, "quota for your school is limited, sign in failed");
        message.put(ARTICLE_NO_EXISTS, "article not exists");
        message.put(ARTICLE_NO_ACCESS, "You have no access to modify this article");
        message.put(CHECK_ILLEGAL_CODE, "illegal status code when modifying article status");
        message.put(ZIP_ERROR, "zip file format error");
        message.put(CREDENTIAL_NOT_EXISTS , "credential not exists, please contact the engineering RD");
    }
}
