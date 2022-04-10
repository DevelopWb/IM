package com.juntai.wisdom.im.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2022-01-11 11:24
 * @UpdateUser: 更新者
 * @UpdateDate: 2022-01-11 11:24
 */
public class FileSelectBean implements Parcelable {

    private String fileName;
    private String filePath;
    private boolean  select;
    private long fileSize;
    private int fileChilds;
    //谁发的
    private  String from;
    //发送时间
    private  String createTime;

    public FileSelectBean(String fileName, String filePath, boolean select, long fileSize, int fileChilds, String from, String createTime) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.select = select;
        this.fileSize = fileSize;
        this.fileChilds = fileChilds;
        this.from = from;
        this.createTime = createTime;
    }

    public String getFrom() {
        return from == null ? "" : from;
    }

    public void setFrom(String from) {
        this.from = from == null ? "" : from;
    }

    public String getCreateTime() {
        return createTime == null ? "" : createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime == null ? "" : createTime;
    }

    public int getFileChilds() {
        return fileChilds;
    }

    public void setFileChilds(int fileChilds) {
        this.fileChilds = fileChilds;
    }

    public String getFileName() {
        return fileName == null ? "" : fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName == null ? "" : fileName;
    }

    public String getFilePath() {
        return filePath == null ? "" : filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath == null ? "" : filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fileName);
        dest.writeString(this.filePath);
        dest.writeByte(this.select ? (byte) 1 : (byte) 0);
        dest.writeLong(this.fileSize);
        dest.writeInt(this.fileChilds);
        dest.writeString(this.from);
        dest.writeString(this.createTime);
    }

    protected FileSelectBean(Parcel in) {
        this.fileName = in.readString();
        this.filePath = in.readString();
        this.select = in.readByte() != 0;
        this.fileSize = in.readLong();
        this.fileChilds = in.readInt();
        this.from = in.readString();
        this.createTime = in.readString();
    }

    public static final Creator<FileSelectBean> CREATOR = new Creator<FileSelectBean>() {
        @Override
        public FileSelectBean createFromParcel(Parcel source) {
            return new FileSelectBean(source);
        }

        @Override
        public FileSelectBean[] newArray(int size) {
            return new FileSelectBean[size];
        }
    };
}
