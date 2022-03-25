package com.juntai.disabled.basecomponent;


import android.graphics.Typeface;

import es.dmoral.toasty.Toasty;

/**
 * @aouther Ma
 * @date 2019/3/14
 */
public class Test {

    /**
     * Toasty使用
     */
    public void test1(){
        Toasty.Config.getInstance()
                .tintIcon(true) // optional (apply textColor also to the icon)
                .setToastTypeface(Typeface.DEFAULT) // optional
                .setTextSize(15) // optional
                .allowQueue(true) // optional (prevents several Toastys from queuing)
                .apply(); // required
        //重置
        Toasty.Config.reset();

//        To display an error Toast:
//
//        Toasty.error(yourContext, "This is an error toast.", Toast.LENGTH_SHORT, true).show();
//        To display a success Toast:
//
//        Toasty.success(yourContext, "Success!", Toast.LENGTH_SHORT, true).show();
//        To display an info Toast:
//
//        Toasty.info(yourContext, "Here is some info for you.", Toast.LENGTH_SHORT, true).show();
//        To display a warning Toast:
//
//        Toasty.warning(yourContext, "Beware of the dog.", Toast.LENGTH_SHORT, true).show();
//        To display the usual Toast:
//
//        Toasty.normal_recycleview_layout(yourContext, "Normal toast w/o icon").show();
//        To display the usual Toast with icon:
//
//        Toasty.normal_recycleview_layout(yourContext, "Normal toast w/ icon", yourIconDrawable).show();
//        You can also create your custom Toasts with the custom() method:
//
//        Toasty.custom(yourContext, "I'm a custom Toast", yourIconDrawable, tintColor, duration, withIcon,
//                shouldTint).show();
    }
}
