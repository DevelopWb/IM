package com.juntai.wisdom.im.mine.myCollect;

import android.view.View;
import android.widget.TextView;

import com.juntai.disabled.basecomponent.mvp.BasePresenter;
import com.juntai.disabled.federation.R;
import com.juntai.disabled.video.CustomStandardNoUiGSYVideoPlayer;
import com.juntai.disabled.video.img.PhotoView;
import com.juntai.wisdom.im.base.BaseAppActivity;
import com.juntai.wisdom.im.bean.MessageBodyBean;
import com.juntai.wisdom.im.utils.CalendarUtil;

public class CollectionDetailActivity extends BaseAppActivity implements View.OnClickListener {

    private MessageBodyBean messageBodyBean;
    /**
     * 来自
     */
    private TextView mMsgFromTv;
    private PhotoView mCollectIv;
    private CustomStandardNoUiGSYVideoPlayer videoPlayer;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }


    @Override
    public int getLayoutView() {
        return R.layout.activity_collection_detail;
    }

    public void initView() {
        setTitleName("详情");
        messageBodyBean = getIntent().getParcelableExtra(BASE_PARCELABLE);
        mMsgFromTv = (TextView) findViewById(R.id.msg_from_tv);
        String fromNameTv = null;
        String timeTv = null;
        if (2 == messageBodyBean.getChatType()) {
            //群聊
            fromNameTv = messageBodyBean.getGroupNickname();
        } else {
            fromNameTv = messageBodyBean.getFromNickname();
        }
        timeTv = CalendarUtil.formatCollectDataOfChatList(messageBodyBean.getCollectionCreateTime());
        mMsgFromTv.setText(String.format("来自 %s %s", fromNameTv, timeTv));


    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onSuccess(String tag, Object o) {

    }

}
