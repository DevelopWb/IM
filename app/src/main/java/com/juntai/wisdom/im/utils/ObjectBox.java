package com.juntai.wisdom.im.utils;

import android.content.Context;

import com.juntai.wisdom.im.bean.GroupDetailInfoBean;
import com.juntai.wisdom.im.bean.GroupDetailInfoBean_;
import com.juntai.wisdom.im.bean.MessageBodyBean;
import com.juntai.wisdom.im.bean.MyObjectBox;

import java.util.List;

import io.objectbox.BoxStore;
import io.objectbox.query.QueryBuilder;

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
        get().boxFor(GroupDetailInfoBean.class).removeAll();
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
        get().boxFor(GroupDetailInfoBean.class).removeAll();
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



    /**
     * 查找所有的群组
     *
     * @return
     */
    public static List<GroupDetailInfoBean> getAllGroupList() {
        QueryBuilder<GroupDetailInfoBean> builder = ObjectBox.get().boxFor(GroupDetailInfoBean.class).query(
                GroupDetailInfoBean_.owner.equal(UserInfoManager.getUserUUID()));
        return builder
                .build()
                .find();
    }
    /**
     * 查找群组
     *
     * @return
     */
    public static GroupDetailInfoBean getGroup(int groupId) {
        QueryBuilder<GroupDetailInfoBean> builder = ObjectBox.get().boxFor(GroupDetailInfoBean.class).query(
                GroupDetailInfoBean_.owner.equal(UserInfoManager.getUserUUID())
        .and(GroupDetailInfoBean_.groupId.equal(groupId)));
        return builder
                .build()
                .findUnique();
    }
    /**
     * 检测群组是否存在
     *
     * @return
     */
    public static boolean checkGroupIsExist(int groupId) {
        QueryBuilder<GroupDetailInfoBean> builder = ObjectBox.get().boxFor(GroupDetailInfoBean.class).query(
                GroupDetailInfoBean_.groupId.equal(groupId)
                        .and(GroupDetailInfoBean_.owner.equal(UserInfoManager.getUserUUID())));
        List<GroupDetailInfoBean> arrays = builder.build().find();
        return !arrays.isEmpty();
    }

    /**
     * 检测群组是否存在
     *
     * @return
     */
    public static boolean checkGroupIsExist(String uuid) {
        QueryBuilder<GroupDetailInfoBean> builder = ObjectBox.get().boxFor(GroupDetailInfoBean.class).query(
                GroupDetailInfoBean_.groupUuid.equal(uuid)
                        .and(GroupDetailInfoBean_.owner.equal(UserInfoManager.getUserUUID())));
        List<GroupDetailInfoBean> arrays = builder.build().find();
        return !arrays.isEmpty();
    }

}
