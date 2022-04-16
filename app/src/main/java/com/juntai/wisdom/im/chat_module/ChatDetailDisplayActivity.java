package com.juntai.wisdom.im.chat_module;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.juntai.disabled.basecomponent.mvp.BasePresenter;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.base.BaseAppActivity;
import com.juntai.wisdom.im.bean.MessageBodyBean;
import com.juntai.wisdom.im.chat_module.chat.displayFile.DisplayAudioFileFragment;
import com.juntai.wisdom.im.chat_module.chat.displayFile.DisplayImageFileFragment;
import com.juntai.wisdom.im.chat_module.chat.displayFile.DisplayTextFragment;
import com.juntai.wisdom.im.chat_module.chat.displayFile.DisplayVideoFileFragment;

/**
 * @aouther tobato
 * @description 描述  展示消息详情的界面  用于展示文本消息 图片消息 视频消息  音频消息
 * @date 2022/4/16 10:25
 */
public class ChatDetailDisplayActivity extends BaseAppActivity {

    private MessageBodyBean messageBodyBean;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_chat_detail_display;
    }

    @Override
    public void initView() {
        setTitleName("详情");
        messageBodyBean = getIntent().getParcelableExtra(BASE_PARCELABLE);
        if (messageBodyBean == null) {
            finish();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction
                = fragmentManager.beginTransaction();
        switch (messageBodyBean.getMsgType()) {
            case 0:
                transaction.replace(R.id.display_chat_fl, DisplayTextFragment.getInstance(messageBodyBean.getContent()));
                transaction.commit();
                break;
            case 1:
                transaction.replace(R.id.display_chat_fl, DisplayImageFileFragment.getInstance(messageBodyBean.getContent()));
                transaction.commit();
                break;
            case 2:
                transaction.replace(R.id.display_chat_fl, DisplayVideoFileFragment.getInstance(messageBodyBean.getContent()));
                transaction.commit();
                break;
            case 3:
                transaction.replace(R.id.display_chat_fl, DisplayAudioFileFragment.getInstance(messageBodyBean.getContent()));
                transaction.commit();
                break;
            default:
                break;
        }

    }

    @Override
    public void initData() {

    }


    @Override
    public void onSuccess(String tag, Object o) {

    }
}
