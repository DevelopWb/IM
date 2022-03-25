package com.juntai.wisdom.im.entrance;

import com.juntai.disabled.basecomponent.mvp.IView;

/**
 * @Author: tobato
 * @Description: 作用描述  APP入口
 * @CreateDate: 2020/3/5 15:53
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/3/5 15:53
 */
public interface EntranceContract {
    String BIND_QQ_OR_WECHAT = "bindQQOrWeChat";
    String BIND_PHONE = "bindphone";
    String LOGIN_TAG = "login";//登录的标识
    String OTHER_REGIST = "other_regist";//第三方注册

    interface IEntranceView extends IView {
    }

    interface IEntrancePresent {
        void login(String account, String password, String weChatId, String qqId, String tag);



    }
}
