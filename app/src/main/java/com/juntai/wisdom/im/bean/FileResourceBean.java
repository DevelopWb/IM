package com.juntai.wisdom.im.bean;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2022/3/17 15:42
 * @UpdateUser: 更新者
 * @UpdateDate: 2022/3/17 15:42
 */
public class FileResourceBean {

    private int fileResourceIcon;
    private String fileResourceName;
    private boolean isSelected;

    public FileResourceBean(int fileResourceIcon, String fileResourceName, boolean isSelected) {
        this.fileResourceIcon = fileResourceIcon;
        this.fileResourceName = fileResourceName;
        this.isSelected = isSelected;
    }

    public int getFileResourceIcon() {
        return fileResourceIcon;
    }

    public void setFileResourceIcon(int fileResourceIcon) {
        this.fileResourceIcon = fileResourceIcon;
    }

    public String getFileResourceName() {
        return fileResourceName == null ? "" : fileResourceName;
    }

    public void setFileResourceName(String fileResourceName) {
        this.fileResourceName = fileResourceName == null ? "" : fileResourceName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
