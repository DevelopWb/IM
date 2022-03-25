package com.juntai.disabled.basecomponent.net;


import com.google.gson.Gson;
import com.juntai.disabled.basecomponent.utils.LogUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * 仅用于文件下载
 */
public class FileRetrofit {
    public final String BASE_SERVER_URL = "https://wawa-api.vchangyi.com/";

    private static FileRetrofit apiRetrofit;
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
            Response response = chain.proceed(chain.request());
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            LogUtil.d( "----------Request Start----------------");
            LogUtil.d("| " + request.toString() + request.headers().toString());
            LogUtil.d("----------Request End:" + duration + "毫秒----------");
            return response.newBuilder()
                    .body(response.body())
                    .build();
        }
    };


    public FileServer getFileService() {
        client = new OkHttpClient.Builder()
                //添加log拦截器
                .addInterceptor(interceptor)
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
        return retrofit.create(FileServer.class);
    }

    public static FileRetrofit getInstance() {
        if (apiRetrofit == null) {
            synchronized (Object.class) {
                if (apiRetrofit == null) {
                    apiRetrofit = new FileRetrofit();
                }
            }
        }
        return apiRetrofit;
    }

    public interface FileServer {
        @GET()
        Observable<ResponseBody> getFile_GET(@Url String path);

        @POST()
        Observable<ResponseBody> getFile_POST(@Url String path);
    }

}
