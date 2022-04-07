package com.juntai.wisdom.im.bean;

import com.juntai.disabled.basecomponent.base.BaseResult;

import java.util.List;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2022-01-15 14:34
 * @UpdateUser: 更新者
 * @UpdateDate: 2022-01-15 14:34
 */
public class GroupPeoplesBean extends BaseResult {


    private List<UserInfoVoListBean> data;

    public List<UserInfoVoListBean> getData() {
        return data;
    }

    public void setData(List<UserInfoVoListBean> data) {
        this.data = data;
    }
}
