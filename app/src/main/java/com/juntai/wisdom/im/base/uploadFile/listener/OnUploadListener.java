package com.juntai.wisdom.im.base.uploadFile.listener;

import com.juntai.wisdom.im.base.uploadFile.UploadFileBean;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2022-03-06 15:52
 * @UpdateUser: 更新者
 * @UpdateDate: 2022-03-06 15:52
 */
public interface OnUploadListener {//主线程回调
    void onAllSuccess();
    void onAllFailed();
    void onThreadProgressChange(UploadFileBean uploadFileBean,int position, int percent);
    void onThreadFinish(UploadFileBean uploadFileBean,int position);
    void onThreadInterrupted(int position);
}