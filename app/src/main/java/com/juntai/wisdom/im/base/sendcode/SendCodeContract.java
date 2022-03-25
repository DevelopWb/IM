package com.juntai.wisdom.im.base.sendcode;


import com.juntai.disabled.basecomponent.mvp.IView;

/**
 * Author:wang_sir
 * Time:2018/7/16 18:20
 * Description:This is ISendCode
 */
public interface SendCodeContract {


    String REGIST = "regist";

    interface ISendCodeModel {
        /**
         * 同步获取验证码按钮的状态
         */
        void initGetTestCodeButtonStatus();

        /**
         * 接收到验证码了 将observerble dispose
         */
        void receivedCheckCodeAndDispose();

    }
    interface ISendCodeView extends IView {
        /**
         * 接收到验证码后更改view得状态
         *
         * @param second
         */
        void updateSendCheckCodeViewStatus(long second);

        /**
         * 校验手机号错误
         *
         * @param error
         */
        void checkFormatError(String error);


    }

    interface  ISendCodePresent{
        /**
         * 发送验证码
         *
         * @param mobile
         * @param tempCode 短信模板
         */
        void sendCheckCode(String mobile, String tempCode);

        /**
         * 接收到验证码了 将observerble dispose
         */
        void receivedCheckCodeAndDispose();

    }

    interface IUpdateView {
        //开始计时
        void  startTiming(long value);
        //结束计时
        void endTiming(long value);
        /**
         * 校验手机号错误
         * @param error
         */
        void checkFormatError(String error);
    }
}
