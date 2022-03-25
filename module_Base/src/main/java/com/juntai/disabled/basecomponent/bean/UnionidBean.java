package com.juntai.disabled.basecomponent.bean;


import com.juntai.disabled.basecomponent.base.BaseResult;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2020/12/30 16:06
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/12/30 16:06
 */
public class UnionidBean extends BaseResult {

    /**
     * client_id : 1110400936
     * openid : E58A54A16014EF2C7EDB5AF253D636DA
     * unionid : UID_B4BCFA38ECA2CAE1D348F74296984304
     */

    private String client_id;
    private String openid;
    private String unionid;

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }
}
