package com.juntai.disabled.basecomponent.net;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.juntai.disabled.basecomponent.BuildConfig;
import com.juntai.disabled.basecomponent.net.convert.NullAdapterFactory;
import com.juntai.disabled.basecomponent.utils.CustomeHttpLogger;
import com.juntai.disabled.basecomponent.utils.LogUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiRetrofit {
    public final String BASE_SERVER_URL = "https://wawa-api.vchangyi.com/";
//    public final String BASE_SERVER_URL = "http://192.168.1.106:8080/dongGuanPoliceStation/";

    private static ApiRetrofit apiRetrofit;
    private Retrofit retrofit;
    private OkHttpClient client;

    private static Gson gson;
    private String TAG = "ApiRetrofit";

    /**
     * 请求访问quest
     * response拦截器
     */
    private Interceptor interceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            long startTime = System.currentTimeMillis();
            Response response = chain.proceed(request);
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            MediaType mediaType = response.body().contentType();
            String content = response.body().string();
            LogUtil.d("| response:" + content);
//            LogUtil.d("----------Request End:" + duration + "毫秒----------");
            return response.newBuilder()
                    .body(ResponseBody.create(mediaType, content))
                    .build();
        }
    };

    /**
     * 请求访问quest
     * response拦截器
     */
    private static HttpLoggingInterceptor getLogger() {
        //设置日志拦截器
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new CustomeHttpLogger());
        /**
         * log拦截器默认不打印，要设置打印的等级(NONE,BASIC,HEADERS,BODY)，有4个，body是最大的级别，把她放入okhttp对象中，再把okhttp放入retrofit ，那么就可以打印对应的body，所有的代码
         */
        if (BuildConfig.DEBUG) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);// 重要
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.NONE);// 重要
        }
        return logging;
    }


    public <T> T getApiService(Class<T> service) {
        client = new OkHttpClient.Builder()
                //添加log拦截器
                .addInterceptor(getLogger())
                .connectTimeout(30, TimeUnit.SECONDS)//连接超时
                .writeTimeout(60, TimeUnit.SECONDS)//写入超时
                .readTimeout(60, TimeUnit.SECONDS)//读取超时
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create(buildGson()))
                .addConverterFactory(ScalarsConverterFactory.create())
                //支持RxJava2
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit.create(service);
    }

    public static Gson buildGson() {
        if (gson == null) {
            gson = new GsonBuilder()
                    .registerTypeAdapterFactory(new NullAdapterFactory())
                    .create();
        }
        return gson;
    }

    public static ApiRetrofit getInstance() {
        if (apiRetrofit == null) {
            synchronized (Object.class) {
                if (apiRetrofit == null) {
                    apiRetrofit = new ApiRetrofit();
                }
            }
        }
        return apiRetrofit;
    }
}
