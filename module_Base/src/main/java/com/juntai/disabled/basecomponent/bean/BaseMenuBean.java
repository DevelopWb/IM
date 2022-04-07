package com.juntai.disabled.basecomponent.bean;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2022-02-28 11:23
 * @UpdateUser: 更新者
 * @UpdateDate: 2022-02-28 11:23
 */
public class BaseMenuBean {


    public final static String  PIC_MENU_FORWARD = "转发";
    public final static String  PIC_MENU_SHARE = "分享";
    public final static String  PIC_MENU_COLLECT = "收藏";
    public final static String  PIC_MENU_SAVE = "保存到本地";
    public final static String  PIC_MENU_SPOT_QRCODE = "识别二维码";

    private String name;
    private int  res;

    public BaseMenuBean(String name) {
        this.name = name;
    }

    public BaseMenuBean(String name, int res) {
        this.name = name;
        this.res = res;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name == null ? "" : name;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }
}
