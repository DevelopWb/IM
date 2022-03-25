package com.juntai.wisdom.im.chatlist.chat.displayFile;

import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.juntai.disabled.basecomponent.mvp.IPresenter;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.base.BaseAppFragment;
import com.zyl.chathelp.audio.AudioPlayManager;
import com.zyl.chathelp.audio.IAudioPlayListener;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2022-03-05 15:16
 * @UpdateUser: 更新者
 * @UpdateDate: 2022-03-05 15:16
 */
public class DisplayAudioFileFragment extends BaseAppFragment implements View.OnClickListener {
    private ImageView mIvAudio;
    private String filePath;
    private RelativeLayout mAudioBgRl;

    @Override
    protected IPresenter createPresenter() {
        return null;
    }

    public static DisplayAudioFileFragment getInstance(String picPath) {
        DisplayAudioFileFragment fragment = new DisplayAudioFileFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BASE_STRING, picPath);
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.display_audio_file_fg;
    }

    @Override
    protected void initView() {
        filePath = getArguments().getString(BASE_STRING);
        mIvAudio = (ImageView) getView(R.id.ivAudio);
        mAudioBgRl = (RelativeLayout) getView(R.id.audio_bg_rl);
        mAudioBgRl.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onSuccess(String tag, Object o) {

    }

    @Override
    public void onDestroy() {
        AudioPlayManager.getInstance().stopPlay();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.audio_bg_rl:
                AudioPlayManager.getInstance().stopPlay();
                AudioPlayManager.getInstance().startPlay(mContext, Uri.parse(filePath), new IAudioPlayListener() {
                    @Override
                    public void onStart(Uri var1) {
                        if (mIvAudio != null && mIvAudio.getBackground() instanceof AnimationDrawable) {
                            AnimationDrawable animation = (AnimationDrawable) mIvAudio.getBackground();
                            animation.start();
                        }
                    }

                    @Override
                    public void onStop(Uri var1) {
                        if (mIvAudio != null && mIvAudio.getBackground() instanceof AnimationDrawable) {
                            AnimationDrawable animation = (AnimationDrawable) mIvAudio.getBackground();
                            animation.stop();
                            animation.selectDrawable(0);
                        }

                    }

                    @Override
                    public void onComplete(Uri var1) {
                        if (mIvAudio != null && mIvAudio.getBackground() instanceof AnimationDrawable) {
                            AnimationDrawable animation = (AnimationDrawable) mIvAudio.getBackground();
                            animation.stop();
                            animation.selectDrawable(0);
                        }
                    }
                });
                break;
        }
    }
}
