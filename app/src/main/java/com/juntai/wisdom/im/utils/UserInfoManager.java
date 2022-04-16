package com.juntai.wisdom.im.utils;

import com.juntai.wisdom.im.bean.MessageBodyBean;
import com.juntai.wisdom.im.bean.UserBean;
import com.juntai.wisdom.im.socket.SocketManager;
import com.juntai.wisdom.im.utils.aliPush.AliPushManager;
import com.orhanobut.hawk.Hawk;

/**
 * @Author: tobato
 * @Description: 作用描述  用户信息管理类
 * @CreateDate: 2020/12/19 14:04
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/12/19 14:04
 */
public class UserInfoManager {
    public static String QQ_ID = null;//qqid
    public static String WECHAT_ID = null;//wechatid
    public static String OTHER_NICK_NAME = null;//第三方昵称

    /**
     * 获取账号的状态  0 代表游客登录 1代表手机号登录 2代表第三方登录（未绑定手机号）
     *
     * @return
     */
    public static int getAccountStatus() {
        int status = -1;
        if (isLogin()) {
            String phoneNum = getUser().getData().getPhoneNumber();
            if ("未绑定".equals(phoneNum)) {
                status = 2;
            } else {
                status = 1;
            }
        } else {
            status = 0;
        }
        return status;
    }
    /**
     * 退出登录清理缓存配置
     */
    public static void clearUserData() {
        Hawk.delete(HawkProperty.SP_KEY_USER);
        Hawk.delete(HawkProperty.SP_KEY_TOKEN);
//        Hawk.delete(HawkProperty.SP_KEY_UNREAD_COUNT);
        //ws退出连接
        SocketManager.getInstance().unConnect();
        AliPushManager.getInstance().unbindAccount(UserInfoManager.getUserUUID());

    }
    /**
     * 获取用户信息
     *
     * @return
     */
    public static UserBean getUser() {
        UserBean userBean = Hawk.get(HawkProperty.SP_KEY_USER);
        return userBean;
    }

    /**
     * 判定用户是否登录
     *
     * @return
     */
    public static boolean isLogin() {
        return Hawk.contains(HawkProperty.SP_KEY_USER)&&Hawk.contains(HawkProperty.SP_KEY_TOKEN);
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public static String getPhoneNumber() {
        return getUser() != null && getUser().getData() != null ? getUser().getData().getPhoneNumber() : "";
    }


    /**
     * 获取用户的备注信息
     * @return
     */
    public static String getContactRemarkName(MessageBodyBean messageBodyBean) {
        return  HawkProperty.getGlobleMap().containsKey(messageBodyBean.getFromUserId()) ? HawkProperty.getGlobleMap().get(messageBodyBean.getFromUserId()) : messageBodyBean.getFromNickname();
    }
    /**
     * 获取用户的备注信息
     * @return
     */
    public static String getContactRemarkName(int userId,String remarkName) {
        return  HawkProperty.getGlobleMap().containsKey(userId) ? HawkProperty.getGlobleMap().get(userId) : remarkName;
    }
    /**
     * 获取用户信息
     *
     * @return
     */
    public static String getAddr() {
        return getUser() != null && getUser().getData() != null ? getUser().getData().getAddress() : "";
    }
    public static String getHeadPic() {
        return getUser() != null && getUser().getData() != null ? getUser().getData().getHeadPortrait() : "";
    }

    /**
     * 获取账户
     *
     * @return
     */
    public static String getUserNickName() {
        return getUser() != null && getUser().getData() != null ? getUser().getData().getNickname() : "";
    }
    public static String getQRCode() {
        return getUser() != null && getUser().getData() != null ? getUser().getData().getQrCode() : "";
    }
    /**
     * 获取usertoken
     *
     * @return
     */
    public static String getUserToken() {
        return Hawk.get(HawkProperty.SP_KEY_TOKEN);
    }

    /**
     * 获取getUserId
     *
     * @return
     */
    public static int getUserId() {
        return getUser() != null && getUser().getData() != null ? getUser().getData().getId() : -1;
    }
    public static boolean getUserVerifyFriend() {
        return getUser() == null || getUser().getData() == null || getUser().getData().isAddFriendVerification();
    }
    /**
     * 获取getUserId
     *
     * @return
     */
    public static int getSexId() {
        return getUser() != null && getUser().getData() != null ? getUser().getData().getGender() : 2;
    }

    /**
     * 获取账户
     *
     * @return
     */
    public static String getUserAccount() {
        return getUser() != null && getUser().getData() != null ? getUser().getData().getAccountNumber() : "unregistered";
    }
    /**
     * 获取getUserId
     *
     * @return
     */
    public static String getUserUUID() {
        return getUser() != null && getUser().getData() != null ? getUser().getData().getUuid() : "unregistered";
    }


}
