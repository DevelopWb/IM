package com.juntai.wisdom.im.utils;

import android.text.TextUtils;

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
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

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

    /**
     * 获取当前的时间
     *
     * @return
     */
    public static String getCurrentTime() {
        Calendar ca = Calendar.getInstance();
        return sdf.format(ca.getTime());
    }
    public static String formatSystemCurrentMillis(String  timeMillis) {
        if (TextUtils.isEmpty(timeMillis)) {
            return "";
        }
        if (timeMillis.contains("-")||timeMillis.contains(":")) {
            return timeMillis;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(timeMillis));
        return sdf.format(calendar.getTime());
    }

    /**
     * 获取当前系统时间
     *
     * @param format "yyyy-MM-dd  HH:mm:ss"
     * @return
     */
    public static String getCurrentTime(String format) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        String currentTime = sdf.format(date);
        return currentTime;
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
     * 获取两个时间段的分钟差
     *
     * @param startDate 年月日时分秒
     * @param endDate
     * @return
     */

    public static int getGapMinutes(String startDate, String endDate) {
        long start = 0;
        long end = 0;
        try {
            start = sdf.parse(startDate).getTime();
            end = sdf.parse(endDate).getTime();

        } catch (Exception e) {
        }
//        CLog.e("开始结束时间1", (end - start) + "");
        int minutes = (int) ((end - start) / (1000 * 60));
        return minutes;

    }

    /**
     * 格式化时间
     * 如果是当天的  只显示时分
     * 如果是昨天的  显示 昨天 时分
     * 如果日期还早  就显示 月日 时分
     * 如果跨年  就显示 年月日 时分
     *
     * @return
     */
    public static String formatData(String targetTime) {
        targetTime = formatSystemCurrentMillis(targetTime);
        SimpleDateFormat sdfYmdhm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat sdfMdhm = new SimpleDateFormat("MM-dd HH:mm");
        SimpleDateFormat sdfHm = new SimpleDateFormat("HH:mm");
        if (TextUtils.isEmpty(targetTime)) {
            return "";
        }
        Date targetDate = null;
        try {
            targetDate = sdf.parse(targetTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date());
        int currentYear = ca.get(Calendar.YEAR);
        int currentMonth = ca.get(Calendar.MONTH) + 1;
        int currentDay = ca.get(Calendar.DATE);
        Calendar targetCa = Calendar.getInstance();
        targetCa.setTime(targetDate);
        int targetYear = targetCa.get(Calendar.YEAR);
        int targetMonth = targetCa.get(Calendar.MONTH) + 1;
        int targetDay = targetCa.get(Calendar.DATE);

        if (currentYear == targetYear) {
            //当年的数据
            if (currentMonth == targetMonth) {
                //当月的数据
                if (currentDay==targetDay) {
                    //当天的数据
                    return sdfHm.format(targetDate);
                }else {
                    if (ca.get(Calendar.DAY_OF_YEAR)-targetCa.get(Calendar.DAY_OF_YEAR)==1) {
                        //昨天的数据
                        return "昨天 "+sdfHm.format(targetDate);
                    }else {
                        return sdfMdhm.format(targetDate);
                    }
                }
            } else {
                return sdfMdhm.format(targetDate);
            }
        } else {
            //不是当年的数据
            return sdfYmdhm.format(targetDate);
        }
    }
    public static Date parseData(String time) {
        time = formatSystemCurrentMillis(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if (TextUtils.isEmpty(time)) {
            return null;
        }
        Date targetDate = null;
        try {
            targetDate = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return targetDate;
    }
    /**
     * 格式化时间
     * 如果是当天的  只显示时分
     * 如果是昨天的  显示 昨天 时分
     * 如果日期还早  就显示 月日 时分
     * 如果跨年  就显示 年月日 时分
     *
     * @return
     */
    public static String formatDataOfChatList(String targetTime) {
        SimpleDateFormat sdfYmdhm = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfMdhm = new SimpleDateFormat("MM-dd");
        SimpleDateFormat sdfHm = new SimpleDateFormat("HH:mm");
        if (TextUtils.isEmpty(targetTime)) {
            return "";
        }
        Date targetDate = null;
        try {
            targetDate = sdf.parse(targetTime);
        } catch (ParseException e) {
            targetTime = formatSystemCurrentMillis(targetTime);
            try {
                targetDate = sdf.parse(targetTime);
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
        if (targetDate == null) {
            return "";
        }
        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date());
        int currentYear = ca.get(Calendar.YEAR);
        int currentMonth = ca.get(Calendar.MONTH) + 1;
        int currentDay = ca.get(Calendar.DATE);
        Calendar targetCa = Calendar.getInstance();
        targetCa.setTime(targetDate);
        int targetYear = targetCa.get(Calendar.YEAR);
        int targetMonth = targetCa.get(Calendar.MONTH) + 1;
        int targetDay = targetCa.get(Calendar.DATE);

        if (currentYear == targetYear) {
            //当年的数据
            if (currentMonth == targetMonth) {
                //当月的数据
                if (currentDay==targetDay) {
                    //当天的数据
                    return sdfHm.format(targetDate);
                }else {
                    if (ca.get(Calendar.DAY_OF_YEAR)-targetCa.get(Calendar.DAY_OF_YEAR)==1) {
                        //昨天的数据
                        return "昨天";
                    }else {
                        return sdfMdhm.format(targetDate);
                    }
                }
            } else {
                return sdfMdhm.format(targetDate);
            }
        } else {
            //不是当年的数据
            return sdfYmdhm.format(targetDate);
        }
    }
    /**
     * 格式化时间
     * 如果是当天的  只显示时分
     * 如果是昨天的  显示 昨天 时分
     * 如果日期还早  就显示 月日 时分
     * 如果跨年  就显示 年月日 时分
     *
     * @return
     */
    public static String formatCollectDataOfChatList(String targetTime) {
        SimpleDateFormat sdfYmdhm = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfMdhm = new SimpleDateFormat("MM-dd");
        SimpleDateFormat sdfHm = new SimpleDateFormat("HH:mm");
        if (TextUtils.isEmpty(targetTime)) {
            return "";
        }
        Date targetDate = null;
        try {
            targetDate = sdf.parse(targetTime);
        } catch (ParseException e) {
            targetTime = formatSystemCurrentMillis(targetTime);
            try {
                targetDate = sdf.parse(targetTime);
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
        if (targetDate == null) {
            return "";
        }
        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date());
        int currentYear = ca.get(Calendar.YEAR);
        int currentMonth = ca.get(Calendar.MONTH) + 1;
        int currentDay = ca.get(Calendar.DATE);
        Calendar targetCa = Calendar.getInstance();
        targetCa.setTime(targetDate);
        int targetYear = targetCa.get(Calendar.YEAR);
        int targetMonth = targetCa.get(Calendar.MONTH) + 1;
        int targetDay = targetCa.get(Calendar.DATE);

        if (currentYear == targetYear) {
            //当年的数据
            if (currentMonth == targetMonth) {
                //当月的数据
                if (currentDay==targetDay) {
                    //当天的数据
                    return "今天";
                }else {
                    if (ca.get(Calendar.DAY_OF_YEAR)-targetCa.get(Calendar.DAY_OF_YEAR)==1) {
                        //昨天的数据
                        return "昨天";
                    }else {
                        return sdfMdhm.format(targetDate);
                    }
                }
            } else {
                return sdfMdhm.format(targetDate);
            }
        } else {
            //不是当年的数据
            return sdfYmdhm.format(targetDate);
        }
    }
}
