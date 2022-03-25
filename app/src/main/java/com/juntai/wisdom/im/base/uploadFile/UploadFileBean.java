package com.juntai.wisdom.im.base.uploadFile;

import android.os.Parcel;
import android.os.Parcelable;

import com.juntai.disabled.basecomponent.base.BaseResult;
import com.juntai.wisdom.im.bean.MessageBodyBean;

/**
 * @Author: tobato
 * @Description: 作用描述  上传文件
 * @CreateDate: 2022-03-06 15:19
 * @UpdateUser: 更新者
 * @UpdateDate: 2022-03-06 15:19
 */
public class UploadFileBean extends BaseResult implements Parcelable {

    private int uploadProgress;// 上传进度

    private byte status;//0：默认 1：正在上传 2：上传完成 3：上传失败

    private String filePath;
    private MessageBodyBean messageBodyBean;

    public UploadFileBean( String filePath, MessageBodyBean messageBodyBean) {
        this.filePath = filePath;
        this.messageBodyBean = messageBodyBean;
    }

    public MessageBodyBean getMessageBodyBean() {
        return messageBodyBean;
    }

    public void setMessageBodyBean(MessageBodyBean messageBodyBean) {
        this.messageBodyBean = messageBodyBean;
    }

    public String getFilePath() {
        return filePath == null ? "" : filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath == null ? "" : filePath;
    }

    public int getUploadProgress() {
        return uploadProgress;
    }

    public void setUploadProgress(int uploadProgress) {
        this.uploadProgress = uploadProgress;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.uploadProgress);
        dest.writeByte(this.status);
        dest.writeString(this.filePath);
        dest.writeParcelable(this.messageBodyBean, flags);
    }

    protected UploadFileBean(Parcel in) {
        this.uploadProgress = in.readInt();
        this.status = in.readByte();
        this.filePath = in.readString();
        this.messageBodyBean = in.readParcelable(MessageBodyBean.class.getClassLoader());
    }

    public static final Creator<UploadFileBean> CREATOR = new Creator<UploadFileBean>() {
        @Override
        public UploadFileBean createFromParcel(Parcel source) {
            return new UploadFileBean(source);
        }

        @Override
        public UploadFileBean[] newArray(int size) {
            return new UploadFileBean[size];
        }
    };
}
