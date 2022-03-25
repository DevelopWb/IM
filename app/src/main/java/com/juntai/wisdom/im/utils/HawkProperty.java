package com.juntai.wisdom.im.utils;

import android.content.Context;

import com.juntai.wisdom.im.bean.ContactBean;
import com.orhanobut.hawk.Hawk;

import java.util.HashMap;
import java.util.List;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2021/2/27 10:59
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/2/27 10:59
 */
public class HawkProperty {
    public final static String SP_KEY_USER = "userBean";
    public final static String SP_KEY_TOKEN = "token";
    public static String DEV_REGID = "dev_regid";



    //联系人列表
    public static String CONTACT_LIST = "contactlist"+UserInfoManager.getUserId();
    /**
     * 所有的群
     */
    public static String GROUP_LIST = "grouplist"+UserInfoManager.getUserId();
    /**
     * 好友请求
     */
    public static String FRIEND_APPLY = "friendApply";
    public static String UNREAD = "unreadCount";


    //主要用于缓存联系人对应的备注名
    private static HashMap<Integer, String> remarkNameMap;

    public static HashMap<Integer, String> getGlobleMap() {

        if (remarkNameMap == null) {
            remarkNameMap = new HashMap<>();
        }
        if (!remarkNameMap.containsKey(UserInfoManager.getUserId())) {
            remarkNameMap.put(UserInfoManager.getUserId(), UserInfoManager.getUserNickName());
        }
        return remarkNameMap;
    }

    public static void releaseGlobleMap() {

        if (remarkNameMap != null) {
            remarkNameMap.clear();
            remarkNameMap = null;
        }
    }

    /**
     * 所有的好友 包括黑名单里面的好友
     *
     * @return
     */
    public static String getContactListKey() {
        return CONTACT_LIST + UserInfoManager.getUserId();
    }

    /**
     * 草稿  draft
     * @param id  联系人ID  或者群ID
     * @return
     */
    public static String getDraftKey(int id,boolean isContact) {
        if (isContact) {
            return UserInfoManager.getUserUUID() +"Contact"+ id;
        }
        return UserInfoManager.getUserUUID() +"Group"+ id;
    }




    public static boolean isInBlockList(int userId) {
        List<ContactBean> data = Hawk.get(HawkProperty.getContactListKey());
        for (ContactBean datum : data) {
            if (datum.getStatusType() == 2 && datum.getId() == userId) {
                return true;
            }
        }
        return false;
    }

    /**
     * 配置小红点
     * @param mContext
     * @param badgeCount
     */
    public static void setRedPoint(Context mContext, int badgeCount) {
        int unread = Hawk.get(UNREAD, 0);
        unread += badgeCount;
        Hawk.put(UNREAD,unread);
        ShortcutBadger.applyCount(mContext.getApplicationContext(), unread);
    }
    /**
     * 配置小红点
     * @param mContext
     */
    public static void clearRedPoint(Context mContext) {
        Hawk.put(UNREAD,0);
        ShortcutBadger.applyCount(mContext.getApplicationContext(), 0);
    }
}
