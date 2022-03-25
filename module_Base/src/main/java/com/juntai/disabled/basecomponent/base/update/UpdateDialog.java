package com.juntai.disabled.basecomponent.base.update;

import android.app.Dialog;
import android.content.Context;

/**
 * 升级
 * Created by Ma
 * on 2019/10/11
 */
public class UpdateDialog extends Dialog {
    private int res;
    public UpdateDialog(Context context, int theme, int res) {
        super(context,theme);
        setContentView(res);
        this.res = res;
        setCanceledOnTouchOutside(false);
    }
}
