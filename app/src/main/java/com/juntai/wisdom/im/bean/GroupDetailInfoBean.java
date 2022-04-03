package com.juntai.wisdom.im.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2022/4/1 17:19
 * @UpdateUser: 更新者
 * @UpdateDate: 2022/4/1 17:19
 */
public class GroupDetailInfoBean implements Parcelable {

    /**
     * groupId : 18
     * uuid : f3a6714211f746078ede255d7eb0ce10
     * groupName : tomato153、铁人王进喜、tobato、
     * groupPicture : /group/f3a6714211f746078ede255d7eb0ce10/2022-01-16/f3a6714211f746078ede255d7eb0ce10.png
     * isTop : 0
     * userNickname : aa
     * userInfoVoList : [{"id":10,"uuid":"9aef4a4544ed43c6a57e64ae0a2389a5","accountNumber":"66666666","phoneNumber":"15311810032","nickname":"tomato153","myNickname":null,"groupNickname":"aa","headPortrait":"/9aef4a4544ed43c6a57e64ae0a2389a5/2021-11-27/b79909374a934e79a99a051c5e584f74.png","gender":0,"qrCode":"/9aef4a4544ed43c6a57e64ae0a2389a5/2021-12-08/9aef4a4544ed43c6a57e64ae0a2389a5.jpeg","address":"山东临沂"},{"id":5,"uuid":"6d9b0683413948d89ad2693e1d121a19","accountNumber":"csj_btv93lg8iv2t5g","phoneNumber":"18669505929","nickname":"铁人王进喜","myNickname":null,"groupNickname":null,"headPortrait":"/6d9b0683413948d89ad2693e1d121a19/2021-12-04/d48e5c7ff2f243bc897f7a6743361047.png","gender":1,"qrCode":"/6d9b0683413948d89ad2693e1d121a19/2021-12-08/6d9b0683413948d89ad2693e1d121a19.jpeg","address":"山东临沂"},{"id":9,"uuid":"2fc39f48adf24c66915239c0918af2de","accountNumber":"88888888","phoneNumber":"17568086930","nickname":"tobato","myNickname":"bbb","groupNickname":null,"headPortrait":"/2fc39f48adf24c66915239c0918af2de/2021-12-13/2b61ed1e7b074109ae8f96aa3a70dfea.png","gender":1,"qrCode":"/2fc39f48adf24c66915239c0918af2de/2022-01-15/2fc39f48adf24c66915239c0918af2de.jpeg","address":null}]
     */
    private long id;
    private boolean selected;
    private String groupUuid;
    private boolean hasEndLine;
    private MessageBodyBean lastMessage;
    private int groupId;
    private String uuid;
    private String groupName;
    private String groupPicture;
    private int isTop;
    private int groupCreateUserId;
    private String userNickname;
    private List<GroupPeoplesBean.DataBean> userInfoVoList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getGroupUuid() {
        return groupUuid == null ? "" : groupUuid;
    }

    public void setGroupUuid(String groupUuid) {
        this.groupUuid = groupUuid == null ? "" : groupUuid;
    }

    public boolean isHasEndLine() {
        return hasEndLine;
    }

    public void setHasEndLine(boolean hasEndLine) {
        this.hasEndLine = hasEndLine;
    }

    public MessageBodyBean getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(MessageBodyBean lastMessage) {
        this.lastMessage = lastMessage;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getUuid() {
        return uuid == null ? "" : uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid == null ? "" : uuid;
    }

    public String getGroupName() {
        return groupName == null ? "" : groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName == null ? "" : groupName;
    }

    public String getGroupPicture() {
        return groupPicture == null ? "" : groupPicture;
    }

    public void setGroupPicture(String groupPicture) {
        this.groupPicture = groupPicture == null ? "" : groupPicture;
    }

    public int getIsTop() {
        return isTop;
    }

    public void setIsTop(int isTop) {
        this.isTop = isTop;
    }

    public int getGroupCreateUserId() {
        return groupCreateUserId;
    }

    public void setGroupCreateUserId(int groupCreateUserId) {
        this.groupCreateUserId = groupCreateUserId;
    }

    public String getUserNickname() {
        return userNickname == null ? "" : userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname == null ? "" : userNickname;
    }

    public List<GroupPeoplesBean.DataBean> getUserInfoVoList() {
        if (userInfoVoList == null) {
            return new ArrayList<>();
        }
        return userInfoVoList;
    }

    public void setUserInfoVoList(List<GroupPeoplesBean.DataBean> userInfoVoList) {
        this.userInfoVoList = userInfoVoList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
        dest.writeString(this.groupUuid);
        dest.writeByte(this.hasEndLine ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.lastMessage, flags);
        dest.writeInt(this.groupId);
        dest.writeString(this.uuid);
        dest.writeString(this.groupName);
        dest.writeString(this.groupPicture);
        dest.writeInt(this.isTop);
        dest.writeInt(this.groupCreateUserId);
        dest.writeString(this.userNickname);
        dest.writeList(this.userInfoVoList);
    }

    public GroupDetailInfoBean() {
    }

    protected GroupDetailInfoBean(Parcel in) {
        this.id = in.readLong();
        this.selected = in.readByte() != 0;
        this.groupUuid = in.readString();
        this.hasEndLine = in.readByte() != 0;
        this.lastMessage = in.readParcelable(MessageBodyBean.class.getClassLoader());
        this.groupId = in.readInt();
        this.uuid = in.readString();
        this.groupName = in.readString();
        this.groupPicture = in.readString();
        this.isTop = in.readInt();
        this.groupCreateUserId = in.readInt();
        this.userNickname = in.readString();
        this.userInfoVoList = new ArrayList<GroupPeoplesBean.DataBean>();
        in.readList(this.userInfoVoList, GroupPeoplesBean.DataBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<GroupDetailInfoBean> CREATOR = new Parcelable.Creator<GroupDetailInfoBean>() {
        @Override
        public GroupDetailInfoBean createFromParcel(Parcel source) {
            return new GroupDetailInfoBean(source);
        }

        @Override
        public GroupDetailInfoBean[] newArray(int size) {
            return new GroupDetailInfoBean[size];
        }
    };
}
