package com.juntai.wisdom.im;


import com.juntai.disabled.basecomponent.net.ApiRetrofit;

/**
 * 网络请求
 */
public class AppNetModule {
    static com.juntai.wisdom.im.AppServer appServer ;
    public static com.juntai.wisdom.im.AppServer createrRetrofit() {
        if (appServer == null){
            appServer = ApiRetrofit.getInstance().getApiService(com.juntai.wisdom.im.AppServer.class);
        }
        return appServer;
    }
}
