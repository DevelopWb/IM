package com.juntai.wisdom.im.bean;

import java.io.Serializable;

/**
 * Describe:个人中心菜单数据
 * Create by tobato
 * 2020/3/7
 * email:954101549@qq.com
 */
public class MyMenuBean implements Serializable {
    private String name;
    private int number;
    private int imageId;
    private String tag;
    //分割线
    private boolean hasEndLine;

    public MyMenuBean(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public MyMenuBean(String name, int number, int imageId, String tag, boolean hasEndLine) {
        this.name = name;
        this.number = number;
        this.imageId = imageId;
        this.tag = tag;
        this.hasEndLine = hasEndLine;
    }

    public MyMenuBean(String name, int number, int imageId, boolean hasEndLine) {
        this.name = name;
        this.number = number;
        this.imageId = imageId;
        this.hasEndLine = hasEndLine;
    }

    public MyMenuBean(String name, int imageId, boolean hasEndLine) {
        this.name = name;
        this.imageId = imageId;
        this.hasEndLine = hasEndLine;
    }

    public boolean isHasEndLine() {
        return hasEndLine;
    }

    public void setHasEndLine(boolean hasEndLine) {
        this.hasEndLine = hasEndLine;
    }

    public String getTag() {
        return tag == null ? "" : tag;
    }

    public void setTag(String tag) {
        this.tag = tag == null ? "" : tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }


    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

}
