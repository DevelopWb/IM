package com.juntai.wisdom.im.utils;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.juntai.disabled.basecomponent.base.BaseActivity;
import com.juntai.disabled.basecomponent.utils.GsonTools;
import com.juntai.wisdom.im.bean.MessageBodyBean;
import com.juntai.wisdom.im.bean.MultipleItem;
import com.juntai.wisdom.im.chatlist.chat.videocall.VideoRequestActivity;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2021-09-29 10:57
 * @UpdateUser: 更新者
 * @UpdateDate: 2021-09-29 10:57
 */
public class SendMsgUtil {

    /**
     * 跳转类型
     */
    public static int TYPE = 888888;

    /**
     * 华为推送的时候 需要将推送相关的数据整理好之后发给服务端
     *
     * @return
     */
    public static String getHuaWeiPushIntentStr(MessageBodyBean messageBodyBean) {
        Intent intent = null;
// Scheme协议（例如：pushscheme://com.huawei.codelabpush/deeplink?）需要您自定义
        intent = new Intent(Intent.ACTION_VIEW);
        if (messageBodyBean.getGroupId() > 0) {
            //群组消息
            intent.setData(Uri.parse("pushscheme://com.juntai.wisdom.im.groupchat/push?"));
        } else {
            if (messageBodyBean.getMsgType() == 4 || messageBodyBean.getMsgType() == 5) {
                if (VideoRequestActivity.EVENT_CAMERA_REQUEST.equals(messageBodyBean.getEvent())) {
                    intent.setData(Uri.parse("pushscheme://com.juntai.wisdom.im.videocall/push?"));
                } else {
                    intent.setData(Uri.parse("pushscheme://com.juntai.wisdom.im.privatechat/push?"));
                }
            } else {
                intent.setData(Uri.parse("pushscheme://com.juntai.wisdom.im.privatechat/push?"));
            }
        }


// 往intent中添加参数，用户可以根据自己的需求添加参数
        intent.putExtra(BaseActivity.BASE_STRING, GsonTools.createGsonString(messageBodyBean));
        intent.putExtra(VideoRequestActivity.IS_SENDER, false);
// 应用必须带上该Flag，如果不添加该选项有可能会显示重复的消息
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        String intentUri = intent.toUri(Intent.URI_INTENT_SCHEME);

// 打印出的intentUri值就是设置到推送消息中intent字段的值
        return intentUri;
    }

    /**
     * 华为推送的时候 需要将推送相关的数据整理好之后发给服务端
     *
     * @return
     */
    public static String getXiaomiPushIntentStr(MessageBodyBean messageBodyBean) {
        Intent intent = new Intent();
// Scheme协议（例如：pushscheme://com.huawei.codelabpush/deeplink?）需要您自定义
        ComponentName componentName = null;
        if (messageBodyBean.getGroupId() > 0) {
            //群组消息
            componentName = new ComponentName("com.juntai.wisdom.im", "com.juntai.wisdom.im.chatlist.groupchat.GroupChatActivity");
        } else {
            if (messageBodyBean.getMsgType() == 4 || messageBodyBean.getMsgType() == 5) {
                if (VideoRequestActivity.EVENT_CAMERA_REQUEST.equals(messageBodyBean.getEvent())) {
                    componentName = new ComponentName("com.juntai.wisdom.im", "com.juntai.wisdom.im.chatlist.chat.videocall.VideoRequestActivity");
                } else {
                    componentName = new ComponentName("com.juntai.wisdom.im", "com.juntai.wisdom.im.chatlist.chat.PrivateChatActivity");
                }
            } else {
                componentName = new ComponentName("com.juntai.wisdom.im", "com.juntai.wisdom.im.chatlist.chat.PrivateChatActivity");
            }
        }

        intent.setComponent(componentName);//调用Intent的setComponent()方法实现传递

// 往intent中添加参数，用户可以根据自己的需求添加参数
        intent.putExtra(BaseActivity.BASE_STRING, GsonTools.createGsonString(messageBodyBean));

// 应用必须带上该Flag，如果不添加该选项有可能会显示重复的消息
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        String intentUri = intent.toUri(Intent.URI_INTENT_SCHEME);

// 打印出的intentUri值就是设置到推送消息中intent字段的值
        return intentUri;
    }

    /**
     * 选中的 待编辑的消息
     */
    public static List<MultipleItem> selectedToEditMsgItems = new ArrayList<>();

    /**
     * 获取私聊文本消息
     *
     * @param toUserId
     * @param toUserAccout
     * @param toNickName
     * @param content
     * @param msgType      msgType":"消息类型（0：text；1：image；2：video；3：语音；4视频通话；5音频通话，6位置消息 7分享名片 8 文件",
     * @return
     */
    public static MessageBodyBean getPrivateMsg(int msgType, int toUserId, String toUserAccout, String toNickName, String toHead, String content) {
        MessageBodyBean messageBody = new MessageBodyBean();
        messageBody.setContent(content);
        messageBody.setCreateTime(String.valueOf(System.currentTimeMillis()));
        messageBody.setFromAccount(UserInfoManager.getUserUUID());
        messageBody.setFromNickname(UserInfoManager.getUserNickName());
        messageBody.setFromHead(UserInfoManager.getHeadPic());
        messageBody.setFromUserId(UserInfoManager.getUserId());
        messageBody.setRead(true);
        // TODO: 2021-11-19 阅后即焚  先默认1 否
        messageBody.setReadBurn(1);
        messageBody.setToAccount(toUserAccout);
        messageBody.setToNickname(toNickName);
        messageBody.setToHead(toHead);
        messageBody.setToUserId(toUserId);
        messageBody.setChatType(1);
        messageBody.setMsgType(msgType);
        return messageBody;
    }

    /**
     * 群聊消息
     *
     * @param msgType 消息类型
     * @param content
     * @return
     */
    public static MessageBodyBean getGroupMsg(int msgType, int groupId, String groupNickName, String content) {

        if (TextUtils.isEmpty(groupNickName)) {
            groupNickName = UserInfoManager.getUserNickName();
        }

        MessageBodyBean messageBody = new MessageBodyBean();
        messageBody.setContent(content);
        messageBody.setCreateTime(String.valueOf(System.currentTimeMillis()));
        messageBody.setFromAccount(UserInfoManager.getUserUUID());
        messageBody.setFromNickname(groupNickName);
        messageBody.setFromHead(UserInfoManager.getHeadPic());
        messageBody.setFromUserId(UserInfoManager.getUserId());
        messageBody.setRead(true);
        // TODO: 2021-11-19 阅后即焚  先默认1 否
        messageBody.setReadBurn(1);
        messageBody.setChatType(2);
        messageBody.setGroupId(groupId);
        messageBody.setMsgType(msgType);
        return messageBody;
    }

    /**
     * 群聊消息
     *
     * @param msgType        消息类型  普通群聊消息  涉及@功能
     * @param content
     * @param groupCreaterId 群主id
     * @return
     */
    public static MessageBodyBean getGroupMsg(int msgType, int groupId, int groupCreaterId, String groupNickName, String content) {
        MessageBodyBean messageBody = getGroupMsg(msgType, groupId, groupNickName, content);
        messageBody.setIsGroupCreater(groupCreaterId == UserInfoManager.getUserId() ? 1 : 0);
        return messageBody;
    }

    /**
     * 获取私聊位置消息
     *
     * @param toUserId
     * @param toUserAccout
     * @param toNickName
     * @param content      用于保存地图的截屏
     * @return
     */
    public static MessageBodyBean getPrivateLocateMsg(int toUserId, String toUserAccout, String toNickName, String toHead, String content, String addrName,
                                                      String addrDes, String lat, String lng) {
        MessageBodyBean messageBody = getPrivateMsg(6, toUserId, toUserAccout, toNickName, toHead, content);
        messageBody.setAddrName(addrName);
        messageBody.setAddrDes(addrDes);
        messageBody.setLat(lat);
        messageBody.setLng(lng);
        return messageBody;
    }

    /**
     * 获取私聊位置消息
     *
     * @param content 用于保存地图的截屏
     * @return
     */
    public static MessageBodyBean getGroupLocateMsg(int groupId, String content, String addrName,
                                                    String addrDes, String lat, String lng, String groupUserName) {
        MessageBodyBean messageBody = getGroupMsg(6, groupId, groupUserName, content);
        messageBody.setAddrName(addrName);
        messageBody.setAddrDes(addrDes);
        messageBody.setLat(lat);
        messageBody.setLng(lng);
        return messageBody;
    }


    public static MultipartBody.Builder getMsgBuilder(MessageBodyBean messageBodyBean) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("token", UserInfoManager.getUserToken())
                .addFormDataPart("userId", String.valueOf(UserInfoManager.getUserId()))
                .addFormDataPart("type", "1")
                .addFormDataPart("fromUserId", String.valueOf(messageBodyBean.getFromUserId()))
                .addFormDataPart("fromAccount", messageBodyBean.getFromAccount())
                .addFormDataPart("fromNickname", messageBodyBean.getFromNickname())
                .addFormDataPart("event", messageBodyBean.getEvent())
                .addFormDataPart("fromHead", messageBodyBean.getFromHead())
                .addFormDataPart("toUserId", String.valueOf(messageBodyBean.getToUserId()))
                .addFormDataPart("toAccount", TextUtils.isEmpty(messageBodyBean.getToAccount()) ? "0" : messageBodyBean.getToAccount())
                .addFormDataPart("toNickname", messageBodyBean.getToNickname())
                .addFormDataPart("rotation", messageBodyBean.getRotation())
                .addFormDataPart("toHead", messageBodyBean.getToHead())
                .addFormDataPart("otherUserId", String.valueOf(messageBodyBean.getOtherUserId()))
                .addFormDataPart("otherAccount", messageBodyBean.getOtherAccount())
                .addFormDataPart("otherNickname", messageBodyBean.getOtherNickname())
                .addFormDataPart("otherHead", messageBodyBean.getOtherHead())
                .addFormDataPart("content", messageBodyBean.getContent())
                .addFormDataPart("sdp", messageBodyBean.getSdp())
                .addFormDataPart("sdpMid", messageBodyBean.getSdpMid())
                .addFormDataPart("sdpMLineIndex", String.valueOf(messageBodyBean.getSdpMLineIndex()))
                .addFormDataPart("duration", messageBodyBean.getDuration())
                .addFormDataPart("videoCover", messageBodyBean.getVideoCover())
                .addFormDataPart("fileSize", messageBodyBean.getFileSize())
                .addFormDataPart("fileName", messageBodyBean.getFileName())
                .addFormDataPart("hwPushIntentUrl", SendMsgUtil.getHuaWeiPushIntentStr(messageBodyBean))
                .addFormDataPart("xiaomiPushIntentUrl", SendMsgUtil.getXiaomiPushIntentStr(messageBodyBean))
                .addFormDataPart("faceTimeType", String.valueOf(messageBodyBean.getFaceTimeType()))
                .addFormDataPart("readBurn", String.valueOf(messageBodyBean.getReadBurn()))
                .addFormDataPart("msgType", String.valueOf(messageBodyBean.getMsgType()))
                .addFormDataPart("groupNickname", String.valueOf(messageBodyBean.getGroupNickname()))
                .addFormDataPart("chatType", String.valueOf(messageBodyBean.getChatType()));
        if (messageBodyBean.getFromUserId() == UserInfoManager.getUserId()) {
            //我发送的信息
            //收藏的时候需要上传
            builder.addFormDataPart("localCatchPath", String.valueOf(messageBodyBean.getLocalCatchPath()));
        }
        if (2 == messageBodyBean.getChatType()) {
            builder.addFormDataPart("groupId", String.valueOf(messageBodyBean.getGroupId()));
            builder.addFormDataPart("isGroupCreater", String.valueOf(messageBodyBean.getIsGroupCreater()));
            if (!TextUtils.isEmpty(messageBodyBean.getAtUserId())) {
               builder.addFormDataPart("atUserId",messageBodyBean.getAtUserId());
            }

        }
        if (6 == messageBodyBean.getMsgType()) {
            builder.addFormDataPart("lat", messageBodyBean.getLat())
                    .addFormDataPart("lng", messageBodyBean.getLng())
                    .addFormDataPart("addrName", messageBodyBean.getAddrName())
                    .addFormDataPart("addrDes", messageBodyBean.getAddrDes());
        }
        return builder;

    }

}
