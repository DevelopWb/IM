package com.juntai.disabled.basecomponent.net.convert;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * @aouther Ma
 * @date 2019/3/6
 */
public class NullAdapterFactory<T> implements TypeAdapterFactory {
//    @SuppressWarnings("unchecked")
//    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
//        Class<T> rawType = (Class<T>) type.getRawType();
//        if (rawType != String.class) {
//            return null;
//        }
//        return (TypeAdapter<T>) new StringNullAdapter();
//    }
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<T> rawType = (Class<T>) type.getRawType();
        Log.e("ffffffff","11111111111");
        if (rawType == String.class) {
            return (TypeAdapter<T>) new StringNullAdapter();
        } else if (rawType == Float.class || rawType == float.class) {
            return (TypeAdapter<T>) new FloatNullAdapter();
        } else if (rawType == Double.class || rawType == double.class) {
            return (TypeAdapter<T>) new DoubleNullAdapter();
        } else if (rawType == Integer.class || rawType == int.class) {
            Log.e("ffffffff","1232321231");
            return (TypeAdapter<T>) new IntegerNullAdapter();
        } else if (rawType == Long.class || rawType == long.class) {
            return (TypeAdapter<T>) new LongNullAdapter();
        }
        return null;
    }
}