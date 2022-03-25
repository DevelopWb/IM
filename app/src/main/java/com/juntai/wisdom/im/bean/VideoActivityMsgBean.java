package com.juntai.wisdom.im.bean;

/**
 * @Author: tobato
 * @Description: 作用描述  关闭视频通话界面
 * @CreateDate: 2022-02-10 10:59
 * @UpdateUser: 更新者
 * @UpdateDate: 2022-02-10 10:59
 */
public class VideoActivityMsgBean {

    private MessageBodyBean  messageBodyBean;

    public VideoActivityMsgBean(MessageBodyBean messageBodyBean) {
        this.messageBodyBean = messageBodyBean;
    }

    public MessageBodyBean getMessageBodyBean() {
        return messageBodyBean;
    }

    public void setMessageBodyBean(MessageBodyBean messageBodyBean) {
        this.messageBodyBean = messageBodyBean;
    }
}
