package com.juntai.disabled.basecomponent.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;

import com.orhanobut.logger.Logger;

import java.lang.reflect.Method;

/**
 * Describe:
 * Create by tobato
 * 2020-3-17
 * email:954101549@qq.com
 */

public class NavigationBarInfo {

    private static final String TAG = "NavigationBarInfo";

    /**
     * 获取虚拟按键的高度
     */
    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        if (hasNavBar(context)) {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
        }
        Logger.d(TAG,"NavigationBarHeight = "+result);
        return result;
    }

    /**
     * 检查是否存在虚拟按键栏
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static boolean hasNavBar(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
        if (resourceId != 0) {
            boolean hasNav = res.getBoolean(resourceId);
            String sNavBarOverride = getNavBarOverride();
            if ("1".equals(sNavBarOverride)) {
                hasNav = false;
            } else if ("0".equals(sNavBarOverride)) {
                hasNav = true;
            }
            return hasNav;
        } else {
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
    }

    /**
     * 判断虚拟按键栏是否重写
     */
    public static String getNavBarOverride() {
        String sNavBarOverride = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Exception e) {
                Logger.e(TAG, e.toString());
            }
        }
        return sNavBarOverride;
    }

    public static void adaptiveStartPage(Activity activity, ImageView imageView, boolean bottom) {
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
//        float height = dm.heightPixels + getNavigationBarHeight(activity) - getXiaomiNotchHight(activity);
        float height = dm.heightPixels + getNavigationBarHeight(activity);
//        float height = getHeight(activity);
        float width = dm.widthPixels;

        float standard = (float) (1920.0 / 1080.0);
        float actual = height / width;
        Logger.d(TAG, "heigth = " + height);
        Logger.d(TAG, "standard = " + standard);
        Logger.d(TAG, "actual = " + actual);
        //以宽为参考
//        if (actual > standard) {
//            imageView.setScaleY(actual / standard);
//            imageView.setScaleX(actual / standard);
//        } else if (actual < standard) {
//            imageView.setScaleY(standard / actual);
//            imageView.setScaleX(standard / actual);
//        }
    }

    public static void hideNavigationBar(Activity activity) {
        if (Build.VERSION.SDK_INT < 19) {
            View v = activity.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else {
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /**
     * 判断是否是刘海屏
     *
     * @return 是否是刘海屏
     */
    public static boolean hasNotchScreen(Activity activity) {

        return getInt("ro.miui.notch", activity) || hasNotchAtHuawei(activity) || hasNotchAtOPPO(activity)
                || hasNotchAtVivo(activity);
    }

//    /**
//     * Android P 刘海屏判断
//     * @param activity
//     * @return
//     */
//    public static DisplayCutout isAndroidP(Activity activity){
//        View decorView = activity.getWindow().getDecorView();
//        if (decorView != null && android.os.Build.VERSION.SDK_INT >= 28){
//            WindowInsets windowInsets = decorView.getRootWindowInsets();
//            if (windowInsets != null)
//                return windowInsets.getDisplayCutout();
//        }
//        return null;
//    }

    /**
     * 小米刘海屏判断.
     *
     * @return property ro.miui.notch，值为1时则是 Notch 屏手机
     * SystemProperties.getInt("ro.miui.notch", 0) == 1;
     */
    public static boolean getInt(String key, Activity activity) {
        int result = 0;
        if (isXiaomi()) {
            try {
                ClassLoader classLoader = activity.getClassLoader();
                @SuppressWarnings("rawtypes")
                Class SystemProperties = classLoader.loadClass("android.os.SystemProperties");
                //参数类型
                @SuppressWarnings("rawtypes")
                Class[] paramTypes = new Class[2];
                paramTypes[0] = String.class;
                paramTypes[1] = int.class;
                Method getInt = SystemProperties.getMethod("getInt", paramTypes);
                //参数
                Object[] params = new Object[2];
                params[0] = key;
                params[1] = 0;
                result = (Integer) getInt.invoke(SystemProperties, params);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result == 1;
    }

    /**
     * 华为刘海屏判断
     */
    public static boolean hasNotchAtHuawei(Context context) {
        boolean ret = false;
        try {
            ClassLoader classLoader = context.getClassLoader();
            Class HwNotchSizeUtil = classLoader.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
            ret = (boolean) get.invoke(HwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
            Logger.e(TAG, "hasNotchAtHuawei ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Logger.e(TAG, "hasNotchAtHuawei NoSuchMethodException");
        } catch (Exception e) {
            Logger.e(TAG, "hasNotchAtHuawei Exception");
        } finally {
            return ret;
        }
    }

    /**
     * 华为手机获取刘海尺寸：width、height
     * int[0]值为刘海宽度 int[1]值为刘海高度。
     */
    public static int[] getNotchSize(Context context) {

        int[] ret = new int[]{0, 0};

        try {

            ClassLoader cl = context.getClassLoader();

            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");

            Method get = HwNotchSizeUtil.getMethod("getNotchSize");

            ret = (int[]) get.invoke(HwNotchSizeUtil);

        } catch (Exception e) {

            Logger.e(TAG, "getNotchSize Exception");

        } finally {

            return ret;

        }

    }

    public static final int VIVO_NOTCH = 0x00000020;//是否有刘海
    public static final int VIVO_FILLET = 0x00000008;//是否有圆角

    /**
     * VIVO刘海屏判断
     *
     * @return
     */
    public static boolean hasNotchAtVivo(Context context) {
        boolean ret = false;
        try {
            ClassLoader classLoader = context.getClassLoader();
            Class FtFeature = classLoader.loadClass("android.util.FtFeature");
            Method method = FtFeature.getMethod("isFeatureSupport", int.class);
            ret = (boolean) method.invoke(FtFeature, VIVO_NOTCH);
        } catch (ClassNotFoundException e) {
            Logger.e(TAG, "hasNotchAtVivo ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Logger.e(TAG, "hasNotchAtVivo NoSuchMethodException");
        } catch (Exception e) {
            Logger.e(TAG, "hasNotchAtVivo Exception");
        } finally {
            return ret;
        }
    }

    /**
     * OPPO刘海屏判断
     */
    public static boolean hasNotchAtOPPO(Context context) {
        return context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }

    // 是否是小米手机
    public static boolean isXiaomi() {
        return "Xiaomi".equals(Build.MANUFACTURER);
    }

    public static boolean isHuawei() {
        return "Huawei".equals(Build.MANUFACTURER);
    }

    public static boolean isOppo() {
        return "Oppo".equals(Build.MANUFACTURER);
    }

    public static boolean isVivo() {
        return "Vivo".equals(Build.MANUFACTURER);
    }

    /**
     * 获取小米刘海高度
     *
     * @param activity
     * @return
     */
    public static int getXiaomiNotchHight(Activity activity) {
        int resourceId = activity.getResources().getIdentifier("notch_height", "dimen", "android");
        if (resourceId > 0) {
            return activity.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static float getHeight(Activity activity) {
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        if (hasNotchScreen(activity)) {
            float notchHight = 0;
            if (isHuawei()) {
                notchHight = getNotchSize(activity)[1];
            } else if (isXiaomi()) {
                notchHight = getXiaomiNotchHight(activity);
            } else if (isVivo()) {
                notchHight = getXiaomiNotchHight(activity);
            } else if (isOppo()) {
                notchHight = getXiaomiNotchHight(activity);
            }

            return dm.heightPixels + getNavigationBarHeight(activity) - notchHight;
        }
        return dm.heightPixels + getNavigationBarHeight(activity);
    }


}
