package com.juntai.disabled.video.record;

import android.app.Activity;

import com.mabeijianxi.smallvideorecord2.LocalMediaCompress;
import com.mabeijianxi.smallvideorecord2.MediaRecorderActivity;
import com.mabeijianxi.smallvideorecord2.model.AutoVBRMode;
import com.mabeijianxi.smallvideorecord2.model.BaseMediaBitrateConfig;
import com.mabeijianxi.smallvideorecord2.model.LocalMediaConfig;
import com.mabeijianxi.smallvideorecord2.model.MediaRecorderConfig;
import com.mabeijianxi.smallvideorecord2.model.OnlyCompressOverBean;


/**
 * @aouther Ma
 * @date 2019/3/20
 */
public class VideoRecordUtils {
    public void test(Activity activity,String name){
        // 录制
        MediaRecorderConfig config = new MediaRecorderConfig.Buidler()
                .fullScreen(false)
                .smallVideoWidth(360)
                .smallVideoHeight(480)
                .recordTimeMax(6000)
                .recordTimeMin(1500)
                .maxFrameRate(20)
                .videoBitrate(600000)
                .captureThumbnailsTime(1)
                .build();
        MediaRecorderActivity.goSmallVideoRecorder(activity, name, config);
    }

    public void videoSelect(String path){
        //选择本地视频压缩,太慢了
        LocalMediaConfig.Buidler buidler = new LocalMediaConfig.Buidler();
        final LocalMediaConfig config = buidler
                .setVideoPath(path)
                .captureThumbnailsTime(1)
                .doH264Compress(new AutoVBRMode(20).setVelocity(BaseMediaBitrateConfig.Velocity.ULTRAFAST))
                .setFramerate(15)
                .setScale(1.0f)
                .build();
        OnlyCompressOverBean onlyCompressOverBean = new LocalMediaCompress(config)
                .startCompress();
    }
}
