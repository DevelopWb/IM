package com.juntai.wisdom.im.bean;

import android.os.Parcel;
import android.os.Parcelable;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2021-09-29 9:39
 * @UpdateUser: 更新者
 * @UpdateDate: 2021-09-29 9:39
 */
@Entity
public class MessageBodyBean extends BaseWsMessageBean implements Parcelable {

    /**
     * fromUserId : 101
     * fromAccount : 发送者账号
     * toUserId : 97
     * toAccount : 接收者账号
     * content : 消息内容
     * date : 2021-09-29 09:35:00
     * "faceTimeType":"音视频通话状态（1发起通话；2接听；3结束）",
     * "msgType":"消息类型（0：text；1：image；2：video；3：语音；4视频通话；5音频通话，6位置消息，7分享名片，8文件，9合并消息）",
     * "chatType":"聊天类型（1：私聊；2公聊；4好友添加）",
     * groupId : 群组id；仅chat_type为2时需要
     */
    @Id
    public long id;
    private int fromUserId;
    private String fromAccount;
    /**
     * 此条消息的拥有者   主要用于解决切换账号后  数据紊乱的问题  存储用户的account就可以
     */
    private String   owner;
    private String fromNickname;
    /**
     * 华为推送需要的intenturi
     */
    private String hwPushIntentUrl;
    private String fromHead;
    private int toUserId;
    private int adapterPosition;
    private String toAccount;
    private String toNickname;
    private String toHead;
    private String content;
    //可被删除  主要用于离线的最后一条消息
    private  boolean  canDelete;
    //是否是草稿
    private  boolean  isDraft;

    private int otherUserId;
    private String otherAccount;
    private String otherNickname;
    private String collectionCreateTime;
    private String otherHead;
    private String createTime;
    /**
     * 1已接听；2已取消；3超时；4通话中断
     */
    private int faceTimeType;
    /**
     * 0：text；1：image；2：video；3：语音；4视频通话；5音频通话，6位置消息，7分享名片，8文件9合并消息 10 群聊创建成功的通知消息 100  消息时间
     */
    private int msgType;
    /**
     * * "chatType":"聊天类型（1：私聊；2公聊；4好友添加）",
     */
    private int chatType;
    private int groupId;
    private String groupNickname;//群昵称（备注）;
    /**
     * 是否是群主发的  0不是  1是
     */
    private int isGroupCreater;
    private int readBurn;//阅后即焚（1：否；2：是）
    private boolean isRead;
    //已经收藏
    private boolean isCollected;
    //是否被选中
    private boolean isSelected;
    private String duration;//发送音频的总时长
    private int unreadCount;//

    //文件大小
    private String fileSize;
    private String fileName;
    //图片 视频 文件 本地缓存路径
    private String localCatchPath;
    //上传进度
    private  int  uploadProgress;


    //视频通话相关
    private String event;//
    private String sdp;//
    private int sdpMLineIndex;//
    private String sdpMid;//
    //图片 视频的旋转角度
    private String rotation;//
    private String videoCover;//

    public String getHwPushIntentUrl() {
        return hwPushIntentUrl == null ? "" : hwPushIntentUrl;
    }

    public void setHwPushIntentUrl(String hwPushIntentUrl) {
        this.hwPushIntentUrl = hwPushIntentUrl == null ? "" : hwPushIntentUrl;
    }

    //位置信息相关
    private String addrName;//
    private String addrDes;//
    private String lat;//
    private String lng;//





    public MessageBodyBean(String content, int msgType) {
        this.content = content;
        this.msgType = msgType;
    }

    public boolean isCollected() {
        return isCollected;
    }

    public void setCollected(boolean collected) {
        isCollected = collected;
    }

    public boolean isDraft() {
        return isDraft;
    }

    public void setDraft(boolean draft) {
        isDraft = draft;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    public String getLocalCatchPath() {
        return localCatchPath == null ? "" : localCatchPath;
    }

    public void setLocalCatchPath(String localCatchPath) {
        this.localCatchPath = localCatchPath == null ? "" : localCatchPath;
    }

    public String getVideoCover() {
        return videoCover == null ? "" : videoCover;
    }

    public void setVideoCover(String videoCover) {
        this.videoCover = videoCover == null ? "" : videoCover;
    }

    public String getOwner() {
        return owner == null ? "" : owner;
    }

    public void setOwner(String owner) {
        this.owner = owner == null ? "" : owner;
    }

    public int getAdapterPosition() {
        return adapterPosition;
    }

    public void setAdapterPosition(int adapterPosition) {
        this.adapterPosition = adapterPosition;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getIsGroupCreater() {
        return isGroupCreater;
    }

    public void setIsGroupCreater(int isGroupCreater) {
        this.isGroupCreater = isGroupCreater;
    }

    public int getOtherUserId() {
        return otherUserId;
    }

    public void setOtherUserId(int otherUserId) {
        this.otherUserId = otherUserId;
    }

    public String getGroupNickname() {
        return groupNickname == null ? "" : groupNickname;
    }

    public int getUploadProgress() {
        return uploadProgress;
    }

    public void setUploadProgress(int uploadProgress) {
        this.uploadProgress = uploadProgress;
    }

    public void setGroupNickname(String groupNickname) {
        this.groupNickname = groupNickname == null ? "" : groupNickname;
    }

    public String getCollectionCreateTime() {
        return collectionCreateTime == null ? "" : collectionCreateTime;
    }

    public void setCollectionCreateTime(String collectionCreateTime) {
        this.collectionCreateTime = collectionCreateTime == null ? "" : collectionCreateTime;
    }

    public String getOtherAccount() {
        return otherAccount == null ? "" : otherAccount;
    }

    public void setOtherAccount(String otherAccount) {
        this.otherAccount = otherAccount == null ? "" : otherAccount;
    }

    public String getOtherNickname() {
        return otherNickname == null ? "" : otherNickname;
    }

    public void setOtherNickname(String otherNickname) {
        this.otherNickname = otherNickname == null ? "" : otherNickname;
    }

    public String getOtherHead() {
        return otherHead == null ? "" : otherHead;
    }

    public void setOtherHead(String otherHead) {
        this.otherHead = otherHead == null ? "" : otherHead;
    }

    public String getFileName() {
        return fileName == null ? "" : fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName == null ? "" : fileName;
    }

    public String getFileSize() {
        return fileSize == null ? "" : fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize == null ? "" : fileSize;
    }

    public String getAddrName() {
        return addrName == null ? "" : addrName;
    }

    public void setAddrName(String addrName) {
        this.addrName = addrName == null ? "" : addrName;
    }

    public String getAddrDes() {
        return addrDes == null ? "" : addrDes;
    }

    public void setAddrDes(String addrDes) {
        this.addrDes = addrDes == null ? "" : addrDes;
    }

    public String getLat() {
        return lat == null ? "" : lat;
    }

    public void setLat(String lat) {
        this.lat = lat == null ? "" : lat;
    }

    public String getLng() {
        return lng == null ? "" : lng;
    }

    public void setLng(String lng) {
        this.lng = lng == null ? "" : lng;
    }

    public String getSdp() {
        return sdp == null ? "" : sdp;
    }

    public String getEvent() {
        return event == null ? "" : event;
    }

    public void setEvent(String event) {
        this.event = event == null ? "" : event;
    }

    public void setSdp(String sdp) {
        this.sdp = sdp == null ? "" : sdp;
    }

    public int getSdpMLineIndex() {
        return sdpMLineIndex;
    }

    public void setSdpMLineIndex(int sdpMLineIndex) {
        this.sdpMLineIndex = sdpMLineIndex;
    }

    public String getSdpMid() {
        return sdpMid == null ? "" : sdpMid;
    }

    public void setSdpMid(String sdpMid) {
        this.sdpMid = sdpMid == null ? "" : sdpMid;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getRotation() {
        return rotation == null ? "" : rotation;
    }

    public void setRotation(String rotation) {
        this.rotation = rotation == null ? "" : rotation;
    }

    public String getDuration() {
        return duration == null ? "" : duration;
    }

    public void setDuration(String duration) {
        this.duration = duration == null ? "" : duration;
    }

    public int getFaceTimeType() {
        return faceTimeType;
    }

    public void setFaceTimeType(int faceTimeType) {
        this.faceTimeType = faceTimeType;
    }

    public String getFromHead() {
        return fromHead == null ? "" : fromHead;
    }

    public void setFromHead(String fromHead) {
        this.fromHead = fromHead == null ? "" : fromHead;
    }

    public String getToHead() {
        return toHead == null ? "" : toHead;
    }

    public void setToHead(String toHead) {
        this.toHead = toHead == null ? "" : toHead;
    }


    public int getReadBurn() {
        return readBurn;
    }

    public void setReadBurn(int readBurn) {
        this.readBurn = readBurn;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getFromNickname() {
        return fromNickname == null ? "" : fromNickname;
    }

    public void setFromNickname(String fromNickname) {
        this.fromNickname = fromNickname == null ? "" : fromNickname;
    }

    public String getToNickname() {
        return toNickname == null ? "" : toNickname;
    }

    public void setToNickname(String toNickname) {
        this.toNickname = toNickname == null ? "" : toNickname;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getFromAccount() {
        return fromAccount == null ? "" : fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount == null ? "" : fromAccount;
    }

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public String getToAccount() {
        return toAccount == null ? "" : toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount == null ? "" : toAccount;
    }

    public String getContent() {
        return content == null ? "" : content;
    }

    public void setContent(String content) {
        this.content = content == null ? "" : content;
    }

    public String getCreateTime() {
        return createTime == null ? "" : createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime == null ? "" : createTime;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public MessageBodyBean() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeInt(this.fromUserId);
        dest.writeString(this.fromAccount);
        dest.writeString(this.owner);
        dest.writeString(this.fromNickname);
        dest.writeString(this.hwPushIntentUrl);
        dest.writeString(this.fromHead);
        dest.writeInt(this.toUserId);
        dest.writeInt(this.adapterPosition);
        dest.writeString(this.toAccount);
        dest.writeString(this.toNickname);
        dest.writeString(this.toHead);
        dest.writeString(this.content);
        dest.writeByte(this.canDelete ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isDraft ? (byte) 1 : (byte) 0);
        dest.writeInt(this.otherUserId);
        dest.writeString(this.otherAccount);
        dest.writeString(this.otherNickname);
        dest.writeString(this.collectionCreateTime);
        dest.writeString(this.otherHead);
        dest.writeString(this.createTime);
        dest.writeInt(this.faceTimeType);
        dest.writeInt(this.msgType);
        dest.writeInt(this.chatType);
        dest.writeInt(this.groupId);
        dest.writeString(this.groupNickname);
        dest.writeInt(this.isGroupCreater);
        dest.writeInt(this.readBurn);
        dest.writeByte(this.isRead ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isCollected ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
        dest.writeString(this.duration);
        dest.writeInt(this.unreadCount);
        dest.writeString(this.fileSize);
        dest.writeString(this.fileName);
        dest.writeString(this.localCatchPath);
        dest.writeInt(this.uploadProgress);
        dest.writeString(this.event);
        dest.writeString(this.sdp);
        dest.writeInt(this.sdpMLineIndex);
        dest.writeString(this.sdpMid);
        dest.writeString(this.rotation);
        dest.writeString(this.videoCover);
        dest.writeString(this.addrName);
        dest.writeString(this.addrDes);
        dest.writeString(this.lat);
        dest.writeString(this.lng);
    }

    protected MessageBodyBean(Parcel in) {
        this.id = in.readLong();
        this.fromUserId = in.readInt();
        this.fromAccount = in.readString();
        this.owner = in.readString();
        this.fromNickname = in.readString();
        this.hwPushIntentUrl = in.readString();
        this.fromHead = in.readString();
        this.toUserId = in.readInt();
        this.adapterPosition = in.readInt();
        this.toAccount = in.readString();
        this.toNickname = in.readString();
        this.toHead = in.readString();
        this.content = in.readString();
        this.canDelete = in.readByte() != 0;
        this.isDraft = in.readByte() != 0;
        this.otherUserId = in.readInt();
        this.otherAccount = in.readString();
        this.otherNickname = in.readString();
        this.collectionCreateTime = in.readString();
        this.otherHead = in.readString();
        this.createTime = in.readString();
        this.faceTimeType = in.readInt();
        this.msgType = in.readInt();
        this.chatType = in.readInt();
        this.groupId = in.readInt();
        this.groupNickname = in.readString();
        this.isGroupCreater = in.readInt();
        this.readBurn = in.readInt();
        this.isRead = in.readByte() != 0;
        this.isCollected = in.readByte() != 0;
        this.isSelected = in.readByte() != 0;
        this.duration = in.readString();
        this.unreadCount = in.readInt();
        this.fileSize = in.readString();
        this.fileName = in.readString();
        this.localCatchPath = in.readString();
        this.uploadProgress = in.readInt();
        this.event = in.readString();
        this.sdp = in.readString();
        this.sdpMLineIndex = in.readInt();
        this.sdpMid = in.readString();
        this.rotation = in.readString();
        this.videoCover = in.readString();
        this.addrName = in.readString();
        this.addrDes = in.readString();
        this.lat = in.readString();
        this.lng = in.readString();
    }

    public static final Parcelable.Creator<MessageBodyBean> CREATOR = new Parcelable.Creator<MessageBodyBean>() {
        @Override
        public MessageBodyBean createFromParcel(Parcel source) {
            return new MessageBodyBean(source);
        }

        @Override
        public MessageBodyBean[] newArray(int size) {
            return new MessageBodyBean[size];
        }
    };
}
