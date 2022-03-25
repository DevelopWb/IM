package com.juntai.wisdom.im.bean;


import com.juntai.disabled.basecomponent.base.BaseResult;

/**
 * 个人信息
 * Created by Ma
 * on 2019/4/27
 */
public class UserBean extends BaseResult {

    /**
     * data : {"id":101,"account":"18669505929","phoneNumber":"18669505929","nickname":"铁人王进喜","headPortrait":"https://www.juntaikeji.com:17002/head_img/8150129b631a4cc89702083d0ddb54a1.jpeg"}
     */

    private ContactBean data;

    public ContactBean getData() {
        return data;
    }

    public void setData(ContactBean data) {
        this.data = data;
    }

}
