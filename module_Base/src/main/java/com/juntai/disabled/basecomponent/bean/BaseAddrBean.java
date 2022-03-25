package com.juntai.disabled.basecomponent.bean;


import com.juntai.disabled.basecomponent.base.BaseResult;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2020/8/23 16:52
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/8/23 16:52
 */
public class BaseAddrBean extends BaseResult {
    private String status;

    public String getStatus() {
        return status == null ? "" : status;
    }

    public void setStatus(String status) {
        this.status = status == null ? "" : status;
    }
}
