package com.juntai.wisdom.im.mine;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.disabled.basecomponent.utils.ToastUtils;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.base.BaseAppActivity;
import com.juntai.wisdom.im.base.selectPics.SelectPhotosFragment;
import com.juntai.wisdom.im.entrance.main.MainContract;
import com.juntai.wisdom.im.utils.UserInfoManager;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @aouther tobato
 * @description 描述   意见反馈
 * @date 2022-02-10 15:36
 */
public class SuggestionActivity extends BaseAppActivity<MyCenterPresent> implements MyCenterContract.ICenterView, SelectPhotosFragment.OnPhotoItemClick, View.OnClickListener {

    private List<String> picPaths;
    /**
     * 请填写10个字以上的问题描述以便我们提供更好的帮助
     */
    private EditText mContentValueEt;
    /**
     * 选填，便于我们与你联系
     */
    private EditText mMobileEt;
    /**
     * 提交
     */
    private TextView mCommitTv;

    @Override
    protected MyCenterPresent createPresenter() {
        return new MyCenterPresent();
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_suggestion;
    }

    @Override
    public void initView() {
        setTitleName("意见反馈");
        SelectPhotosFragment selectPhotosFragment = (SelectPhotosFragment) getSupportFragmentManager().findFragmentById(R.id.photo_fg);
        selectPhotosFragment.setMaxCount(4).setSpanCount(4).setPhotoTitle("图片（选填，提供问题截图）").setPhotoDelateable(true)
                .setOnPicLoadSuccessCallBack(new SelectPhotosFragment.OnPicLoadSuccessCallBack() {
                    @Override
                    public void loadSuccess(List<String> icons) {
                        if (icons == null || icons.isEmpty()) {
                            return;
                        }
                        //将图片上传
                        MultipartBody.Builder builder = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM);
                        for (String filePath : icons) {
                            String fileName = null;
                            if (filePath.contains("/")) {
                                fileName = filePath.substring(filePath.lastIndexOf("/"), filePath.length());
                            }
                            try {
                                builder.addFormDataPart("file",  URLEncoder.encode(filePath,"utf-8"), RequestBody.create(MediaType.parse("file"), new File(filePath)));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                        mPresenter.uploadFile(icons, MainContract.UPLOAD_IMAGES);
                    }
                });
        mContentValueEt = (EditText) findViewById(R.id.content_value_et);
        mMobileEt = (EditText) findViewById(R.id.mobile_et);
        mCommitTv = (TextView) findViewById(R.id.commit_tv);
        mCommitTv.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }


    @Override
    public void onSuccess(String tag, Object o) {
        switch (tag) {
            case MainContract.UPLOAD_IMAGES:
                //发送图片
                picPaths = (List<String>) o;
                break;
            case AppHttpPath.COMMIT_SUGGESTION:
                //提交建议
                ToastUtils.toast(mContext,"提交成功");
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    public void onVedioPicClick(BaseQuickAdapter adapter, int position) {

    }

    @Override
    public void onPicClick(BaseQuickAdapter adapter, int position) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.commit_tv:
                FormBody.Builder builder = new FormBody.Builder()
                        .add("userId", String.valueOf(UserInfoManager.getUserId()));
                if (TextUtils.isEmpty(getTextViewValue(mContentValueEt))||getTextViewValue(mContentValueEt).length()<11) {
                    ToastUtils.toast(mContext, "请输入问题及建议,不能低于10个字");
                    return;
                }
                builder.add("content", getTextViewValue(mContentValueEt));
                if (picPaths != null && !picPaths.isEmpty()) {
                    for (int i = 0; i < picPaths.size(); i++) {
                        String path = picPaths.get(i);
                        switch (i) {
                            case 0:
                                builder.add("photoOne", path);
                                break;
                            case 1:
                                builder.add("photoTwo", path);
                                break;
                            case 2:
                                builder.add("photoThree", path);
                                break;
                            case 3:
                                builder.add("photoFour", path);
                                break;
                            default:
                                break;
                        }

                    }
                }


                if (!TextUtils.isEmpty(getTextViewValue(mMobileEt))) {
                    builder.add("phoneNumber", getTextViewValue(mMobileEt));
                }

                mPresenter.commitSuggestion(builder.build(), AppHttpPath.COMMIT_SUGGESTION);

                break;
        }
    }
}
