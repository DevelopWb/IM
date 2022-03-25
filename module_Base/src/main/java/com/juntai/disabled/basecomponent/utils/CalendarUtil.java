package com.juntai.disabled.basecomponent.utils;

import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2020/4/3 15:58
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/4/3 15:58
 */
public class CalendarUtil {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    /**
     * 将时间戳转换为固定格式的时间
     *
     * @return
     */
    public static String getSpecialTypeTime(String time) {
        // 将时间戳转为字符串
        String re_StrTime = null;
        long lcc_time = Long.valueOf(time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;
    }

    /**
     * 获取下次提醒的时间,day天后
     */
    public static String getNextDayTime(int day, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, day);
        Date date = calendar.getTime();
        return new SimpleDateFormat(format, Locale.CHINA).format(date);
    }
    /**
     *  时间类型转换
     * @param beginDate
     * @return
     */
    public static long formatTime(Date beginDate) {
        java.util.Calendar calendarBegin = java.util.Calendar.getInstance();
        calendarBegin.setTime(beginDate);
        return calendarBegin.getTimeInMillis();
    }
    /**
     * 获取当前的时间
     *
     * @return
     */
    public static String getCurrentTime() {
        Calendar ca = Calendar.getInstance();
        return sdf.format(ca.getTime());

    }

    /**
     * 获取当前的时间
     *
     * @return
     */
    public static String getCurrentTime(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar ca = Calendar.getInstance();
        return sdf.format(ca.getTime());

    }

    /**
     * 获取当天12点的时间
     *
     * @return
     */
    public static String getTimeMidOfDay() {
        Calendar ca = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return sdf.format(ca.getTime()) + " " + "12:00:00";

    }

    /**
     * 获取0点的时间
     *
     * @return yyyy-MM-dd 00:00:00
     * @time yyyy-MM-dd **:**:**
     */
    public static String getZeroTime(String time) {
        String time_return = "";
        SimpleDateFormat sdf_a = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        try {
            Date date = sdf.parse(time);
            time_return = sdf_a.format(date) + " 00:00:00";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time_return;
    }

    /**
     * 获取本周的第一天||上周的最后一天
     *
     * @return
     */
    public static String getTimeOfWeekStart() {

        Calendar ca = getCalendar();
        ca.set(Calendar.DAY_OF_WEEK, ca.getFirstDayOfWeek() + 1);
        return sdf.format(ca.getTime());
    }

    @NonNull
    private static Calendar getCalendar() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.clear(Calendar.MINUTE);
        ca.clear(Calendar.SECOND);
        ca.clear(Calendar.MILLISECOND);
        return ca;
    }

    /**
     * 获取本周的最后一天
     *
     * @return
     */
    public static String getTimeOfWeekEnd() {
        Calendar ca = getCalendar();
        ca.set(Calendar.DAY_OF_WEEK, ca.getFirstDayOfWeek() + 1);
        ca.add(Calendar.DAY_OF_WEEK, 7);
        return sdf.format(ca.getTime());
    }

    /**
     * 获取上周的第一天
     *
     * @return
     */
    public static String getTimeOfLastWeekStart() {

        Calendar ca = getCalendar();
        ca.set(Calendar.DAY_OF_WEEK, ca.getFirstDayOfWeek() + 1);
        ca.add(Calendar.DAY_OF_MONTH, -7);
        return sdf.format(ca.getTime());
    }

    /**
     * 获取开始时间 month:-11代表最近一年的开始年月，-5代表最近半年，-2代表最近3个月 0代表最近一个月
     *
     * @return
     */
    public static String getLastDateOfMonth(int month, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, month);
        Date date = calendar.getTime();
        return new SimpleDateFormat(format, Locale.CHINA).format(date);
    }

    /**
     * 通过日期获取对应的星期
     *
     * @param time yyyy年MM月dd日
     * @return
     */
    public static String GetWeekFromDate(String time) {

        Calendar cal = Calendar.getInstance();

        int i = -1;

        // 对 calendar 设置时间的方法

        // 设置传入的时间格式

        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);

            // 指定一个日期

            Date date;

            date = dateFormat.parse(time);

            cal.setTime(date);

            i = cal.get(Calendar.DAY_OF_WEEK);

        } catch (ParseException e) {


            e.printStackTrace();

        }
        String week = "";
        if (i == 1) {
            week = "星期日";
        }
        if (i == 2) {
            week = "星期一";
        }
        if (i == 3) {
            week = "星期二";
        }
        if (i == 4) {
            week = "星期三";
        }
        if (i == 5) {
            week = "星期四";
        }
        if (i == 6) {
            week = "星期五";
        }
        if (i == 7) {
            week = "星期六";
        }
        return week;
    }


    /**
     * 比较两个时间串的大小
     *
     * @param dateFormat 时间格式
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @return
     */
    public static boolean compareDateSize(String dateFormat, String startTime, String endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.CHINA);
        try {
            Long a = sdf.parse(startTime).getTime();
            Long b = sdf.parse(endTime).getTime();
            if (a > b) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 计算两个时间只差
     *
     * @param startTime
     * @param endTime
     * @param format
     * @return
     */
    public static String dateDiff(String startTime, String endTime, String format) {
        // 按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat sd = new SimpleDateFormat(format);
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        long ns = 1000;// 一秒钟的毫秒数
        long diff;
        long day = 0;
        String time = null;
        try {
            // 获得两个时间的毫秒时间差异
            diff = sd.parse(endTime).getTime()
                    - sd.parse(startTime).getTime();
            day = diff / nd;// 计算差多少天
            long hour = diff % nd / nh;// 计算差多少小时
            long min = diff % nd % nh / nm;// 计算差多少分钟
            long sec = diff % nd % nh % nm / ns;// 计算差多少秒

            time = day + "天" + hour + "小时" + min
                    + "分钟";
            if (day >= 1) {
                time = day + "天" + hour + "小时" + min
                        + "分钟";
                return time;
            } else {
                if (hour >= 1) {
                    time = hour + "小时" + min
                            + "分钟";
                    return time;
                } else {
                    time = min
                            + "分钟";
                    return time;
                }

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;

    }

    /**
     * 比较两个时间串的大小
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    public static boolean compareTimes(String startTime, String endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        try {
            Long a = sdf.parse(startTime).getTime();
            Long b = sdf.parse(endTime).getTime();
            Long currentTime = sdf.parse(getCurrentTimeNormal("yyyy年MM月dd日")).getTime();
            if (a < currentTime) {//开始时间小于当前时间
                return false;
            } else {
                if (a > b) {//开始时间大于结束时间
                    return false;
                } else {//开始时间小于结束时间
                    return true;
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 转换时间格式
     * @param olderStr
     * @param olderSdf
     * @param returnSdf
     * @return
     */
    public static  String  formatTimeType(String olderStr,String olderSdf,String returnSdf){
        SimpleDateFormat sdfOlder = new SimpleDateFormat(olderSdf);
        SimpleDateFormat sdfReturn = new SimpleDateFormat(returnSdf);
        try {
            Date oldDate =  sdfOlder.parse(olderStr);
            return sdfReturn.format(oldDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前的时间
     *
     * @return
     */
    public static String getCurrentTimeNormal(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        Calendar ca = Calendar.getInstance();
        return sdf.format(ca.getTime());

    }

    /**
     * 根据传入的日期，得到对应的日期
     *
     * @param time 一个日期 yyyy-MM-dd HH:mm:ss
     */
    public static String[] getWeekStartAndWeekEndBaseTime(String time) {
        StringBuilder sb = new StringBuilder();
        Calendar ca = getCalendar();
        try {
            Date date = sdf.parse(time);
            ca.setTime(date);
            ca.set(Calendar.DAY_OF_WEEK, ca.getFirstDayOfWeek() + 1);
            String time_start = sdf.format(ca.getTime());
            sb.append(time_start);
            sb.append(",");

            ca.add(Calendar.DAY_OF_WEEK, 6);
            String time_end = sdf.format(ca.getTime());
            sb.append(time_end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sb.toString().split(",");
    }

    /**
     * 将yyyy年MM月dd日格式的日期转换为yyyy-MM-dd HH:mm:ss
     */
    public static String GetUploadTime(String time, String tag) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        String time_info = "";
        if ("start".equals(tag)) {//开始时间
            try {
                Date startDate = simpleDateFormat.parse(time);
                return sdf.format(startDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if ("end".equals(tag)) {//结束时间
            try {
                Date endDate = simpleDateFormat.parse(time);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(endDate);
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                return sdf.format(calendar.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }
        return time_info;
    }

    /**
     * 将yyyy年MM月dd日格式的日期转换为yyyy-MM-dd
     */
    public static String GetUploadTime(String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        String time_info = "";
        try {
            Date startDate = simpleDateFormat.parse(time);
            return sdf.format(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time_info;
    }

    /**
     * @param date yyyy-MM
     * @return
     */
    public static int getMonth(String date) {
        Calendar calendar = Calendar.getInstance();
        int month = -1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
        try {
            Date time = sdf.parse(date);
            calendar.setTime(time);
            month = calendar.get(Calendar.MONTH);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return month + 1;
    }

    /**
     * 获取当前的月份
     *
     * @return
     */
    public static String getCurrentYearAndMonth() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        return String.valueOf(year) + "-" + String.valueOf(month);
    }

    /**
     * 获取当前的月份
     *
     * @return
     */
    public static int getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取当前的年份
     *
     * @return
     */
    public static int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取当前天数
     *
     * @return
     */
    public static int getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DATE);
    }

    /**
     * 获取7天前得日期  yyyy-MM-dd
     *
     * @return
     */
    public static String getDateSomeDaysAgo(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, day);
        Date date = calendar.getTime();
        return new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(date);
    }

    /**
     * 2018-10-20 09:00
     *
     * @param min
     * @return min分钟前或者后的时间
     */
    public static String getTimeAfterChange(String time, int min) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(time));
            calendar.add(Calendar.MINUTE, min);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date = calendar.getTime();

        return sdf.format(date);
    }

    /**
     * 加一分钟后的时间
     *
     * @param time
     */
    private String getTimeAfterAddOneMinute(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);

        String aDataStr = null;
        try {
            Date date = sdf.parse(time);
            date.setTime(date.getTime() + 60 * 1000);
            aDataStr = sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return aDataStr;
    }

    /**
     * 获取对应格式的时间
     *
     * @param date
     * @return
     */
    public static String getTimeFromDate(String format, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        return sdf.format(date);
    }

    /**
     * 获取对应格式的时间
     *
     * @return
     */
    public static String getTimeFromString(String olderFormat, String olderTime, String format) {
        SimpleDateFormat olderSdf = new SimpleDateFormat(olderFormat);
        try {
            Date olderDate = olderSdf.parse(olderTime);
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(olderDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 上一个月对应的年
     *
     * @return
     */
    public static int getYearOfPreMonth(Date date) {
        Date preDate = getDateOfPreMonth(date);
        Calendar calendar = getCalendar();
        calendar.setTime(preDate);
        return calendar.get(Calendar.YEAR);
    }
    /**
     * 上一个月对应的月
     *
     * @return
     */
    public static int getMonthOfPreMonth(Date date) {
        Date preDate = getDateOfPreMonth(date);
        Calendar calendar = getCalendar();
        calendar.setTime(preDate);
        return calendar.get(Calendar.MONTH)+1;
    }

    /**
     * 上一个月对应的date
     *
     * @return
     */
    public static Date getDateOfPreMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);
        return calendar.getTime();
    }
    /**
     * 下一个月对应的年
     *
     * @return
     */
    public static int getYearOfNexMonth(Date date) {
        Date nextDate = getDateOfNexMonth(date);
        Calendar calendar = getCalendar();
        calendar.setTime(nextDate);
        return calendar.get(Calendar.YEAR);
    }
    /**
     * xia一个月对应的月
     *
     * @return
     */
    public static int getMonthOfNexMonth(Date date) {
        Date nextDate = getDateOfNexMonth(date);
        Calendar calendar = getCalendar();
        calendar.setTime(nextDate);
        return calendar.get(Calendar.MONTH)+1;
    }

    /**
     * 下一个月对应的date
     *
     * @return
     */
    public static Date getDateOfNexMonth(Date date) {
        Calendar calendar = getCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        return calendar.getTime();
    }

    /**
     * 时间戳转 yyyy-MM-dd HH:mm:ss
     *
     * @param data
     * @return
     */
    public static String getStringData(long data) {
        return sdf.format(data);
    }

    /**
     * 获取今日得开始时间
     *
     * @return
     */
    public static String getTodayStartTime() {
        return getZeroTime(getCurrentTime());
    }

    /**
     * 获取昨天的开始时间
     *
     * @return
     */
    public static String getYesterdayStartTime() {
        return getZeroTime(getYesterday());
    }

    public static String getYesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return sdf.format(calendar.getTime());
    }
}
