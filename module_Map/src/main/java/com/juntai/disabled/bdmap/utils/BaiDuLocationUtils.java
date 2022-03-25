package com.juntai.disabled.bdmap.utils;

import android.content.Context;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

/**
 * 百度定位
 */
public class BaiDuLocationUtils {

    public static LocationClient mLocationClient;

    private Object objLock;

    //BDAbstractLocationListener为7.2版本新增的Abstract类型的监听接口
    //原有BDLocationListener接口暂时同步保留。具体介绍请参考后文第四步的说明

    public BaiDuLocationUtils(Context context) {
        objLock = new Object();
        synchronized (objLock) {
            if (mLocationClient == null) {
                mLocationClient = new LocationClient(context.getApplicationContext());
                mLocationClient.setLocOption(getDefaultLocationClientOption());
            }
        }
    }

    public void reStart(){
        mLocationClient.restart();
    }
    /**
     * 初始化参数
     */
    public static LocationClientOption getDefaultLocationClientOption() {
        LocationClientOption mOption = new LocationClientOption();
        mOption.setLocationMode(LocationMode.Hight_Accuracy); // 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        mOption.setCoorType( "bd09ll" ); // 可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        mOption.setScanSpan(10000); // 可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
        mOption.setIsNeedAddress(true); // 可选，设置是否需要地址信息，默认不需要
        mOption.setIsNeedLocationDescribe(true); // 可选，设置是否需要地址描述
        mOption.setNeedDeviceDirect(false); // 可选，设置是否需要设备方向结果
        mOption.setLocationNotify(false); // 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        mOption.setIgnoreKillProcess(true); // 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop
        mOption.setIsNeedLocationDescribe(true); // 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation
        mOption.setIsNeedLocationPoiList(true); // 可选，默认false，设置是否需要POI结果，可以在BDLocation
        mOption.SetIgnoreCacheException(false); // 可选，默认false，设置是否收集CRASH信息，默认收集
        mOption.setOpenGps(true); // 可选，默认false，设置是否开启Gps定位
        mOption.setIsNeedAltitude(false); // 可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        return mOption;
    }


    public void start(BDAbstractLocationListener listener) {
        synchronized (objLock) {
            if (mLocationClient != null && !mLocationClient.isStarted()) {
                registerListener(listener);
                mLocationClient.start();
            }
        }
    }

    public void requestLocation() {
        if (mLocationClient != null) {
            mLocationClient.requestLocation();
        }
    }

    public void stop(BDAbstractLocationListener listener) {
        synchronized (objLock) {
            if (mLocationClient != null && mLocationClient.isStarted()) {
                unregisterListener(listener);
                mLocationClient.stop();
            }
        }
    }
    /***
     * 注册定位监听
     *
     * @param listener
     * @return
     */

    public boolean registerListener(BDAbstractLocationListener listener) {
        boolean isSuccess = false;
        if (listener != null) {
            mLocationClient.registerLocationListener(listener);
            isSuccess = true;
        }
        return isSuccess;
    }

    public void unregisterListener(BDAbstractLocationListener listener) {
        if (listener != null) {
            mLocationClient.unRegisterLocationListener(listener);
        }
    }
}
