package com.juntai.wisdom.im.bean;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2021-11-23 11:42
 * @UpdateUser: 更新者   ws消息体
 * @UpdateDate: 2021-11-23 11:42
 */
public class BaseWsMessageBean {

    //1代表未读消息
    private int typeJson;

    public int getTypeJson() {
        return typeJson;
    }

    public void setTypeJson(int typeJson) {
        this.typeJson = typeJson;
    }
}
