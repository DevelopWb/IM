package com.juntai.disabled.basecomponent.utils;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;

import com.juntai.disabled.basecomponent.app.BaseApplication;

public class SnackbarUtil {
    private static final int color_danger = 0xffa94442;
    private static final int color_success = 0xff3c763d;
    private static final int color_info = 0xff31708f;
    private static final int color_warning = 0xff8a6d3b;

    private static final int action_color = 0xffCDC5BF;

    private Snackbar mSnackbar;

    private SnackbarUtil(Snackbar snackbar) {
        mSnackbar = snackbar;
    }

    public static SnackbarUtil makeShort(View view, String text) {
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
        return new SnackbarUtil(snackbar);
    }

    public static SnackbarUtil makeLong(View view, String text) {
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG);
        return new SnackbarUtil(snackbar);
    }

    private View getSnackBarLayout(Snackbar snackbar) {
        if (snackbar != null) {
            return snackbar.getView();
        }
        return null;

    }


    private Snackbar setSnackBarBackColor(int colorId) {
        View snackBarView = getSnackBarLayout(mSnackbar);
        if (snackBarView != null) {
            snackBarView.setBackgroundColor(colorId);
        }
        return mSnackbar;
    }

    public void info() {
        setSnackBarBackColor(color_info);
        show();
    }

    public void info(String actionText, View.OnClickListener listener) {
        setSnackBarBackColor(color_info);
        show(actionText, listener);
    }

    public void warning() {
        setSnackBarBackColor(color_warning);
        show();
    }

    public void warning(String actionText, View.OnClickListener listener) {
        setSnackBarBackColor(color_warning);
        show(actionText, listener);
    }

    public void danger() {
        setSnackBarBackColor(color_danger);
        show();
    }

    public void danger(String actionText, View.OnClickListener listener) {
        setSnackBarBackColor(color_danger);
        show(actionText, listener);
    }

    public void confirm() {
        setSnackBarBackColor(color_success);
        show();
    }

    public void confirm(String actionText, View.OnClickListener listener) {
        setSnackBarBackColor(color_success);
        show(actionText, listener);
    }

    public void show() {
        mSnackbar.show();
    }

    public void show(String actionText, View.OnClickListener listener) {
        mSnackbar.setActionTextColor(action_color);
        mSnackbar.setAction(actionText, listener).show();
    }
    /**
     *
     */
    public void showCaseMessage(String actionText, View.OnClickListener listener){
        mSnackbar.setDuration(1500);
        mSnackbar.setAction(actionText, listener);
//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(mSnackbar.getView().getLayoutParams().width,mSnackbar.getView().getLayoutParams().height);
//        params.gravity = Gravity.;
//        mSnackbar.getView().setLayoutParams(params);
        ViewGroup.LayoutParams params = mSnackbar.getView().getLayoutParams();
        ((ViewGroup.MarginLayoutParams) params).setMargins(0,0,0,DisplayUtil.dp2px(BaseApplication.app,50));
        mSnackbar.getView().setLayoutParams(params);
        mSnackbar.show();
    }
}
