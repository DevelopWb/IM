package com.juntai.wisdom.im.base.uploadFile;


import com.juntai.wisdom.im.bean.MessageBodyBean;

import io.reactivex.observers.DefaultObserver;

/**
 * 上传文件的RxJava2回调
 */

public abstract class FileUploadObserver<T> extends DefaultObserver<T> {
    private int lastProgress = 0;
    private MessageBodyBean messageBodyBean;

    @Override
    public void onNext(T t) {
        UploadFileBean uploadFileBean = (UploadFileBean) t;
        uploadFileBean.setMessageBodyBean(messageBodyBean);
        onUpLoadSuccess((T) uploadFileBean);
    }

    @Override
    public void onError(Throwable e) {
        onUpLoadFail(e);
    }

    @Override
    public void onComplete() {

    }

    //监听进度的改变
    public void onProgressChange(T t, long bytesWritten, long contentLength) {
        int currentProgress = (int) (bytesWritten * 100 / contentLength);
        if (currentProgress != lastProgress && currentProgress % 10 == 0) {
            lastProgress = currentProgress;
            this.messageBodyBean = ((UploadFileBean) t).getMessageBodyBean();
            onProgress(t, currentProgress);
        }

    }

    //上传成功的回调
    public abstract void onUpLoadSuccess(T t);

    //上传失败回调
    public abstract void onUpLoadFail(Throwable e);

    //上传进度回调
    public abstract void onProgress(T t, int progress);

}
