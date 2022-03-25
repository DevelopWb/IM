package com.juntai.wisdom.im.bean;

import android.text.TextUtils;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2021/4/18 15:33
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/18 15:33
 */
public class HomePageMenuBean {
    private String  name;
    private int  headRes;
    private boolean hasEndLine;

    public HomePageMenuBean(String name, int headRes, boolean hasEndLine) {
        this.name = name;
        this.headRes = headRes;
        this.hasEndLine = hasEndLine;
    }

    public HomePageMenuBean(String name, int headRes) {
        this.name = name;
        this.headRes = headRes;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name == null ? "" : name;
    }

    public int getHeadRes() {
        return headRes;
    }

    public void setHeadRes(int headRes) {
        this.headRes = headRes;
    }

    public boolean isHasEndLine() {
        return hasEndLine;
    }

    public void setHasEndLine(boolean hasEndLine) {
        this.hasEndLine = hasEndLine;
    }
}
