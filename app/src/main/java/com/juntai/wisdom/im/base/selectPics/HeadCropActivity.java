package com.juntai.wisdom.im.base.selectPics;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.juntai.disabled.basecomponent.mvp.BasePresenter;
import com.juntai.disabled.basecomponent.utils.FileCacheUtils;
import com.juntai.disabled.basecomponent.utils.RxScheduler;
import com.juntai.disabled.basecomponent.utils.RxTask;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.base.BaseAppActivity;
import com.juntai.wisdom.im.utils.MyFileProvider;
import com.juntai.wisdom.im.utils.UserInfoManager;
import com.oginotihiro.cropview.CropView;

import java.io.File;


/**
 * @aouther tobato
 * @description 描述  头像裁剪
 * @date 2021/3/21 10:31
 */
public class HeadCropActivity extends BaseAppActivity implements View.OnClickListener {

    private CropView mHeadCropCv;
    public static String HEAD_PIC = "headpic";
    public static String CROPED_HEAD_PIC = "cropedheadpic";
    private Bitmap croppedBitmap;
    /**
     * 头像名称
     */
    private static String HEAD_PIC_NAME = String.format("headpic_%s.png", System.currentTimeMillis());


    /**
     * 确定
     */
    private TextView mCropTv;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_head_crop;
    }

    @Override
    public void initView() {
        setTitleName("裁切头像");
        mHeadCropCv = (CropView) findViewById(R.id.head_crop_cv);
        mCropTv = (TextView) findViewById(R.id.crop_tv);
        mCropTv.setOnClickListener(this);
    }

    @Override
    public void initData() {

        if (getIntent() != null) {
            String picPath = getIntent().getStringExtra(HEAD_PIC);
            if (!TextUtils.isEmpty(picPath)) {
                mHeadCropCv.of(MyFileProvider.getUriFromFile(this, new File(picPath)))
                        .withAspect(40, 40)
                        .initialize(this);
            }
        }

    }

    @Override
    public void onSuccess(String tag, Object o) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.crop_tv:

                RxScheduler.doTask(this, new RxTask<String>() {
                    @Override
                    public String doOnIoThread() {
                        croppedBitmap = mHeadCropCv.getOutput();
                        return FileCacheUtils.saveBitmap(croppedBitmap, HEAD_PIC_NAME);
                    }

                    @Override
                    public void doOnUIThread(String str) {
                        Intent intent = new Intent().putExtra(CROPED_HEAD_PIC, str);
                        setResult(BASE_REQUEST_RESULT, intent);
                        finish();
                    }
                });


                break;
        }
    }
}
