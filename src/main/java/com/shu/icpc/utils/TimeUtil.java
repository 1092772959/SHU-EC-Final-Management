package com.shu.icpc.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class TimeUtil {
    /**
     * 获取当前时间的时间戳
     * @return
     * */
    public static String getTimeInMillis() {
        long timeInMillis = Calendar.getInstance().getTimeInMillis();
        return timeInMillis+"";
    }
    /**
     * 获取位数为7位的随机数
     * @return
     * */
    public static String getRandom() {
        Random random = new Random();
        int nextInt = random.nextInt(90000);
        nextInt=nextInt+10000;
        String str=nextInt+"";
        return str;
    }
    public static String getTimeYearMonthDay() {
        Calendar c = Calendar.getInstance();
        int year=c.get(Calendar.YEAR);
        int month=c.get(Calendar.MONTH)+1;
        int day=c.get(Calendar.DAY_OF_MONTH);
        int hh=c.get(Calendar.HOUR_OF_DAY);
        int mi=c.get(Calendar.MINUTE);
        int ss=c.get(Calendar.SECOND);
        System.out.println(year+" "+month+" "+day+" "+hh+" "+mi+" "+ss);
        return null;
    }
    /**
     * 输入日期格式，返回格式内的内容，输入格式以-分隔
     * @param dateFormat
     * @return
     **/
    public static String getTimeYearMonthDay(String dateFormat) {
        String[] strNow = new SimpleDateFormat(dateFormat).format(new Date()).toString().split("-");
        String str="";
        for (String string : strNow) {
            str=str+string;
        }
        return str;
    }
    /**
     * 获得20位的主键值，主键值的格式是13位的当前时间戳加7位的随机数
     * @return
     **/
    public static String getMillPrimaryKey() {
        return getTimeInMillis()+getRandom();
    }

    /**
     * 获得21位的主键值，主键值的格式是14位的当前时间加7位的随机数
     * @return
     **/
    public static String getDatePrimaryKey() {
        return getTimeYearMonthDay("yyyyMMddHHmmss")+getRandom();
    }

}
