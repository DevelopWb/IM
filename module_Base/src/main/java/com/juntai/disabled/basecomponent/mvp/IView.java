package com.juntai.disabled.basecomponent.mvp;


import com.trello.rxlifecycle2.LifecycleTransformer;

public interface IView extends IViewExpand{
    /**
     * 显示dialog
     */
    void showLoading();

    /**
     * 隐藏 dialog
     */

    void hideLoading();

    /**
     * 显示信息
     *
     * @param message
     */
    void showMsg(String message);

    /**
     * 使用默认的样式显示信息
     *
     * @param msg
     */
    void showDefaultMsg(String msg);

    /**
     * 显示错误信息
     *
     * @param errorMsg
     */
    void showErrorMsg(String errorMsg);
    /**
     * 绑定生命周期
     */
    <T> LifecycleTransformer<T> bindToLife();
}
