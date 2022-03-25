package com.juntai.wisdom.im.base.uploadFile;

import android.app.Activity;

import com.juntai.disabled.basecomponent.utils.LogUtil;
import com.juntai.wisdom.im.AppNetModule;
import com.juntai.wisdom.im.base.uploadFile.listener.OnThreadResultListener;
import com.juntai.wisdom.im.utils.UserInfoManager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.CountDownLatch;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2022-03-06 15:51
 * @UpdateUser: 更新者
 * @UpdateDate: 2022-03-06 15:51
 */
public class UploadFileThread implements Runnable {


    private String filePath;//本地文件的参数：// 文件名

    private OnThreadResultListener listener;//任务线程回调接口
    private Activity mContext;

    private UploadFileBean fileBean;

    int index = 0;

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    public UploadFileThread(Activity mContext, int index, UploadFileBean fileBean, OnThreadResultListener listener){
        this.fileBean = fileBean;
        this.filePath = fileBean.getFilePath();
        this.listener=listener;
        this.mContext = mContext;
        this.index = index;
    }

    @Override
    public void run() {

        //上传线程

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("account", UserInfoManager.getUserUUID());
        FileUploadObserver<UploadFileBean> fileUploadObserver = new FileUploadObserver<UploadFileBean>() {
            @Override
            public void onUpLoadSuccess(UploadFileBean uploadFileBean) {
                //上传成功
                LogUtil.d("上传成功");
                //继续执行线程
                countDownLatch.countDown();
                listener.onFinish(uploadFileBean);
            }

            @Override
            public void onUpLoadFail(Throwable e) {
                //上传失败
                LogUtil.d("上传失败");
            }

            @Override
            public void onProgress(UploadFileBean uploadFileBean,int progress) {
                //progress 0-100
                LogUtil.d("上传---" + String.valueOf(progress));
                listener.onProgressChange(uploadFileBean,progress);
            }
        };

        UploadFileRequestBody uploadFileRequestBody = new UploadFileRequestBody<UploadFileBean>(fileBean, fileUploadObserver);
        try {
            String fileName = URLEncoder.encode(filePath,"UTF-8");
            builder .addFormDataPart("file", fileName,uploadFileRequestBody);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        AppNetModule.createrRetrofit()
                .uploadFiles(builder.build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fileUploadObserver);

        try {
            //等待
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}