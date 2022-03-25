package com.juntai.disabled.video.record;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.juntai.disabled.basecomponent.base.BaseActivity;
import com.juntai.disabled.basecomponent.utils.ActionConfig;
import com.juntai.disabled.video.R;
import com.juntai.disabled.video.player.PlayerActivity;
import com.mabeijianxi.smallvideorecord2.MediaRecorderActivity;

/**
 * 视频预览
 * Created by jian on 2016/7/21 15:52
 * mabeijianxi@gmail.com
 */
public class VideoPreviewActivity extends BaseActivity implements View.OnClickListener {
    private String videoUri;
    private TextView tv_send;
    private TextView tv_cancel;
    private String videoScreenshot;
    VideoView videoView;
    private AlertDialog dialog;
    LinearLayout videoLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void initEvent() {
        tv_cancel.setOnClickListener(this);
        tv_send.setOnClickListener(this);
    }


    @Override
    public void initData() {
        Intent intent = getIntent();
        videoUri = intent.getStringExtra(MediaRecorderActivity.VIDEO_URI);
        videoScreenshot = intent.getStringExtra(MediaRecorderActivity.VIDEO_SCREENSHOT);

        videoView.setVideoPath(videoUri);
        videoView.start();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.start();
            }
        });
        initEvent();
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_video_preview;
    }

    @Override
    public void initView() {
        getToolbar().setVisibility(View.GONE);
        mBaseRootCol.setFitsSystemWindows(true);
        mImmersionBar.statusBarColor(R.color.black)
                .statusBarDarkFont(false)
                .init();
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_send = (TextView) findViewById(R.id.tv_send);
//        videoView = findViewById(R.id.videopreview);
        videoLayout = findViewById(R.id.video_layout);

        videoView = new VideoView(mContext.getApplicationContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        videoView.setLayoutParams(layoutParams);
        videoLayout.addView(videoView);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_cancel) {
            hesitate();
        } else if (i == R.id.tv_send) {//确认
            Intent intent = new Intent();
            intent.setAction(ActionConfig.BROAD_VIDEO);
            intent.putExtra("videoUri", videoUri);
            intent.putExtra("videoScreenshotUri", videoScreenshot);
            sendBroadcast(intent);
            finish();
        } else if (i == R.id.iv_video_screenshot) {//播放
            startActivity(new Intent(this, PlayerActivity.class).putExtra(
                    "locPath", videoUri));
        }
    }

    @Override
    public void onBackPressed() {
        hesitate();
    }

    private void hesitate() {
        if (dialog == null) {
            dialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.hint)
                    .setMessage(R.string.record_camera_exit_dialog_message)
                    .setNegativeButton(
                            R.string.record_camera_cancel_dialog_yes,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    finish();
//                                    FileUtils.deleteDir(getIntent().getStringExtra(MediaRecorderActivity.OUTPUT_DIRECTORY));

                                }

                            })
                    .setPositiveButton(R.string.record_camera_cancel_dialog_no,
                            null).setCancelable(false).show();
        } else {
            dialog.show();
        }
        setAlertDialogHeightWidth(dialog,-1,0);
    }

    @Override
    protected void onDestroy() {
        videoView.stopPlayback();
        videoView.suspend();
        videoView.setOnErrorListener(null);
        videoView.setOnPreparedListener(null);
        videoView.setOnCompletionListener(null);
        videoLayout.removeAllViews();
        videoView = null;
        super.onDestroy();
    }
}
