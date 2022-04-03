package com.juntai.wisdom.im.bean;

import com.juntai.disabled.basecomponent.base.BaseResult;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2022-01-16 9:52
 * @UpdateUser: 更新者
 * @UpdateDate: 2022-01-16 9:52
 */
public class GroupBean extends BaseResult {


    private GroupDetailInfoBean data;

    public GroupDetailInfoBean getData() {
        return data;
    }

    public void setData(GroupDetailInfoBean data) {
        this.data = data;
    }


}
