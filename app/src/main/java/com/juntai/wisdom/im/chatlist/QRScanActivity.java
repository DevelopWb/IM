package com.juntai.wisdom.im.chatlist;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.juntai.disabled.basecomponent.base.BaseMvpActivity;
import com.juntai.disabled.basecomponent.mvp.BasePresenter;
import com.juntai.wisdom.im.utils.GlideEngine4;
import com.juntai.disabled.basecomponent.utils.JsonUtil;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.entrance.main.MainActivity;
import com.juntai.wisdom.im.utils.StringTools;
import com.king.zxing.CaptureHelper;
import com.king.zxing.OnCaptureCallback;
import com.king.zxing.ViewfinderView;
import com.king.zxing.util.CodeUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import es.dmoral.toasty.Toasty;
import io.reactivex.functions.Consumer;

/**
 * 扫码
 *
 * @aouther tobato
 * @date 2020-3-18
 * @update 2020-06-08 tobato
 */
public class QRScanActivity extends BaseMvpActivity implements View.OnClickListener, OnCaptureCallback {
    private SurfaceView mSurfaceView;
    private ViewfinderView mViewfinderView;
    private ImageView mZxingPic;
    private ImageView mIvTorch;
    private ImageView mZxingBackBtn;
    private CaptureHelper mCaptureHelper;
public static int SCAN_RESULT = 10094;
    private int SELECT_PIC_RESULT = 1001;

    //    private int pageType;//0扫码，1扫描巡检内容

    @Override
    public int getLayoutView() {
        return R.layout.activity_qrscan;
    }

    @Override
    public void initView() {
        getToolbar().setVisibility(View.GONE);
        mBaseRootCol.setFitsSystemWindows(false);
        mImmersionBar.reset().statusBarDarkFont(false).init();
        //        pageType = getIntent().getIntExtra("pageType",0);
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        mViewfinderView = (ViewfinderView) findViewById(R.id.viewfinderView);
        mZxingPic = (ImageView) findViewById(R.id.zxing_pic);
        mZxingPic.setOnClickListener(this);
        mIvTorch = (ImageView) findViewById(R.id.ivTorch);
        mZxingBackBtn = (ImageView) findViewById(R.id.zxing_back_btn);
        mZxingBackBtn.setOnClickListener(this);
        mIvTorch.setOnClickListener(this);

        mCaptureHelper = new CaptureHelper(this, mSurfaceView, mViewfinderView, null);
        mCaptureHelper.setOnCaptureCallback(this);
        mCaptureHelper.characterSet("ISO-8859-1");
        mCaptureHelper.onCreate();
        mCaptureHelper.playBeep(true);
    }

    @Override
    public void initData() {

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zxing_pic:
                choseImageFromFragment(0, this, 1, SELECT_PIC_RESULT);
                break;
            case R.id.zxing_back_btn:
                finish();
                break;
            case R.id.ivTorch:
                mIvTorch.setSelected(!mIvTorch.isSelected());
                mCaptureHelper.getCameraManager().setTorch(mIvTorch.isSelected());
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PIC_RESULT && resultCode == RESULT_OK) {
            String content = CodeUtils.parseCode(Matisse.obtainPathResult(data).get(0));
            showResult(content);
            finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mCaptureHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mCaptureHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCaptureHelper.onDestroy();
        mCaptureHelper = null;
    }

    @Override
    protected boolean canCancelLoadingDialog() {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mCaptureHelper.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onResultCallback(String result) {
        String resultData = JsonUtil.transcoding(result);
        if (JsonUtil.isNumber(resultData)){
            mCaptureHelper.onPause();
            mCaptureHelper.onResume();
        }else {
            showResult(resultData);
            finish();
        }
        return true;
    }

    /**
     * 结果处理
     * 最好是用ActivityResult将结果返回上个界面
     *
     * @param result
     */
    private void showResult(String result) {
        if (!StringTools.isStringValueOk(result)) {
            return;
        }
        setResult(SCAN_RESULT, new Intent(this, MainActivity.class).putExtra(BASE_STRING, result));
    }

    /**
     * 图片选择
     *
     * @param type          选择类型 0 图片 1 短视频
     * @param activity
     * @param maxSelectable 最大图片选择数
     * @param requestCode   请求得code
     */
    public void choseImageFromFragment(int type, Activity activity, int maxSelectable, int requestCode) {
        new RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .compose(this.bindToLife())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            if (type == 0) {
                                Matisse.from(activity)
                                        .choose(MimeType.ofImage())
                                        .showSingleMediaType(true)//是否只显示选择的类型的缩略图，就不会把所有图片视频都放在一起，而是需要什么展示什么
                                        .countable(true)
                                        .maxSelectable(maxSelectable)
                                        //                                        .capture(true)//是否显示照相，两行连用
                                        //                                        .captureStrategy(new
                                        //                                        CaptureStrategy(true, BaseAppUtils
                                        //                                        .getFileprovider()))
                                        //参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与
                                        // AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                                        .thumbnailScale(0.85f)
                                        .imageEngine(new GlideEngine4())
                                        .forResult(requestCode);
                                //包括裁剪和压缩后的缓存，要在上传成功后调用，注意：需要系统sd卡权限
                            } else {
                                //选择小视频
                                //                                mPresenter.recordVideo(getActivity());
                            }
                        } else {
                            Toasty.info(mContext, "请给与相应权限").show();
                        }
                    }
                });
    }

    @Override
    public void onSuccess(String tag, Object o) {

    }

    @Override
    public void onError(String tag, Object o) {

    }
}
