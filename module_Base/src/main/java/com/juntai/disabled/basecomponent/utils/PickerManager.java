package com.juntai.disabled.basecomponent.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;


import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.juntai.disabled.basecomponent.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Author:wang_sir
 * Time:2019/6/21 17:03
 * Description:This is PickerManager  时间选择器和条件选择器
 */
public class PickerManager<T> {
    public static PickerManager getInstance() {
        return TimePickerManagerHolder.timePickerManager;
    }

    private static class TimePickerManagerHolder {
        private static PickerManager timePickerManager = new PickerManager();
    }

    /**
     * @param context
     * @param type             默认 new boolean[] {true,true,true,false,false,false}
     * @param title
     * @param selectedListener 包含时间范围
     */
    public void showTimePickerViewIncludeRangDate(Context context, boolean[] type, String title, final OnTimePickerTimeSelectedListener selectedListener, Calendar startDate, Calendar endDate) {
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        if (TextUtils.isEmpty(title)) {
            title = "";
        }
        if (type == null) {
            //默认年月日
            type = new boolean[]{true, true, true, false, false, false};
        }
        getBuilder(context, type, title, selectedListener).setRangDate(startDate, endDate).setDate(selectedDate).build().show();

    }

    /**
     * @param context
     * @param type             默认 new boolean[] {true,true,true,false,false,false}
     * @param title
     * @param selectedListener
     */
    public void showTimePickerView(Context context, boolean[] type, String title, final OnTimePickerTimeSelectedListener selectedListener) {
        if (TextUtils.isEmpty(title)) {
            title = "";
        }
        if (type == null) {
            //默认年月日
            type = new boolean[]{true, true, true, false, false, false};
        }
        getBuilder(context, type, title, selectedListener).build().show();
    }

    /**
     * 展示条件选择器
     */
    public void showOptionPicker(Context mContext, List<T> optionsItems, final OnOptionPickerSelectedListener optionPickerSelectedListener) {
        OptionsPickerView pvOptions = getOptionsPickerBuilder(mContext, optionPickerSelectedListener)
                .build();
        pvOptions.setPicker(optionsItems);
        pvOptions.show();
    }

    /**
     * 展示条件选择器
     */
    public OptionsPickerView getOptionPicker(Context mContext, String title, List<T> optionsItems, final OnOptionPickerSelectedListener optionPickerSelectedListener) {
        OptionsPickerView pvOptions = getOptionsPickerBuilder(mContext, optionPickerSelectedListener)
                .setTitleText(title)
                .build();
        pvOptions.setPicker(optionsItems);
        return pvOptions;
    }

    /**
     * 展示条件选择器
     */
    public void showOptionPicker(Context mContext, List<T> optionsItems1, List<T> optionsItems2, final OnOptionPickerSelectedListener optionPickerSelectedListener) {
        OptionsPickerView pvOptions = getOptionsPickerBuilder(mContext, optionPickerSelectedListener).build();
        pvOptions.setPicker(optionsItems1, optionsItems2);
        pvOptions.show();
    }

    /**
     * 展示条件选择器
     */
    public void showOptionPicker(Context mContext, List<T> optionsItems1, List<T> optionsItems2, List<T> optionsItems3, final OnOptionPickerSelectedListener optionPickerSelectedListener) {
        OptionsPickerView pvOptions = getOptionsPickerBuilder(mContext, optionPickerSelectedListener).build();
        pvOptions.setPicker(optionsItems1, optionsItems2, optionsItems3);
        pvOptions.show();
    }

    /**
     * @param mContext
     * @param optionPickerSelectedListener
     * @return
     */
    private OptionsPickerBuilder getOptionsPickerBuilder(Context mContext, final OnOptionPickerSelectedListener optionPickerSelectedListener) {
        return new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                if (optionPickerSelectedListener != null) {
                    optionPickerSelectedListener.onOptionsSelect(options1, option2, options3, v);
                }
            }
        }).setContentTextSize(20)//设置滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
//                .setSelectOptions(0, 1)//默认选中项
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(ContextCompat.getColor(mContext,R.color.colorThemeDark))//确定按钮文字颜色
                .setCancelColor(ContextCompat.getColor(mContext,R.color.text_title))//取消按钮文字颜色
                .setTitleBgColor(ContextCompat.getColor(mContext,R.color.gray_light))//标题背景颜色 Night mode

                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .isCenterLabel(false); //是否只显示中间选中项的label文字，false则每项item全部都带有label。
    }


    /**
     * 获取builder
     *
     * @return
     */
    private TimePickerBuilder getBuilder(Context context, boolean[] type, String title, final OnTimePickerTimeSelectedListener selectedListener) {
        return new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                if (selectedListener != null) {
                    selectedListener.onTimeSelect(date, v);
                }
            }
        })
                .setType(type)// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setTitleSize(20)//标题文字大小
                .setTitleText(title)//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(false)//是否循环滚动
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(ContextCompat.getColor(context,R.color.colorThemeDark))//确定按钮文字颜色
                .setCancelColor(ContextCompat.getColor(context,R.color.text_title))//取消按钮文字颜色
                .setTitleBgColor(ContextCompat.getColor(context,R.color.gray_light))//标题背景颜色 Night mode
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false);//是否显示为对话框样式

    }

    /**
     * 获取选择条件  有些时候 需要根据后台返回的类型定义显示哪些条件
     *
     * @param time
     * @return
     */
    public boolean[] getTimeType(String time) {

        boolean[] booleans = new boolean[0];
        switch (time) {

            case "year":

                booleans = new boolean[]{true, false, false, false, false, false};
                break;
            case "month":

                booleans = new boolean[]{true, true, false, false, false, false};
                break;

            case "day":

                booleans = new boolean[]{true, true, true, false, false, false};
                break;
            case "minute":
                booleans = new boolean[]{true, true, true, true, true, false};
                break;
            case "second":
                booleans = new boolean[]{true, true, true, true, true, true};
                break;
            default:
                booleans = new boolean[]{true, true, true, true, true, true};
                break;


        }

        return booleans;


    }

    public interface OnTimePickerTimeSelectedListener {
        void onTimeSelect(Date date, View v);

    }

    public interface OnOptionPickerSelectedListener {
        void onOptionsSelect(int options1, int option2, int options3, View v);

    }

}
