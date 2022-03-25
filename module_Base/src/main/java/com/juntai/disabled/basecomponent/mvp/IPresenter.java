package com.juntai.disabled.basecomponent.mvp;

/**
 * @aouther Ma
 * @date 2019/3/6
 */
public interface IPresenter<V extends IView> {

    /**
     * 绑定 View
     *
     * @param mView
     */
    void attachView(V mView);

    /**
     * 解绑 View
     */
    void detachView();

}
