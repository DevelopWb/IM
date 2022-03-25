package com.juntai.disabled.basecomponent.base;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;


/**
 * Describe:自定义抽屉布局，自适应高度
 * Create by tobato
 * 2021-1-2
 * email:954101549@qq.com
 */
public class BaseDrawerLayout extends DrawerLayout {
    public BaseDrawerLayout(Context context) {
        super(context);
    }

    public BaseDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(
                MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
