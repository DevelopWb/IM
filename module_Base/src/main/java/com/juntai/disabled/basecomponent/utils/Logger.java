package com.juntai.disabled.basecomponent.utils;

import android.util.Log;

/**
 * Created by efan on 2017/4/13.
 */

public class Logger {

    //设为false关闭日志
    public static boolean LOG_ENABLE = false;

    public static void i(String tag, String msg){
        if (LOG_ENABLE && msg != null){
            Log.i(tag, msg);
        }
    }
    public static void v(String tag, String msg){
        if (LOG_ENABLE && msg != null){
            Log.v(tag, msg);
        }
    }
    public static void d(String tag, String msg){
        if (LOG_ENABLE && msg != null){
            Log.d(tag, msg);
        }
    }
    public static void w(String tag, String msg){
        if (LOG_ENABLE && msg != null){
            Log.w(tag, msg);
        }
    }
    public static void e(String tag, String msg){
        if (LOG_ENABLE && msg != null){
            Log.e(tag, msg);
        }
    }

}
