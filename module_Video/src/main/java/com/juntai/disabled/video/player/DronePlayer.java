package com.juntai.disabled.video.player;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.juntai.disabled.basecomponent.base.BaseActivity;
import com.juntai.disabled.basecomponent.utils.ToastUtils;
import com.juntai.disabled.video.R;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import tv.danmaku.ijk.media.exo2.Exo2PlayerManager;

public class DronePlayer extends BaseActivity
{
    @Override
    public int getLayoutView() {
        return R.layout.activity_drone_player;
    }
    StandardGSYVideoPlayer videoPlayer;

    OrientationUtils orientationUtils;
    Intent intent;
    int droneId;
    @Override
    public void initView() {
        intent = getIntent();
        droneId = intent.getIntExtra("droneId",-1);
        if(droneId<0){
            ToastUtils.toast(mContext,"无人机错误");
            finish();
        }
        getToolbar().setVisibility(View.GONE);
        init();
    }

    private void init() {
        videoPlayer =  (StandardGSYVideoPlayer)findViewById(R.id.video_player);
        //RTMP播放需切换至exo播放
        PlayerFactory.setPlayManager(Exo2PlayerManager.class);
        String source1 = "rtmp://61.156.157.132:33332/live/"+droneId;
        videoPlayer.setUp(source1, false, "无人机实时画面");
        //增加封面
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.drawable.empty_drawable);
        videoPlayer.setThumbImageView(imageView);
        //增加title
        videoPlayer.getTitleTextView().setVisibility(View.VISIBLE);
        //设置返回键
        videoPlayer.getBackButton().setVisibility(View.VISIBLE);
        //设置旋转
        orientationUtils = new OrientationUtils(this, videoPlayer);
        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orientationUtils.resolveByClick();
            }
        });
        //是否可以滑动调整
        videoPlayer.setIsTouchWiget(true);
        //设置返回按键功能
        videoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        videoPlayer.startPlayLogic();
    }


    @Override
    public void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoPlayer.release();
        videoPlayer = null;
    }
}
