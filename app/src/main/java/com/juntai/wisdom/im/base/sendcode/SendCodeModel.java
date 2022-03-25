package com.juntai.wisdom.im.base.sendcode;

import android.text.TextUtils;


import com.juntai.disabled.basecomponent.mvp.IModel;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Author:wang_sir
 * Time:2018/7/16 18:18
 * Description:This is SendCodeModel
 */
public class SendCodeModel  implements SendCodeContract.ISendCodeModel, IModel {


    private SendCodeContract.IUpdateView iUpdateView;
    private Disposable disposable;
    public static String CODE_WRONG = "短信验证码错误";

    public SendCodeModel(SendCodeContract.IUpdateView iUpdateView) {
        this.iUpdateView = iUpdateView;
    }

    @Override
    public void initGetTestCodeButtonStatus() {
        disposable = Observable.interval(0, 1000, TimeUnit.MILLISECONDS)
                .take(60)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if (iUpdateView != null) {
                            iUpdateView.startTiming(59 - aLong);
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                    }
                });
    }

    @Override
    public void receivedCheckCodeAndDispose() {
        if (disposable != null) {
            disposable.dispose();
        }
    }

    /**
     * 检查手机号的格式
     */
    public boolean mobileFormatIsOk(String mobile) {
        if (mobile == null || TextUtils.isEmpty(mobile)) {
            if (iUpdateView != null) {
                iUpdateView.checkFormatError("手机号码不能为空");
            }

            return false;
        }
        if (!isMobileNO(mobile)) {
            if (iUpdateView != null) {
                iUpdateView.checkFormatError("手机号码格式不正确");
            }
            return false;
        }
        return true;

    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */
        String telRegex = "[1][23456789]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        return !TextUtils.isEmpty(mobiles) && mobiles.matches(telRegex);
    }

    @Override
    public void onDetach() {

    }
}
