package com.juntai.disabled.basecomponent.widght;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import java.lang.reflect.Field;

/**
 * Describe:防止出现内存泄漏
 * Create by tobato
 * 2020-4-11
 * email:954101549@qq.com1
 */
@SuppressLint("AppCompatCustomView")
public class BaseEditText extends EditText {
    private static Field mParent;

    static {
        try {
            mParent = View.class.getDeclaredField("mParent");
            mParent.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public BaseEditText(Context context) {
        super(context.getApplicationContext());
    }

    public BaseEditText(Context context, AttributeSet attrs) {
        super(context.getApplicationContext(), attrs);
    }

    public BaseEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context.getApplicationContext(), attrs, defStyleAttr);
    }

    @Override
    protected void onDetachedFromWindow() {
        try {
            if (mParent != null)
                mParent.set(this, null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        super.onDetachedFromWindow();
    }
}