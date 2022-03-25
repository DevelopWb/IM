package com.juntai.disabled.video.player;

import android.net.Uri;
import android.os.Handler;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.juntai.disabled.basecomponent.mvp.BasePresenter;
import com.juntai.disabled.basecomponent.utils.BaseAppUtils;
import com.juntai.disabled.basecomponent.base.BaseDownLoadActivity;
import com.juntai.disabled.video.R;

/**
 * @aouther Ma
 * @date 2019/4/1
 * <p>
 * 视频播放
 * Intent intent = new Intent(mContext, PlayerActivity.class);
 * String ss = "http://kb167.cn:43212/zhcg/u/appInferFace/appDownloadVideo.shtml?token="
 * +App.getUserToken()+"&account="
 * +App.getAccount()+"&id=1628";
 * intent.putExtra("playPath",ss);
 * intent.putExtra("savePath", FileCacheUtils.getAppVideoPath()+1628+".mp4");
 * startActivity(intent);
 */

public class PlayerActivity extends BaseDownLoadActivity {
    private String playPath;
    private PlayerView playerview;
    SimpleExoPlayer player;

    @Override
    public int getLayoutView() {
        return R.layout.activity_player;
    }

    @Override
    public void initView() {
        setTitleName("视频");
        //下载视频到本地播放
        playPath = getIntent().getStringExtra("playPath");
        playerview = findViewById(R.id.playerview);
    }


    @Override
    public void initData() {
        playVideo();
    }


    /**
     * 播放视频
     */
    public void playVideo() {
        //1.创建默认的 TrackSelector
        Handler mainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);
        //2.创建播放器
        player =ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);

        // 将播放器附加到view
        playerview.setPlayer(player);

        /**准备播放*/
        // 在播放期间测量带宽。 如果不需要，可以为null
        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
        // 生成用于加载媒体数据的 DataSource 实例
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mContext,
                Util.getUserAgent(mContext, BaseAppUtils.getAppName()), defaultBandwidthMeter);
        // 这是要播放媒体的MediaSource
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(playPath));
        // 准备播放器的资源
        player.prepare(videoSource);
        player.setPlayWhenReady(true);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.stop();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) {
            player.release();
        }
    }

    @Override
    protected String getDownloadTitleRightName() {
        return "保存视频";
    }

    @Override
    protected String getDownLoadPath() {
        return playPath;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected boolean canCancelLoadingDialog() {
        return true;
    }

    @Override
    public void onSuccess(String tag, Object o) {

    }
}
