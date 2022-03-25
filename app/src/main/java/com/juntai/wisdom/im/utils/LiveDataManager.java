package com.juntai.wisdom.im.utils;

import android.arch.lifecycle.MutableLiveData;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2020/11/4 14:12
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/11/4 14:12
 */
public class LiveDataManager {


    private static LiveDataManager mLiveDataManager = new LiveDataManager();

    private Map<String, MutableLiveData<Object>> mMap;

    public static LiveDataManager getInstance() {
        return mLiveDataManager;
    }

    private LiveDataManager() {
        mMap = new HashMap<>();
    }

    /**
     * 存取一体
     *
     * @param key
     * @param mClass
     * @param <T>
     * @return
     */
    public <T> MutableLiveData<T> with(String key, Class<T> mClass) {
        if (!mMap.containsKey(key)) {
            mMap.put(key,new MutableLiveData<Object>());
        }
        return (MutableLiveData<T>) mMap.get(key);
    }



}
