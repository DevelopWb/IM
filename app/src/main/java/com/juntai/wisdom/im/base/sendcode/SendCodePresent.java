package com.juntai.wisdom.im.base.sendcode;


import com.juntai.disabled.basecomponent.base.BaseObserver;
import com.juntai.disabled.basecomponent.base.BaseResult;
import com.juntai.disabled.basecomponent.utils.RxScheduler;
import com.juntai.wisdom.im.AppNetModule;
import com.juntai.wisdom.im.base.BaseAppPresent;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2021/7/14 10:30
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/7/14 10:30
 */
public class SendCodePresent extends BaseAppPresent<SendCodeModel, SendCodeContract.ISendCodeView> implements SendCodeContract.IUpdateView, SendCodeContract.ISendCodePresent {
    @Override
    protected SendCodeModel createModel() {
        return new SendCodeModel(this);
    }


    /**
     * 从网络获取验证码
     *
     * @param mobile
     */
    private void getCheckCodeFromNet(String mobile, String tag) {
        //获取验证码
        AppNetModule.createrRetrofit()
                .getSMSCode(mobile)
                .compose(RxScheduler.ObsIoMain(getView()))
                .subscribe(new BaseObserver<BaseResult>(getView()) {
                    @Override
                    public void onSuccess(BaseResult o) {
                        if (getView() != null) {
                            getView().onSuccess(tag, o);
                        }

                    }

                    @Override
                    public void onError(String msg) {
                        if (getView() != null) {
                            getView().onError(tag, msg);
                        }
                    }
                });
    }

    @Override
    public void startTiming(long value) {
        if (getView() != null) {
            getView().updateSendCheckCodeViewStatus(value);
        }
    }

    @Override
    public void endTiming(long value) {
        if (getView() != null) {
            getView().updateSendCheckCodeViewStatus(value);
        }
    }

    @Override
    public void checkFormatError(String error) {
        if (getView() != null) {
            getView().checkFormatError(error);
        }
    }

    /**
     * 检查手机号的格式
     */
    public boolean mobileFormatIsOk(String mobile) {
        return getModel().mobileFormatIsOk(mobile);
    }

    @Override
    public void sendCheckCode(String mobile, String tempCode) {
        if (mobileFormatIsOk(mobile)) {
            getCheckCodeFromNet(mobile, tempCode);
        }
    }

    @Override
    public void receivedCheckCodeAndDispose() {
        getModel().receivedCheckCodeAndDispose();
    }

    /**
     * 初始化获取验证码
     */
    public void initGetTestCodeButtonStatus() {
        getModel().initGetTestCodeButtonStatus();
    }

}
