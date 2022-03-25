package com.juntai.disabled.bdmap.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * author:wong
 * Date: 2019/3/21
 * Description:
 */
public class SharedPreferencesUtil {

    public static final String mTAG = "mapSP";
    // 创建一个写入器
    private static SharedPreferences mPreferences;
    private static SharedPreferences.Editor mEditor;
    private static SharedPreferencesUtil mSharedPreferencesUtil;

    // 构造方法
    public SharedPreferencesUtil(Context context) {
        mPreferences = context.getSharedPreferences(mTAG, Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
    }

    // 单例模式
    public static SharedPreferencesUtil getInstance(Context context) {
        if (mSharedPreferencesUtil == null) {
            mSharedPreferencesUtil = new SharedPreferencesUtil(context);
        }
        return mSharedPreferencesUtil;
    }

    // 存入数据
    public void putSP(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }
    public void putIntSP(String key,int value){
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    // 获取数据
    public String getSP(String key) {
        return mPreferences.getString(key, "");
    }
    public int getIntSP(String key){
        return mPreferences.getInt(key, 0);
    }

    // 移除数据
    public void removeSP(String key) {
        mEditor.remove(key);
        mEditor.commit();
    }
}