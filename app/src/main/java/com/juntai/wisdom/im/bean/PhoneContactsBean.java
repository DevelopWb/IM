package com.juntai.wisdom.im.bean;

/**
 * Describe:手机联系人
 * Create by zhangzhenlong
 * 2021-3-15
 * email:954101549@qq.com
 */
public class PhoneContactsBean {
    private String name;        //联系人姓名
    private String phone;    //电话号码


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public PhoneContactsBean() {
    }

    public PhoneContactsBean(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }
}
