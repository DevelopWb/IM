package com.juntai.wisdom.im.bean;

import com.juntai.disabled.basecomponent.base.BaseResult;

import java.util.List;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2021-11-27 9:21
 * @UpdateUser: 更新者
 * @UpdateDate: 2021-11-27 9:21
 */
public class MessageListBean extends BaseResult {


    private List<MessageBodyBean> data;

    public List<MessageBodyBean> getData() {
        return data;
    }

    public void setData(List<MessageBodyBean> data) {
        this.data = data;
    }
}
