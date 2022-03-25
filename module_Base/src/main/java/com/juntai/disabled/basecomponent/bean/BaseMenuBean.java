package com.juntai.disabled.basecomponent.bean;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2022-02-28 11:23
 * @UpdateUser: 更新者
 * @UpdateDate: 2022-02-28 11:23
 */
public class BaseMenuBean {


    private String name;
    private int  res;

    public BaseMenuBean(String name) {
        this.name = name;
    }

    public BaseMenuBean(String name, int res) {
        this.name = name;
        this.res = res;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name == null ? "" : name;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }
}
