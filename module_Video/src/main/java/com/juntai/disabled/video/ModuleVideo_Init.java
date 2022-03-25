package com.juntai.disabled.video;

import com.juntai.disabled.basecomponent.utils.FileCacheUtils;
import com.mabeijianxi.smallvideorecord2.DeviceUtils;
import com.mabeijianxi.smallvideorecord2.JianXiCamera;

import java.io.File;

/**
 * 视频相关初始化
 * @aouther Ma
 * @date 2019/4/5
 */
public class ModuleVideo_Init {
    public static void init(){
        initSmallVideo();
    }
    /**
     * 初始视频拍摄
     */
    public static void initSmallVideo() {
        // 设置拍摄视频缓存路径
        //File dcim = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        //自定义的位置--/ChengGuan/video/
        File dcim = new File(FileCacheUtils.getAppVideoPath(true));
        if (DeviceUtils.isZte()) {
            if (dcim.exists()) {
                JianXiCamera.setVideoCachePath(dcim.getPath()+"/");
            } else {
                JianXiCamera.setVideoCachePath(dcim.getPath().replace("/sdcard/", "/sdcard-ext/")+"/");
            }
        } else {
            JianXiCamera.setVideoCachePath(dcim.getPath()+"/");
        }
        // 初始化拍摄，遇到问题可选择开启此标记，以方便生成日志
        JianXiCamera.initialize(true,null);
    }
}
