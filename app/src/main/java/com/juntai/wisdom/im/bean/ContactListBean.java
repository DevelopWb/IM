package com.juntai.wisdom.im.bean;

import com.juntai.disabled.basecomponent.base.BaseResult;

import java.util.List;

/**
 * @Author: tobato
 * @Description: 作用描述  联系人列表
 * @CreateDate: 2021-10-31 16:22
 * @UpdateUser: 更新者
 * @UpdateDate: 2021-10-31 16:22
 */
public class ContactListBean extends BaseResult {

    private List<ContactBean> data;

    public List<ContactBean> getData() {
        return data;
    }

    public void setData(List<ContactBean> data) {
        this.data = data;
    }

}
