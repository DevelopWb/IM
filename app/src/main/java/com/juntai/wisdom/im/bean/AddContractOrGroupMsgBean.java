package com.juntai.wisdom.im.bean;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2022/3/24 14:45
 * @UpdateUser: 更新者
 * @UpdateDate: 2022/3/24 14:45
 */
public class AddContractOrGroupMsgBean {

    private MessageBodyBean messageBodyBean;

    public AddContractOrGroupMsgBean(MessageBodyBean messageBodyBean) {
        this.messageBodyBean = messageBodyBean;
    }

    public MessageBodyBean getMessageBodyBean() {
        return messageBodyBean;
    }

    public void setMessageBodyBean(MessageBodyBean messageBodyBean) {
        this.messageBodyBean = messageBodyBean;
    }
}
