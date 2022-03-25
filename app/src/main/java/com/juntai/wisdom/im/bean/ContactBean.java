package com.juntai.wisdom.im.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.mcxtzhang.indexlib.IndexBar.bean.BaseIndexPinyinBean;


/**
 * @aouther tobato
 * @description 描述  联系人
 * @date 2021-09-24 16:40
 */

public class ContactBean extends BaseIndexPinyinBean implements Parcelable {
    private String remarksNickname;
    private boolean isTopInContact;//是否是最上面的 不需要被转化成拼音的
    //新朋友申请数
    private int friendApplyAmount;
    private String token;//秘钥
    private MessageBodyBean messageBodyBean;
    //分割线
    private boolean hasEndLine;
    /**
     * 0代表未选中  1代表选中  2代表之前就被选中了
     */
    private  int selected;
    private int isTop;//是否聊天置顶（0：不置顶；1：置顶）
    private int id;//用户id

    private String accountNumber;//超视距号

    private String accountModifyTime;//超视距号上次修改时间

    private String phoneNumber;//手机号

    private String nickname;//昵称
    private String name;//手机通讯录名称
    private boolean addFriendVerification;//添加好友是否需要验证

    public boolean isAddFriendVerification() {
        return addFriendVerification;
    }

    public void setAddFriendVerification(boolean addFriendVerification) {
        this.addFriendVerification = addFriendVerification;
    }

    private String headPortrait;//头像

    private Integer gender;//性别（1男；2女；0未知）
    private boolean friend;//是否为好友
    private String qrCode;//二维码名片
    //好友状态（1：好友；2：拉黑；3；删除）
    private int statusType;
    private String address;//地区
    private String uuid;//唯一识别码

    public boolean isFriend() {
        return friend;
    }

    public void setFriend(boolean friend) {
        this.friend = friend;
    }

    public String getUuid() {
        return uuid == null ? "" : uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid == null ? "" : uuid;
    }

    public String getAccountNumber() {
        return accountNumber == null ? "" : accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber == null ? "" : accountNumber;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name == null ? "" : name;
    }

    public String getAccountModifyTime() {
        return accountModifyTime == null ? "" : accountModifyTime;
    }

    public int getStatusType() {
        return statusType;
    }

    public int getSelected() {
        return selected;
    }

    public int getIsTop() {
        return isTop;
    }

    public void setIsTop(int isTop) {
        this.isTop = isTop;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public void setStatusType(int statusType) {
        this.statusType = statusType;
    }

    public void setAccountModifyTime(String accountModifyTime) {
        this.accountModifyTime = accountModifyTime == null ? "" : accountModifyTime;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getQrCode() {
        return qrCode == null ? "" : qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode == null ? "" : qrCode;
    }

    public String getAddress() {
        return address == null ? "" : address;
    }

    public void setAddress(String address) {
        this.address = address == null ? "" : address;
    }

    public ContactBean() {
    }


    public String getToken() {
        return token == null ? "" : token;
    }

    public void setToken(String token) {
        this.token = token == null ? "" : token;
    }

    public MessageBodyBean getMessageBodyBean() {
        return messageBodyBean;
    }

    public void setMessageBodyBean(MessageBodyBean messageBodyBean) {
        this.messageBodyBean = messageBodyBean;
    }


    public int getId() {
        return id;
    }


    public int getFriendApplyAmount() {
        return friendApplyAmount;
    }

    public ContactBean setFriendApplyAmount(int friendApplyAmount) {
        this.friendApplyAmount = friendApplyAmount;
        return this;
    }

    public boolean isHasEndLine() {
        return hasEndLine;
    }

    public ContactBean setHasEndLine(boolean hasEndLine) {
        this.hasEndLine = hasEndLine;
        return this;
    }


    public ContactBean(String remarksNickname) {
        this.remarksNickname = remarksNickname;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getPhoneNumber() {
        return phoneNumber == null ? "" : phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber == null ? "" : phoneNumber;
    }

    public String getNickname() {
        return nickname == null ? "" : nickname;
    }

    public ContactBean setNickname(String nickname) {
        this.nickname = nickname == null ? getNickname() : nickname;
        return this;
    }

    public String getRemarksNickname() {
        return remarksNickname == null ? getNickname() : remarksNickname;
    }

    public void setRemarksNickname(String remarksNickname) {
        this.remarksNickname = remarksNickname == null ? "" : remarksNickname;
    }

    public String getHeadPortrait() {
        return headPortrait == null ? "" : headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait == null ? "" : headPortrait;
    }

    public boolean isTopInContact() {
        return isTopInContact;
    }

    public ContactBean setTopInContact(boolean topInContact) {
        isTopInContact = topInContact;
        return this;
    }

    @Override
    public String getTarget() {
        return remarksNickname;
    }

    @Override
    public boolean isNeedToPinyin() {
        return !isTopInContact;
    }


    @Override
    public boolean isShowSuspension() {
        return !isTopInContact;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.remarksNickname);
        dest.writeByte(this.isTopInContact ? (byte) 1 : (byte) 0);
        dest.writeInt(this.friendApplyAmount);
        dest.writeString(this.token);
        dest.writeParcelable(this.messageBodyBean, flags);
        dest.writeByte(this.hasEndLine ? (byte) 1 : (byte) 0);
        dest.writeInt(this.selected);
        dest.writeInt(this.isTop);
        dest.writeInt(this.id);
        dest.writeString(this.accountNumber);
        dest.writeString(this.accountModifyTime);
        dest.writeString(this.phoneNumber);
        dest.writeString(this.nickname);
        dest.writeString(this.name);
        dest.writeByte(this.addFriendVerification ? (byte) 1 : (byte) 0);
        dest.writeString(this.headPortrait);
        dest.writeValue(this.gender);
        dest.writeByte(this.friend ? (byte) 1 : (byte) 0);
        dest.writeString(this.qrCode);
        dest.writeInt(this.statusType);
        dest.writeString(this.address);
        dest.writeString(this.uuid);
    }

    protected ContactBean(Parcel in) {
        this.remarksNickname = in.readString();
        this.isTopInContact = in.readByte() != 0;
        this.friendApplyAmount = in.readInt();
        this.token = in.readString();
        this.messageBodyBean = in.readParcelable(MessageBodyBean.class.getClassLoader());
        this.hasEndLine = in.readByte() != 0;
        this.selected = in.readInt();
        this.isTop = in.readInt();
        this.id = in.readInt();
        this.accountNumber = in.readString();
        this.accountModifyTime = in.readString();
        this.phoneNumber = in.readString();
        this.nickname = in.readString();
        this.name = in.readString();
        this.addFriendVerification = in.readByte() != 0;
        this.headPortrait = in.readString();
        this.gender = (Integer) in.readValue(Integer.class.getClassLoader());
        this.friend = in.readByte() != 0;
        this.qrCode = in.readString();
        this.statusType = in.readInt();
        this.address = in.readString();
        this.uuid = in.readString();
    }

    public static final Creator<ContactBean> CREATOR = new Creator<ContactBean>() {
        @Override
        public ContactBean createFromParcel(Parcel source) {
            return new ContactBean(source);
        }

        @Override
        public ContactBean[] newArray(int size) {
            return new ContactBean[size];
        }
    };
}
