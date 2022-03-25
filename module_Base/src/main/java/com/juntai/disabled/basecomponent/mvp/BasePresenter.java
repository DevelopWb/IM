package com.juntai.disabled.basecomponent.mvp;

import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;

import com.juntai.disabled.basecomponent.utils.Preconditions;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BasePresenter<M extends IModel, V extends IView> implements IPresenter<V>, LifecycleObserver {

    protected final String TAG = this.getClass().getSimpleName();
    protected CompositeDisposable mCompositeDisposable;

    protected Reference<V> mReferenceV;
    protected Reference<M> mReferenceM;

    private M mModel;
//    private V mView;

    /**
     * 获取 Model
     *
     * @return
     */
    protected abstract M createModel();

    public BasePresenter() {
        mReferenceM=new SoftReference<>(createModel());
        mModel = mReferenceM.get();
    }

    @Override
    public void attachView(V mView) {
        //将 LifecycleObserver 注册给 LifecycleOwner 后 @OnLifecycleEvent 才可以正常使用
        if (mView != null && mView instanceof LifecycleOwner) {
            mReferenceV = new SoftReference<>(mView);
            ((LifecycleOwner) mView).getLifecycle().addObserver(this);
            if (mModel != null && mModel instanceof LifecycleObserver) {
                ((LifecycleOwner) mView).getLifecycle().addObserver((LifecycleObserver) mModel);
            }
        }
//        if (useEventBus()) {
//            EventBus.getDefault().register(this);
//        }
    }

    public V getView() {
        if (mReferenceV==null) {
            return null;
        }
        Preconditions.checkNotNull(mReferenceV.get(), "%s cannot be null", IView.class.getName());
        return mReferenceV.get();
    }

    public M getModel() {
        Preconditions.checkNotNull(mReferenceM.get(), "%s cannot be null", IModel.class.getName());
        return mReferenceM.get();
    }

    /**
     * 判断view和presenter是否绑定
     * @return
     */
    public boolean isOnAttachedView(){
        return mReferenceV!=null&&mReferenceV.get()!=null;
    }
    /**
     * 判断modle和presenter是否绑定
     * @return
     */
    public boolean isOnAttachedModel(){
        return mReferenceM!=null&&mReferenceM.get()!=null;
    }

    /**
     * 解除绑定
     */
    @Override
    public void detachView() {
//        if (useEventBus()) {
//            EventBus.getDefault().unregister(this);
//        }
        unDispose();// 解除订阅
        if (mReferenceV != null) {
            mReferenceV.clear();
        }
        if (mReferenceM != null) {
            mReferenceM.clear();
        }
        this.mCompositeDisposable = null;
    }

    public void addDispose(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);//将所有的 Disposable 放入集中处理
    }

    /**
     * 停止正在进行的任务
     */
    public void unDispose() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();//保证Activity结束时取消
        }
    }




}
