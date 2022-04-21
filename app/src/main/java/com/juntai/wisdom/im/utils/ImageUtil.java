package com.juntai.wisdom.im.utils;

import android.text.TextUtils;

import com.juntai.disabled.basecomponent.utils.FileCacheUtils;
import com.juntai.wisdom.im.bean.MessageBodyBean;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2022/4/21 10:05
 * @UpdateUser: 更新者
 * @UpdateDate: 2022/4/21 10:05
 */
public class ImageUtil {


    /**
     * 获取图片的本地缓存路径
     * @return
     */
    public static String  getImageCatchPic(MessageBodyBean messageBodyBean){
        return FileCacheUtils.getAppImagePath(true) + getSavedFileName(messageBodyBean);
    }
    /**
     * 获取图片的缩略图的本地缓存路径
     * @return
     */
    public static String  getImageThumCatchPic(MessageBodyBean messageBodyBean){
        return FileCacheUtils.getAppImagePath(true) + getSavedFileName(messageBodyBean)+UrlFormatUtil.IMAGE_THUM;
    }

    /**
     * 获取图片的本地缓存路径
     * @return
     */
    public static String  getVideoImageCatchPic(MessageBodyBean messageBodyBean){
        return FileCacheUtils.getAppImagePath(true) + getSavedFileName(messageBodyBean.getVideoCover());
    }



    /**
     * 获取文件名称
     *
     * @param messageBodyBean
     * @return
     */
    private static String getSavedFileName(MessageBodyBean messageBodyBean) {
        String content = messageBodyBean.getContent();
        if (TextUtils.isEmpty(content)) {
            return null;
        }

        if (content.contains("/")) {
            content = content.substring(content.lastIndexOf("/") + 1, content.length());
        }
        return content;
    }

    /**
     * 获取文件名称
     *
     * @return
     */
    private static String getSavedFileName(String content) {
        if (TextUtils.isEmpty(content)) {
            return null;
        }

        if (content.contains("/")) {
            content = content.substring(content.lastIndexOf("/") + 1, content.length());
        }
        return content;
    }
}
