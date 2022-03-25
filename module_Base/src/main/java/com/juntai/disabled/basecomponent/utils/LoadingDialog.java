package com.juntai.disabled.basecomponent.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;


import com.juntai.disabled.basecomponent.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * created by tobato
 * created date 2019/8/16 14:37.
 * application   仿ios加载框
 */

public class LoadingDialog {
    private Disposable disposable;
    private static Dialog mDialog;

    public static LoadingDialog getInstance() {
        return LoadingDialogHolder.Instance;
    }

    private static class LoadingDialogHolder {
        private static LoadingDialog Instance = new LoadingDialog();
    }

    private void initDialog(Context mContext) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View mProgressDialogView = inflater.inflate(R.layout.ios_loading_dialog_layout, null);// 得到加载view
        mDialog = new Dialog(mContext, R.style.MNCustomDialog);// 创建自定义样式dialog
        mDialog.setCancelable(false);// 不可以用“返回键”取消
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setContentView(mProgressDialogView);// 设置布局

        //设置整个Dialog的宽高
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = ((Activity) mContext).getWindowManager();
        windowManager.getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        int screenH = dm.heightPixels;
        WindowManager.LayoutParams layoutParams = mDialog.getWindow().getAttributes();
        layoutParams.width = screenW;
        layoutParams.height = screenH;
        mDialog.getWindow().setAttributes(layoutParams);

    }


    @SuppressLint("CheckResult")
    public void showProgress(Context context, boolean canCancel) {
        //在dialog show之前判断一下
        dismissProgress();
        initDialog(context);
        if (canCancel) {
            disposable = Observable.interval(0, 1000, TimeUnit.MILLISECONDS)
                    .take(6)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {

                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {

                        }
                    }, new Action() {
                        @Override
                        public void run() throws Exception {
                            if (mDialog != null) {
                                mDialog.setCancelable(true);// 不可以用“返回键”取消
                            }
                        }
                    });
        }
        if (mDialog != null) {
            mDialog.show();
        }

    }


    public void dismissProgress() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
            if (null != disposable) {
                disposable.dispose();
                disposable = null;
            }
        }
    }

    public boolean isShowing() {
        if (mDialog != null) {
            return mDialog.isShowing();
        }
        return false;
    }
}
