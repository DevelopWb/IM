package com.juntai.wisdom.im.bean;

import com.juntai.disabled.basecomponent.base.BaseResult;

import java.util.List;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2022-01-13 11:46
 * @UpdateUser: 更新者
 * @UpdateDate: 2022-01-13 11:46
 */
public class GroupListBean extends BaseResult {


    private List<GroupDetailBean.DataBean> data;

    public List<GroupDetailBean.DataBean> getData() {
        return data;
    }

    public void setData(List<GroupDetailBean.DataBean> data) {
        this.data = data;
    }

}
