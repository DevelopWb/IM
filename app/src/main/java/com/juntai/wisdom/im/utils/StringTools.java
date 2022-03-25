package com.juntai.wisdom.im.utils;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by Ma
 * on 2019/4/16
 */
public class StringTools {
    /**
     * 判断str是否为空或者是空字符串
     *
     * @param str
     * @return
     */
    public static boolean isStringValueOk(String str) {
        if (str != null && !TextUtils.isEmpty(str)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 设置文字部分颜色
     *
     * @return
     */
    public static void setTextPartColor(TextView textView, String content, int startIndex, int endIndex) {
        SpannableString spannableString = null;
        if (StringTools.isStringValueOk(content)) {
            spannableString = new SpannableString(content);
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#45C11A"));
            spannableString.setSpan(colorSpan, startIndex, endIndex, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            textView.setText(spannableString);
        }
    }
//    /**
//     * 设置文字部分颜色
//     *
//     * @return
//     */
//    public static void setTextPartColor2(TextView textView, String content, int startIndex, int endIndex,
//                                        String textColor) {
//        SpannableStringBuilder spannableString = null;
//        if (StringTools.isStringValueOk(content)) {
//            spannableString = new SpannableStringBuilder (content);
//
//            spannableString.setSpan(new ClickableSpan() {
//                @Override
//                public void onClick(@NonNull View widget) {
//                    textView.setText("2寸近照");
////                    if (onClickListener != null) {
////                        onClickListener.onClick(widget);
////                    }
//                }
//            }, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            textView.setText(spannableString);
//        }
//    }

    /**
     * 设置文字部分颜色
     *
     * @return
     */
    public static void setTextPartColor2(TextView textView, String content, int startIndex, int endIndex,
                                         String textColor) {
        SpannableStringBuilder spannableStringBuilder = null;
        if (StringTools.isStringValueOk(content)) {
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor(textColor));
            spannableStringBuilder = new SpannableStringBuilder ();
            SpannableString spannableString = new SpannableString(content);
            spannableString.setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    textView.setText("111");
                }
            }, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(colorSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableStringBuilder.append(spannableString);
            textView.setText(spannableStringBuilder);
        }
    }

    /**
     * 大图
     * @param str
     * @return
     */
    public static ArrayList<String> getImagesBig(String str) {
        String[] ss = getStrings(str);
        for (int i = 0; i < ss.length; i++) {
//            ss[i] = getImageUrlBig(ss[i]);
        }
        return new ArrayList<>(Arrays.asList(ss));
    }

    /**
     * 分割字符串
     * @param str
     * @return
     */
    public static String[] getStrings(String str) {
        if (str != null && !"".equals(str))
            if (str.contains(","))
                return str.split(",");
            else
                return new String[]{str};
        else
            return new String[]{};
    }

    /**
     * 手机号用****号隐藏中间数字
     *
     * @param phone
     * @return
     */
    public static String settingPhone(String phone) {
        String phone_s = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        return phone_s;
    }

    /**
     * 邮箱用****号隐藏前面的字母
     *
     * @return
     */
    public static String settingEmail(String email) {
        String emails = email.replaceAll("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)", "$1****$3$4");
        return emails;
    }

    /**
     * 身份证号用****隐藏前面数字
     *
     * @param IDNumber
     * @return
     */
    public static String settingIDNumber(String IDNumber){
        StringBuilder stringBuilder = new StringBuilder(IDNumber);
        stringBuilder.replace(4, 14, "****");
        return stringBuilder.toString();
    }
    /**
     * 添加<p></p>标签，控制文字大小与颜色
     * @param htmlString
     * @return
     */
    public static String formatHtml(String htmlString){
        htmlString = "<p style=\"font-size:16px; color:#171717; line-height:30px\">"+htmlString+"</p>";
        return htmlString;
    }

    /**
     * 只允许字母、数字和汉字
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static String stringFilter(String str)throws PatternSyntaxException {
        // 只允许字母、数字和汉字其余的还可以随时添加比如下划线什么的，但是注意英文符号和中文符号区别
        String regEx = "[^a-zA-Z0-9\u4E00-\u9FA5]";//正则表达式
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }
}
