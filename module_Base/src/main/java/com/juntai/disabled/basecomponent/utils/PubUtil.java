package com.juntai.disabled.basecomponent.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import java.io.File;
import java.lang.reflect.Field;
import java.util.regex.Pattern;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2020/3/9 14:11
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/3/9 14:11
 */
public class PubUtil {
    public  static  final  String  ONE_CLICK_EVENT = "one_click_event";//单次点击事件
    public  static  final  String  ERROR_NOTICE = "请检查网络连接";//服务器异常的提醒

    /**
     * 拨打电话（直接拨打电话）
     *
     * @param phoneNum 电话号码
     */
    public static void callPhone(Context context, String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        context.startActivity(intent);
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */
        String telRegex = "[1][23456789]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        return !TextUtils.isEmpty(mobiles) && mobiles.matches(telRegex);
    }

    /**
     * 验证用户名只包含字母，数字，中文,下划线
     *\u4E00-\u9FA5A标识中文  \w是下划线
     * @param account
     * @return
     */
    public static boolean checkAccountMark(String account) {
        String all = "^[a-zA-Z0-9\\u4e00-\\u9fa5\\w]+$";
        return Pattern.matches(all, account);
    }
    /**
     * 验证密码 字母 数字 下划线 必须有两种
     * \u4E00-\u9FA5A标识中文  \w是下划线 {5,21}代表最小6位 最大21位
     *
     * @param account
     * @return
     */
    public static boolean checkPwdMark(String account) {
        String all =  "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{5,21}\\w$";
        return Pattern.matches(all, account);
    }
    /**
     * 验证身份证号码
     *
     * @param idCard 居民身份证号码15位或18位，最后一位可能是数字或字母
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkIdCard(String idCard) {
        String regex = "[1-9]\\d{13,16}[a-zA-Z0-9]{1}";
        return Pattern.matches(regex, idCard);
    }
    /**
     * 设置tablayout底部导航条的宽度
     *
     * @param tabs
     * @param leftDip  距离左边的边距
     * @param rightDip 距离右边的边距
     */
    public static void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        try {
            Field tabStrip = tabs.getClass().getDeclaredField("mTabStrip");
            tabStrip.setAccessible(true);
            LinearLayout llTab = null;
            llTab = (LinearLayout) tabStrip.get(tabs);
            int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
            int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

            for (int i = 0; i < llTab.getChildCount(); i++) {
                View child = llTab.getChildAt(i);
                child.setPadding(0, 0, 0, 0);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                params.leftMargin = left;
                params.rightMargin = right;
                child.setLayoutParams(params);
                child.invalidate();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


}
