package com.juntai.wisdom.im.base.sendcode;


import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.juntai.disabled.basecomponent.R;
import com.juntai.disabled.basecomponent.utils.ToastUtils;
import com.juntai.wisdom.im.base.BaseAppActivity;


/**
 * @aouther tobato 短信验证码接收
 * @description 描述
 * @date 2020/3/25 8:46
 */
public abstract class SmsCheckCodeActivity extends BaseAppActivity<SendCodePresent> implements SendCodeContract.ISendCodeView {
    public final static String GET_CODE_TAG = "getCodeTag";//获取短信验证码的标识

    /**
     * 获取发送短信验证码的控件
     * @return
     */
    protected abstract TextView  getSendCodeTv();

    /**
     * 发送验证码
     *
     * @param mobile
     */
    public void sendCheckCode(String mobile) {
        mPresenter.sendCheckCode(mobile, GET_CODE_TAG);
    }


    @Override
    public void onSuccess(String tag, Object o) {
        switch (tag) {
            case GET_CODE_TAG:
                ToastUtils.success(SmsCheckCodeActivity.this, "已发送");
                mPresenter.initGetTestCodeButtonStatus();
                break;
            default:
                initGetTestCodeButtonStatusStop();
                break;
        }

    }

    /**
     * 初始化获取短信验证码的view
     */
    private void initGetTestCodeButtonStatusStop() {
        mPresenter.receivedCheckCodeAndDispose();
        getSendCodeTv().setText("获取验证码");
        getSendCodeTv().setClickable(true);
        getSendCodeTv().setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
    }


    @Override
    protected SendCodePresent createPresenter() {
        return new SendCodePresent();
    }

    @Override
    public void onError(String tag, Object o) {
        String msg = (String) o;
        if ("短信验证码错误".equals(msg)) {
            ToastUtils.error(this, "验证码输入有误");
            initGetTestCodeButtonStatusStop();

        }
        super.onError(tag, o);
    }


    @Override
    public void updateSendCheckCodeViewStatus(long second) {
        if (second > 0) {
            getSendCodeTv().setText("重新发送 " + second + "s");
            getSendCodeTv().setClickable(false);
            getSendCodeTv().setTextColor(ContextCompat.getColor(mContext, R.color.gray));
        } else {
            getSendCodeTv().setText("获取验证码");
            getSendCodeTv().setClickable(true);
            getSendCodeTv().setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));

        }
    }

    @Override
    public void checkFormatError(String error) {
        ToastUtils.warning(mContext, error);
    }
}
