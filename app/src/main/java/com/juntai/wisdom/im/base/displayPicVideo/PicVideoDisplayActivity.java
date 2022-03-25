package com.juntai.wisdom.im.base.displayPicVideo;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.juntai.disabled.PicVideoViewPagerAdapter;
import com.juntai.disabled.basecomponent.mvp.BasePresenter;
import com.juntai.disabled.basecomponent.utils.ToastUtils;
import com.juntai.disabled.video.R;
import com.juntai.wisdom.im.base.BaseAppActivity;
import com.juntai.wisdom.im.bean.MessageBodyBean;
import com.juntai.wisdom.im.entrance.main.MainContract;
import com.juntai.wisdom.im.utils.UrlFormatUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @aouther tobato
 * @description 描述  图片和视频全屏展示
 * @date 2022-02-25 16:04
 */
public class PicVideoDisplayActivity extends BaseAppActivity implements MainContract.IBaseView {
    PicVideoViewPagerAdapter myViewPagerAdapter;
    ViewPager viewPager;
    List<Fragment> fragmentList = new ArrayList<>();
    private List<MessageBodyBean> messageBodyBeanList;
    private String diaplayPath;

    public static String IMAGEITEM = "imageitem";

    @Override
    public int getLayoutView() {
        return R.layout.activity_imagezoom;
    }

    @Override
    public void initView() {
        initToolbarAndStatusBar(false);
        viewPager = findViewById(R.id.imagezoom_viewpager);

    }


    @Override
    public void initData() {
        messageBodyBeanList = getIntent().getParcelableArrayListExtra(BASE_PARCELABLE);
        if (messageBodyBeanList == null) {
            ToastUtils.toast(mContext, "请传入需要展示图片的路径");
            finish();
            return;
        }
        int item = getIntent().getIntExtra(IMAGEITEM, 0);
        if (item >= messageBodyBeanList.size()) {
            ToastUtils.toast(mContext, "图片的索引越界");
            finish();
            return;
        }

        diaplayPath = UrlFormatUtil.getImageOriginalUrl(messageBodyBeanList.get(item).getContent());
        for (MessageBodyBean messageBodyBean : messageBodyBeanList) {
            switch (messageBodyBean.getMsgType()) {
                case 1:
                    fragmentList.add(DisplayPhotoFragment.getInstance(UrlFormatUtil.getImageOriginalUrl(messageBodyBean.getContent()), messageBodyBean));
                    break;
                case 2:
                    fragmentList.add(DisplayVideoFragment.getInstance(UrlFormatUtil.getImageOriginalUrl(messageBodyBean.getContent()), messageBodyBean));
                    break;
                default:
                    break;
            }
        }
        myViewPagerAdapter = new PicVideoViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.setCurrentItem(item);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                diaplayPath = UrlFormatUtil.getImageOriginalUrl(messageBodyBeanList.get(i).getContent());

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {
//                EventManager.getEventBus().post(DisplayVideoFragment.STOP_VIDEO_PLAY);

            }
        });
    }


    @Override
    protected String getDownloadTitleRightName() {
        return "保存图片";
    }

    @Override
    protected String getDownLoadPath() {
        return diaplayPath;
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
