package com.juntai.wisdom.im.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.juntai.disabled.basecomponent.base.BaseResult;

import java.util.List;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2022-01-15 14:34
 * @UpdateUser: 更新者
 * @UpdateDate: 2022-01-15 14:34
 */
public class GroupPeoplesBean extends BaseResult {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Parcelable {
        /**
         * id : 5
         * uuid : 6d9b0683413948d89ad2693e1d121a19
         * accountNumber : csj_btv93lg8iv2t5g
         * phoneNumber : 18669505929
         * nickname : 铁人王进喜
         * myNickname : 顾启航
         * groupNickname : aaa
         * headPortrait : /csj_btv93lg8iv2t5g/2021-11-20/e0e1d1d72489438b83e233f7d8e8bde7.png
         * gender : 2
         * qrCode : /csj_btv93lg8iv2t5g/2021-11-17/deed0a079acd4ba69569a817d796d9eb.jpeg
         * address : 山东临沂
         */

        private int id;
        private String uuid;
        private String accountNumber;
        private String phoneNumber;
        private String nickname;
        private String myNickname;
        private String groupNickname;
        private String headPortrait;
        private int gender;
        private String qrCode;
        private String address;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public String getPhoneNumber() {
            return phoneNumber == null ? "" : phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber == null ? "" : phoneNumber;
        }

        public String getNickname() {
            return nickname == null ? "" : nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname == null ? "" : nickname;
        }

        public String getMyNickname() {
            return myNickname == null ? "" : myNickname;
        }

        public void setMyNickname(String myNickname) {
            this.myNickname = myNickname == null ? "" : myNickname;
        }

        public String getGroupNickname() {
            return groupNickname == null ? nickname : groupNickname;
        }

        public void setGroupNickname(String groupNickname) {
            this.groupNickname = groupNickname == null ? "" : groupNickname;
        }

        public String getHeadPortrait() {
            return headPortrait == null ? "" : headPortrait;
        }

        public void setHeadPortrait(String headPortrait) {
            this.headPortrait = headPortrait == null ? "" : headPortrait;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.uuid);
            dest.writeString(this.accountNumber);
            dest.writeString(this.phoneNumber);
            dest.writeString(this.nickname);
            dest.writeString(this.myNickname);
            dest.writeString(this.groupNickname);
            dest.writeString(this.headPortrait);
            dest.writeInt(this.gender);
            dest.writeString(this.qrCode);
            dest.writeString(this.address);
        }

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            this.id = in.readInt();
            this.uuid = in.readString();
            this.accountNumber = in.readString();
            this.phoneNumber = in.readString();
            this.nickname = in.readString();
            this.myNickname = in.readString();
            this.groupNickname = in.readString();
            this.headPortrait = in.readString();
            this.gender = in.readInt();
            this.qrCode = in.readString();
            this.address = in.readString();
        }

        public static final Parcelable.Creator<DataBean> CREATOR = new Parcelable.Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel source) {
                return new DataBean(source);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };
    }
}
