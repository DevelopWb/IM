package com.juntai.wisdom.im.mine;


import com.juntai.disabled.basecomponent.mvp.IView;
import com.juntai.wisdom.im.bean.MyMenuBean;

import java.util.List;

/**
 * Describe: 个人信息接口类
 * Create by tobato
 * 2020/3/7
 * email:954101549@qq.com
 */
public interface MyCenterContract {
    String SET_UPDATE_TAG = "setUpdateTag";
    String SET_CLEAR_TAG ="setClearTag";
    //设置相关
    String SET_UPDATE_PSD_TAG = "setUpdatePsdTag";
    String SET_UPDATE_TEL_TAG = "setUpdateTelTag";
    String SET_WEIXIN_TAG ="setWeiXinTag";
    String SET_QQ_TAG = "setQQTag";
    String SET_ABOUT_TAG ="setAboutTag";
    String CENTER_SETTING_TAG ="centerSettingTag";
    String USER_DATA_TAG = "getUserInfo";



    //设置相关
    String MODIFY_PWD = "setUpdatePsdTag";
    String MENU_PRIVATE = "隐私";
    String MENU_COLLECT = "我的收藏";
    //我的消息
    String MENU_NEWS ="centerSettingTag";

    interface ICenterView extends IView {
    }

    interface ICenterPresent {
        void initList();

    }
}
