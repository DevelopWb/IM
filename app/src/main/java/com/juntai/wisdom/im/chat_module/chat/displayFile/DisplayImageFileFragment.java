package com.juntai.wisdom.im.chat_module.chat.displayFile;

import android.os.Bundle;

import com.juntai.disabled.basecomponent.mvp.IPresenter;
import com.juntai.disabled.basecomponent.utils.FileCacheUtils;
import com.juntai.disabled.basecomponent.utils.ImageLoadUtil;
import com.juntai.disabled.basecomponent.utils.ToastUtils;
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
public class DisplayImageFileFragment extends BaseAppFragment {
    private PhotoView mPhotoDisplayPv;

    @Override
    protected IPresenter createPresenter() {
        return null;
    }

    public static DisplayImageFileFragment getInstance(String picPath) {
        DisplayImageFileFragment fragment = new DisplayImageFileFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BASE_STRING, picPath);
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.display_image_file_fg;
    }

    @Override
    protected void initView() {
        String picPath = getArguments().getString(BASE_STRING);
        if (!FileCacheUtils.isFileExists(picPath)) {
            ToastUtils.toast(mContext,"文件已失效");
            ((FileDetailActivity)getActivity()).finish();
            return;
        }
        mPhotoDisplayPv = (PhotoView) getView(R.id.photo_display_pv);
        mPhotoDisplayPv.enable();
        ImageLoadUtil.loadImageCache(mContext, picPath, mPhotoDisplayPv);

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onSuccess(String tag, Object o) {

    }

}
