package com.negier.emojifragment.util;

import android.content.Context;

/**
 * 像素相关
 */

public class PxUtils {
    public static int dpToPx(Context context,int dp){
        float density = context.getResources().getDisplayMetrics().density;
        return (int)(density*dp+0.5f);
    }
}
