package com.test.onlinetest.utils;

import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

public class DateTimeUtils {
    public static Date getCurrentDateTime(){
        return Calendar.getInstance().getTime();
    }

    public static String getFormattedDateTimeHour(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy/MM/dd HH:mm:ss");
        String result = formatter.format(date);
        return result;
    }

    public static String getFormattedDateTimeDay(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy/MM/dd");
        String result = formatter.format(date);
        return result;
    }

    public static Date plusSevenDays(Date currentDate){
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DATE, 7);

        //The end of day
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);

        Date newDate = c.getTime();

        return newDate;
    }

    public static Date plusThreeDays(Date currentDate){
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DATE, 3);

        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);

        Date newDate = c.getTime();

        return newDate;
    }

    public static Date plusOneDay(Date currentDate){
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DATE, 1);

        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);

        Date newDate = c.getTime();

        return newDate;
    }

    public static Date plusEightHours(Date currentDate){
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.HOUR, 8);
        Date newDate = c.getTime();

        return newDate;
    }

    //4 Seconds for verifying expire checking
    public static Date plusFourHours(Date currentDate){
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.HOUR, 4);
        Date newDate = c.getTime();

        return newDate;
    }
}