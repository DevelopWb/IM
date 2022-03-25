package com.juntai.disabled.basecomponent.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.juntai.disabled.basecomponent.app.BaseApplication;

/**
 * @aouther Ma
 * @date 2019/3/13
 */
public class BaseAppUtils {

    /**
     * 获取Fileprovider
     * @return
     */
    public static String getFileprovider(){
        return BaseAppUtils.getPackageName(BaseApplication.app) + ".fileProvider";
    }

    /**
     * 获取应用名称
     * @return
     */
    public static synchronized String getAppName() {
        try {
            PackageManager packageManager = BaseApplication.app.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    BaseApplication.app.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return BaseApplication.app.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取logo
     * @return
     */
    public static synchronized int getAppLogo() {
        try {
            int ic = BaseApplication.app.getResources().getIdentifier("logo", "mipmap", BaseApplication.app.getPackageName());
            return ic;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * [获取应用程序版本名称信息]
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * [获取应用程序包名信息]
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized String getPackageName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    //判断当前应用是否是debug状态
    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }
}
