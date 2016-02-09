package com.erpyjune;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by oj.bae on 2016. 2. 9..
 */
public class Test {
    public static void main(String args[]){
        StdUtils stdUtils = new StdUtils();

        System.out.println("날짜 : "+stdUtils.getDayBeforeAfter(-3));
        System.out.println("시간 : "+stdUtils.getHourBeforeAfter(-1));
        System.out.println("분 : "+stdUtils.getMinutesBeforeAfter(-15));

//        String today = null;
//        Date date = new Date();
//
//
//        System.out.println(date);
//
//        // 포맷변경 ( 년월일 시분초)
//        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//        // Java 시간 더하기
//
//        Calendar cal = Calendar.getInstance();
//
//        cal.setTime(date);
//
//        // 10분 더하기
//        cal.add(Calendar.MINUTE, 10);
//        today = sdformat.format(cal.getTime());
//        System.out.println("10분후 : " + today);
//
//        cal.add(Calendar.MINUTE, -30);
//        today = sdformat.format(cal.getTime());
//        System.out.println("30분전 : " + today.replace(" ","").replace("-","").replace(":",""));
//
//        cal.setTime(date);
//        // 1시간 전
//        cal.add(Calendar.HOUR, -1);
//
//        today = sdformat.format(cal.getTime());
//        System.out.println("1시간 전 : " + today);
//
//        cal.setTime(date);
//        // 하루 전
//        cal.add(Calendar.DATE, -1);
//
//        today = sdformat.format(cal.getTime());
//        System.out.println("1일 전 : " + today);

    }
}
