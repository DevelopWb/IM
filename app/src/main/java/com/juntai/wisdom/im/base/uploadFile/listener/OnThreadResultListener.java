package com.juntai.wisdom.im.base.uploadFile.listener;

import com.juntai.wisdom.im.base.uploadFile.UploadFileBean;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2022-03-06 15:51
 * @UpdateUser: 更新者
 * @UpdateDate: 2022-03-06 15:51
 */
public interface OnThreadResultListener {
    void onProgressChange(UploadFileBean uploadFileBean, int percent);//进度变化回调
    void onFinish(UploadFileBean uploadFileBean);//线程完成时回调
    void onInterrupted();//线程被中断回调
}
