package com.juntai.wisdom.im.utils;

import android.content.Context;

import com.juntai.wisdom.im.bean.GroupDetailInfoBean;
import com.juntai.wisdom.im.bean.MessageBodyBean;
import com.juntai.wisdom.im.bean.MyObjectBox;

import java.util.List;

import io.objectbox.BoxStore;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2021/9/11 15:51
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/9/11 15:51
 */
public class ObjectBox {

    public ObjectBox() {
    }

    private static BoxStore boxStore;

    public static void init(Context context) {
        boxStore = MyObjectBox.builder()
                .androidContext(context.getApplicationContext())
                .build();
    }

    public static BoxStore get() {
        return boxStore;
    }

    /**
     * 添加消息
     * @param messageBodyBean
     */
    public static void addMessage(MessageBodyBean...  messageBodyBean) {
        if (messageBodyBean.length>0) {
            for (MessageBodyBean bodyBean : messageBodyBean) {
                bodyBean.setOwner(UserInfoManager.getUserUUID());
                get().boxFor(MessageBodyBean.class).put(bodyBean);
            }
        }

    }
    /**
     * 保存群组信息
     */
    public static void addGroup(GroupDetailInfoBean...  groupDetailInfoBeans) {
        if (groupDetailInfoBeans.length>0) {
            for (GroupDetailInfoBean groupDetailInfoBean : groupDetailInfoBeans) {
                groupDetailInfoBean.setOwner(UserInfoManager.getUserUUID());
                get().boxFor(GroupDetailInfoBean.class).put(groupDetailInfoBean);
            }
        }

    }
    /**
     * 保存群组信息
     */
    public static void addGroup(List<GroupDetailInfoBean> groupDetailInfoBeans) {
        if (groupDetailInfoBeans.size()>0) {
            for (GroupDetailInfoBean groupDetailInfoBean : groupDetailInfoBeans) {
                groupDetailInfoBean.setOwner(UserInfoManager.getUserUUID());
                get().boxFor(GroupDetailInfoBean.class).put(groupDetailInfoBean);
            }
        }

    }



    /**
     * 删除消息
     * @param messageBodyBean
     */
    public static void deleteMessage(MessageBodyBean...  messageBodyBean) {
        get().boxFor(MessageBodyBean.class).remove(messageBodyBean);
    }
}
