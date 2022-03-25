package com.juntai.wisdom.im.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Author: tobato
 * @Description: 作用描述  群里成员
 * @CreateDate: 2021-12-10 16:07
 * @UpdateUser: 更新者
 * @UpdateDate: 2021-12-10 16:07
 */
public class PeopleBean implements Parcelable {


    private String  name;
    private String  head;
    private  int  userId;

    //0代表正常用户  1代表 添加 2代表删除
    private  int  type;


    public PeopleBean(String name, String head, int userId, int type) {
        this.name = name;
        this.head = head;
        this.userId = userId;
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name == null ? "" : name;
    }

    public String getHead() {
        return head == null ? "" : head;
    }

    public void setHead(String head) {
        this.head = head == null ? "" : head;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.head);
        dest.writeInt(this.userId);
        dest.writeInt(this.type);
    }

    protected PeopleBean(Parcel in) {
        this.name = in.readString();
        this.head = in.readString();
        this.userId = in.readInt();
        this.type = in.readInt();
    }

    public static final Parcelable.Creator<PeopleBean> CREATOR = new Parcelable.Creator<PeopleBean>() {
        @Override
        public PeopleBean createFromParcel(Parcel source) {
            return new PeopleBean(source);
        }

        @Override
        public PeopleBean[] newArray(int size) {
            return new PeopleBean[size];
        }
    };
}
