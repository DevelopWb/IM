package com.juntai.disabled.bdmap.utils;

import android.annotation.SuppressLint;


import com.juntai.disabled.basecomponent.utils.LogUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * author:wong
 * Date: 2019/3/29
 * Description:
 */
public class DateUtil {
    public static final String USUAL = "yyyy-MM-dd HH:mm:ss";
    //获取
    public static String getRealDate() {
        calendar = Calendar.getInstance();
        return String.format("%d-%d-%d  %s", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH), switchDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK)));
    }
    /**
     * 获取当前系统时间
     * @param format "yyyy-MM-dd  HH:mm:ss"
     * @return
     */
    public static String getCurrentTime(String format) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        String currentTime = sdf.format(date);
        return currentTime;
    }
    //获取对应格式的字符型时间

    public static String getStringWithFormat(String format){
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(calendar.getTime());

    }

    static SimpleDateFormat simpleDateFormat = null;
    static Calendar calendar = null;
    //将dayOfWeek转换成字符星期
    public static String switchDayOfWeek(int dayOfWeek) {
        LogUtil.d(String.valueOf(dayOfWeek));
        switch (dayOfWeek) {
            case 1:
                return "星期天";
            case 2:
                return "星期一";
            case 3:
                return "星期二";
            case 4:
                return "星期三";
            case 5:
                return "星期四";
            case 6:
                return "星期五";
            case 7:
                return "星期六";
            default:
                return "";
        }
    }
    //时间戳转字符
    public static String getDateToString(long milSecond) {
        Date date = new Date(milSecond);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }
    //获取当前时间
    public static String hourOfDay(String time) {
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date date = simpleDateFormat.parse(time);
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm");
            return simpleDateFormat1.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }
    //获取年月日和星期
    public static String dateAndDayOfWeek(String date) {
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dateDate = simpleDateFormat.parse(date);
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MM月dd日");
            calendar = Calendar.getInstance();
            calendar.setTime(dateDate);
            return simpleDateFormat1.format(dateDate) + " " + switchDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    //获取月日
    public static String dateToDay(String date) {
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dateDate = simpleDateFormat.parse(date);
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MM月dd日");
            return simpleDateFormat1.format(dateDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    //获取星期和日期
    public static String weekAndDate() {
        calendar = Calendar.getInstance();
        return switchDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK))
                + " "
                + (calendar.get(Calendar.MONTH) + 1)
                + "月"
                + calendar.get(Calendar.DAY_OF_MONTH);
    }
    //获取当前12小时制时间
    public static String timeAnd() {
        calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        String hourStr = "";
        String minStr = "";
        if (hour < 10) {
            hourStr = "0" + String.valueOf(hour);
        } else
            hourStr = String.valueOf(hour);
        if (min < 10)
            minStr = "0" + String.valueOf(min);
        else
            minStr = String.valueOf(min);

        if (hour > 11) {
            return hourStr
                    + ":"
                    + minStr
                    + " "
                    + "PM";
        } else
            return hourStr
                    + ":"
                    + minStr
                    + " "
                    + "AM";
    }

    //获取今日开始时间
    public static long getTodayStart() {
        calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                0,
                0,
                0);
        return calendar.getTimeInMillis()/1000;
    }
    //获取当前秒制时间戳
    public static long getNowStamp() {
        calendar = Calendar.getInstance();
        return calendar.getTimeInMillis()/1000;
    }
    //获取昨天开始时间
    public static long getYesterdayStart() {
        calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH) - 1,
                0,
                0,
                0);
        //        return simpleDateFormat.format(calendar.getTime());
        return calendar.getTimeInMillis()/1000;
    }
    //获取昨天结束时间
    public static long getYesterdayEnd() {
        calendar = Calendar.getInstance();
        //        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH) - 1,
                23,
                59,
                59);
        return calendar.getTimeInMillis()/1000;
    }
    //时间戳转字符
    public static String stampToString(long stamp) {
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date(stamp));
    }
    //获取当前小时
    public static int getHour(){
        calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

}
