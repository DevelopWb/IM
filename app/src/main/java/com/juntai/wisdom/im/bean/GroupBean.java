package com.juntai.wisdom.im.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.juntai.disabled.basecomponent.base.BaseResult;

import java.util.List;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2022-01-16 9:52
 * @UpdateUser: 更新者
 * @UpdateDate: 2022-01-16 9:52
 */
public class GroupBean extends BaseResult {


    private GroupListBean.DataBean data;

    public GroupListBean.DataBean getData() {
        return data;
    }

    public void setData(GroupListBean.DataBean data) {
        this.data = data;
    }


}
