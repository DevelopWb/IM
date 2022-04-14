package com.juntai.disabled.basecomponent.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.juntai.disabled.basecomponent.mvp.BasePresenter;
import com.juntai.disabled.basecomponent.mvp.IView;
import com.juntai.disabled.basecomponent.utils.LogUtil;
import com.juntai.disabled.basecomponent.utils.ToastUtils;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.ActivityEvent;

public abstract class BaseMvpActivity<P extends BasePresenter> extends BaseActivity implements IView {


    protected P mPresenter;

    protected abstract P createPresenter();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        super.onCreate(savedInstanceState);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter =null;
        }
    }

    @Override
    public void showLoading() {
        showLoadingDialog(this,canCancelLoadingDialog());
    }

    protected abstract boolean canCancelLoadingDialog();

    @Override
    public void hideLoading() {
        stopLoadingDialog();
    }

    @Override
    public void showMsg(String message) {

    }

    @Override
    public void showDefaultMsg(String msg) {

    }

    @Override
    public void showErrorMsg(String errorMsg) {

    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return bindUntilEvent(ActivityEvent.DESTROY);
    }

    @Override
    public void onError(String tag, Object o) {
        LogUtil.d("tag-->" +tag);
        ToastUtils.toast(this,(String)o);

    }
}
