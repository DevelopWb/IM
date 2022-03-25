package com.juntai.disabled.basecomponent.widght;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.juntai.disabled.basecomponent.R;
import com.juntai.disabled.basecomponent.app.BaseApplication;

/**
 * 商品规格选择
 */
public abstract class BottomDialogFragment extends DialogFragment {
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.dialog_specifications,container,false);
//        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyTheme);
//        // 设置宽度为屏宽、靠近屏幕底部。
//        final Window window = getDialog().getWindow();
//        window.setBackgroundDrawableResource(R.color.transparent);
//        window.getDecorView().setPadding(0, 0, 0, 0);
//        WindowManager.LayoutParams wlp = window.getAttributes();
//        wlp.gravity = Gravity.BOTTOM;
//        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        window.setAttributes(wlp);
//        return view;
//    }

    View view;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(setView(), null);
        Dialog dialog = new Dialog(getActivity(), R.style.CusDialog);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.setTitle("标题");
        dialog.setCanceledOnTouchOutside(true);
        //Do something
        // 设置宽度为屏宽、位置靠近屏幕底部
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.dialogWindowAnim);
        window.setBackgroundDrawableResource(R.color.transparent);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = BaseApplication.HEIGHT / 4 * 3 ;
        window.setAttributes(wlp);
        initView(view);
        return dialog;
    }
    public abstract int setView();
    public abstract void initView(View view);

}
