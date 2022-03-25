package com.juntai.disabled.basecomponent.base;

import android.view.View;

/**
 * @Author: tobato
 * @Description: 作用描述  防止重复点击
 * @CreateDate: 2020/5/13 10:06
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/5/13 10:06
 */
public abstract class OnMultiClickListener implements View.OnClickListener{
    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;
    public abstract void onMultiClick(View v);
    @Override
    public void onClick(View view) {
        long curClickTime = System.currentTimeMillis();
        if((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            // 超过点击间隔后再将lastClickTime重置为当前点击时间
            lastClickTime = curClickTime;
            onMultiClick(view);
        }
    }
}
