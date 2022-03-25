package com.juntai.wisdom.im.bean;

/**
 * @Author: tobato
 * @Description: 作用描述  添加好友里面的菜单
 * @CreateDate: 2021-12-13 16:54
 * @UpdateUser: 更新者
 * @UpdateDate: 2021-12-13 16:54
 */
public class AddFriendMenuBean {

    private String name;
    private String des;
    private int imageRes;

    public AddFriendMenuBean(String name, String des, int imageRes) {
        this.name = name;
        this.des = des;
        this.imageRes = imageRes;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name == null ? "" : name;
    }

    public String getDes() {
        return des == null ? "" : des;
    }

    public void setDes(String des) {
        this.des = des == null ? "" : des;
    }

}
