package com.juntai.wisdom.im.bean;

/**
 * @Author: tobato
 * @Description: 作用描述  是否是必填
 * @CreateDate: 2021/3/20 14:44
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/3/20 14:44
 */
public class ImportantTagBean {


    private  String  titleName;
    private boolean  isImportant;

    public ImportantTagBean(String titleName, boolean isImportant) {
        this.titleName = titleName;
        this.isImportant = isImportant;
    }

    public String getTitleName() {
        return titleName == null ? "" : titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName == null ? "" : titleName;
    }

    public boolean isImportant() {
        return isImportant;
    }

    public void setImportant(boolean important) {
        isImportant = important;
    }
}
