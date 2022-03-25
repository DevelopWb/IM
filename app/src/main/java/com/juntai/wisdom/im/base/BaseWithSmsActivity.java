package com.juntai.wisdom.im.base;

import android.content.Intent;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.juntai.disabled.basecomponent.utils.ImageLoadUtil;
import com.juntai.disabled.basecomponent.utils.MD5;
import com.juntai.disabled.basecomponent.utils.RuleTools;
import com.juntai.disabled.basecomponent.utils.ToastUtils;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.base.selectPics.HeadCropActivity;
import com.juntai.wisdom.im.base.sendcode.SmsCheckCodeActivity;
import com.juntai.wisdom.im.entrance.LoginActivity;
import com.juntai.wisdom.im.entrance.regist.RegistActivity;

import java.util.List;

import okhttp3.FormBody;

/**
 * @aouther tobato
 * @description 描述
 * @date 2021-10-13 14:49
 */
public abstract class BaseWithSmsActivity extends SmsCheckCodeActivity implements View.OnClickListener {
    public static String REGIST_PHONE = "regist_phone";

    /**
     * 注册
     */
    public TextView mRegistTv;
    /**
     * 注册手机号
     */
    public EditText mRegistPhoneEt;
    /**
     * 短信验证码
     */
    public EditText mRegistCheckCodeEt;
    /**
     * 获取验证码
     */
    public TextView mRegistSendCheckCodeTv;
    /**
     * 密码
     */
    public EditText mPasswordEt;
    public ImageView mHideShowIv;
    public boolean isHide = true;//默认隐藏
    private ImageView mHeadIv;
    /**
     * 昵称
     */
    private EditText mRegistNameEt;
    protected String headPicPath;
    protected FormBody.Builder builder;

    @Override
    public int getLayoutView() {
        return R.layout.activity_modify_pwd;
    }

    @Override
    public void initView() {
        setTitleName(getTitleName());
        mRegistTv = (TextView) findViewById(R.id.regist_tv);
        mRegistTv.setText(getCommitTextName());
        mRegistTv.setOnClickListener(this);
        mRegistPhoneEt = (EditText) findViewById(R.id.regist_phone_et);
        mRegistCheckCodeEt = (EditText) findViewById(R.id.regist_check_code_et);
        mRegistSendCheckCodeTv = (TextView) findViewById(R.id.regist_send_check_code_tv);
        mRegistSendCheckCodeTv.setOnClickListener(this);
        mPasswordEt = (EditText) findViewById(R.id.password_et);
        mPasswordEt.setHint(getPwdHint());
        mHideShowIv = (ImageView) findViewById(R.id.hide_show_iv);
        mHideShowIv.setOnClickListener(this);
        mHeadIv = (ImageView) findViewById(R.id.head_iv);
        mHeadIv.setOnClickListener(this);
        mRegistNameEt = (EditText) findViewById(R.id.regist_name_et);
        if (this instanceof RegistActivity) {
            mHeadIv.setVisibility(View.VISIBLE);
            mRegistNameEt.setVisibility(View.VISIBLE);
        } else {
            mHeadIv.setVisibility(View.GONE);
            mRegistNameEt.setVisibility(View.GONE);
        }
    }

    protected abstract String getPwdHint();

    protected abstract String getCommitTextName();

    protected abstract String getTitleName();

    @Override
    public void initData() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.regist_tv:
                if (this instanceof RegistActivity) {
                    if (TextUtils.isEmpty(headPicPath)) {
                        ToastUtils.warning(mContext, "请选择头像");
                        return;
                    }
                }

                // 调用注册的接口  成功后跳转到登录界面
                if (!RuleTools.isMobileNO(getTextViewValue(mRegistPhoneEt))) {
                    ToastUtils.error(mContext, "手机号码格式不正确");
                    return;
                }
                if (TextUtils.isEmpty(getTextViewValue(mRegistCheckCodeEt))) {
                    ToastUtils.error(mContext, "请输入验证码");
                    return;
                }
                if (TextUtils.isEmpty(getTextViewValue(mPasswordEt))) {
                    ToastUtils.error(mContext, "请输入密码");
                    return;
                }
                builder = new FormBody.Builder();
                builder.add("phoneNumber", getTextViewValue(mRegistPhoneEt));
                builder.add("code", getTextViewValue(mRegistCheckCodeEt));
                builder.add("password", MD5.md5(String.format("%s#%s", getTextViewValue(mRegistPhoneEt), getTextViewValue(mPasswordEt))));
                builder.add("nickname", getTextViewValue(mRegistNameEt));
                commit();

                break;
            default:
                break;
            case R.id.regist_send_check_code_tv:
                // 注册界面发送验证码
                if (!RuleTools.isMobileNO(getTextViewValue(mRegistPhoneEt))) {
                    ToastUtils.error(mContext, "手机号码格式不正确");
                    return;
                }
                sendCheckCode(getTextViewValue(mRegistPhoneEt));
                break;
            case R.id.hide_show_iv:
                if (isHide) {
                    isHide = false;
                    //设置EditText的密码为可见的
                    mHideShowIv.setImageResource(R.mipmap.show_icon);
                    mPasswordEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    isHide = true;
                    //设置EditText的密码为隐藏
                    mHideShowIv.setImageResource(R.mipmap.hide_icon);
                    mPasswordEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                mPasswordEt.setSelection(mPasswordEt.getText().length());
                break;
            case R.id.head_iv:
                choseImage(0, BaseWithSmsActivity.this, 1);
                break;
        }
    }
    @Override
    protected void selectedPicsAndEmpressed(List<String> icons) {
        super.selectedPicsAndEmpressed(icons);
        if (icons.size() > 0) {
            String path = icons.get(0);
            //跳转到裁剪头像的界面
            startActivityForResult(new Intent(this, HeadCropActivity.class).putExtra(HeadCropActivity.HEAD_PIC,
                    path), BASE_REQUEST_RESULT);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == BASE_REQUEST_RESULT) {
            if (data != null) {
                headPicPath = data.getStringExtra(HeadCropActivity.CROPED_HEAD_PIC);
                ImageLoadUtil.loadCirImgNoCrash(getApplicationContext(), headPicPath,
                        mHeadIv,
                        R.mipmap.default_user_head_icon, R.mipmap.default_user_head_icon);
            }
        }
    }
    protected abstract void commit();

    @Override
    protected TextView getSendCodeTv() {
        return mRegistSendCheckCodeTv;
    }

    @Override
    public void onSuccess(String tag, Object o) {
        super.onSuccess(tag,o);
        switch (tag) {
            case AppHttpPath.MODIFY_PWD:
                ToastUtils.toast(mContext,"修改成功");
                startActivity(new Intent(this, LoginActivity.class).putExtra(REGIST_PHONE,
                        getTextViewValue(mRegistPhoneEt)));
                break;
            default:
                break;
        }

    }

}
