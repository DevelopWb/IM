package com.juntai.wisdom.im.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.juntai.disabled.basecomponent.utils.ImageLoadUtil;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.youth.banner.loader.ImageLoaderInterface;

import tv.danmaku.ijk.media.exo2.Exo2PlayerManager;

public class GlideImageLoader implements ImageLoaderInterface<View> {
    StandardGSYVideoPlayer videoPlayer;
    long seek = 0;
    String  pathV;
    private OnFullScreenListener onFullScreenListener;

    public GlideImageLoader setOnFullScreenCallBack(OnFullScreenListener onFullScreenListener) {
        this.onFullScreenListener = onFullScreenListener;
        return this;
    }

    @Override
    public void displayImage(Context context, Object path, View view) {
        /**
         注意：
         1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
         2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
         传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
         切记不要胡乱强转！
         */
        //eg：
        if (((String)path).contains(".mp4") || ((String) path).contains("getVideo")){
            pathV = (String) path;
            startVideo(context);
        }else {
            ImageView imageView = (ImageView) view;
            //Glide 加载图片简单用法
            ImageLoadUtil.loadImageCache(context, (String) path,imageView);
        }
    }


    @Override
    public View createImageView(Context context, Object path) {
        //使用fresco，需要创建它提供的ImageView，当然你也可以用自己自定义的具有图片加载功能的ImageView
        if (((String) path).contains(".mp4") || ((String) path).contains("getVideo")){
            videoPlayer = new StandardGSYVideoPlayer(context);
            return videoPlayer;
        }else {
            ImageView simpleDraweeView = new ImageView(context);
            return simpleDraweeView;
        }
    }

    private ImageView startVideo(Context mContext) {
        //RTMP播放需切换至exo播放
        PlayerFactory.setPlayManager(Exo2PlayerManager.class);
        videoPlayer.setUp(pathV, false, "");
        //增加封面
        ImageView imageView = new ImageView(mContext);
        Glide.with(mContext).asBitmap().load(pathV).into(imageView);
        videoPlayer.setThumbImageView(imageView);
        //增加title
        videoPlayer.getTitleTextView().setVisibility(View.GONE);
        //设置返回键
        videoPlayer.getBackButton().setVisibility(View.GONE);
        videoPlayer.seekTo(seek);
        //全屏键
        videoPlayer.getFullscreenButton().setVisibility(View.VISIBLE);
        videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onFullScreenListener != null) {
                    onFullScreenListener.onFullScreen();
                }
            }
        });
        //是否可以滑动调整
        videoPlayer.setIsTouchWiget(false);
        //设置返回按键功能
        videoPlayer.getBackButton().setVisibility(View.GONE);
        videoPlayer.getLayoutId();
        return imageView;
    }

    public void pause(){
        if (videoPlayer != null){
            GSYVideoManager.onPause();
        }
    }
    //释放所有
    public void release(){
        GSYVideoManager.releaseAllVideos();
    }

    /**
     * 全屏得点击事件
     */
    public interface OnFullScreenListener{
        void onFullScreen();
    }
}
