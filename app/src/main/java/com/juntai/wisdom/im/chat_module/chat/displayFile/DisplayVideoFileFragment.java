package com.juntai.wisdom.im.chat_module.chat.displayFile;

import android.os.Bundle;
import android.view.View;

import com.juntai.disabled.basecomponent.mvp.IPresenter;
import com.juntai.disabled.basecomponent.utils.FileCacheUtils;
import com.juntai.disabled.basecomponent.utils.ToastUtils;
import com.juntai.disabled.federation.R;
import com.juntai.disabled.video.CustomStandardNoUiGSYVideoPlayer;
import com.juntai.wisdom.im.base.BaseAppFragment;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;

import tv.danmaku.ijk.media.exo2.Exo2PlayerManager;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2022-03-05 15:16
 * @UpdateUser: 更新者
 * @UpdateDate: 2022-03-05 15:16
 */
public class DisplayVideoFileFragment extends BaseAppFragment {
    private CustomStandardNoUiGSYVideoPlayer mVideoplayCp;

    @Override
    protected IPresenter createPresenter() {
        return null;
    }

    public static DisplayVideoFileFragment getInstance(String picPath) {
        DisplayVideoFileFragment fragment = new DisplayVideoFileFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BASE_STRING, picPath);
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.display_video_file_fg;
    }

    @Override
    protected void initView() {
       String filePath = getArguments().getString(BASE_STRING);
        if (!FileCacheUtils.isFileExists(filePath)) {
                    ToastUtils.toast(mContext,"文件已失效");
            ((FileDetailActivity)getActivity()).finish();
            return;
        }
        mVideoplayCp = (CustomStandardNoUiGSYVideoPlayer)getView(R.id.videoplay_cp);
        PlayerFactory.setPlayManager(Exo2PlayerManager.class);
        mVideoplayCp.setUp(filePath, true, "");
//                //增加封面
//                ImageView imageView = new ImageView(getActivity());
//                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                imageView.setImageResource(R.drawable.empty_drawable);
//                mVideoplayCp.setThumbImageView(imageView);
        //增加title
        mVideoplayCp.getTitleTextView().setVisibility(View.GONE);
        //设置返回键
        mVideoplayCp.getBackButton().setVisibility(View.GONE);
        //设置旋转
        //是否可以滑动调整
        mVideoplayCp.setIsTouchWiget(true);
        //设置返回按键功能
        mVideoplayCp.startPlayLogic();
        mVideoplayCp.setVideoAllCallBack(new GSYSampleCallBack() {
            @Override
            public void onPrepared(String url, Object... objects) {
                super.onPrepared(url, objects);
            }

            @Override
            public void onAutoComplete(String url, Object... objects) {
            }

        });
    }

    @Override
    protected void initData() {

    }
    @Override
    public void onPause() {
        if (mVideoplayCp != null) {
            mVideoplayCp.onVideoPause();

        }
        super.onPause();
    }

    @Override
    public void onResume() {
        if (mVideoplayCp != null) {
            mVideoplayCp.onVideoResume();

        }
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mVideoplayCp != null) {
            mVideoplayCp.getCurrentPlayer().release();
            mVideoplayCp.release();
        }
    }
    @Override
    public void onSuccess(String tag, Object o) {

    }

}
