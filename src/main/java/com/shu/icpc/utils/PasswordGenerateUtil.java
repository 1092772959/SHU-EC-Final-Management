package com.shu.icpc.utils;

import org.apache.shiro.crypto.hash.Md5Hash;

public class PasswordGenerateUtil {

    public static String getPassword(String password,String salt,int hashTimes){
        Md5Hash md5Hash = new Md5Hash(password,salt,hashTimes);
        return md5Hash.toString();
    }
}