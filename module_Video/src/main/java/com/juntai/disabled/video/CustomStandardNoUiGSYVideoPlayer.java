package com.juntai.disabled.video;

import android.content.Context;
import android.util.AttributeSet;

import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2022-02-27 8:31
 * @UpdateUser: 更新者
 * @UpdateDate: 2022-02-27 8:31
 */
public class CustomStandardNoUiGSYVideoPlayer extends StandardGSYVideoPlayer {


    public CustomStandardNoUiGSYVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.display_no_ui_video;
    }


}
