package com.juntai.wisdom.im.bean;

import com.juntai.disabled.basecomponent.base.BaseResult;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2021-11-01 15:26
 * @UpdateUser: 更新者
 * @UpdateDate: 2021-11-01 15:26
 */
public class ContactInfoBean extends BaseResult {

    /**
     * data : {"id":129,"account":"15605390600","phoneNumber":"15605390600","nickname":"李海涛","headPortrait":"https://www.juntaikeji.com:17002/head_img/default1.jpg","type":0}
     */

    private ContactBean data;

    public ContactBean getData() {
        return data;
    }

    public void setData(ContactBean data) {
        this.data = data;
    }

}
