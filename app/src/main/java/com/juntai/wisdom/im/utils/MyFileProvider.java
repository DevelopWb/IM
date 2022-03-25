package com.juntai.wisdom.im.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.juntai.disabled.federation.R;

import java.io.File;

/**
 * @aouther Ma
 * @date 2019/4/2
 */
public class MyFileProvider extends FileProvider {


    public static Uri getUriFromFile(Context context, File file) {
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {   //如果在Android7.0以上,使用FileProvider获取Uri
            try {
                uri = FileProvider.getUriForFile(context, "com.juntai.wisdom.im" +
                                ".fileProvider",
                        file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {    //否则使用Uri.fromFile(file)方法获取Uri
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    /**
     * 获取文件对应的图标
     * 替换图标的逻辑
     *
     * @return
     */
    public static int getFileResId(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return R.mipmap.file_unknow_icon;
        }
        if (fileName.contains(".jpg") || fileName.contains(".jpeg") || fileName.contains(".png") || fileName.contains(".bpm")) {
            return R.mipmap.file_image_icon;
        } else if (fileName.contains(".mp4") || fileName.contains(".avi") || fileName.contains(".mpeg")) {
            return R.mipmap.file_video_icon;
        } else if (fileName.contains(".zip")) {
            return R.mipmap.file_zip_icon;
        } else if (fileName.contains(".pdf")) {
            return R.mipmap.file_pdf_icon;
        } else if (fileName.contains(".ppt")) {
            return R.mipmap.file_ppt_icon;
        } else if (fileName.contains(".xls") || fileName.contains(".xlsx")) {
            return R.mipmap.file_excel_icon;
        } else if (fileName.contains(".doc") || fileName.contains(".docx")) {
            return R.mipmap.file_word_icon;
        }
        return R.mipmap.file_unknow_icon;

    }


}
