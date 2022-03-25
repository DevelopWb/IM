package com.juntai.disabled.basecomponent.utils;

import android.content.Context;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.*;

/**
 * @Author: tobato
 * @Description: 作用描述  正则表达式
 * @CreateDate: 2021/3/5 17:20
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/3/5 17:20
 */
public class RuleTools {

    /**
     * 身份证号码验证
     */
    public static boolean isIdNO(Context context, String num) {
        // 去掉所有空格
        num = num.replace(" ", "");

        Pattern idNumPattern = compile("(\\d{17}[0-9xX])");

        //通过Pattern获得Matcher
        Matcher idNumMatcher = idNumPattern.matcher(num);

        //判断用户输入是否为身份证号
        if (idNumMatcher.matches()) {
            System.out.println("您的出生年月日是：");
            //如果是，定义正则表达式提取出身份证中的出生日期
            Pattern birthDatePattern = compile("\\d{6}(\\d{4})(\\d{2})(\\d{2}).*");//身份证上的前6位以及出生年月日

            //通过Pattern获得Matcher
            Matcher birthDateMather = birthDatePattern.matcher(num);

            //通过Matcher获得用户的出生年月日
            if (birthDateMather.find()) {
                String year = birthDateMather.group(1);
                String month = birthDateMather.group(2);
                String date = birthDateMather.group(3);
                if (Integer.parseInt(year) < 1900 // 如果年份是1900年之前
                        || Integer.parseInt(month) > 12 // 月份>12月
                        || Integer.parseInt(date) > 31 // 日期是>31号
                ) {
//                    ToastUtils.toast(context, "身份证号码不正确, 请检查");
                    return false;
                }
            }
            return true;
        } else {
//            ToastUtils.toast(context, "请输入正确的身份证号码");
            return false;
        }
    }

    /**
     * 残疾号码验证
     */
    public static boolean isDisabledIdNO(Context context, String num) {
        // 去掉所有空格
        num = num.replace(" ", "");

        Pattern idNumPattern = compile("(\\d{17}[0-9xX][0-9xX][0-9xX])");

        //通过Pattern获得Matcher
        Matcher idNumMatcher = idNumPattern.matcher(num);

        //判断用户输入是否为身份证号
        if (idNumMatcher.matches()) {
            System.out.println("您的出生年月日是：");
            //如果是，定义正则表达式提取出身份证中的出生日期
            Pattern birthDatePattern = compile("\\d{6}(\\d{4})(\\d{2})(\\d{2}).*");//身份证上的前6位以及出生年月日

            //通过Pattern获得Matcher
            Matcher birthDateMather = birthDatePattern.matcher(num);

            //通过Matcher获得用户的出生年月日
            if (birthDateMather.find()) {
                String year = birthDateMather.group(1);
                String month = birthDateMather.group(2);
                String date = birthDateMather.group(3);
                if (Integer.parseInt(year) < 1900 // 如果年份是1900年之前
                        || Integer.parseInt(month) > 12 // 月份>12月
                        || Integer.parseInt(date) > 31 // 日期是>31号
                ) {
//                    ToastUtils.toast(context, "残疾证号不正确, 请检查");
                    return false;
                }
            }
            return true;
        } else {
//            ToastUtils.toast(context, "请输入正确的残疾证号码");
            return false;
        }
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

    //判断email格式是否正确
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))" +
                "([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }
}
