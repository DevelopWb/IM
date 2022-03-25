package com.juntai.wisdom.im.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.juntai.disabled.basecomponent.base.BaseResult;

import java.util.List;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2022-01-13 11:46
 * @UpdateUser: 更新者
 * @UpdateDate: 2022-01-13 11:46
 */
public class GroupListBean extends BaseResult {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Parcelable {
        /**
         * groupId : 7
         * groupName : 铁人王进喜,北大还行撒贝宁,tobato,tomato153,
         * groupPicture : /group/8124beb760a54cd88568d78793244ce8/2021-12-08/8124beb760a54cd88568d78793244ce8.png
         */

        private  int id;
        private  int isTop;
        private int groupId;
        private  boolean selected;
        private String groupName;
        private String groupUuid;
        private String userNickname;
        private String groupPicture;
        private boolean hasEndLine;
        private MessageBodyBean lastMessage;

        public String getGroupUuid() {
            return groupUuid == null ? "" : groupUuid;
        }

        public void setGroupUuid(String groupUuid) {
            this.groupUuid = groupUuid == null ? "" : groupUuid;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public int getId() {
            return 0==id?groupId:id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIsTop() {
            return isTop;
        }

        public void setIsTop(int isTop) {
            this.isTop = isTop;
        }

        public int getGroupId() {
            return groupId;
        }

        public void setGroupId(int groupId) {
            this.groupId = groupId;
        }

        public String getGroupName() {
            return groupName == null ? "" : groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName == null ? "" : groupName;
        }

        public String getUserNickname() {
            return userNickname == null ? "" : userNickname;
        }

        public void setUserNickname(String userNickname) {
            this.userNickname = userNickname == null ? "" : userNickname;
        }

        public String getGroupPicture() {
            return groupPicture == null ? "" : groupPicture;
        }

        public void setGroupPicture(String groupPicture) {
            this.groupPicture = groupPicture == null ? "" : groupPicture;
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

        public DataBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeInt(this.isTop);
            dest.writeInt(this.groupId);
            dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
            dest.writeString(this.groupName);
            dest.writeString(this.groupUuid);
            dest.writeString(this.userNickname);
            dest.writeString(this.groupPicture);
            dest.writeByte(this.hasEndLine ? (byte) 1 : (byte) 0);
            dest.writeParcelable(this.lastMessage, flags);
        }

        protected DataBean(Parcel in) {
            this.id = in.readInt();
            this.isTop = in.readInt();
            this.groupId = in.readInt();
            this.selected = in.readByte() != 0;
            this.groupName = in.readString();
            this.groupUuid = in.readString();
            this.userNickname = in.readString();
            this.groupPicture = in.readString();
            this.hasEndLine = in.readByte() != 0;
            this.lastMessage = in.readParcelable(MessageBodyBean.class.getClassLoader());
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
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
