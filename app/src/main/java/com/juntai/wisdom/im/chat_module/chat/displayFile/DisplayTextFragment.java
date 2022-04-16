package com.juntai.wisdom.im.chat_module.chat.displayFile;

import android.os.Bundle;
import android.widget.TextView;

import com.juntai.disabled.basecomponent.mvp.IPresenter;
import com.juntai.disabled.basecomponent.utils.ImageLoadUtil;
import com.juntai.disabled.federation.R;
import com.juntai.disabled.video.img.PhotoView;
import com.juntai.wisdom.im.base.BaseAppFragment;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2022-03-05 15:16
 * @UpdateUser: 更新者
 * @UpdateDate: 2022-03-05 15:16
 */
public class DisplayTextFragment extends BaseAppFragment {
    private TextView mContentTv;

    @Override
    protected IPresenter createPresenter() {
        return null;
    }

    public static DisplayTextFragment getInstance(String content) {
        DisplayTextFragment fragment = new DisplayTextFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BASE_STRING, content);
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.display_text_fg;
    }

    @Override
    protected void initView() {
        String content = getArguments().getString(BASE_STRING);
        mContentTv = (TextView) getView(R.id.display_content_tv);
        mContentTv.setText(content);

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onSuccess(String tag, Object o) {

    }

}
