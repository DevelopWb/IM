package com.juntai.disabled.video;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2022-02-27 8:31
 * @UpdateUser: 更新者
 * @UpdateDate: 2022-02-27 8:31
 */
public class CustomStandardGSYVideoPlayer extends StandardGSYVideoPlayer {


    public CustomStandardGSYVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.display_video;
    }


    public ImageView getMoreActionIv() {
        return findViewById(R.id.display_video_action_more_iv);
    }

    public ImageView getDownloadActionIv() {
        return findViewById(R.id.display_video_action_download_iv);
    }
}
